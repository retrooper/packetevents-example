package main;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onLoad() {
        // Building, loading, and initializing the library is necessary when bundling (or shading).
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
        /* 
        * MORE CODE MAY APPEAR HERE (such as registering listeners)
        */
    }

    @Override
    public void onEnable() {
        // Initialize the library!
        PacketEvents.getAPI().init();
    }

    @Override
    public void onDisable() {
        // Clean-up process
        PacketEvents.getAPI().terminate();
    }
}

