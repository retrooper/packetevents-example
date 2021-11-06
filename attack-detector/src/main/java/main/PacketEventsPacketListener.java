package main;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import org.bukkit.entity.Player;

public class PacketEventsPacketListener extends PacketListenerAbstract {
    public PacketEventsPacketListener() {
        super(PacketListenerPriority.LOW);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity interactEntity = new WrapperPlayClientInteractEntity(event);
            WrapperPlayClientInteractEntity.Type type = interactEntity.getType();
            if (type == WrapperPlayClientInteractEntity.Type.ATTACK) {
                //TODO getEntityByID
                int entityID = interactEntity.getEntityID();
                player.sendMessage("You have attacked a entity with the Entity ID: " + entityID);
            }
        }
    }
}
