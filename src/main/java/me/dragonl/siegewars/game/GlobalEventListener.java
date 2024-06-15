package me.dragonl.siegewars.game;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

@InjectableComponent
@RegisterAsListener
public class GlobalEventListener implements Listener {
    @EventHandler
    public void onHunger(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void getAchievement(PlayerAchievementAwardedEvent event){
        event.setCancelled(true);
    }
}
