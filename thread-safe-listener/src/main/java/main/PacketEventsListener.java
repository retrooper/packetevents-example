package main;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import org.bukkit.ChatColor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PacketEventsListener extends PacketListenerAbstract {
    private static final boolean READ_ONLY = true;
    private static final boolean THREAD_SAFE = true;
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    public PacketEventsListener() {
        super(PacketListenerPriority.NORMAL, READ_ONLY, THREAD_SAFE);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            EXECUTOR.execute(() -> {
                WrapperPlayClientInteractEntity interaction = new WrapperPlayClientInteractEntity(event);
                if (interaction.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                    event.getUser().sendMessage(ChatColor.RED + "You hit an entity!");
                }
                //Make sure to cleanup the event, to prevent memory leaks
                event.cleanUp();
            });
        } else {
            //Cleanup event once we are done dealing with it.
            event.cleanUp();
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        //Cleanup event
        event.cleanUp();
    }
}
