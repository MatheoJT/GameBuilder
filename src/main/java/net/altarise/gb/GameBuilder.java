package net.altarise.gb;

import net.altarise.gb.bedwars.Bedwars;
import net.altarise.gb.bedwars.Commands;
import net.altarise.gb.utils.AbstractInventory;
import net.altarise.gb.utils.BlockBreakTool;
import net.altarise.gb.utils.PlayerInput;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class GameBuilder extends JavaPlugin {

    private static GameBuilder instance;

    private final HashMap<Player, Bedwars> bedwars = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
            saveDefaultConfig();
        }

        instance = this;
        final Commands bwCommands = new Commands();
        getCommand("world").setExecutor(new WorldCommand());
        getCommand("bedwars").setExecutor(bwCommands);

        Bukkit.getPluginManager().registerEvents(bwCommands, this);
        Bukkit.getPluginManager().registerEvents(new AbstractInventory.handler(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakTool.BreakListener(), this);
        PlayerInput.register(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static GameBuilder INSTANCE() {
        return instance;
    }



    public File createFile(String name) {
        File file = new File(getDataFolder(), name + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
                return file;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }



    public HashMap<Player, Bedwars> getBedwars() {
        return bedwars;
    }
}
