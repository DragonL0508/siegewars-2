package me.dragonl.siegewars.game.lobby;

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
import me.dragonl.siegewars.game.preparing.PlayerPreparingManager;
import me.dragonl.siegewars.itemStack.items.PlayerPrepareItem;
import me.dragonl.siegewars.team.Team;
import me.dragonl.siegewars.team.TeamManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@InjectableComponent
public class LobbyAdminMenu {
    private final GuiFactory guiFactory;
    private final TeamManager teamManager;
    private final GameStateManager gameStateManager;
    private final PlayerPreparingManager playerPreparingManager;
    private final PlayerPrepareItem playerPrepareItem;
    private final MapSelectMenu mapSelectMenu;

    public LobbyAdminMenu(GuiFactory guiFactory, TeamManager teamManager, GameStateManager gameStateManager, PlayerPreparingManager playerPreparingManager, PlayerPrepareItem playerPrepareItem, MapSelectMenu mapSelectMenu) {
        this.guiFactory = guiFactory;
        this.teamManager = teamManager;
        this.gameStateManager = gameStateManager;
        this.playerPreparingManager = playerPreparingManager;
        this.playerPrepareItem = playerPrepareItem;
        this.mapSelectMenu = mapSelectMenu;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("管理員選單"));
        NormalPane pane = Pane.normal(9, 3);

        pane.setSlot(0, 1, 8, 1, GuiSlot.of(ItemBuilder.of(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE)
                .name(" ")
                .build()));
        pane.fillEmptySlots(GuiSlot.of(ItemBuilder.of(XMaterial.GRAY_STAINED_GLASS_PANE)
                .name(" ")
                .build()));

        pane.setSlot(9, GuiSlot.of(ItemBuilder.of(XMaterial.NAME_TAG)
                .name("§e隨機分隊")
                .lore("§7點我隨機分隊!")
                .build(), p -> {
            List<Player> playerList = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player1 -> {
                if (!teamManager.isInTeam(player1, teamManager.getTeam("spectator")))
                    playerList.add(player1);
            });
            Collections.shuffle(playerList);
            teamManager.swTeamSplits(playerList);
        }));

        pane.setSlot(10, GuiSlot.of(ItemBuilder.of(XMaterial.MAP)
                .name("&e選擇地圖")
                .build(), mapSelectMenu::open));

        pane.setSlot(17, GuiSlot.of(ItemBuilder.of(Material.INK_SACK)
                .name("§a開始遊戲!")
                .lore("§7點我開始遊戲")
                .data(10)
                .build(), p -> {
            gameStateManager.setCurrentGameState(GameState.PREPARING);
            Bukkit.getOnlinePlayers().forEach(target -> {
                if (!teamManager.isInTeam(target, teamManager.getTeam("spectator"))) {
                    playerPreparingManager.setPlayerPreparedMap(target, false);
                    target.getInventory().setItem(4, playerPrepareItem.get(target));
                    target.closeInventory();
                }
                target.playSound(target.getLocation(), Sound.CLICK, 1, 0.5f);
            });
        }));

        gui.addPane(pane);
        gui.open(player);
    }
}
