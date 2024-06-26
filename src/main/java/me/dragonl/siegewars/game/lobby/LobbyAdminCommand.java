package me.dragonl.siegewars.game.lobby;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.game.MapObjectCatcher;
import me.dragonl.siegewars.game.ingame.InGamePreparingExecuter;
import me.dragonl.siegewars.game.kit.SiegeWarsKit;
import me.dragonl.siegewars.game.shop.ShopMenu;
import me.dragonl.siegewars.itemStack.items.gameplay.AxeItem;
import me.dragonl.siegewars.itemStack.items.gameplay.BaffleItem;
import me.dragonl.siegewars.itemStack.items.gameplay.BombItem;
import me.dragonl.siegewars.itemStack.items.gameplay.TNTItem;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.SiegeWarsTeam;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

@Command(value = "admin", permissionNode = "siegewars.admin")
@InjectableComponent
public class LobbyAdminCommand extends BaseCommand {
    private final GameStateManager gameStateManager;
    private final TeamManager teamManager;
    private final TNTItem tntItem;
    private final BaffleItem baffleItem;
    private final MapObjectCatcher mapObjectCatcher;
    private final AxeItem axeItem;
    private final BombItem bombItem;
    private final LobbyAdminMenu lobbyAdminMenu;
    private final PlayerKitManager playerKitManager;
    private final ShopMenu shopMenu;
    private final InGamePreparingExecuter inGamePreparingExecuter;

    public LobbyAdminCommand(GameStateManager gameStateManager, TeamManager teamManager, TNTItem tntItem, BaffleItem baffleItem, MapObjectCatcher mapObjectCatcher, AxeItem axeItem, BombItem bombItem, LobbyAdminMenu lobbyAdminMenu, PlayerKitManager playerKitManager, ShopMenu shopMenu, InGamePreparingExecuter inGamePreparingExecuter) {
        this.gameStateManager = gameStateManager;
        this.teamManager = teamManager;
        this.tntItem = tntItem;
        this.baffleItem = baffleItem;
        this.mapObjectCatcher = mapObjectCatcher;
        this.axeItem = axeItem;
        this.bombItem = bombItem;
        this.lobbyAdminMenu = lobbyAdminMenu;
        this.playerKitManager = playerKitManager;
        this.shopMenu = shopMenu;
        this.inGamePreparingExecuter = inGamePreparingExecuter;
    }

    @Command("menu")
    public void openMenu(BukkitCommandContext ctx) {
        lobbyAdminMenu.open(ctx.getPlayer());
    }

    @Command("setKit")
    public void setKit(BukkitCommandContext ctx, @Arg("kit") SiegeWarsKit kit) {
        playerKitManager.playerSelectKit(ctx.getPlayer(), kit);
    }

    @Command("setGameState")
    public void setGameState(BukkitCommandContext ctx, @Arg("gameState") GameState gameState) {
        gameStateManager.setCurrentGameState(gameState);
    }

    @Command("joinTeam")
    public void joinTeam(BukkitCommandContext ctx, @Arg("team") SiegeWarsTeam team, @Arg("player") Player player) {
        teamManager.joinTeam(player, team);
    }

    @Command("goToMap")
    public void goToMap(BukkitCommandContext ctx, @Arg("map") World world) {
        ctx.getPlayer().teleport(world.getSpawnLocation());
    }

    @Command("giveItem")
    public void giveItem(BukkitCommandContext ctx, @Arg("player") Player player, @Arg("item") GetItemArgs item) {
        PlayerInventory inv = player.getInventory();
        switch (item) {
            case TNT: {
                inv.addItem(tntItem.get(player));
                break;
            }
            case baffle: {
                inv.addItem(baffleItem.get(player));
                break;
            }
            case axe: {
                inv.addItem(axeItem.get(player));
                break;
            }
            case Bomb:{
                inv.addItem(bombItem.get(player));
                break;
            }
        }
    }

    @Command("restoreMapObjects")
    public void restoreMapObjects(BukkitCommandContext ctx) {
        mapObjectCatcher.getDestroyableWalls().forEach((element, blockStates) -> {
            blockStates.forEach(blockState -> {
                blockState.update(true);
            });
        });

        mapObjectCatcher.getDestroyableWindow().forEach((element, blockStates) -> {
            blockStates.forEach(blockState -> {
                blockState.update(true);
            });
        });

        mapObjectCatcher.getBafflePlaced().forEach(location -> {
            location.getBlock().setType(Material.AIR);
            while (location.add(0, 1, 0).getBlock().getType() == Material.ACACIA_FENCE)
                location.getBlock().setType(Material.AIR);
        });

        //clear maps
        mapObjectCatcher.getDestroyableWalls().clear();
        mapObjectCatcher.getDestroyableWindow().clear();
        mapObjectCatcher.getBafflePlaced().clear();
    }

    @Command("shopMenu")
    public void openShopMenu(BukkitCommandContext ctx) {
        shopMenu.open(ctx.getPlayer());
    }

    @Command("transformInvToBuyCounts")
    public void transformInvToBuyCounts(BukkitCommandContext ctx) {
        inGamePreparingExecuter.transformInvToBuyCounts(ctx.getPlayer());
    }
}
