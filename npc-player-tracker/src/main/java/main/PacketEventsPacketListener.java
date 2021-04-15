package main;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.chat.WrappedPacketInChat;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.utils.npc.NPC;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PacketEventsPacketListener extends PacketListenerAbstract {
    public final Map<UUID, Integer> npcTrackerMap = new ConcurrentHashMap<>();

    /*
     * Example
     * npctracker begin
     * npctracker end
     */
    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        Player player = event.getPlayer();
        if (event.getPacketId() == PacketType.Play.Client.CHAT) {
            WrappedPacketInChat wrappedPacketInChat = new WrappedPacketInChat(event.getNMSPacket());
            String message = wrappedPacketInChat.getMessage();
            String[] args = message.split(" ");
            if (message.startsWith("npctracker ") && args.length == 2) {
                if (args[1].equalsIgnoreCase("begin")) {
                    NPC npc = new NPC(player.getName() + " Clone", player.getLocation());
                    npc.spawn(player);
                    //Register the NPC so we can access the very same NPC by its ID again.
                    PacketEvents.get().getServerUtils().getNPCManager().registerNPC(npc);
                    npcTrackerMap.put(player.getUniqueId(), npc.getEntityId());
                    player.sendMessage(ChatColor.GREEN + "Successfully started your NPC tracker!");
                } else if (args[1].equalsIgnoreCase("end")) {
                    Integer npcEntityID = npcTrackerMap.get(player.getUniqueId());
                    if (npcEntityID != null) {
                        NPC npc = PacketEvents.get().getServerUtils().getNPCManager().getNPCById(npcEntityID);
                        if (npc != null) {
                            npc.despawn(player);
                            player.sendMessage(ChatColor.RED + "Ended your NPC tracker.");
                        } else {
                            player.sendMessage(ChatColor.DARK_RED + "Failed to find the NPC.");
                        }
                    } else {
                        player.sendMessage(ChatColor.DARK_RED + "Failed to end the NPC tracker. You never began the tracker on this player.");

                    }
                }
            }
        } else if (PacketType.Play.Client.Util.isInstanceOfFlying(event.getPacketId())) {
            Integer npcEntityID = npcTrackerMap.get(player.getUniqueId());
            NPC npc = npcEntityID == null ? null : PacketEvents.get().getServerUtils().getNPCManager().getNPCById(npcEntityID);
            if (npc != null) {
                WrappedPacketInFlying wrappedPacketInFlying = new WrappedPacketInFlying(event.getNMSPacket());
                boolean isLook = wrappedPacketInFlying.isLook();
                boolean isPos = wrappedPacketInFlying.isPosition();
                List<Player> players = new ArrayList<>();
                players.add(player);
                if (isPos && isLook) {
                    npc.moveAndRotate(players,
                            new Vector3d(wrappedPacketInFlying.getX(), wrappedPacketInFlying.getY(), wrappedPacketInFlying.getZ()),
                            wrappedPacketInFlying.getYaw(),
                            wrappedPacketInFlying.getPitch());
                }
                else if (isPos) {
                    npc.move(players, new Vector3d(wrappedPacketInFlying.getX(), wrappedPacketInFlying.getY(), wrappedPacketInFlying.getZ()));
                }
                else if (isLook) {
                    npc.rotate(players, wrappedPacketInFlying.getYaw(), wrappedPacketInFlying.getPitch());
                }

            }
        }
    }
}
