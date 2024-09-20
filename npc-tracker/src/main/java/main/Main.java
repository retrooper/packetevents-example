package main;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onLoad() {
        //PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        //PacketEvents.getAPI().load();
        PacketEvents.getAPI().getEventManager().registerListener(new PacketEventsPacketListener(),
                PacketListenerPriority.NORMAL);
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
