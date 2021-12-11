package main;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.chat.Color;
import com.github.retrooper.packetevents.protocol.chat.component.TextComponent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PacketEventsPacketListener extends PacketListenerAbstract {
    public PacketEventsPacketListener() {
        super(PacketListenerPriority.LOW);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CHAT_MESSAGE) {
            WrapperPlayClientChatMessage chatMessage = new WrapperPlayClientChatMessage(event);
            String message = chatMessage.getMessage();
            if (message.equalsIgnoreCase("cv?")) {
                ClientVersion clientVersion = PacketEvents.getAPI().getPlayerManager().getClientVersion(event.getChannel());
                List<TextComponent> components = new ArrayList<>();
                components.add(TextComponent.builder()
                        .text("Your client version: " + clientVersion.name())
                        .color(Color.WHITE)
                        .build()
                );
                WrapperPlayServerChatMessage chatMessagePacket = new WrapperPlayServerChatMessage(components,
                        WrapperPlayServerChatMessage.ChatPosition.CHAT,
                        new UUID(0L, 0L));
                PacketEvents.getAPI().getPlayerManager().sendPacket(event.getChannel(), chatMessagePacket);
                event.setCancelled(true);
            }
        }
    }
}
