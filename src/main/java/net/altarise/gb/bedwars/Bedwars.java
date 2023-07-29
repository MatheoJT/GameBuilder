package net.altarise.gb.bedwars;

import net.altarise.gb.GameBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bedwars {

        private final String gameType;
        private int maxPlayers;
        private int minPlayersStart;
        private int startTimer;
        private Location spawn;
        private int maxPerTeam;
        private final HashMap<String, Team> registeredTeams = new HashMap<>();
        private final World world;



        public Bedwars(Player player, String gameType) {
            this.world = player.getWorld();
            this.gameType = gameType;

        }



    public String getGameType() {
        return gameType;
    }



    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMinPlayersStart() {
        return minPlayersStart;
    }

    public void setMinPlayersStart(int minPlayersStart) {
        this.minPlayersStart = minPlayersStart;
    }

    public int getStartTimer() {
        return startTimer;
    }

    public void setStartTimer(int startTimer) {
        this.startTimer = startTimer;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public int getMaxPerTeam() {
        return maxPerTeam;
    }

    public void setMaxPerTeam(int maxPerTeam) {
        this.maxPerTeam = maxPerTeam;
    }

    public HashMap<String, Team> getRegisteredTeams() {
        return registeredTeams;
    }

    public World getWorld() {
        return world;
    }

    public HashMap<String, Team> getTeams() {
        return registeredTeams;
    }

    public boolean isFinish() {
        return maxPlayers != 0 && minPlayersStart != 0 && startTimer != 0 && spawn != null && maxPerTeam != 0 && registeredTeams.size() != 2;
    }

    public String notSet() {
        if(maxPlayers == 0) return "maxPlayers";
        if(minPlayersStart == 0) return "minPlayersStart";
        if(startTimer == 0) return "startTimer";
        if(spawn == null) return "spawn";
        if(maxPerTeam == 0) return "maxPerTeam";
        if(registeredTeams.size() != 2) return "registeredTeams";
        return null;
    }

    public void save() throws IOException {
            File file = GameBuilder.INSTANCE().createFile(world.getName());
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("game-type", gameType);
            config.createSection("properties");
            ConfigurationSection properties = config.getConfigurationSection("properties");
            properties.set("max-players", maxPlayers);
            properties.set("min-players-start", minPlayersStart);
            properties.set("start-timer", startTimer);
            properties.set("spawn", spawn.getWorld().getName() + "," + spawn.getX() + "," + spawn.getY() + "," + spawn.getZ() + "," + spawn.getYaw() + "," + spawn.getPitch());
            properties.set("max-per-team", maxPerTeam);

            config.createSection("teams");
            ConfigurationSection teams = config.getConfigurationSection("teams");
            for(String keys : registeredTeams.keySet()) {
                teams.createSection(keys);
                ConfigurationSection team = teams.getConfigurationSection(keys);
                team.set("name", registeredTeams.get(keys).getName());
                team.set("color", registeredTeams.get(keys).getColor());
                team.set("byte-color", registeredTeams.get(keys).getByteColor());
                team.set("spawn", registeredTeams.get(keys).getSpawn().getWorld().getName() + ", " + registeredTeams.get(keys).getSpawn().getX() + ", " + registeredTeams.get(keys).getSpawn().getY() + ", " + registeredTeams.get(keys).getSpawn().getZ() + ", " + registeredTeams.get(keys).getSpawn().getYaw() + ", " + registeredTeams.get(keys).getSpawn().getPitch());
                team.set("generator", registeredTeams.get(keys).getGenerator().getWorld().getName() + ", " + registeredTeams.get(keys).getGenerator().getX() + ", " + registeredTeams.get(keys).getGenerator().getY() + ", " + registeredTeams.get(keys).getGenerator().getZ() + ", " + registeredTeams.get(keys).getGenerator().getYaw() + ", " + registeredTeams.get(keys).getGenerator().getPitch());
                team.set("shop", registeredTeams.get(keys).getShop().getWorld().getName() + ", " + registeredTeams.get(keys).getShop().getX() + ", " + registeredTeams.get(keys).getShop().getY() + ", " + registeredTeams.get(keys).getShop().getZ() + ", " + registeredTeams.get(keys).getShop().getYaw() + ", " + registeredTeams.get(keys).getShop().getPitch());
                team.set("upgrade", registeredTeams.get(keys).getUpgrade().getWorld().getName() + ", " + registeredTeams.get(keys).getUpgrade().getX() + ", " + registeredTeams.get(keys).getUpgrade().getY() + ", " + registeredTeams.get(keys).getUpgrade().getZ() + ", " + registeredTeams.get(keys).getUpgrade().getYaw() + ", " + registeredTeams.get(keys).getUpgrade().getPitch());
                team.set("protected-area-1", registeredTeams.get(keys).getProtectedArea1().getWorld().getName() + ", " + registeredTeams.get(keys).getProtectedArea1().getX() + ", " + registeredTeams.get(keys).getProtectedArea1().getY() + ", " + registeredTeams.get(keys).getProtectedArea1().getZ() + ", " + registeredTeams.get(keys).getProtectedArea1().getYaw() + ", " + registeredTeams.get(keys).getProtectedArea1().getPitch());
                team.set("protected-area-2", registeredTeams.get(keys).getProtectedArea2().getWorld().getName() + ", " + registeredTeams.get(keys).getProtectedArea2().getX() + ", " + registeredTeams.get(keys).getProtectedArea2().getY() + ", " + registeredTeams.get(keys).getProtectedArea2().getZ() + ", " + registeredTeams.get(keys).getProtectedArea2().getYaw() + ", " + registeredTeams.get(keys).getProtectedArea2().getPitch());
                team.set("bed-locations", registeredTeams.get(keys).getBeds());

            }

            config.save(file);
            final List<String> worlds = GameBuilder.INSTANCE().getConfig().getStringList("worlds") == null ? new ArrayList<>() : GameBuilder.INSTANCE().getConfig().getStringList("worlds");
            worlds.add(world.getName());
            GameBuilder.INSTANCE().getConfig().set("worlds", worlds);
            GameBuilder.INSTANCE().saveConfig();
        }

}
