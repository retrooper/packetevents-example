package main;

import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.npc.NPC;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.MojangAPIUtil;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PacketEventsPacketListener extends SimplePacketListenerAbstract {
    private final Map<UUID, NPC> NPC_MAP = new HashMap<>();

    public PacketEventsPacketListener() {
        super(PacketListenerPriority.HIGH);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        Player player = (Player) event.getPlayer();
        switch (event.getPacketType()) {
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
                    List<TextureProperty> skin = MojangAPIUtil.requestPlayerTextureProperties(player.getUniqueId());
                    npc = new NPC(new UserProfile(UUID.randomUUID(), displayName, skin),
                            SpigotReflectionUtil.generateEntityId(),
                            null,
                            NamedTextColor.RED,
                            null,
                            null);
                    npc.setLocation(new Location(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
                            player.getLocation().getYaw(), player.getLocation().getPitch()));
                    npc.spawn(event.getChannel());
                    NPC_MAP.put(player.getUniqueId(), npc);
                    player.sendMessage("Spawned: " + npc.getProfile().getName());
                } else if (message.equals("destroy npc")) {
                    NPC npc = NPC_MAP.get(player.getUniqueId());
                    if (npc != null) {
                        npc.despawn(event.getChannel());
                        player.sendMessage("Despawned: " + npc.getProfile().getName());
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
                    npc.updateLocation(to);
                }
                break;
            }
            case PLAYER_ROTATION: {
                NPC npc = NPC_MAP.get(player.getUniqueId());
                if (npc != null) {
                    WrapperPlayClientPlayerRotation rotationPacket = new WrapperPlayClientPlayerRotation(event);
                    float yaw = rotationPacket.getYaw();
                    float pitch = rotationPacket.getPitch();
                    npc.updateRotation(yaw, pitch);
                }
                break;
            }
            case PLAYER_POSITION_AND_ROTATION: {
                NPC npc = NPC_MAP.get(player.getUniqueId());
                if (npc != null) {
                    WrapperPlayClientPlayerPositionAndRotation positionAndRotationPacket
                            = new WrapperPlayClientPlayerPositionAndRotation(event);
                    //Make sure wrapper names are consistent with packet types
                    Location to = new Location(positionAndRotationPacket.getPosition(), positionAndRotationPacket.getYaw(), positionAndRotationPacket.getPitch());
                    npc.updateLocation(to);
                }
                break;
            }
        }
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {

    }
}
