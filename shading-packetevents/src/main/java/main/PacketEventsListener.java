package main;

import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import org.bukkit.ChatColor;

import java.net.InetSocketAddress;


public class PacketEventsListener extends SimplePacketListenerAbstract {
    public PacketEventsListener() {
        super(PacketListenerPriority.NORMAL);
    }

    @Override
    public void onUserConnect(UserConnectEvent event) {
        User user = event.getUser();
        InetSocketAddress address = user.getAddress();
        System.out.println("User has joined with IP: " + address.getHostString() + ":" + address.getPort());
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        User user = event.getUser();
        //System.out.println("A user has disconnected!");
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        User user = event.getUser();
        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            WrapperPlayClientClickWindow clickWindow = new WrapperPlayClientClickWindow(event);
            //If the window ID is 0, we are dealing with the player inventory
            if (clickWindow.getWindowId() == 0) {
                ItemStack itemStack = clickWindow.getCarriedItemStack();
                //Chicken egg max amount is 16
                if (itemStack.getType().equals(ItemTypes.EGG) &&
                        itemStack.getAmount() == ItemTypes.EGG.getMaxAmount()) {
                    user.sendMessage(ChatColor.GOLD + "You have clicked on fully stacked chicken eggs!");
                } else if (itemStack.getType().equals(ItemTypes.DIAMOND_SWORD)) {
                    user.sendMessage(ChatColor.AQUA + "You have clicked on a diamond sword!");
                }
            }
        }
    }
}
