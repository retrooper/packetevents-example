import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PacketEventsPacketListener extends PacketListenerDynamic {
    public PacketEventsPacketListener() {
        super(PacketEventPriority.LOW);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        Player player = event.getPlayer();
        if (event.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
            WrappedPacketInUseEntity wrappedPacketInUseEntity = new WrappedPacketInUseEntity(event.getNMSPacket());
            WrappedPacketInUseEntity.EntityUseAction action = wrappedPacketInUseEntity.getAction();
            Entity entity = wrappedPacketInUseEntity.getEntity(player.getWorld());
            if (action == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                if (entity != null) {
                    //It is NOT an NPC if we can find the Bukkit Entity
                    player.sendMessage("You have attacked an " + entity.getType());
                }
                else {
                    int entityID = wrappedPacketInUseEntity.getEntityId();
                    player.sendMessage("You have attacked a fake entity with the Entity ID: " + entityID);
                }
            }

        }
    }
}
