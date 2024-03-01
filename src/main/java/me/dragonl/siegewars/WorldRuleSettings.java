package me.dragonl.siegewars;

import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.log.Log;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

@InjectableComponent
public class WorldRuleSettings extends BukkitRunnable {
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
    }

    @Override
    public void run() {
        Bukkit.getWorlds().forEach(this::worldSetup);
    }

    @PostInitialize
    public void init(){
        this.runTask(BukkitPlugin.INSTANCE);
    }
}
