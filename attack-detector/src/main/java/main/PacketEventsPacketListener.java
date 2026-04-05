package main;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientAttack;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateHealth;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Entity;

public class PacketEventsPacketListener implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = event.getUser();
        // This code supports Minecraft 26.1 and newer.
        if (event.getPacketType() == PacketType.Play.Client.ATTACK) {
            WrapperPlayClientAttack attackPacket = new WrapperPlayClientAttack(event);
            // Execute our code here
            doSomething(user, attackPacket.getEntityId());
        }
        // This code works on Minecraft 1.8-1.21.11.
        else if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);
            WrapperPlayClientInteractEntity.InteractAction action = packet.getAction();
            // Check if the action is an ATTACK and not a right click
            if (action == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                // Execute our code here
                doSomething(user, packet.getEntityId());
            }
        }
    }

    public void doSomething(User user, int entityID) {
        // Find the Bukkit entity that was attacked (WARNING: unsafe method!)
        Entity entity = SpigotConversionUtil.getEntityById(null, entityID);

        // Create a chat component with the Adventure API
        Component message = Component.text("You attacked an entity.")
                .hoverEvent(HoverEvent.hoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        Component.text("Entity Name: " + entity.getName())
                                .color(NamedTextColor.GREEN)
                                .decorate(TextDecoration.BOLD)
                                .decorate(TextDecoration.ITALIC)));

        // Send a message to the user using PacketEvents
        user.sendMessage(message);

    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.UPDATE_HEALTH) {
            //Health of an entity was updated!
            WrapperPlayServerUpdateHealth packet = new WrapperPlayServerUpdateHealth(event);
            float health = packet.getHealth();
        }
    }
}
