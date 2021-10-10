package main;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.factory.bukkit.PacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onLoad() {
        PacketEvents.build(PacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        //We register before calling PacketEvents#init, because that method might already call some events.
        PacketEvents.getAPI().getEventManager().registerListener(new PacketEventsPacketListener());
        PacketEvents.getAPI().init();
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}