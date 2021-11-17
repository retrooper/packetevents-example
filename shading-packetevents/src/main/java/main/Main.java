package main;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.factory.bukkit.PacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onLoad() {
        PacketEvents.setAPI(PacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().getEventManager().registerListener(new PacketEventsListener());
        PacketEvents.getAPI().init();
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}

