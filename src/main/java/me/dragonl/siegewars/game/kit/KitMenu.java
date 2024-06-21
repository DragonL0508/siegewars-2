package me.dragonl.siegewars.game.kit;

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
import me.dragonl.siegewars.game.events.PlayerSelectKitEvent;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@InjectableComponent
public class KitMenu {
    private final GuiFactory guiFactory;
    private final PlayerKitManager playerKitManager;
    private final KitManager kitManager;
    private final TeamManager teamManager;
    private final NameGetter nameGetter;
    private final GameStateManager gameStateManager;

    public KitMenu(GuiFactory guiFactory, PlayerKitManager playerKitManager, KitManager kitManager, TeamManager teamManager, NameGetter nameGetter, GameStateManager gameStateManager) {
        this.guiFactory = guiFactory;
        this.playerKitManager = playerKitManager;
        this.kitManager = kitManager;
        this.teamManager = teamManager;
        this.nameGetter = nameGetter;
        this.gameStateManager = gameStateManager;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("選擇職業:"));
        NormalPane pane = Pane.normal(9, 6);
        AtomicReference<Boolean> isManualClose = new AtomicReference<>(true);

        pane.fillEmptySlots(GuiSlot.of(ItemBuilder.of(XMaterial.GRAY_STAINED_GLASS_PANE)
                .name(" ")
                .build()));
        pane.setSlot(1, 1, 7, 2, GuiSlot.of(XMaterial.AIR));
        pane.setSlot(0, 4, 8, 5, GuiSlot.of(ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE)
                .name(" ")
                .build()));
        pane.setSlot(2, 4, 6, 4, GuiSlot.of(ItemBuilder.of(XMaterial.BARRIER)
                .name("&c空")
                .build()));
        pane.setSlot(2, 5, 6, 5, GuiSlot.of(ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE)
                .name("&c沒有職業")
                .build()));

        //load kits
        AtomicInteger i = new AtomicInteger();
        kitManager.getKits().forEach((k, v) -> {
            if (Objects.equals(k, "none"))
                return;
            pane.setSlot(1 + i.get(), 1, GuiSlot.of(ItemBuilder.of(v.getKitIcon())
                    .name("&e" + v.getKitName())
                    .lore(v.getDescription())
                    .itemFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .build(), clickedPlayer -> {
                playerKitManager.playerSelectKit(clickedPlayer, v);
                isManualClose.set(false);
            }));

            i.getAndIncrement();
        });

        //update gui when any player clicked
        gui.onOpenCallback($ -> {
            gui.getEventNode().addListener(PlayerSelectKitEvent.class, event -> {
                gui.update(player);
            });
        });

        //show every player selected
        gui.onDrawCallback($ -> {
            List<UUID> playerList = teamManager.getPlayerTeam(player).getPlayers();
            playerList.forEach(uuid -> {
                Player p = Bukkit.getPlayer(uuid);
                SiegeWarsKit kit = playerKitManager.getPlayerKit(p);

                int slot = playerList.indexOf(uuid);
                pane.setSlot(slot + 2, 4, GuiSlot.of(ItemBuilder.of(XMaterial.PLAYER_HEAD)
                        .skull(p.getName())
                        .name(nameGetter.getChatName(p))
                        .build()));

                pane.setSlot(slot + 2, 5, GuiSlot.of(ItemBuilder.of(kit.getKitIcon())
                        .name("&a已選擇: &e" + kit.getKitName())
                        .itemFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .build()));
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
}
