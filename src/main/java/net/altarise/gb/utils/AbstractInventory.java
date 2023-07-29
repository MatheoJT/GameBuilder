package net.altarise.gb.utils;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AbstractInventory implements InventoryHolder, Listener {


    private final String name;
    private final int size = 9*6;
    private final Inventory inventory;
    private static final Map<Integer, Consumer<InventoryClickEvent>> itemHandlers  = new HashMap<>();



    public AbstractInventory(String name) {
        this.name = name;
        this.inventory = Bukkit.createInventory(this, size, name);

    }

    public AbstractInventory() {
        this.name = "default";
        this.inventory = Bukkit.createInventory(this, size, name);
    }







    public void open(Player player) {
        player.openInventory(inventory);
    }









    public void setItem(int slot, ItemStack it) {
        setItem(slot, it, null);
    }


    public void setItem(int slot, ItemStack it, Consumer<InventoryClickEvent> eventConsumer) {
        this.inventory.setItem(slot, it);
        if(eventConsumer != null) {
            itemHandlers.put(slot, eventConsumer);
        }
    }

    public void setItems(int slot1, int slot2, ItemStack it) {
        setItems(slot1, slot2, it, null);
    }

    public void setItems(int slot1, int slot2, ItemStack it, Consumer<InventoryClickEvent> eventConsumer) {
        for(int i = slot1; i <= slot2; i++) {
            setItem(i, it, eventConsumer);
        }
    }




    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public static class handler implements Listener {

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if(event.getInventory().getHolder() instanceof AbstractInventory) {
                event.setCancelled(true);
                if(itemHandlers.containsKey(event.getSlot())) {
                    itemHandlers.get(event.getSlot()).accept(event);
                }
            }
        }

        @EventHandler
        public void ondrop(PlayerDropItemEvent event) {
        if(event.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof AbstractInventory) {
            event.setCancelled(true);
        }
    }

    }


}
