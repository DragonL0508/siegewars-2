package me.dragonl.siegewars.itemStack;

import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.game.MapObjectCatcher;
import me.dragonl.siegewars.itemStack.items.gameplay.TNTItem;
import me.dragonl.siegewars.yaml.MapConfig;
import me.dragonl.siegewars.yaml.element.MapConfigElement;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@InjectableComponent
public class TNTParticleRunTime extends BukkitRunnable {
    private final GameStateManager gameStateManager;
    private final TNTItem tntItem;
    private final MapConfig mapConfig;
    private final MapObjectCatcher catcher;

    public TNTParticleRunTime(GameStateManager gameStateManager, TNTItem tntItem, MapConfig mapConfig, MapObjectCatcher catcher) {
        this.gameStateManager = gameStateManager;
        this.tntItem = tntItem;
        this.mapConfig = mapConfig;
        this.catcher = catcher;
    }

    @Override
    public void run() {
        if (gameStateManager.isCurrentGameState(GameState.IN_GAME)) {
            TNTItemParticle();
        }
    }

    private void TNTItemParticle() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (!mapConfig.getMaps().containsKey(p.getWorld().getName()))
                return;

            MapConfigElement element = mapConfig.getMaps().get(p.getWorld().getName());
            if (tntItem.isSimilar(p.getItemInHand())) {
                element.getDestroyableWallList().forEach(wall -> {
                    if (catcher.isSaved(wall))
                        return;

                    int minX = wall.getPosition2().getBlockX();
                    int maxX = wall.getPosition1().getBlockX();
                    int minY = wall.getPosition2().getBlockY();
                    int maxY = wall.getPosition1().getBlockY();
                    int minZ = wall.getPosition2().getBlockZ();
                    int maxZ = wall.getPosition1().getBlockZ();

                    showCuboid(p, minX, minY, minZ, maxX, maxY, maxZ);
                });
            }
        });
    }

    public static void showCuboid(Player p, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        World world = p.getWorld();
        double step = 0.5;

        double[] xArr = {minX, maxX + 1};
        double[] yArr = {minY, maxY + 1};
        double[] zArr = {minZ, maxZ + 1};

        for (double x = xArr[0]; x < xArr[1]; x += step)
            for (double y : yArr)
                for (double z : zArr) {
                    p.spigot().playEffect(new Location(world, x, y, z), Effect.COLOURED_DUST, 0, 1, 1, 0, 0, 0, 0, 32);
                }
        for (double y = yArr[0]; y < yArr[1]; y += step)
            for (double x : xArr)
                for (double z : zArr) {
                    world.spigot().playEffect(new Location(world, x, y, z), Effect.COLOURED_DUST, 0, 1, 1, 0, 0, 0, 0, 32);
                }
        for (double z = zArr[0]; z < zArr[1]; z += step)
            for (double y : yArr)
                for (double x : xArr) {
                    world.spigot().playEffect(new Location(world, x, y, z), Effect.COLOURED_DUST, 0, 1, 1, 0, 0, 0, 0, 32);
                }
    }

    @PostInitialize
    public void init() {
        this.runTaskTimer(BukkitPlugin.INSTANCE, 0, 5);
    }
}
