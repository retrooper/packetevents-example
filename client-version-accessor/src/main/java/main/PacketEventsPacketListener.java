package main;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.chat.WrappedPacketInChat;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import org.bukkit.entity.Player;

public class PacketEventsPacketListener extends PacketListenerAbstract {
    public PacketEventsPacketListener() {
        super(PacketListenerPriority.LOW);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        Player player = event.getPlayer();
        if (event.getPacketId() == PacketType.Play.Client.CHAT) {
            WrappedPacketInChat wrappedPacketInChat = new WrappedPacketInChat(event.getNMSPacket());
            String message = wrappedPacketInChat.getMessage();
            if (message.equalsIgnoreCase("what is my client version")) {
                ClientVersion clientVersion = PacketEvents.get().getPlayerUtils().getClientVersion(player);
                player.sendMessage("Your client version: " + clientVersion);
            }
        }
    }
}
