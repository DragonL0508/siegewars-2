package me.dragonl.siegewars.game.ingame;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.game.RoundState;
import me.dragonl.siegewars.itemStack.items.gameplay.BombItem;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Command(value = "bomb")
@InjectableComponent
public class BombCommand extends BaseCommand {
    private final GameStateManager gameStateManager;
    private final BombItem bombItem;

    public BombCommand(GameStateManager gameStateManager, BombItem bombItem) {
        this.gameStateManager = gameStateManager;
        this.bombItem = bombItem;
    }

    @Command(value = "clearBombKeeper", permissionNode = "siegewars.admin")
    public void clearBombKeeper(BukkitCommandContext ctx) {
        gameStateManager.setBombClaimer(null);
    }

    @Command("claim")
    public void claimBomb(BukkitCommandContext ctx) {
        Player player = ctx.getPlayer();

        if (!gameStateManager.isCurrentGameState(GameState.IN_GAME) && !gameStateManager.isCurrentRoundState(RoundState.POSITION_CHOOSING)) {
            player.sendMessage("§c現在不能領取炸藥包！");
            return;
        }

        if (gameStateManager.getBombClaimer() != null) {
            player.sendMessage("§c已經有人領取炸藥包了！");
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
            return;
        }

        player.sendMessage("§a成功領取炸藥包！");
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
        gameStateManager.setBombClaimer(player.getUniqueId());
        player.getInventory().addItem(bombItem.get(player));
    }
}
