package me.dragonl.siegewars.game.lobby;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.game.Kit;
import me.dragonl.siegewars.game.kit.KitSelectLogic;
import me.dragonl.siegewars.game.preparing.PlayerPreparingManager;
import me.dragonl.siegewars.itemStack.items.PlayerPrepareItem;
import me.dragonl.siegewars.team.SiegeWarsTeam;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.entity.Player;

@Command(value = "admin",permissionNode = "siegewars.admin")
@InjectableComponent
public class LobbyAdminCommand extends BaseCommand {
    private final GameStateManager gameStateManager;
    private final KitSelectLogic kitSelectLogic;
    private final TeamManager teamManager;
    private final PlayerPreparingManager playerPreparingManager;
    private final PlayerPrepareItem playerPrepareItem;

    public LobbyAdminCommand(GameStateManager gameStateManager, KitSelectLogic kitSelectLogic, TeamManager teamManager, PlayerPreparingManager playerPreparingManager, PlayerPrepareItem playerPrepareItem) {
        this.gameStateManager = gameStateManager;
        this.kitSelectLogic = kitSelectLogic;
        this.teamManager = teamManager;
        this.playerPreparingManager = playerPreparingManager;
        this.playerPrepareItem = playerPrepareItem;
    }

    @Command("menu")
    public void openMenu(BukkitCommandContext ctx) {
        new LobbyAdminMenu(gameStateManager,playerPreparingManager, teamManager, playerPrepareItem).open(ctx.getPlayer());
    }

    @Command("setKit")
    public void setKit(BukkitCommandContext ctx, @Arg("kit") Kit kit) {
        kitSelectLogic.kitSelect(ctx.getPlayer(), kit);
    }

    @Command("setGameState")
    public void setGameState(BukkitCommandContext ctx, @Arg("gameState") GameState gameState) {
        gameStateManager.setCurrentGameState(gameState);
    }

    @Command("joinTeam")
    public void joinTeam(BukkitCommandContext ctx, @Arg("team") SiegeWarsTeam team, @Arg("player") Player player) {
        teamManager.joinTeam(player, team);
    }
}
