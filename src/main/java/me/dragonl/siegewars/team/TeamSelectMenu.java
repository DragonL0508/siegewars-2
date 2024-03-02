package me.dragonl.siegewars.team;

import com.google.common.collect.Lists;
import io.fairyproject.bukkit.menu.Button;
import io.fairyproject.bukkit.menu.Menu;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.itemStack.CustomItem;
import me.dragonl.siegewars.itemStack.RemoveCustomItem;
import me.dragonl.siegewars.itemStack.items.LeaveSpectatorItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
public class TeamSelectMenu extends Menu {
    private final TeamManager teamManager;
    private final LeaveSpectatorItem leaveSpectatorItem;
    private final RemoveCustomItem removeCustomItem;

    public TeamSelectMenu(TeamManager teamManager, LeaveSpectatorItem leaveSpectatorItem, RemoveCustomItem removeCustomItem) {
        this.teamManager = teamManager;
        this.leaveSpectatorItem = leaveSpectatorItem;
        this.removeCustomItem = removeCustomItem;
    }

    @Override
    public void draw(boolean firstInitial) {
        this.set(0,new AteamButton());
        this.set(1,new BteamButton());
        this.set(2,new spectatorButton());
    }

    @Override
    public String getTitle() {
        return ChatColor.YELLOW + "Select Your Team";
    }

    private class AteamButton extends Button{
        @Override
        public ItemStack getButtonItem(Player player) {
            return ItemBuilder.of(Material.WOOL)
                    .name("¡±6Join team A")
                    .lore("¡±7click to join team A")
                    .data(1)
                    .build();
        }
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            teamManager.joinTeam(player, teamManager.getTeam("A"));
            removeCustomItem.removeCustomItem(player, Lists.newArrayList(leaveSpectatorItem));
        }
    }

    private class BteamButton extends Button{
        @Override
        public ItemStack getButtonItem(Player player) {
            return ItemBuilder.of(Material.WOOL)
                    .name("¡±bJoin team B")
                    .lore("¡±7click to join team B")
                    .data(3)
                    .build();
        }
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            teamManager.joinTeam(player, teamManager.getTeam("B"));
            removeCustomItem.removeCustomItem(player, Lists.newArrayList(leaveSpectatorItem));
        }
    }

    private class spectatorButton extends Button{
        @Override
        public ItemStack getButtonItem(Player player) {
            return ItemBuilder.of(Material.WOOL)
                    .name("¡±7Join spectator")
                    .lore("¡±7click to join spectator")
                    .data(8)
                    .build();
        }
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            teamManager.joinTeam(player, teamManager.getTeam("spectator"));
            player.getInventory().setItem(8,leaveSpectatorItem.get(player));
        }
    }
}
