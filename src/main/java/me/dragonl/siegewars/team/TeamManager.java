package me.dragonl.siegewars.team;

import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.events.PlayerJoinTeamEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.*;

@InjectableComponent
public class TeamManager {
    List<Team> teams = new ArrayList<>();

    public Team createTeam(String name){
        Team team = new Team();
        team.setDisplayName(name);
        teams.add(team);

        Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        org.bukkit.scoreboard.Team scoreboardTeam = mainScoreboard.registerNewTeam(name);
        scoreboardTeam.setAllowFriendlyFire(false);

        return team;
    }

    public boolean isInTeam(Player player){
        for(Team team : teams){
            if(team.getPlayers().contains(player.getUniqueId()))
                return true;
        }
        return false;
    }

    public boolean isInTeam(Player player, Team team){
        return team.getPlayers().contains(player.getUniqueId());
    }

    public void joinTeam(Player player, Team team){
        if(isInTeam(player))
            leaveTeam(player);
        team.addPlayer(player);

        //add to scoreboard team
        Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        org.bukkit.scoreboard.Team scoreboardTeam = mainScoreboard.getTeam(team.getDisplayName());
        scoreboardTeam.addPlayer(player);

        //message
        player.sendMessage(ChatColor.GREEN + "You joined the team : " + team.getColor() + team.getDisplayName());
        player.playSound(player.getLocation(), Sound.VILLAGER_YES,1,1.3f);

        //event call
        Bukkit.getPluginManager().callEvent(new PlayerJoinTeamEvent(player));
    }

    public void joinTeam(Player player, SiegeWarsTeam team){
        if(team.equals(SiegeWarsTeam.TeamA))
            joinTeam(player, getTeam("A"));
        else if(team.equals(SiegeWarsTeam.TeamB))
            joinTeam(player, getTeam("B"));
        else if(team.equals(SiegeWarsTeam.Lobby))
            joinTeam(player, getTeam("lobby"));
        else if(team.equals(SiegeWarsTeam.Spectator))
            joinTeam(player, getTeam("spectator"));
    }

    public void leaveTeam(Player player){
        Team team = getPlayerTeam(player);
        team.removePlayer(player);

        //remove from scoreboard team
        Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        org.bukkit.scoreboard.Team scoreboardTeam = mainScoreboard.getTeam(team.getDisplayName());
        scoreboardTeam.removePlayer(player);

        //message
        player.sendMessage( ChatColor.RED + "You left the team : " + team.getColor() + team.getDisplayName());

        //event call
        Bukkit.getPluginManager().callEvent(new PlayerJoinTeamEvent(player));
    }

    public Team getTeam(String name){
        for(Team team : teams){
            if (Objects.equals(team.getDisplayName(), name)){
                return team;
            }
        }
        return null;
    }

    public Team getPlayerTeam(Player player){
        for(Team team : teams){
            if(team.getPlayers().contains(player.getUniqueId()))
                return team;
        }
        return null;
    }

    public Team swGetAnotherTeam(Player player){
        Team team = getPlayerTeam(player);

        if(team.getDisplayName().equals("A")) return getTeam("B");
        if(team.getDisplayName().equals("B")) return getTeam("A");

        return null;
    }

    public void swTeamSplits(List<Player> players){
        players.forEach(this::leaveTeam);

        Team teamA = getTeam("A");
        Team teamB = getTeam("B");

        players.forEach(player -> {
            if(teamA.getPlayers().size() <= teamB.getPlayers().size())
                joinTeam(player,teamA);
            else
                joinTeam(player,teamB);
        });
    }
}
