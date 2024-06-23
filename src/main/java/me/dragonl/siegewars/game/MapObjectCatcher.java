package me.dragonl.siegewars.game;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.util.Position;
import me.dragonl.siegewars.yaml.MapConfig;
import me.dragonl.siegewars.yaml.element.DestroyableWallElement;
import me.dragonl.siegewars.yaml.element.DestroyableWindowElement;
import me.dragonl.siegewars.yaml.element.MapConfigElement;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@InjectableComponent
public class MapObjectCatcher {
    private Map<DestroyableWallElement, List<BlockState>> destroyableWalls = new HashMap<>();
    private Map<DestroyableWindowElement, List<BlockState>> destroyableWindow = new HashMap<>();
    private List<Location> bafflePlaced = new ArrayList<>();
    private final MapConfig mapConfig;

    public MapObjectCatcher(MapConfig mapConfig) {
        this.mapConfig = mapConfig;
    }

    public List<Location> getBafflePlaced() {
        return bafflePlaced;
    }

    public void setBafflePlaced(List<Location> bafflePlaced) {
        this.bafflePlaced = bafflePlaced;
    }

    public Map<DestroyableWallElement, List<BlockState>> getDestroyableWalls() {
        return destroyableWalls;
    }

    public void setDestroyableWalls(Map<DestroyableWallElement, List<BlockState>> destroyableWalls) {
        this.destroyableWalls = destroyableWalls;
    }

    public Map<DestroyableWindowElement, List<BlockState>> getDestroyableWindow() {
        return destroyableWindow;
    }

    public void setDestroyableWindow(Map<DestroyableWindowElement, List<BlockState>> destroyableWindow) {
        this.destroyableWindow = destroyableWindow;
    }

    public boolean isSaved(DestroyableWallElement element) {
        return destroyableWalls.containsKey(element);
    }

    public boolean isSaved(DestroyableWindowElement element) {
        return destroyableWindow.containsKey(element);
    }

    public boolean isBaffle(Location location) {
        if (bafflePlaced.contains(location))
            return true;
        while (location.add(0, -1, 0).getBlock().getType() == Material.ACACIA_FENCE) {
            if (bafflePlaced.contains(location))
                return true;
        }
        return false;
    }

    public boolean isDestroyableWall(Position position) {
        AtomicReference<Boolean> bool = new AtomicReference<>(false);
        MapConfigElement element = mapConfig.getMaps().get(position.getWorld());

        element.getDestroyableWallList().forEach(wall -> {
            int minX = wall.getPosition2().getBlockX();
            int maxX = wall.getPosition1().getBlockX();
            int minY = wall.getPosition2().getBlockY();
            int maxY = wall.getPosition1().getBlockY();
            int minZ = wall.getPosition2().getBlockZ();
            int maxZ = wall.getPosition1().getBlockZ();

            if (position.getBlockX() >= minX && position.getBlockX() <= maxX
                    && position.getBlockY() >= minY && position.getBlockY() <= maxY
                    && position.getBlockZ() >= minZ && position.getBlockZ() <= maxZ)
                bool.set(true);
        });
        return bool.get();
    }

    public boolean isDestroyableWindow(Position position) {
        AtomicReference<Boolean> bool = new AtomicReference<>(false);
        MapConfigElement element = mapConfig.getMaps().get(position.getWorld());

        element.getDestroyableWindowList().forEach(window -> {
            int minX = window.getPosition2().getBlockX();
            int maxX = window.getPosition1().getBlockX();
            int minY = window.getPosition2().getBlockY();
            int maxY = window.getPosition1().getBlockY();
            int minZ = window.getPosition2().getBlockZ();
            int maxZ = window.getPosition1().getBlockZ();

            if (position.getBlockX() >= minX && position.getBlockX() <= maxX
                    && position.getBlockY() >= minY && position.getBlockY() <= maxY
                    && position.getBlockZ() >= minZ && position.getBlockZ() <= maxZ)
                bool.set(true);
        });
        return bool.get();
    }

    public List<Location> getBafflesGroup(Location location) {
        if (bafflePlaced.contains(location))
            return getBafflesBeside(location.clone());
        else {
            while (location.add(0, -1, 0).getBlock().getType() == Material.ACACIA_FENCE) {
                if(bafflePlaced.contains(location)){
                    return getBafflesBeside(location.clone());
                }
            }
        }
        return new ArrayList<>();
    }

    private List<Location> getBafflesBeside(Location location) {
        List<Location> baffles = new ArrayList<>();
        baffles.add(location.clone());
        Location clone = location.clone();

        while (location.add(1,0,0).getBlock().getType() == Material.ACACIA_FENCE){
            baffles.add(location.clone());
        }
        location = clone.clone();
        while (location.add(-1,0,0).getBlock().getType() == Material.ACACIA_FENCE){
            baffles.add(location.clone());
        }
        location = clone.clone();
        while (location.add(0,0,1).getBlock().getType() == Material.ACACIA_FENCE){
            baffles.add(location.clone());
        }
        location = clone.clone();
        while (location.add(0,0,-1).getBlock().getType() == Material.ACACIA_FENCE){
            baffles.add(location.clone());
        }

        return baffles;
    }
}
