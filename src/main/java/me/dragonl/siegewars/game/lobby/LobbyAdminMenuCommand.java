package me.dragonl.siegewars.game.lobby;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.game.Kit;

@Command(value = "admin")
@InjectableComponent
public class LobbyAdminMenuCommand extends BaseCommand {
    private final GameStateManager gameStateManager;

    public LobbyAdminMenuCommand(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    @Command("menu")
    public void openMenu(BukkitCommandContext ctx) {
        new LobbyAdminMenu().open(ctx.getPlayer());
    }

    @Command("kit")
    public void setKit(BukkitCommandContext ctx, @Arg("kit") Kit kit) {
        if (kit == Kit.ATTACKER)
            ctx.getPlayer().sendMessage("ATTACKER!");
    }

    @Command("setGameState")
    public void setGameState(BukkitCommandContext ctx, @Arg("gameState") GameState gameState) {
        gameStateManager.setCurrentState(gameState);
    }
}
