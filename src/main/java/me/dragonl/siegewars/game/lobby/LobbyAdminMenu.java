package me.dragonl.siegewars.game.lobby;

import com.cryptomorin.xseries.messages.ActionBar;
import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.bukkit.menu.Button;
import io.fairyproject.bukkit.menu.Menu;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.game.preparing.PlayerPreparingManager;
import me.dragonl.siegewars.itemStack.items.PlayerPrepareItem;
import me.dragonl.siegewars.team.TeamManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LobbyAdminMenu extends Menu {
    private final GameStateManager gameStateManager;
    private final PlayerPreparingManager playerPreparingManager;
    private final TeamManager teamManager;
    private final PlayerPrepareItem playerPrepareItem;

    public LobbyAdminMenu(GameStateManager gameStateManager, PlayerPreparingManager playerPreparingManager, TeamManager teamManager, PlayerPrepareItem playerPrepareItem) {
        this.gameStateManager = gameStateManager;
        this.playerPreparingManager = playerPreparingManager;
        this.teamManager = teamManager;
        this.playerPrepareItem = playerPrepareItem;
    }

    @Override
    public void draw(boolean firstInitial) {
        for(int i = 0; i < 9; i++){
            this.set(i,new border());
        }
        for(int i = 18; i < 27; i++){
            this.set(i,new border());
        }
        this.set(17, new gameStartButton());
        this.set(9, new teamSplitButton());
    }

    @Override
    public String getTitle() {
        return ChatColor.GOLD + "管理員 GUI";
    }

    private class gameStartButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return ItemBuilder.of(Material.INK_SACK)
                    .name("§a開始遊戲!")
                    .lore("§7點我開始遊戲")
                    .data(10)
                    .build();
        }
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            gameStateManager.setCurrentGameState(GameState.PREPARING);
            Bukkit.getOnlinePlayers().forEach(p -> {
                if(!teamManager.isInTeam(p, teamManager.getTeam("spectator"))){
                    playerPreparingManager.setPlayerPreparedMap(p,false);
                    p.getInventory().setItem(4,playerPrepareItem.get(p));
                    p.closeInventory();
                }
                p.playSound(p.getLocation(), Sound.CLICK,1,0.5f);
            });
        }
    }

    private class teamSplitButton extends Button{

        @Override
        public ItemStack getButtonItem(Player player) {
            return ItemBuilder.of(Material.NAME_TAG)
                    .name("§e隨機分隊")
                    .lore("§7點我隨機分隊!")
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            List<Player> playerList = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player1 -> {
                if(!teamManager.isInTeam(player1,teamManager.getTeam("spectator")))
                    playerList.add(player1);
            });
            teamManager.swTeamSplits(playerList);
        }
    }

    private class border extends Button{
        @Override
        public ItemStack getButtonItem(Player player) {
            return ItemBuilder.of(Material.STAINED_GLASS_PANE)
                    .data(7)
                    .name("")
                    .build();
        }
    }
}
