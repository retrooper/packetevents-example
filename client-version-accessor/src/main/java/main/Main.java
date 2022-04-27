package main;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onLoad() {
        PacketEvents.create(this);
        PacketEventsSettings settings = PacketEvents.get().getSettings();
        settings
                .fallbackServerVersion(ServerVersion.v_1_7_10)
                .checkForUpdates(false)
                .bStats(true);
        PacketEvents.get().load();
    }

    @Override
    public void onEnable() {
        //We register before calling PacketEvents#init, because that method might already call some events.
        PacketEvents.get().registerListener(new PacketEventsPacketListener());
        PacketEvents.get().init();
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
    }
}