package me.dragonl.siegewars.game.lobby;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;

@Command(value = "admin")
@InjectableComponent
public class LobbyAdminMenuCommand extends BaseCommand {
    @Command("menu")
    public void openMenu(BukkitCommandContext ctx){
        new LobbyAdminMenu().open(ctx.getPlayer());
    }
}
