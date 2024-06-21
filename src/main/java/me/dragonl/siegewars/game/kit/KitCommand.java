package me.dragonl.siegewars.game.kit;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.player.PlayerKitManager;

@Command(value = "kit", permissionNode = "siegewars.admin")
@InjectableComponent
public class KitCommand extends BaseCommand {
    private final KitMenu kitMenu;

    public KitCommand(KitMenu kitMenu) {
        this.kitMenu = kitMenu;
    }

    @Command("menu")
    public void openMenu(BukkitCommandContext ctx) {
        kitMenu.open(ctx.getPlayer());
    }

    @Command("openMenuAndDeop")
    public void openMenuAndDeop(BukkitCommandContext ctx) {
        kitMenu.open(ctx.getPlayer());
        ctx.getPlayer().setOp(false);
    }
}
