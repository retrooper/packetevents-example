package io.github.retrooper.examplepacketevents;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.ClientVersion;
import io.github.retrooper.packetevents.enums.EntityAnimationType;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.chat.WrappedPacketInChat;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
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
    public void onPacketSend(PacketSendEvent e) {
        if (e.getPacketName().equals(PacketType.Server.ENTITY_VELOCITY)) {
            WrappedPacketOutEntityVelocity velocityPacket = new WrappedPacketOutEntityVelocity(e.getNMSPacket());
            double velocityX = velocityPacket.getVelocityX();
            double velocityY = velocityPacket.getVelocityY();
            double velocityZ = velocityPacket.getVelocityZ();

            int ping = PacketEvents.getPing(e.getPlayer());

            final ClientVersion clientVersion = PacketEvents.getClientVersion(e.getPlayer());

        } else if (e.getPacketName().equals(PacketType.Server.CHAT)) {
            WrappedPacketOutChat chatPacket = new WrappedPacketOutChat(e.getNMSPacket());
            String message= chatPacket.getMessage();
        }
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        if (e.getPacketName().equals(PacketType.Client.CHAT)) {
            WrappedPacketInChat chat = new WrappedPacketInChat(e.getNMSPacket());
            if (chat.getMessage().contains("pls jump")) {
                WrappedPacketOutEntityVelocity velocity = new WrappedPacketOutEntityVelocity(e.getPlayer(), 0, 1, 0);
                PacketEvents.sendPacket(e.getPlayer(), velocity);
            } else if (chat.getMessage().contains("pls swing")) {
                WrappedPacketOutAnimation anim = new WrappedPacketOutAnimation(e.getPlayer(), EntityAnimationType.SWING_MAIN_ARM);
                PacketEvents.sendPacket(e.getPlayer(), anim);
            }
        }
        else if(PacketType.Util.isInstanceOfFlyingPacket(e.getNMSPacket())) {
            WrappedPacketInFlying flying = new WrappedPacketInFlying(e.getNMSPacket());
            boolean isLook = flying.isLook();
            boolean onGround = flying.isOnGround();

            double x = flying.getX(), y = flying.getY(), z = flying.getZ();

            float yaw = flying.getYaw();
            float pitch = flying.getPitch();
        }
    }
}
