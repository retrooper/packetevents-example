package main;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PacketEventsPacketListener extends PacketListenerAbstract {
    public PacketEventsPacketListener() {
        super(PacketListenerPriority.HIGH);
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        Player player = event.getPlayer();
        if (event.getPacketId() == PacketType.Play.Server.ENTITY_VELOCITY) {
            WrappedPacketOutEntityVelocity velocityPacket = new WrappedPacketOutEntityVelocity(event.getNMSPacket());
            Entity entity = velocityPacket.getEntity(player.getWorld());
            if (entity != null && entity.getEntityId() == player.getEntityId()) {
                Vector3d velocity = velocityPacket.getVelocity();

                //How to modify:
                //velocityPacket.setVelocity(newVelocity);

                //How to cancel the velocity:
                //event.setCancelled(true);
                player.sendMessage("You have taken velocity!");
            }
        }
    }
}
