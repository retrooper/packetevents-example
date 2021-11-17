package main;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.chat.Color;
import com.github.retrooper.packetevents.protocol.chat.component.TextComponent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PacketEventsPacketListener implements PacketListener {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        //Whenever the player sends an entity interaction packet.
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity interactEntity = new WrapperPlayClientInteractEntity(event);
            WrapperPlayClientInteractEntity.Type type = interactEntity.getType();
            if (type == WrapperPlayClientInteractEntity.Type.ATTACK) {
                int entityID = interactEntity.getEntityId();
                //Send them a message using the chat packet.
                List<TextComponent> components = new ArrayList<>();
                components.add(TextComponent.builder()
                        .text("You have attacked an entity with the ENTITY ID: ")
                        .color(Color.DARK_GREEN)
                        .build()
                );

                components.add(TextComponent.builder()
                        .text("" + entityID)
                        .color(Color.GOLD)
                        .build()
                );
                WrapperPlayServerChatMessage chatMessagePacket = new WrapperPlayServerChatMessage(components,
                        WrapperPlayServerChatMessage.ChatPosition.CHAT,
                        new UUID(0L, 0L));

                PacketEvents.getAPI().getPlayerManager().sendPacket(event.getChannel(), chatMessagePacket);
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
