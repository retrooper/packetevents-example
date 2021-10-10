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
        //TODO Register a packet listener here
        PacketEvents.getAPI().init();
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}

