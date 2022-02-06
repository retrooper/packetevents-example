package main;

import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import org.bukkit.entity.Player;


public class PacketEventsListener extends SimplePacketListenerAbstract {

    public PacketEventsListener() {
        super(PacketListenerPriority.NORMAL, true);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            WrapperPlayClientClickWindow clickWindow = new WrapperPlayClientClickWindow(event);
            //If the window ID is 0, we are dealing with the player inventory
            if (clickWindow.getWindowId() == 0) {
                ItemStack itemStack = clickWindow.getClickedItemStack();
                //Chicken egg max amount is 16
                if (itemStack.getType().equals(ItemTypes.EGG) &&
                        itemStack.getAmount() == ItemTypes.EGG.getMaxAmount()) {
                    player.sendMessage("You have clicked on fully stacked chicken eggs!");
                } else if (itemStack.getType().equals(ItemTypes.DIAMOND_SWORD)) {
                    player.sendMessage("You have clicked on a diamond sword!");
                }
            }
        }
    }
}
