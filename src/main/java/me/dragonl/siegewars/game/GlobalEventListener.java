package me.dragonl.siegewars.game;

import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.bukkit.events.player.PlayerDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTagService;
import me.dragonl.siegewars.game.events.PlayerJoinTeamEvent;
import me.dragonl.siegewars.game.events.SpecialAbilityEndEvent;
import me.dragonl.siegewars.game.events.SpecialAbilityStartEvent;
import me.dragonl.siegewars.player.NameTagTemporaryManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.swing.plaf.PanelUI;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@InjectableComponent
@RegisterAsListener
public class GlobalEventListener implements Listener {
    private final NameTagService nameTagService;
    private final NameTagTemporaryManager nameTagTemporaryManager;
    private final TeamManager teamManager;

    public GlobalEventListener(NameTagService nameTagService, NameTagTemporaryManager nameTagTemporaryManager, TeamManager teamManager) {
        this.nameTagService = nameTagService;
        this.nameTagTemporaryManager = nameTagTemporaryManager;
        this.teamManager = teamManager;
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void getAchievement(PlayerAchievementAwardedEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        nameTagService.update(MCPlayer.from(event.getPlayer()));
    }

    @EventHandler
    public void onJoinTeam(PlayerJoinTeamEvent event){
        nameTagService.update(MCPlayer.from(event.getPlayer()));
    }

    @EventHandler
    public void specialAbilityStart(SpecialAbilityStartEvent event){
        nameTagService.update(MCPlayer.from(event.getPlayer()));
    }

    @EventHandler
    public void specialAbilityEnd(SpecialAbilityEndEvent event){
        nameTagService.update(MCPlayer.from(event.getPlayer()));
    }

    //Team NameTag trigger
    @EventHandler
    public void onPlayerDamage(PlayerDamageByPlayerEvent event){
        if(teamManager.getPlayerTeam(event.getPlayer()).isShowNameTagToClicker() == true){
            Player target = event.getPlayer();
            Player player = event.getDamager();

            nameTagTemporaryManager.startDisplayToPlayer(target, player);
            nameTagService.update(MCPlayer.from(target));

            BukkitTask nameTagRemoveTask = new BukkitRunnable(){
                @Override
                public void run() {
                    nameTagTemporaryManager.getPlayerSetMap().get(target).remove(player);
                    nameTagService.update(MCPlayer.from(target));
                }
            }.runTaskLater(BukkitPlugin.INSTANCE, 25);
        }
    }
}