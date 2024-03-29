package me.dragonl.siegewars.game.lobby;

import io.fairyproject.bukkit.events.player.PlayerDamageEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.itemStack.items.SelectTeamItem;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

@InjectableComponent
@RegisterAsListener
public class LobbyEventListener implements Listener {
    private final GameStateManager gameStateManager;
    private final TeamManager teamManager;
    private final SelectTeamItem selectTeamItem;

    public LobbyEventListener(GameStateManager gameStateManager, TeamManager teamManager, SelectTeamItem selectTeamItem) {
        this.gameStateManager = gameStateManager;
        this.teamManager = teamManager;
        this.selectTeamItem = selectTeamItem;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        MCPlayer mcPlayer = MCPlayer.from(event.getPlayer());
        if(gameStateManager.isCurrentGameState(GameState.IN_LOBBY)){
            event.setJoinMessage(ChatColor.YELLOW + event.getPlayer().getName() + "進入了大廳 ! §8(MC " + mcPlayer.getVersion().getFormatted() + "§8)");
            //Join lobby Team
            teamManager.joinTeam(event.getPlayer(), teamManager.getTeam("lobby"));
            //Set lobby items
            player.getInventory().setItem(0,selectTeamItem.get(player));
        }
        if(gameStateManager.isCurrentGameState(GameState.PREPARING) && !teamManager.isInTeam(player)){
            //Join Team
            teamManager.joinTeam(event.getPlayer(), teamManager.getTeam("spectator"));
        }
    }

    @EventHandler
    public void playerHurt(PlayerDamageEvent event){
        if(gameStateManager.isCurrentGameState(GameState.IN_LOBBY) || gameStateManager.isCurrentGameState(GameState.PREPARING)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void playerInteract(PlayerDropItemEvent event){
        if(gameStateManager.isCurrentGameState(GameState.IN_LOBBY) || gameStateManager.isCurrentGameState(GameState.PREPARING)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event){
        if(gameStateManager.isCurrentGameState(GameState.IN_LOBBY) || gameStateManager.isCurrentGameState(GameState.PREPARING)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event){
        if(gameStateManager.isCurrentGameState(GameState.IN_LOBBY) || gameStateManager.isCurrentGameState(GameState.PREPARING)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryChanged(InventoryClickEvent event){
        if(gameStateManager.isCurrentGameState(GameState.IN_LOBBY) || gameStateManager.isCurrentGameState(GameState.PREPARING))
            if(event.getClickedInventory() == event.getWhoClicked().getInventory())
                event.setCancelled(true);
    }
}
