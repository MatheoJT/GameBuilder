package net.altarise.gb.bedwars;

import net.altarise.gb.GameBuilder;
import net.altarise.gb.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Commands implements CommandExecutor, Listener {


    private final ItemStack menu = new ItemBuilder(Material.NETHER_STAR).displayname("§6§lBedWars Menu").build();


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        Player player = (Player) commandSender;

        if(GameBuilder.INSTANCE().getBedwars().containsKey(player)) {
            commandSender.sendMessage("§cYou already creating a BedWars game");
            commandSender.sendMessage("§cPlease finish creating before creating a new one or use cancel button in the menu");
            return true;
        }



        if(args.length == 0) {
            commandSender.sendMessage("§cUsage: /bedwars <create/delete>");
            return true;
        }

        List<String> worlds = GameBuilder.INSTANCE().getConfig().getStringList("worlds");


        if(args[0].equalsIgnoreCase("create")) {

        if(worlds != null && worlds.contains(player.getWorld().getName())) {
            commandSender.sendMessage("§aWorld is already configured");
            commandSender.sendMessage("§aUse /bedwars delete to recreate the world");
            commandSender.sendMessage("§4CAUTION: This will delete previous world data");
               return false;
            } else {
            player.getInventory().setItem(8, menu);
            player.sendMessage("§aYour now creating a BedWars game");
                player.sendMessage("§cUse the menu to edit the game");

                GameBuilder.INSTANCE().getBedwars().put(player, new Bedwars(player, "Bedwars"));
                return true;
            }
        }

        if(args[0].equalsIgnoreCase("delete")) {
            if(worlds != null && worlds.contains(player.getWorld().getName())) {
                worlds.remove(player.getWorld().getName());
                GameBuilder.INSTANCE().getConfig().set("worlds", worlds);
                GameBuilder.INSTANCE().saveConfig();
                GameBuilder.INSTANCE().createFile(player.getWorld().getName()).delete();
                commandSender.sendMessage("§aWorld has been deleted");
                return true;
            } else {
                commandSender.sendMessage("§cWorld is not configured");
                return false;
            }
        }

        commandSender.sendMessage("§cUsage: /bedwars <create/delete>");


        return false;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getItem() == null) return;
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if(event.getPlayer().getItemInHand().equals(menu)) {
                new Inventory(event.getPlayer()).open(event.getPlayer());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(event.getItemDrop().getItemStack().equals(menu)) {
            event.setCancelled(true);
            new Inventory(event.getPlayer()).open(event.getPlayer());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().equals(menu) && !event.getWhoClicked().getOpenInventory().getTitle().equals("§6§lBedWars Menu")) {
            event.setCancelled(true);
            new Inventory((Player) event.getWhoClicked()).open((Player) event.getWhoClicked());
        }
    }

}
