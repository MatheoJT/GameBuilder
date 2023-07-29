package net.altarise.gb.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.HashMap;
import java.util.function.Consumer;

public class BlockBreakTool {

    private static final HashMap<Player, Consumer<BlockBreakEvent>> playerConsumerHashMap = new HashMap<>();

    public BlockBreakTool(Player player, Consumer<BlockBreakEvent> eventConsumer) {
        player.getInventory().setItem(0, new ItemBuilder(Material.STICK).displayname("§aBlockBreakTool").build());
        player.sendMessage("§aBreak the block to get the location");
        playerConsumerHashMap.put(player, eventConsumer);
    }


    public static class BreakListener implements Listener {


        @EventHandler
        public void onBlockBreak(BlockBreakEvent event) {
            Player player = event.getPlayer();

            if (playerConsumerHashMap.containsKey(player)) {
                Location location = event.getBlock().getLocation();
                playerConsumerHashMap.get(player).accept(event);
                player.sendMessage("§aSet block at " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
                player.getInventory().removeItem(new ItemBuilder(Material.STICK).displayname("§aBlockBreakTool").build());
                event.setCancelled(true);
                playerConsumerHashMap.remove(player);
            }
        }

        @EventHandler
        public void onDrop(PlayerDropItemEvent event) {
            Player player = event.getPlayer();
            if (playerConsumerHashMap.containsKey(player) && event.getItemDrop().getItemStack().getType() == Material.STICK) {
                event.setCancelled(true);
            }

            }


        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            if (playerConsumerHashMap.containsKey(player) && event.getCurrentItem().getType() == Material.STICK) {
                event.setCancelled(true);
            }
        }
    }




}
