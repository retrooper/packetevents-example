package io.github.retrooper.examplepacketevents;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.enums.EntityAnimationType;
import io.github.retrooper.packetevents.event.PacketHandler;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.in.chat.WrappedPacketInChat;
import io.github.retrooper.packetevents.packetwrappers.out.animation.WrappedPacketOutAnimation;
import io.github.retrooper.packetevents.packetwrappers.out.chat.WrappedPacketOutChat;
import io.github.retrooper.packetevents.packetwrappers.out.entityvelocity.WrappedPacketOutEntityVelocity;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements PacketListener {

    @Override
    public void onEnable() {
        PacketEvents.start(this);
        PacketEvents.getEventManager().registerListener(this);
        //cancel server tick task if u don't want to use the server tick event
        PacketEvents.getServerTickTask().cancel();
    }

    @Override
    public void onDisable() {
        PacketEvents.stop();
    }

    @PacketHandler
    public void onPacketSend(final PacketSendEvent e) {
        if (e.getPacketName().equals(Packet.Server.ENTITY_VELOCITY)) {
            WrappedPacketOutEntityVelocity velocityPacket = new WrappedPacketOutEntityVelocity(e.getPacket());
            double velocityX = velocityPacket.getVelocityX();
            double velocityY = velocityPacket.getVelocityY();
            double velocityZ = velocityPacket.getVelocityZ();


        } else if (e.getPacketName().equals(Packet.Server.CHAT)) {
            WrappedPacketOutChat chatPacket = new WrappedPacketOutChat(e.getPacket());
            String message= chatPacket.getMessage();
        }
    }

    @PacketHandler
    public void onReceive(final PacketReceiveEvent e) {
        if (e.getPacketName().equals(Packet.Client.CHAT)) {
            final WrappedPacketInChat chat = new WrappedPacketInChat(e.getPacket());
            if (chat.getMessage().contains("pls jump")) {
                WrappedPacketOutEntityVelocity velocity = new WrappedPacketOutEntityVelocity(e.getPlayer(), 0, 0.1, 0);
                PacketEvents.sendPacket(e.getPlayer(), velocity);
            } else if (chat.getMessage().contains("pls swing")) {
                WrappedPacketOutAnimation anim = new WrappedPacketOutAnimation(e.getPlayer(), EntityAnimationType.SWING_MAIN_ARM);
                PacketEvents.sendPacket(e.getPlayer(), anim);
            }
        }
    }
}
