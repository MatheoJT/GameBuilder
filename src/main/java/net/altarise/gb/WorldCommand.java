package net.altarise.gb;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class WorldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(args.length == 0) {
            commandSender.sendMessage("§cUsage: /world [name]");
            return true;
        }

        if(!worldExist(args[0])) {

            commandSender.sendMessage("§cWorld not found");
            return true;
        }
        commandSender.sendMessage("§aLoading and teleporting to world " + args[0] + " please wait...");
        Bukkit.getServer().createWorld(new WorldCreator(args[0]));

        commandSender.sendMessage("§aWorld name: " + Bukkit.getWorld(args[0]).getName());

        List<String> worlds = GameBuilder.INSTANCE().getConfig().getStringList("worlds");
        if(worlds != null && worlds.contains(args[0])) {
            commandSender.sendMessage("§aWorld is configured");
            ConfigurationSection worldSection = YamlConfiguration.loadConfiguration(new File(GameBuilder.INSTANCE().getDataFolder(), args[0] + ".yml"));
            commandSender.sendMessage("§aWorld game type: " + worldSection.getString("game-type"));
        } else {
            commandSender.sendMessage("§cWorld is not configured");
        }

        Player player = (Player) commandSender;
        player.teleport(Bukkit.getWorld(args[0]).getSpawnLocation());


        return true;
    }

    private boolean worldExist(String name) {
        return new File(Bukkit.getWorldContainer(), name).exists();
    }
}

