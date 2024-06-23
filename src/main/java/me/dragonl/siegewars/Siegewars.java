package me.dragonl.siegewars;

import io.fairyproject.FairyLaunch;
import io.fairyproject.log.Log;
import io.fairyproject.plugin.Plugin;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

@FairyLaunch
public class Siegewars extends Plugin {
    @Override
    public void onPluginEnable() {
        Log.info("Plugin Enabled.");

        ScoreboardManager sm = Bukkit.getScoreboardManager();
        Scoreboard sb = sm.getMainScoreboard();
        sb.getTeams().forEach(Team::unregister);
    }
}