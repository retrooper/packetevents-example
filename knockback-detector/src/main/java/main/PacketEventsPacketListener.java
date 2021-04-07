package main;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PacketEventsPacketListener extends PacketListenerAbstract {
    public PacketEventsPacketListener() {
        super(PacketEventPriority.HIGH);
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        Player player = event.getPlayer();
        if (event.getPacketId() == PacketType.Play.Server.ENTITY_VELOCITY) {
            WrappedPacketOutEntityVelocity velocity = new WrappedPacketOutEntityVelocity(event.getNMSPacket());
            Entity entity = velocity.getEntity(player.getWorld());
            if (entity != null && entity.getEntityId() == player.getEntityId()) {
                double velX = velocity.getVelocityX();
                double velY = velocity.getVelocityY();
                double velZ = velocity.getVelocityZ();

                //How to modify:
                //velocity.setVelocityX(newVelX);
                //velocity.setVelocityY(newVelY);
                //velocity.setVelocityZ(newVelZ);

                //How to cancel the velocity:
                //event.setCancelled(true);
                player.sendMessage("You have taken velocity!");
            }
        }
    }
}
