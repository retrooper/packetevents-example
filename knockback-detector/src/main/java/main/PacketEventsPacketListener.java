package main;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import org.bukkit.entity.Player;

public class PacketEventsPacketListener extends PacketListenerAbstract {
    public PacketEventsPacketListener() {
        super(PacketListenerPriority.HIGH);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.ENTITY_VELOCITY) {
            Player player = (Player) event.getPlayer();
            WrapperPlayServerEntityVelocity entityVelocity = new WrapperPlayServerEntityVelocity(event);
            int entityID = entityVelocity.getEntityId();
            if (entityID != player.getEntityId()) {
                Vector3d velocity = entityVelocity.getVelocity();
                //How to modify:
                //velocityPacket.setVelocity(newVelocity);

                //How to cancel the velocity:
                //event.setCancelled(true);
                player.sendMessage("You have taken velocity!");
            }
        }
    }
}
