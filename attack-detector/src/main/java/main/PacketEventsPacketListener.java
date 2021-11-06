package main;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn;
import org.bukkit.entity.Player;

public class PacketEventsPacketListener implements PacketListener {
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

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.RESPAWN) {
            WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn(event);
            Dimension dimension = respawn.getDimension();
            GameMode gameMode = respawn.getGameMode();
            System.out.println("You have respawned! Dimension type: " + dimension.getType().name() + ", Game mode: " + gameMode.name());
        }
    }
}
