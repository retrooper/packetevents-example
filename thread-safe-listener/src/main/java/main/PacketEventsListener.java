package main;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;
import org.bukkit.ChatColor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PacketEventsListener extends PacketListenerAbstract {
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    public PacketEventsListener() {
        super(PacketListenerPriority.NORMAL);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        // In this listener we aim to process packets on another thread.
        if (event.getPacketType() == PacketType.Play.Client.UPDATE_SIGN) {
            // In order to do that we need to clone the event, allowing us to retain the data in the packet.
            PacketReceiveEvent copy = event.clone();
            EXECUTOR.execute(() -> {
                // We may now use wrappers to process this event.
                WrapperPlayClientUpdateSign signUpdate =
                        new WrapperPlayClientUpdateSign(copy);
                if (signUpdate.getTextLines()[0].equals("hey")) {
                    copy.getUser().sendMessage(ChatColor.RED + "You said 'hey' on the first line of a sign!");
                }

                // IMPORTANT: Since we retained the data in the event, we must clean it up to avoid memory leaks!
                copy.cleanUp();
            });
        }
    }
}
