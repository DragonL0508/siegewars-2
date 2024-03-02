package me.dragonl.siegewars.team;

import io.fairyproject.container.InjectableComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

@InjectableComponent
public class TeamManager {
    List<Team> teams = new ArrayList<>();

    public Team createTeam(String name){
        Team team = new Team();
        team.setDisplayName(name);
        teams.add(team);

        return team;
    }

    public boolean isInTeam(Player player){
        for(Team team : teams){
            if(team.getPlayers().contains(player.getUniqueId()))
                return true;
        }
        return false;
    }

    public void joinTeam(Player player, Team team){
        if(isInTeam(player))
            leaveTeam(player);
        team.addPlayer(player);

        //message
        player.sendMessage(ChatColor.GREEN + "You joined the team : " + team.getColor() + team.getDisplayName());
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

        //message
        player.sendMessage( ChatColor.RED + "You left the team : " + team.getColor() + team.getDisplayName());
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
}
