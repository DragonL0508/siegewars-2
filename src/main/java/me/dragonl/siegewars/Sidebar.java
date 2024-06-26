package me.dragonl.siegewars;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.sidebar.SidebarAdapter;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.game.ingame.ingameTimer.InGameTimerManager;
import me.dragonl.siegewars.game.ingame.ingameTimer.Timer;
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
    private final InGameTimerManager inGameTimerManager;

    public Sidebar(GameStateManager gameStateManager, TeamManager teamManager, PlayerKitManager playerKitManager, PlayerDataManager playerDataManager, InGameTimerManager inGameTimerManager) {
        this.gameStateManager = gameStateManager;
        this.teamManager = teamManager;
        this.playerKitManager = playerKitManager;
        this.playerDataManager = playerDataManager;
        this.inGameTimerManager = inGameTimerManager;
    }

    @Override
    public Component getTitle(MCPlayer player) {
        return Component.text("§eSiege§6wars §f§lII");
    }

    @Override
    public List<Component> getLines(MCPlayer player) {
        Player bukkitPlayer = player.as(Player.class);
        PlayerData playerData = playerDataManager.getPlayerData(bukkitPlayer);
        if (gameStateManager.isCurrentGameState(GameState.IN_LOBBY) || gameStateManager.isCurrentGameState(GameState.PREPARING)) {
            return Arrays.asList(
                    Component.text("§7§m--------------------"),
                    Component.text("§f玩家: §6" + player.getName()),
                    Component.text("§f隊伍: " + teamManager.getPlayerTeam(bukkitPlayer).getColor() + teamManager.getPlayerTeam(bukkitPlayer).getDisplayName()),
                    Component.text("§f延遲: §6" + player.getPing() + "§6ms"),
                    Component.text(""),
                    Component.text("§f遊戲階段: §6" + gameStateManager.getCurrentGameState().toString()),
                    Component.text("§7§m--------------------")
            );
        } else if (gameStateManager.isCurrentGameState(GameState.IN_GAME)) {

            Timer timerRunningNow = inGameTimerManager.getTimers().get(0).getValue();
            if(inGameTimerManager.getTimerRunningNow() != null)
                timerRunningNow = inGameTimerManager.getTimerRunningNow();

            return Arrays.asList(
                    Component.text("§7§m--------------------"),
                    Component.text("§6" + gameStateManager.getScoreA() + " §7- §b" + gameStateManager.getScoreB()),
                    Component.text("§f回合: §6" + gameStateManager.getRound()),
                    Component.text(""),
                    Component.text("§f玩家: §6" + player.getName()),
                    Component.text("§f隊伍: §6" + teamManager.getPlayerTeam(bukkitPlayer).getColor() + teamManager.getPlayerTeam(bukkitPlayer).getDisplayName()),
                    Component.text("§f職業: §6" + playerKitManager.getPlayerKit(bukkitPlayer).getKitName()),
                    Component.text(""),
                    Component.text("§f金錢: §6" + playerData.getMoney() + "$"),
                    Component.text("§f擊殺: §6" + playerData.getKills()),
                    Component.text(""),
                    Component.text("§f當前: §6" + timerRunningNow.getName()),
                    Component.text("§f" + inGameTimerManager.getNextTimer(timerRunningNow).getName() + ": §6" + inGameTimerManager.getFormattedTimer(timerRunningNow)),
                    Component.text("§7§m--------------------")
            );
        }
        return null;
    }
}