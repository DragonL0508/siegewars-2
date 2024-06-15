package me.dragonl.siegewars.team;

import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.log.Log;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.scheduler.BukkitRunnable;

@InjectableComponent
public class TeamSetup extends BukkitRunnable {
    private final TeamManager teamManager;

    public TeamSetup(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @Override
    public void run() {
        Log.info("Teams Setting up...");
        Team teamA = teamManager.createTeam("A");
        Team teamB = teamManager.createTeam("B");
        Team spectator = teamManager.createTeam("spectator");
        Team lobby = teamManager.createTeam("lobby");

        teamA.setColor(ChatColor.GOLD);
        teamA.setPrefix(ChatColor.GOLD + "[隊伍A] ");
        teamA.setFriendlyFire(false);
        teamA.setNametagVisibility(NametagVisibility.hideForOtherTeams);
        teamA.setPrivateChat(true);

        teamB.setColor(ChatColor.AQUA);
        teamB.setPrefix(ChatColor.AQUA + "[隊伍B] ");
        teamB.setFriendlyFire(false);
        teamB.setNametagVisibility(NametagVisibility.hideForOtherTeams);
        teamB.setPrivateChat(true);

        spectator.setColor(ChatColor.DARK_GRAY);
        spectator.setPrefix(ChatColor.DARK_GRAY + "[觀戰者] ");
        spectator.setPrivateChat(true);

        lobby.setColor(ChatColor.GRAY);
        lobby.setPrefix(ChatColor.GRAY + "[大廳玩家] ");
    }

    @PostInitialize
    public void init(){
        this.runTask(BukkitPlugin.INSTANCE);
    }
}
