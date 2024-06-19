package me.dragonl.siegewars.game;

import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.yaml.element.DestroyableWallElement;
import me.dragonl.siegewars.yaml.element.DestroyableWindowElement;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@InjectableComponent
public class MapObjectDestroyer {
    private final MapObjectCatcher mapObjectCatcher;

    public MapObjectDestroyer(MapObjectCatcher mapObjectCatcher) {
        this.mapObjectCatcher = mapObjectCatcher;
    }

    public void destroyWall(DestroyableWallElement element) {
        World w = Bukkit.getWorld(element.getPosition1().getWorld());
        int x2 = element.getPosition1().getBlockX(), y2 = element.getPosition1().getBlockY(), z2 = element.getPosition1().getBlockZ();
        int x1 = element.getPosition2().getBlockX(), y1 = element.getPosition2().getBlockY(), z1 = element.getPosition2().getBlockZ();
        mapObjectCatcher.getDestroyableWalls().put(element, new ArrayList<>());
        List<BlockState> blockStates = mapObjectCatcher.getDestroyableWalls().get(element);

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    blockStates.add(w.getBlockAt(x, y, z).getState());
                    w.spigot().playEffect(new Location(w, x + 0.5, y + 0.5, z + 0.5), Effect.STEP_SOUND, w.getBlockAt(x, y, z).getType().getId(), 0, 0.25F, 0.25F, 0.25F, 1, 5, 32);
                    w.getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }
    }

    public void destroyWindow(DestroyableWindowElement element) {
        World w = Bukkit.getWorld(element.getPosition1().getWorld());
        int x2 = element.getPosition1().getBlockX(), y2 = element.getPosition1().getBlockY(), z2 = element.getPosition1().getBlockZ();
        int x1 = element.getPosition2().getBlockX(), y1 = element.getPosition2().getBlockY(), z1 = element.getPosition2().getBlockZ();
        mapObjectCatcher.getDestroyableWindow().put(element, new ArrayList<>());
        List<BlockState> blockStates = mapObjectCatcher.getDestroyableWindow().get(element);

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    blockStates.add(w.getBlockAt(x, y, z).getState());
                    w.spigot().playEffect(new Location(w, x + 0.5, y + 0.5, z + 0.5), Effect.STEP_SOUND, w.getBlockAt(x, y, z).getType().getId(), 0, 0.25F, 0.25F, 0.25F, 1, 5, 32);
                    w.getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }
    }
}
