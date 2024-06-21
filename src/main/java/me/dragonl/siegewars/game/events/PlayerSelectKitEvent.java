package me.dragonl.siegewars.game.events;

import me.dragonl.siegewars.game.kit.SiegeWarsKit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSelectKitEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final SiegeWarsKit kit;

    public PlayerSelectKitEvent(Player player, SiegeWarsKit kit) {
        this.player = player;
        this.kit = kit;
    }
    public Player getPlayer(){
        return this.player;
    }
    public SiegeWarsKit getKit(){
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
