package me.dragonl.siegewars.game.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJoinTeamEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;

    public PlayerJoinTeamEvent(Player player) {
        this.player = player;
    }
    public Player getPlayer(){
        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
