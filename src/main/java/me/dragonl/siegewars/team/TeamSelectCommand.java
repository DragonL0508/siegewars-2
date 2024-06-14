package me.dragonl.siegewars.team;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;

@Command(value = {"teamSelect","ts"})
@InjectableComponent
public class TeamSelectCommand extends BaseCommand {
    private final TeamSelectMenu teamSelectMenu;
    private final GameStateManager gameStateManager;
    private boolean canTeamSelect = true;

    public TeamSelectCommand(TeamSelectMenu teamSelectMenu, GameStateManager gameStateManager) {
        this.teamSelectMenu = teamSelectMenu;
        this.gameStateManager = gameStateManager;
    }
    @Command("menu")
    public void openMenu(BukkitCommandContext ctx){
        if(!canTeamSelect || !gameStateManager.isCurrentGameState(GameState.IN_LOBBY)){
            ctx.getPlayer().sendMessage("§c現在不允許玩家選擇隊伍!");
            return;
        }
        teamSelectMenu.open(ctx.getPlayer());
    }

    @Command(value = "toggle",permissionNode = "siegewars.admin")
    public void toggleTeamSelect(BukkitCommandContext ctx){
        canTeamSelect = !canTeamSelect;

        if(canTeamSelect)
            ctx.getPlayer().sendMessage("§aPlayer team select is now on!");
        else
            ctx.getPlayer().sendMessage("§cPlayer team select is now off!");
    }
}
