package me.dragonl.siegewars.player;

import io.fairyproject.bukkit.events.player.PlayerDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.team.Team;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RegisterAsListener
@InjectableComponent
public class PlayerFightListener implements Listener {
    private final TeamManager teamManager;

    public PlayerFightListener(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @EventHandler
    public void onPvp(PlayerDamageByPlayerEvent event){
        Team playerTeam = teamManager.getPlayerTeam(event.getPlayer());
        Team damagerTeam = teamManager.getPlayerTeam(event.getDamager());
        if(playerTeam == damagerTeam && !playerTeam.isFriendlyFire()){
            event.setCancelled(true);
        }
    }
}
