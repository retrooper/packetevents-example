package main;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.chat.ChatPosition;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class PacketEventsPacketListener implements PacketListener {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        //Cross-platform user abstraction
        User user = event.getUser();
        //Whenever the player sends an entity interaction packet.
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity interactEntity = new WrapperPlayClientInteractEntity(event);
            WrapperPlayClientInteractEntity.InteractAction action = interactEntity.getAction();
            if (action == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                int entityID = interactEntity.getEntityId();
                //Create a chat component with the Adventure API
                Component message = Component.text("You attacked an entity.")
                        .hoverEvent(HoverEvent.hoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                Component.text("Entity ID: " + entityID)
                                        .color(NamedTextColor.GREEN)
                                        .decorate(TextDecoration.BOLD)
                                        .decorate(TextDecoration.ITALIC)
                        ));
                //Send it to the cross-platform user
                user.sendMessage(message);
            }
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.RESPAWN) {
            WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn(event);
            Dimension dimension = respawn.getDimension();
            GameMode gameMode = respawn.getGameMode();
            System.out.println(event.getUser().getProfile().getName()
                    + " have respawned! Dimension type: "
                    + dimension.getType().name() + ", Game mode: " + gameMode.name());
        }
    }
}
