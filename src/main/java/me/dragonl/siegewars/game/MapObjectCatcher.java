package me.dragonl.siegewars.game;

import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.yaml.element.DestroyableWallElement;
import me.dragonl.siegewars.yaml.element.DestroyableWindowElement;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@InjectableComponent
public class MapObjectCatcher {
    private Map<DestroyableWallElement, List<BlockState>> destroyableWalls = new HashMap<>();
    private Map<DestroyableWindowElement, List<BlockState>> destroyableWindow = new HashMap<>();

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

    public boolean isSaved(DestroyableWallElement element){
        return destroyableWalls.containsKey(element);
    }

    public boolean isSaved(DestroyableWindowElement element){
        return destroyableWindow.containsKey(element);
    }
}
