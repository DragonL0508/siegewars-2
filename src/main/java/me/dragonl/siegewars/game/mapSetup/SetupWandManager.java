package me.dragonl.siegewars.game.mapSetup;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.util.Position;
import me.dragonl.siegewars.yaml.MapConfig;
import me.dragonl.siegewars.yaml.element.MapConfigElement;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@InjectableComponent
public class SetupWandManager {
    private final MapConfig mapConfig;
    private Map<UUID, Location> playerSelection1 = new HashMap<>();
    private Map<UUID, Location> playerSelection2 = new HashMap<>();

    public SetupWandManager(MapConfig mapConfig) {
        this.mapConfig = mapConfig;
    }

    public Map<UUID, Location> getPlayerSelection1() {
        return playerSelection1;
    }

    public void setPlayerSelection1(Map<UUID, Location> playerSelection1) {
        this.playerSelection1 = playerSelection1;
    }

    public Map<UUID, Location> getPlayerSelection2() {
        return playerSelection2;
    }

    public void setPlayerSelection2(Map<UUID, Location> playerSelection2) {
        this.playerSelection2 = playerSelection2;
    }

    public boolean isDestroyableWall(Position position) {
        AtomicReference<Boolean> bool = new AtomicReference<>(false);
        MapConfigElement element = mapConfig.getMaps().get(position.getWorld());

        element.getDestroyableWallList().forEach(wall -> {
            int minX = Math.min(wall.getPosition1().getBlockX(), wall.getPosition2().getBlockX());
            int maxX = Math.max(wall.getPosition1().getBlockX(), wall.getPosition2().getBlockX());
            int minY = Math.min(wall.getPosition1().getBlockY(), wall.getPosition2().getBlockY());
            int maxY = Math.max(wall.getPosition1().getBlockY(), wall.getPosition2().getBlockY());
            int minZ = Math.min(wall.getPosition1().getBlockZ(), wall.getPosition2().getBlockZ());
            int maxZ = Math.max(wall.getPosition1().getBlockZ(), wall.getPosition2().getBlockZ());

            if (position.getBlockX() >= minX && position.getBlockX() <= maxX
                    && position.getBlockY() >= minY && position.getBlockY() <= maxY
                    && position.getBlockZ() >= minZ && position.getBlockZ() <= maxZ)
                bool.set(true);
        });
        return bool.get();
    }
}
