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
import me.dragonl.siegewars.game.kit.KitMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@InjectableComponent
public class ShopMenu {
    private final GuiFactory guiFactory;
    private final CommodityManager commodityManager;
    private final GameStateManager gameStateManager;

    public ShopMenu(GuiFactory guiFactory, CommodityManager commodityManager, GameStateManager gameStateManager) {
        this.guiFactory = guiFactory;
        this.commodityManager = commodityManager;
        this.gameStateManager = gameStateManager;
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
            if (p.isOp())
                Bukkit.getServer().dispatchCommand(p, "kit menu");
            else {
                p.setOp(true);
                Bukkit.getServer().dispatchCommand(p, "kit openMenuAndDeop");
            }

            isManualClose.set(false);
        }));
        pane.setSlot(5, 3, 6, 4, GuiSlot.of(ItemBuilder.of(XMaterial.GREEN_STAINED_GLASS_PANE)
                .name("&a&l我完成備戰了")
                .build(), p -> {
            isManualClose.set(false);
            p.closeInventory();
        }));

        AtomicInteger i = new AtomicInteger();
        commodityManager.getCommodities().forEach((k, v) -> {
            pane.setSlot(1 + i.get(), 1, GuiSlot.of(v.getShopIcon(), player1 -> {
                buyItem(player1, v);
            }));
            i.set(i.get() + 1);
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
        player.getInventory().addItem(item.getItemStack());
    }
}
