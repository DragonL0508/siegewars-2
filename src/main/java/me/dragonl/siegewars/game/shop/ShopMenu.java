package me.dragonl.siegewars.game.shop;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

@InjectableComponent
public class ShopMenu {
    private final GuiFactory guiFactory;
    private final CommodityManager commodityManager;

    public ShopMenu(GuiFactory guiFactory, CommodityManager commodityManager) {
        this.guiFactory = guiFactory;
        this.commodityManager = commodityManager;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("備戰商店"));
        NormalPane pane = Pane.normal(9, 3);

        pane.fillEmptySlots(GuiSlot.of(ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE)
                .name(" ")
                .build()));
        pane.setSlot(1, 1, 7, 1, GuiSlot.of(XMaterial.AIR));

        AtomicInteger i = new AtomicInteger();
        commodityManager.getCommodities().forEach((k, v) -> {
            pane.setSlot(1 + i.get(), 1, GuiSlot.of(v.getShopIcon(), player1 -> {
                buyItem(player1, v);
            }));
            i.set(i.get() + 1);
        });

        gui.onCloseCallback(gui::open);
        gui.addPane(pane);
        gui.open(player);
    }

    private void buyItem(Player player, Commodity item) {
        player.getInventory().addItem(item.getItemStack());
    }
}
