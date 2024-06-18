package me.dragonl.siegewars.itemStack;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ItemListenerTemplate implements Listener {

    private final CustomItem customItem;

    public ItemListenerTemplate(CustomItem customItem) {
        this.customItem = customItem;
    }

    @EventHandler
    public void listenClick(PlayerInteractEvent event) {
        if (isItem(event.getItem()))
            onClickItem(event);
    }

    @EventHandler
    public void listenRightClick(PlayerInteractEvent event) {
        if (!isItem(event.getItem()))
            return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
            onRightClickItem(event);
    }

    @EventHandler
    public void listenClickBlock(PlayerInteractEvent event) {
        if (!isItem(event.getItem()))
            return;

        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)
            onClickBlock(event);

        if (event.getAction() == Action.LEFT_CLICK_BLOCK)
            onLeftClickBlock(event);

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
            onRightClickBlock(event);
    }


    protected boolean isItem(ItemStack itemStack) {
        return itemStack != null && customItem.isSimilar(itemStack);
    }

    protected void onClickItem(PlayerInteractEvent event) {
    }

    protected void onRightClickItem(PlayerInteractEvent event) {
    }

    protected void onClickBlock(PlayerInteractEvent event) {
    }

    protected void onLeftClickBlock(PlayerInteractEvent event) {
    }

    protected void onRightClickBlock(PlayerInteractEvent event) {
    }
}
