package me.dragonl.siegewars.game.ingame;

import com.cryptomorin.xseries.messages.Titles;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.game.ingame.ingameTimer.BombTimer;
import me.dragonl.siegewars.game.ingame.ingameTimer.InGameTimerManager;
import me.dragonl.siegewars.game.ingame.ingameTimer.Timer;
import me.dragonl.siegewars.game.ingame.ingameTimer.TimerMap;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

@InjectableComponent
public class BombManager {
    private final InGameTimerManager timerManager;
    private final InGameRunTime inGameRunTime;
    private Location bombLocation;
    private Boolean isBombPlanted = false;

    public BombManager(InGameTimerManager timerManager, InGameRunTime inGameRunTime) {
        this.timerManager = timerManager;
        this.inGameRunTime = inGameRunTime;
    }

    public void plant(Player player, Location location) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            Titles.sendTitle(p, 0, 50, 10, "", "§c炸藥包已被放置");
            p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1, 1.25f);
        });

        this.bombLocation = location;

        //timers
        Timer running = timerManager.getTimerRunningNow();
        List<TimerMap> timerMaps = timerManager.getTimers();
        Timer bombTimer = timerManager.registerTimer(new BombTimer(50, inGameRunTime), timerMaps.indexOf(timerManager.getTimerMap(running.getID())) + 1);

        timerManager.stopTimer(timerManager.getTimerRunningNow());
        timerManager.startTimer(bombTimer);
    }
}
