package me.dragonl.siegewars.game.lobby;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.yaml.MapConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.concurrent.atomic.AtomicInteger;

@InjectableComponent
public class MapSelectMenu {
    private final GuiFactory guiFactory;
    private final MapConfig mapConfig;
    private final GameStateManager gameStateManager;

    public MapSelectMenu(GuiFactory guiFactory, MapConfig mapConfig, GameStateManager gameStateManager) {
        this.guiFactory = guiFactory;
        this.mapConfig = mapConfig;
        this.gameStateManager = gameStateManager;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("地圖選擇"));
        NormalPane pane = Pane.normal(9, 6);

        pane.fillEmptySlots(GuiSlot.of(ItemBuilder.of(XMaterial.GRAY_STAINED_GLASS_PANE)
                .name(" ")
                .build()));
        pane.setSlot(1, 1, 7, 4, GuiSlot.of(XMaterial.AIR));
        pane.setSlot(1, 1, 7, 2, GuiSlot.of(ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE)
                .name("&c無")
                .build()));

        gui.onDrawCallback(p -> {
            //load maps
            AtomicInteger i = new AtomicInteger();

            mapConfig.getMaps().values().forEach(e -> {
                String lore = "&7點擊選擇";
                if (gameStateManager.getSelectedMap() == e)
                    lore = "&a已選擇!";

                if (!XMaterial.RED_STAINED_GLASS_PANE.isSimilar(pane.getSlot(10 + i.get()).getItemStack(player, gui))
                        && !XMaterial.MAP.isSimilar(pane.getSlot(10 + i.get()).getItemStack(player, gui))) {
                    i.set(i.get() + 2);
                }
                pane.setSlot(10 + i.get(), GuiSlot.of(ItemBuilder.of(XMaterial.MAP)
                        .name("&e" + e.getMapName())
                        .lore(lore)
                        .build(), click -> {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 0.5f);
                    gameStateManager.setSelectedMap(e);
                    Bukkit.broadcastMessage("§a地圖已更換為: §e" + e.getMapName());
                    gui.update(player);
                }));
            });

            //show selected map
            pane.setSlot(4, 4, GuiSlot.of(ItemBuilder.of(XMaterial.FILLED_MAP)
                    .name("&e目前地圖: &a" + gameStateManager.getSelectedMap().getMapName())
                    .itemFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .build()));
        });

        gui.addPane(pane);
        gui.open(player);
    }
}
