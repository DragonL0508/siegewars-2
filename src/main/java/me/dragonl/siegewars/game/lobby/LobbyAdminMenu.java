package me.dragonl.siegewars.game.lobby;

import io.fairyproject.bukkit.menu.Button;
import io.fairyproject.bukkit.menu.Menu;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.game.preparing.PlayerPreparingManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class LobbyAdminMenu extends Menu {
    private final GameStateManager gameStateManager;
    private final PlayerPreparingManager playerPreparingManager;

    public LobbyAdminMenu(GameStateManager gameStateManager, PlayerPreparingManager playerPreparingManager) {
        this.gameStateManager = gameStateManager;
        this.playerPreparingManager = playerPreparingManager;
    }

    @Override
    public void draw(boolean firstInitial) {
        this.set(0, new playerPreparedButton());
        this.set(8, new gameStartButton());
    }

    @Override
    public String getTitle() {
        return ChatColor.GOLD + "Admin GUI";
    }

    private class gameStartButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return ItemBuilder.of(Material.INK_SACK)
                    .name("¡±aGame Start!")
                    .lore("¡±7click to start the game")
                    .data(10)
                    .build();
        }
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            gameStateManager.setCurrentState(GameState.PREPARING);
        }
    }

    private class playerPreparedButton extends Button{
        @Override
        public ItemStack getButtonItem(Player player) {
            return ItemBuilder.of(Material.NETHER_STAR)
                    .name("¡±aPrepare button")
                    .lore("¡±7left click to prepare")
                    .build();
        }
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            player.sendMessage("You clicked the prepare button!");
            playerPreparingManager.setPlayerPreparedMap(player,true);
        }
    }
}
