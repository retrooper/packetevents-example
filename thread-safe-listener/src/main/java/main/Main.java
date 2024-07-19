package main;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onLoad() {
        //PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        //PacketEvents.getAPI().load();
        PacketEvents.getAPI().getEventManager().registerListener(new PacketEventsListener());
    }

    @Override
    public void onEnable() {
        //PacketEvents.getAPI().init();
    }

    @Override
    public void onDisable() {
        //PacketEvents.getAPI().terminate();
    }
}
