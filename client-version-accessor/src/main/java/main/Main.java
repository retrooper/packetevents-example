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
                .backupServerVersion(ServerVersion.v_1_7_10)
                .compatInjector(false)
                .checkForUpdates(false);
        PacketEvents.get().loadAsyncNewThread();
    }

    @Override
    public void onEnable() {
        //We register before initializing as it is possible that an event is called during initialization.
        PacketEvents.get().registerListener(new PacketEventsPacketListener());
        PacketEvents.get().init();
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
    }
}