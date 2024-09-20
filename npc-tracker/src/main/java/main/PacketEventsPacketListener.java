package main;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.enchantment.Enchantment;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PacketEventsPacketListener implements PacketListener {
    private FakeArmorStand fakeArmorStand = null;

    @Override
    public void onUserLogin(UserLoginEvent event) {
        User user = event.getUser();
        Player player = event.getPlayer();

        // Access the user's current location
        Location spawnLocation = SpigotConversionUtil.fromBukkitLocation(player.getLocation());

        // Create the Armor Stand (if we haven't already)
        if (fakeArmorStand == null) {
            // Generate a random UUID
            UUID uuid = UUID.randomUUID();
            // Generate an Entity ID
            int entityId = SpigotReflectionUtil.generateEntityId();

            fakeArmorStand = new FakeArmorStand(uuid, entityId);
        }
        // Spawn the Armor Stand at the user's current location
        fakeArmorStand.spawn(user, spawnLocation);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = event.getUser();
        Player player = (Player) event.getPlayer();
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);
            int entityId = packet.getEntityId();

            // Check if the client interacted with the Armor Stand (compare their IDs)
            if (entityId == fakeArmorStand.entityId) {
                //Increment their clicks
                int clicks = fakeArmorStand.clicks.getOrDefault(user.getUUID(), 0) + 1;
                fakeArmorStand.clicks.put(user.getUUID(), clicks);
                user.sendMessage("You now have " + clicks + " clicks on the Armor Stand!");
            }
        }
    }

    private static class FakeArmorStand {
        private final int entityId;
        private final UUID uuid;
        // Track their clicks
        private final Map<UUID, Integer> clicks = new ConcurrentHashMap<>();

        public FakeArmorStand(UUID uuid, int entityId) {
            this.uuid = uuid;
            this.entityId = entityId;
        }

        /**
         * Code that spawns an Armor Stand for the player.
         */
        public void spawn(User user, Location location) {
            WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(
                    entityId,
                    uuid,
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