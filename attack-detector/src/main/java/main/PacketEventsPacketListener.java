package main;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.Dimension;
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
        //Whenever the player sends an entity interaction packet.
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity interactEntity = new WrapperPlayClientInteractEntity(event);
            WrapperPlayClientInteractEntity.InteractAction action = interactEntity.getAction();
            if (action == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                int entityID = interactEntity.getEntityId();
                //Find the Bukkit entity
                Entity entity = SpigotConversionUtil.getEntityById(null, entityID);


                //Create a chat component with the Adventure API
                Component message = Component.text("You attacked an entity.")
                        .hoverEvent(HoverEvent.hoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                Component.text("Entity Name: " + entity.getName())
                                        .color(NamedTextColor.GREEN)
                                        .decorate(TextDecoration.BOLD)
                                        .decorate(TextDecoration.ITALIC)
                        ));
                //Send it to the cross-platform user
                user.sendMessage(message);
            }
        }
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
