package me.dragonl.siegewars.team;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;

@Command(value = {"teamSelect","ts"})
@InjectableComponent
public class TeamSelectCommand extends BaseCommand {
    private final TeamSelectMenu teamSelectMenu;
    private boolean canTeamSelect = true;

    public TeamSelectCommand(TeamSelectMenu teamSelectMenu) {
        this.teamSelectMenu = teamSelectMenu;
    }
    @Command("menu")
    public void openMenu(BukkitCommandContext ctx){
        if(!canTeamSelect){
            ctx.getPlayer().sendMessage("¡±cPlayer team select is not allow now!");
            return;
        }
        teamSelectMenu.open(ctx.getPlayer());
    }

    @Command("toggle")
    public void toggleTeamSelect(BukkitCommandContext ctx){
        if(!ctx.getPlayer().isOp())
            return;
        canTeamSelect = !canTeamSelect;

        if(canTeamSelect)
            ctx.getPlayer().sendMessage("¡±aPlayer team select is now on!");
        else
            ctx.getPlayer().sendMessage("¡±cPlayer team select is now off!");
    }
}
