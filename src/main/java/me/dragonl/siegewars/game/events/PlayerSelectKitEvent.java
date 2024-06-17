package me.dragonl.siegewars.game.events;

import me.dragonl.siegewars.game.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSelectKitEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Kit kit;

    public PlayerSelectKitEvent(Player player, Kit kit) {
        this.player = player;
        this.kit = kit;
    }
    public Player getPlayer(){
        return this.player;
    }
    public Kit getKit(){
        return this.kit;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
