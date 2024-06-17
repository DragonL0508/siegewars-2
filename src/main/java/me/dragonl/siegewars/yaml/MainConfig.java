package me.dragonl.siegewars.yaml;

import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.config.annotation.Comment;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.util.Position;
import me.dragonl.siegewars.Siegewars;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@InjectableComponent
public class MainConfig extends YamlConfiguration {
    private Position lobbySpawnLoc = BukkitPos.toMCPos(Bukkit.getWorlds().get(0).getSpawnLocation());

    public MainConfig(Siegewars plugin){
        super(plugin.getDataFolder().resolve("config.yml"));
        this.loadAndSave();
    }

    public Location getLobbySpawnLoc() {
        return BukkitPos.toBukkitLocation(lobbySpawnLoc);
    }

    public void setLobbySpawnLoc(Location lobbySpawnLoc) {
        this.lobbySpawnLoc = BukkitPos.toMCPos(lobbySpawnLoc);
    }
}