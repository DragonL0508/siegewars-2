package me.dragonl.siegewars.game.lobby;

import io.fairyproject.bukkit.menu.Button;
import io.fairyproject.bukkit.menu.Menu;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LobbyAdminMenu extends Menu {
    @Override
    public void draw(boolean firstInitial) {
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
    }
}
