package me.dragonl.siegewars;

import io.fairyproject.bukkit.util.LegacyAdventureUtil;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.sidebar.SidebarAdapter;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.player.data.PlayerData;
import me.dragonl.siegewars.player.data.PlayerDataManager;
import me.dragonl.siegewars.team.TeamManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@InjectableComponent
public class Sidebar implements SidebarAdapter {
    private final GameStateManager gameStateManager;
    private final TeamManager teamManager;
    private final PlayerKitManager playerKitManager;
    private final PlayerDataManager playerDataManager;

    public Sidebar(GameStateManager gameStateManager, TeamManager teamManager, PlayerKitManager playerKitManager, PlayerDataManager playerDataManager) {
        this.gameStateManager = gameStateManager;
        this.teamManager = teamManager;
        this.playerKitManager = playerKitManager;
        this.playerDataManager = playerDataManager;
    }

    @Override
    public Component getTitle(MCPlayer player) {
        return LegacyAdventureUtil.decode("§eSiege§6wars §f§lII");
    }

    @Override
    public List<Component> getLines(MCPlayer player) {
        Player bukkitPlayer = player.as(Player.class);
        PlayerData playerData = playerDataManager.getPlayerData(bukkitPlayer);
        if(gameStateManager.isCurrentGameState(GameState.IN_LOBBY) || gameStateManager.isCurrentGameState(GameState.PREPARING)){
            return Arrays.asList(
                    LegacyAdventureUtil.decode("§7§m--------------------"),
                    LegacyAdventureUtil.decode("§7玩家: §6" + player.getName()),
                    LegacyAdventureUtil.decode("§7隊伍: " + teamManager.getPlayerTeam(bukkitPlayer).getColor() + teamManager.getPlayerTeam(bukkitPlayer).getDisplayName()),
                    LegacyAdventureUtil.decode("§7延遲: §6" + player.getPing() + "§6ms"),
                    LegacyAdventureUtil.decode(""),
                    LegacyAdventureUtil.decode("§7遊戲階段: §6" + gameStateManager.getCurrentGameState().toString()),
                    LegacyAdventureUtil.decode("§7§m--------------------")
            );
        } else if (gameStateManager.isCurrentGameState(GameState.IN_GAME)) {
            return Arrays.asList(
                    LegacyAdventureUtil.decode("§7§m--------------------"),
                    LegacyAdventureUtil.decode("§6" + gameStateManager.getScoreA() + " §7- §b" + gameStateManager.getScoreB()),
                    LegacyAdventureUtil.decode("§7回合: §6" + gameStateManager.getRound()),
                    LegacyAdventureUtil.decode(""),
                    LegacyAdventureUtil.decode("§7玩家: §6" + player.getName()),
                    LegacyAdventureUtil.decode("§7職業: §6" + playerKitManager.getPlayerKitString(bukkitPlayer)),
                    LegacyAdventureUtil.decode(""),
                    LegacyAdventureUtil.decode("§7金錢: §6" + playerData.getMoney() + "$"),
                    LegacyAdventureUtil.decode("§7擊殺: §6" + playerData.getKills()),
                    LegacyAdventureUtil.decode(""),
                    LegacyAdventureUtil.decode("§7分數: §6" + playerData.getScore()),
                    LegacyAdventureUtil.decode("§7§m--------------------")
            );
        }
        return null;
    }
}