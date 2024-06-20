package me.dragonl.siegewars.game;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.util.Position;
import me.dragonl.siegewars.yaml.element.DestroyableWallElement;
import me.dragonl.siegewars.yaml.element.DestroyableWindowElement;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.*;

@InjectableComponent
public class MapObjectCatcher {
    private Map<DestroyableWallElement, List<BlockState>> destroyableWalls = new HashMap<>();
    private Map<DestroyableWindowElement, List<BlockState>> destroyableWindow = new HashMap<>();
    private List<Location> bafflePlaced = new ArrayList<>();

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
