package me.dragonl.siegewars;

import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.log.Log;
import me.dragonl.siegewars.yaml.MainConfig;
import me.dragonl.siegewars.yaml.MapConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitRunnable;

@InjectableComponent
public class WorldSetup extends BukkitRunnable {
    private final MapConfig mapConfig;

    public WorldSetup(MapConfig mapConfig) {
        this.mapConfig = mapConfig;
    }

    public void worldSetup(World world){
        Log.info(world.getName() + " is currently setting up...");

        world.setGameRuleValue("mobGriefing","false");
        world.setGameRuleValue("doDaylightCycle","false");
        world.setGameRuleValue("doMobSpawning","false");
        world.setGameRuleValue("keepInventory","true");
        world.setGameRuleValue("sendCommandFeedback","false");
        world.setGameRuleValue("logAdminCommands","false");
        world.setGameRuleValue("doFireTick","false");
        world.setGameRuleValue("naturalRegeneration","false");
        world.setGameRuleValue("doTileDrops","false");

        world.setTime(12000);
        world.setStorm(false);
        world.setWeatherDuration(2147483647);
        world.setAutoSave(false);
    }

    public void bukkitCreateWorld(String worldName){
        Bukkit.broadcastMessage(ChatColor.YELLOW + "正在創建世界(" + worldName + "), 請稍後...");
        WorldCreator wc = new WorldCreator(worldName);
        wc.createWorld();

        Bukkit.broadcastMessage(ChatColor.GREEN + worldName + " 創建完成!");
    }

    @Override
    public void run() {
        mapConfig.getMaps().forEach((map, element) -> {
            bukkitCreateWorld(map);
        });
        Bukkit.getWorlds().forEach(this::worldSetup);
    }

    @PostInitialize
    public void init(){
        this.runTask(BukkitPlugin.INSTANCE);
    }
}
