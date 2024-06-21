package me.dragonl.siegewars.game.ingame.preparing;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import me.dragonl.siegewars.game.ingame.InGameTimer;
import me.dragonl.siegewars.game.kit.KitManager;
import me.dragonl.siegewars.game.kit.KitMenu;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Bukkit;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@InjectableComponent
public class InGamePreparingExecuter {
    private final TeamManager teamManager;
    private final KitMenu kitMenu;
    private final PlayerKitManager playerKitManager;
    private final KitManager kitManager;
    private final InGameTimer inGameTimer;

    public InGamePreparingExecuter(TeamManager teamManager, KitMenu kitMenu, PlayerKitManager playerKitManager, KitManager kitManager, InGameTimer inGameTimer) {
        this.teamManager = teamManager;
        this.kitMenu = kitMenu;
        this.playerKitManager = playerKitManager;
        this.kitManager = kitManager;
        this.inGameTimer = inGameTimer;
    }

    public void start(){
        Bukkit.getOnlinePlayers().forEach(p -> {
            if(!teamManager.isInTeam(p,teamManager.getTeam("spectator"))){
                playerKitManager.setPlayerKit(p, kitManager.getKit("none"));
                kitMenu.open(p);
            }
        });

        inGameTimer.startTimer();
        MCSchedulers.getGlobalScheduler().schedule(this::gamePosChoosingLogic,20 * 35);
    }

    private void gamePosChoosingLogic(){

    }
}
