package me.dragonl.siegewars.team;

import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.log.Log;
import org.bukkit.ChatColor;
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
        teamA.setPrefix(ChatColor.GOLD + "[TeamA] ");
        teamA.setFriendlyFire(false);
        teamA.setNametagVisibility(NametagVisibility.hideForOtherTeams);
        teamA.setPrivateChat(true);

        teamB.setColor(ChatColor.AQUA);
        teamB.setPrefix(ChatColor.AQUA + "[TeamB] ");
        teamB.setFriendlyFire(false);
        teamB.setNametagVisibility(NametagVisibility.hideForOtherTeams);
        teamB.setPrivateChat(true);

        spectator.setColor(ChatColor.DARK_GRAY);
        spectator.setPrefix(ChatColor.DARK_GRAY + "[Spec] ");
        spectator.setPrivateChat(true);

        lobby.setColor(ChatColor.GRAY);
        lobby.setPrefix(ChatColor.GRAY + "[Lobby] ");
    }

    @PostInitialize
    public void init(){
        this.runTask(BukkitPlugin.INSTANCE);
    }
}
