package me.dragonl.siegewars.itemStack.items;

import com.google.common.collect.Lists;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.itemStack.CustomItem;
import me.dragonl.siegewars.itemStack.CustomItemFairy;
import me.dragonl.siegewars.itemStack.ItemListenerTemplate;
import me.dragonl.siegewars.itemStack.RemoveCustomItem;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

@InjectableComponent
public class LeaveSpectatorItem extends CustomItemFairy {
    private final TeamManager teamManager;

    public LeaveSpectatorItem(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @Override
    protected FairyItem register() {
        return FairyItem.builder("sw:leaveSpectator")
                .item(ItemBuilder.of(Material.WOODEN_DOOR)
                        .name("¡±cLeave Spectator ¡±7(Right Click)")
                        .build())
                .build();
    }

    @InjectableComponent
    @RegisterAsListener
    class LeaveSpectatorItemListener extends ItemListenerTemplate{
        private final RemoveCustomItem removeCustomItem;
        private final CustomItem itemToRemove;
        public LeaveSpectatorItemListener(LeaveSpectatorItem customItem, RemoveCustomItem removeCustomItem) {
            super(customItem);
            this.removeCustomItem = removeCustomItem;
            this.itemToRemove = customItem;
        }

        @Override
        protected void onRightClickItem(PlayerInteractEvent event) {
            teamManager.joinTeam(event.getPlayer(), teamManager.getTeam("lobby"));
            removeCustomItem.removeCustomItem(event.getPlayer(), Lists.newArrayList(itemToRemove));
        }
    }
}
