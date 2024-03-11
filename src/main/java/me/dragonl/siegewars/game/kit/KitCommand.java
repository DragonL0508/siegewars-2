package me.dragonl.siegewars.game.kit;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.player.PlayerKitManager;

@Command(value = "kit")
@InjectableComponent
public class KitCommand extends BaseCommand {
    private final PlayerKitManager playerKitManager;

    public KitCommand(PlayerKitManager playerKitManager) {
        this.playerKitManager = playerKitManager;
    }

    @Command("menu")
    public void openMenu(BukkitCommandContext ctx) {
        new KitSelectMenu(playerKitManager).open(ctx.getPlayer());
    }
}
