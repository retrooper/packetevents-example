package main;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.npc.NPC;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.gameprofile.GameProfile;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PacketEventsPacketListener extends PacketListenerAbstract {
    private final Map<UUID, NPC> NPC_MAP = new HashMap<>();

    public PacketEventsPacketListener() {
        super(PacketListenerPriority.HIGH, true);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getConnectionState() == ConnectionState.PLAY) {
            Player player = (event.getPlayer() != null ? (Player) event.getPlayer() : null);
            PacketType.Play.Client type = (PacketType.Play.Client) event.getPacketType();
            switch (type) {
                case CHAT_MESSAGE: {
                    WrapperPlayClientChatMessage chatMessage = new WrapperPlayClientChatMessage(event);
                    String message = chatMessage.getMessage();
                    if (message.equals("create npc")) {
                        String displayName = player.getDisplayName() + "_clone";
                        NPC npc = NPC_MAP.get(player.getUniqueId());
                        if (npc != null) {
                            player.sendMessage("NPC already exists");
                            return;
                        }
                        npc = new NPC(displayName, SpigotReflectionUtil.generateEntityId(), new GameProfile(UUID.randomUUID(), displayName));
                        npc.setLocation(new Location(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
                                player.getLocation().getYaw(), player.getLocation().getPitch()));
                        PacketEvents.getAPI().getNPCManager().spawn(event.getChannel(), npc);
                        NPC_MAP.put(player.getUniqueId(), npc);
                        player.sendMessage("Spawned: " + npc.getDisplayName());
                    } else if (message.equals("destroy npc")) {
                        NPC npc = NPC_MAP.get(player.getUniqueId());
                        if (npc != null) {
                            PacketEvents.getAPI().getNPCManager().despawn(event.getChannel(), npc);
                            player.sendMessage("Despawned: " + npc.getDisplayName());
                            NPC_MAP.remove(player.getUniqueId());
                        } else {
                            player.sendMessage("No NPC was ever spawned");
                        }
                    }
                    break;
                }
                case PLAYER_POSITION: {
                    NPC npc = NPC_MAP.get(player.getUniqueId());
                    if (npc != null) {
                        WrapperPlayClientPlayerPosition positionPacket = new WrapperPlayClientPlayerPosition(event);
                        Location to = npc.getLocation().clone();
                        to.setPosition(positionPacket.getPosition());
                        PacketEvents.getAPI().getNPCManager().updateNPCLocation(npc, to);
                    }
                    break;
                }
                case PLAYER_ROTATION: {
                    NPC npc = NPC_MAP.get(player.getUniqueId());
                    if (npc != null) {
                        WrapperPlayClientPlayerRotation rotationPacket = new WrapperPlayClientPlayerRotation(event);
                        float yaw = rotationPacket.getYaw();
                        float pitch = rotationPacket.getPitch();
                        PacketEvents.getAPI().getNPCManager().updateNPCRotation(npc, (byte) yaw, (byte) pitch);
                    }
                    break;
                }
                case PLAYER_POSITION_AND_ROTATION: {
                    NPC npc = NPC_MAP.get(player.getUniqueId());
                    if (npc != null) {
                        WrapperPlayClientPlayerPositionRotation positionAndRotationPacket = new WrapperPlayClientPlayerPositionRotation(event);
                        //Make sure wrapper names are consistent with packet types
                        Location to = new Location(positionAndRotationPacket.getPosition(), positionAndRotationPacket.getYaw(), positionAndRotationPacket.getPitch());
                        PacketEvents.getAPI().getNPCManager().updateNPCLocation(npc, to);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {

    }
}
