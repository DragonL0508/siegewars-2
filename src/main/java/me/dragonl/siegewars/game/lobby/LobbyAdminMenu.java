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
        this.set(0, new testButton());
    }

    @Override
    public String getTitle() {
        return ChatColor.GOLD + "Lobby Admin GUI";
    }

    private class testButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return ItemBuilder.of(Material.DIRT)
                    .name("Button")
                    .lore("Line 1", "Line 2", "Line 3")
                    .build();
        }
    }
}
