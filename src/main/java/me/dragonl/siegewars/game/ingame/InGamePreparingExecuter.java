package me.dragonl.siegewars.game.ingame;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.game.RoundState;
import me.dragonl.siegewars.game.ingame.ingameTimer.InGameTimerManager;
import me.dragonl.siegewars.game.kit.KitManager;
import me.dragonl.siegewars.game.kit.KitMenu;
import me.dragonl.siegewars.game.shop.Commodity;
import me.dragonl.siegewars.game.shop.CommodityManager;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.player.data.PlayerData;
import me.dragonl.siegewars.player.data.PlayerDataManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@InjectableComponent
public class InGamePreparingExecuter {
    private final TeamManager teamManager;
    private final KitMenu kitMenu;
    private final PlayerKitManager playerKitManager;
    private final KitManager kitManager;
    private final GameStateManager gameStateManager;
    private final PlayerDataManager playerDataManager;
    private final CommodityManager commodityManager;
    private final InGameTimerManager inGameTimerManager;

    public InGamePreparingExecuter(TeamManager teamManager, KitMenu kitMenu, PlayerKitManager playerKitManager, KitManager kitManager, GameStateManager gameStateManager, PlayerDataManager playerDataManager, CommodityManager commodityManager, InGameTimerManager inGameTimerManager) {
        this.teamManager = teamManager;
        this.kitMenu = kitMenu;
        this.playerKitManager = playerKitManager;
        this.kitManager = kitManager;
        this.gameStateManager = gameStateManager;
        this.playerDataManager = playerDataManager;
        this.commodityManager = commodityManager;
        this.inGameTimerManager = inGameTimerManager;
    }

    public void start() {
        gameStateManager.setCurrentRoundState(RoundState.PREPARING);
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "admin restoreMapObjects");

        Bukkit.getOnlinePlayers().forEach(p -> {
            if (!teamManager.isInTeam(p, teamManager.getTeam("spectator"))) {
                playerKitManager.setPlayerKit(p, kitManager.getKit("none"));
                transformInvToBuyCounts(p);
                kitMenu.open(p);
            }
        });

        inGameTimerManager.startTimer(inGameTimerManager.getTimers().get(0).getValue());
    }

    private void gamePosChoosingLogic() {

    }

    public void transformInvToBuyCounts(Player player) {
        PlayerData data = playerDataManager.getPlayerData(player);
        data.getBuyCountsMap().clear();
        commodityManager.getCommodities().forEach((k, v) -> {
            data.addBuyCounts(v, getCommodityCountsInv(player, v));
        });
    }

    private Integer getCommodityCountsInv(Player player, Commodity commodity) {
        AtomicInteger i = new AtomicInteger();
        List<ItemStack> contents = Arrays.asList(player.getInventory().getContents());
        contents.forEach(item -> {
            if (item == null)
                return;

            if (item.isSimilar(commodity.getItemStack()))
                i.addAndGet(item.getAmount());
        });
        return i.get();
    }
}
