package me.dragonl.siegewars.game.shop;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.game.RoundState;
import me.dragonl.siegewars.game.ingame.InGameRunTime;
import me.dragonl.siegewars.player.data.PlayerData;
import me.dragonl.siegewars.player.data.PlayerDataManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@InjectableComponent
public class ShopMenu {
    private final GuiFactory guiFactory;
    private final CommodityManager commodityManager;
    private final GameStateManager gameStateManager;
    private final PlayerDataManager playerDataManager;
    private final InGameRunTime inGameRunTime;

    public ShopMenu(GuiFactory guiFactory, CommodityManager commodityManager, GameStateManager gameStateManager, PlayerDataManager playerDataManager, InGameRunTime inGameRunTime) {
        this.guiFactory = guiFactory;
        this.commodityManager = commodityManager;
        this.gameStateManager = gameStateManager;
        this.playerDataManager = playerDataManager;
        this.inGameRunTime = inGameRunTime;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("備戰商店"));
        NormalPane pane = Pane.normal(9, 6);
        AtomicReference<Boolean> isManualClose = new AtomicReference<>(true);

        pane.setSlot(0, 3, 8, 4, GuiSlot.of(ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE)
                .name(" ")
                .build()));
        pane.fillEmptySlots(GuiSlot.of(ItemBuilder.of(XMaterial.GRAY_STAINED_GLASS_PANE)
                .name(" ")
                .build()));
        pane.setSlot(1, 1, 7, 1, GuiSlot.of(XMaterial.AIR));
        pane.setSlot(2, 3, 3, 4, GuiSlot.of(ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE)
                .name("&c&l我想重新選擇職業")
                .build(), p -> {
            Bukkit.getServer().dispatchCommand(p.getServer().getConsoleSender(), "kit openMenu " + p.getName());
            isManualClose.set(false);
        }));
        pane.setSlot(5, 3, 6, 4, GuiSlot.of(ItemBuilder.of(XMaterial.GREEN_STAINED_GLASS_PANE)
                .name("&a&l我完成備戰了")
                .build(), p -> {
            isManualClose.set(false);
            inGameRunTime.getPreparingPlayers().put(p.getUniqueId(), true);
            p.closeInventory();
        }));

        gui.onDrawCallback(updatedPlayer -> {
            PlayerData playerData = playerDataManager.getPlayerData(player);
            AtomicInteger i = new AtomicInteger();
            commodityManager.getCommodities().forEach((k, v) -> {
                pane.setSlot(1 + i.get(), 1, GuiSlot.of(ItemBuilder.of(v.getShopIcon())
                        .clearLore()
                        .lore(getLoreWithPrice(player, v, v.getShopIcon().getItemMeta().getLore()))
                        .lore("&a你還能購買&e " + (v.getBuyLimit() - playerData.getBuyCounts(v)) + " &a個")
                        .amount(v.getBuyLimit() - playerData.getBuyCounts(v))
                        .build(), player1 -> {
                    buyItem(player1, v);
                    gui.update(player1);
                }));
                i.set(i.get() + 1);
            });
        });

        //anti-Manual close
        gui.onCloseCallback(p -> {
            if (gameStateManager.isCurrentGameState(GameState.IN_GAME) && gameStateManager.isCurrentRoundState(RoundState.PREPARING)
                    && isManualClose.get())
                gui.open(p);
        });

        gui.addPane(pane);
        gui.open(player);
    }

    private void buyItem(Player player, Commodity item) {
        PlayerData playerData = playerDataManager.getPlayerData(player);
        if (playerData.getBuyCounts(item) >= item.getBuyLimit()) {
            player.sendMessage("§c此物品的購買次數已達上限!");
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
            return;
        }

        if (playerData.getMoney() >= item.getPrice()) {
            player.sendMessage("§a你購買了 " + item.getName());
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);
            player.getInventory().addItem(item.getItemStack());
            playerData.setMoney(playerData.getMoney() - item.getPrice());

            //save buy data
            playerData.addBuyCounts(item, 1);
        } else {
            player.sendMessage("§c金錢不足， 你還差 §e" + (item.getPrice() - playerData.getMoney()) + "$ §c才能購買此商品!");
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
        }
    }

    private String getPriceWithColor(Player player, Commodity item) {
        PlayerData playerData = playerDataManager.getPlayerData(player);
        if (playerData.getMoney() >= item.getPrice())
            return "&a" + item.getPrice();
        else
            return "&c" + item.getPrice();
    }

    private List<String> getLoreWithPrice(Player player, Commodity item, List<String> lore) {
        List<String> loreWithPrice = new ArrayList<>(lore);
        loreWithPrice.add(0, "&e售價: " + getPriceWithColor(player, item) + "$");
        return loreWithPrice;
    }
}
