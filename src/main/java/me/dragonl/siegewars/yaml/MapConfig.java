package me.dragonl.siegewars.yaml;

import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.util.Position;
import me.dragonl.siegewars.Siegewars;
import me.dragonl.siegewars.WorldSetup;
import me.dragonl.siegewars.yaml.element.MapConfigElement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@InjectableComponent
public class MapConfig extends YamlConfiguration {
    @ElementType(MapConfigElement.class)
    private Map<String, MapConfigElement> maps = new HashMap<>();

    public MapConfig(Siegewars plugin){
        super(plugin.getDataFolder().resolve("maps.yml"));

        this.loadAndSave();
    }

    public void addMap(String name){
        maps.put(name, new MapConfigElement());
        this.save();
    }

    public Map<String, MapConfigElement> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, MapConfigElement> maps) {
        this.maps = maps;
    }
}