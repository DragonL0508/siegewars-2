package me.dragonl.siegewars.player;

import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.team.Team;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@InjectableComponent
public class NameGetter {
    private final TeamManager teamManager;

    public NameGetter(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    public String getChatName(Player player) {
        if (teamManager.isInTeam(player)) {
            Team team = teamManager.getPlayerTeam(player);
            return team.getPrefix() + team.getColor() + player.getName() + team.getSuffix() + ChatColor.RESET;
        }
        return "NoTeam " + player.getName();
    }

    public String getNameWithTeamColor(Player player) {
        if (teamManager.isInTeam(player)) {
            Team team = teamManager.getPlayerTeam(player);
            return team.getColor() + player.getName() + ChatColor.RESET;
        }
        return player.getName();
    }
}
