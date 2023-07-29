package net.altarise.gb.bedwars;

import net.altarise.gb.GameBuilder;
import net.altarise.gb.utils.AbstractInventory;
import net.altarise.gb.utils.BlockBreakTool;
import net.altarise.gb.utils.ItemBuilder;
import net.altarise.gb.utils.PlayerInput;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class Inventory extends AbstractInventory {


        private boolean isEditing = false;

        public Inventory(Player player) {
        super("Bedwars Builder");

            if(!GameBuilder.INSTANCE().getBedwars().containsKey(player)) {
                player.sendMessage("§cYou are not editing a Bedwars game!");
                player.sendMessage("§cPlease use /bedwars <create>");
                player.closeInventory();
                return;
            }

            isEditing = true;

            Bedwars bedwars = GameBuilder.INSTANCE().getBedwars().get(player);
            ItemStack information = new ItemBuilder(Material.PAPER).displayname("§6§lCreating bedwars map").build();
            ItemStack cancel = new ItemBuilder(Material.BARRIER).displayname("§c§lCancel").build();
            ItemStack save = new ItemBuilder(Material.EMERALD).displayname("§a§lSave").build();

            ItemStack maxPerTeam = new ItemBuilder(Material.APPLE).displayname("§6§lMax players per team").lore("§7Current: §e" + bedwars.getMaxPerTeam(), "", "§6Click §eto change").build();

            String spawnX = bedwars.getSpawn() == null ? "§cNot set" : String.valueOf(bedwars.getSpawn().getBlockX());
            String spawnY = bedwars.getSpawn() == null ? "§cNot set" : String.valueOf(bedwars.getSpawn().getBlockY());
            String spawnZ = bedwars.getSpawn() == null ? "§cNot set" : String.valueOf(bedwars.getSpawn().getBlockZ());
            ItemStack spawn = new ItemBuilder(Material.BED).displayname("§6§lSpawn").lore("§7Current: §e" + spawnX + " " + spawnY + " " + spawnZ, "", "§6Click §eto change").build();


            setItem(4, information);
            setItem(0, cancel, event -> {
                player.closeInventory();
                player.sendMessage("§cBedwars creation cancelled");
                player.getInventory().clear();
                GameBuilder.INSTANCE().getBedwars().remove(player);
            });
            setItem(8, save, event -> {

                if(!bedwars.isFinish()) {
                    player.sendMessage("§cYou need to finish the game before saving");
                    player.sendMessage("§4Missing: §c" + bedwars.notSet());
                    return;
                }


                player.closeInventory();
                player.sendMessage("§aBedwars creation saved");
                player.getInventory().clear();
                try {
                    bedwars.save();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                GameBuilder.INSTANCE().getBedwars().remove(player);
            });

            /*setItem(10, maxPerTeam, event -> {
                player.closeInventory();
                new SignInput(player, consummer -> {
                    bedwars.setMaxPerTeam(Integer.parseInt(consummer.getLines()[0].getText()));
                    new Inventory(player).open(player);
                }, null, "§8^^^^^^^^", "§8Input", "§8MaxPerTeam");
            });


             */

            ItemStack chatTest = new ItemStack(Material.BOOK);
            ItemStack anvilTest = new ItemStack(Material.ANVIL);
            ItemStack signTest = new ItemStack(Material.SIGN);

            setItem(10, chatTest, event -> {
                player.closeInventory();
                new PlayerInput(player, "§6§lINFORMATION\n§7Veuillez entrer un text:").chat(consummer -> {
                    player.sendMessage("§aYou entered: " + consummer.message);
                    new Inventory(player).open(player);
                });
            });


            setItem(11, anvilTest, event -> {
                player.closeInventory();
                new PlayerInput(player, "ceci est un test").anvil(Material.PAPER, consummer -> {
                    player.sendMessage("§aYou entered: " + consummer.message);
                    new Inventory(player).open(player);
                });
            });



            setItem(12, signTest, event -> {
                player.closeInventory();
                new PlayerInput(player, "\n^^^^^^\ninput\n").sign(consummer -> {
                    player.sendMessage("§aYou entered: " + consummer.lines[0].getText());
                    new Inventory(player).open(player);
                });
            });

            setItem(24, spawn, event -> {
                player.closeInventory();
                new BlockBreakTool(player, consummer -> {
                    bedwars.setSpawn(consummer.getBlock().getLocation());
                    new Inventory(player).open(player);
                });
            });

        }

    @Override
    public void open(Player player) {
        super.open(player);
        if (!isEditing) player.closeInventory();
    }

        /*

        Bedwars bw = new Bedwars(player, "Bedwars");
                bw.setMaxPerTeam(10);
                bw.setMaxPlayers(50);
                bw.setSpawn(player.getLocation());
                bw.setMinPlayersStart(20);
                bw.setStartTimer(30);
                Team team = new Team("Rouge");
                team.addBed(player.getLocation());
                team.setColor("c");
                team.setByteColor((byte) 14);
                team.setGenerator(player.getLocation());
                team.addBed(player.getLocation().clone().add(0, 0, 1));
                team.setSpawn(player.getLocation().clone().add(0, 0, 2));
                team.setShop(player.getLocation().clone().add(0, 0, 3));
                team.setUpgrade(player.getLocation().clone().add(0, 0, 4));
                team.setProtectedArea1(player.getLocation().clone().add(0, 0, 4));
                team.setProtectedArea2(player.getLocation().clone().add(0, 0, 5));
                bw.getTeams().put("red", team);

            try {
                bw.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
         */


}
