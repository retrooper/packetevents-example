package main;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import org.bukkit.ChatColor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PacketEventsListener extends PacketListenerAbstract {
    private static final boolean READ_ONLY = true;
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    public PacketEventsListener() {
        super(PacketListenerPriority.NORMAL, READ_ONLY, false);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            PacketReceiveEvent instance = event.clone();
            EXECUTOR.execute(() -> {
                WrapperPlayClientInteractEntity interaction =
                        new WrapperPlayClientInteractEntity(instance);
                if (interaction.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                    instance.getUser().sendMessage(ChatColor.RED + "You hit an entity!");
                }
                //Make sure to clean up the event, to prevent memory leaks.
                instance.cleanUp();
            });
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
    }
}
