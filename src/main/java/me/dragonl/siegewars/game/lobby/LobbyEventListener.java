package me.dragonl.siegewars.game.lobby;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.events.player.PlayerDamageEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.itemStack.items.SelectTeamItem;
import me.dragonl.siegewars.team.TeamManager;
import me.dragonl.siegewars.yaml.MainConfig;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
@RegisterAsListener
public class LobbyEventListener implements Listener {
    private final GameStateManager gameStateManager;
    private final TeamManager teamManager;
    private final SelectTeamItem selectTeamItem;
    private final MainConfig mainConfig;

    public LobbyEventListener(GameStateManager gameStateManager, TeamManager teamManager, SelectTeamItem selectTeamItem, MainConfig mainConfig) {
        this.gameStateManager = gameStateManager;
        this.teamManager = teamManager;
        this.selectTeamItem = selectTeamItem;
        this.mainConfig = mainConfig;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        MCPlayer mcPlayer = MCPlayer.from(event.getPlayer());
        if(gameStateManager.isCurrentGameState(GameState.IN_LOBBY)){
            event.setJoinMessage(ChatColor.YELLOW + event.getPlayer().getName() + " 進入了大廳 !");

            //Join lobby Team
            teamManager.joinTeam(event.getPlayer(), teamManager.getTeam("lobby"));

            //Set lobby items
            ItemStack air = ItemBuilder.of(XMaterial.AIR).build();
            player.getInventory().clear();
            player.getInventory().setArmorContents(new ItemStack[]{air,air,air,air});
            player.getInventory().setItem(0,selectTeamItem.get(player));
        }
        if(gameStateManager.isCurrentGameState(GameState.PREPARING) && !teamManager.isInTeam(player)){
            //Join Team
            teamManager.joinTeam(event.getPlayer(), teamManager.getTeam("spectator"));
        }
        //tp to lobby spawn
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(mainConfig.getLobbySpawnLoc());
    }

    @EventHandler
    public void playerHurt(PlayerDamageEvent event){
        if(gameStateManager.isCurrentGameState(GameState.IN_LOBBY) || gameStateManager.isCurrentGameState(GameState.PREPARING)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event){
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
