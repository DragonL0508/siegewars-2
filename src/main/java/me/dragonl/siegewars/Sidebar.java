package me.dragonl.siegewars;

import io.fairyproject.bukkit.util.LegacyAdventureUtil;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.sidebar.SidebarAdapter;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.team.TeamManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@InjectableComponent
public class Sidebar implements SidebarAdapter {
    private final GameStateManager gameStateManager;
    private final TeamManager teamManager;

    public Sidebar(GameStateManager gameStateManager, TeamManager teamManager) {
        this.gameStateManager = gameStateManager;
        this.teamManager = teamManager;
    }

    @Override
    public Component getTitle(MCPlayer player) {
        return LegacyAdventureUtil.decode("¡±eSiege¡±6wars ¡±f¡±lII");
    }

    @Override
    public List<Component> getLines(MCPlayer player) {
        Player bukkitPlayer = player.as(Player.class);
        if(gameStateManager.isCurrentState(GameState.IN_LOBBY)){
            return Arrays.asList(
                    LegacyAdventureUtil.decode("¡±7¡±m--------------------"),
                    LegacyAdventureUtil.decode("¡±ePlayer: ¡±6" + player.getName()),
                    LegacyAdventureUtil.decode("¡±eTeam: " + teamManager.getPlayerTeam(bukkitPlayer).getColor() + teamManager.getPlayerTeam(bukkitPlayer).getDisplayName()),
                    LegacyAdventureUtil.decode("¡±ePing: ¡±6" + player.getPing() + "¡±6ms"),
                    LegacyAdventureUtil.decode(""),
                    LegacyAdventureUtil.decode("¡±eGame State: ¡±6In Lobby"),
                    LegacyAdventureUtil.decode("¡±7¡±m--------------------")
            );
        }
        return null;
    }
}
