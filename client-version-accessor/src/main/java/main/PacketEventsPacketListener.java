package main;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.chat.Color;
import com.github.retrooper.packetevents.protocol.chat.component.BaseComponent;
import com.github.retrooper.packetevents.protocol.chat.component.impl.TextComponent;
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
                BaseComponent component = TextComponent.builder().text("Your client version: ")
                        .color(Color.DARK_GREEN)
                        .append(TextComponent.builder()
                                .text(clientVersion.name())
                                .color(Color.GOLD)
                                .build()

                        )
                        .build();
                WrapperPlayServerChatMessage chatMessagePacket = new WrapperPlayServerChatMessage(component,
                        WrapperPlayServerChatMessage.ChatPosition.CHAT,
                        new UUID(0L, 0L));
                PacketEvents.getAPI().getPlayerManager().sendPacket(event.getChannel(), chatMessagePacket);
                event.setCancelled(true);
            }
        }
    }
}
