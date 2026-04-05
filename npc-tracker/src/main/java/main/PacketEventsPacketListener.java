package main;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientAttack;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PacketEventsPacketListener implements PacketListener {

    private @Nullable FakeArmorStand fakeArmorStand = null;

    @Override
    public void onUserLogin(UserLoginEvent event) {
        User user = event.getUser();
        Player player = event.getPlayer();

        // Create the Armor Stand (if we haven't already)
        if (this.fakeArmorStand == null) {
            // Generate a random UUID
            UUID uuid = UUID.randomUUID();
            // Generate an Entity ID
            int entityId = SpigotReflectionUtil.generateEntityId();

            this.fakeArmorStand = new FakeArmorStand(uuid, entityId);
        }

        // Spawn the Armor Stand at the user's current location
        Location spawnLocation = SpigotConversionUtil.fromBukkitLocation(player.getLocation());
        this.fakeArmorStand.spawn(user, spawnLocation);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = event.getUser();
        int entityId = -1; // Not an attack or interaction packet if entityId remains -1
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            // They interacted with an entity.
            WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);
            // Retrieve that entity's ID
            entityId = packet.getEntityId();
        }
        else if (event.getPacketType() == PacketType.Play.Client.ATTACK) {
            // They attacked the Armor Stand
            WrapperPlayClientAttack attack = new WrapperPlayClientAttack(event);
            entityId = attack.getEntityId();
        }


        // Check if the client interacted with (or attacked) the Armor Stand
        if (entityId != -1 && this.fakeArmorStand != null && entityId == this.fakeArmorStand.entityId) {
            // Increment their clicks
            int clicks = this.fakeArmorStand.clicks.getOrDefault(user.getUUID(), 0) + 1;
            this.fakeArmorStand.clicks.put(user.getUUID(), clicks);
            user.sendMessage("You now have " + clicks + " clicks on the Armor Stand!");
        }
    }

    private static class FakeArmorStand {

        private final int entityId;
        private final UUID uuid;

        // track their clicks
        private final Map<UUID, Integer> clicks = new ConcurrentHashMap<>();

        public FakeArmorStand(UUID uuid, int entityId) {
            this.uuid = uuid;
            this.entityId = entityId;
        }

        public void spawn(User user, Location location) {
            WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(
                    this.entityId,
                    this.uuid,
                    EntityTypes.ARMOR_STAND,
                    location,
                    location.getYaw(), // Head yaw
                    0, // No additional data
                    null // We won't specify any initial velocity
            );
            user.sendPacket(packet);
        }
    }
}