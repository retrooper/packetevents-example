package main;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onLoad() {
        //Creating and loading the API is necessary when shading!
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
        //Register the listener
        PacketEvents.getAPI().getEventManager().registerListener(new PacketEventsListener());
    }

    @Override
    public void onEnable() {
        //Initialize the API!
        PacketEvents.getAPI().init();
    }

    @Override
    public void onDisable() {
        //Terminate!
        PacketEvents.getAPI().terminate();
    }
}

