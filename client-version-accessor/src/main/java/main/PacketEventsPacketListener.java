package main;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PacketEventsPacketListener extends PacketListenerAbstract {
    public PacketEventsPacketListener() {
        super(PacketListenerPriority.LOW, true);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        //Cross-platform user
        User user = event.getUser();
        if (event.getPacketType() == PacketType.Play.Client.CHAT_MESSAGE) {
            WrapperPlayClientChatMessage chatMessage = new WrapperPlayClientChatMessage(event);
            String message = chatMessage.getMessage();
            if (message.equalsIgnoreCase("cv?")) {
                ClientVersion clientVersion = PacketEvents.getAPI().getProtocolManager().getClientVersion(event.getChannel());
                Component component = Component.text("Your client version: " + clientVersion.getReleaseName() + ".")
                        .color(NamedTextColor.GOLD);
                user.sendMessage(component);
                event.setCancelled(true);
            }
        }
    }
}
