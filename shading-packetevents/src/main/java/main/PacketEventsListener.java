package main;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import org.bukkit.entity.Player;

public class PacketEventsListener extends PacketListenerAbstract {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
                WrapperPlayClientInteractEntity in = new WrapperPlayClientInteractEntity(event);
                player.sendMessage("eid: " + in.getEntityID() + ", hand: " + in.getHand().name());
            }
        }
    }
}
