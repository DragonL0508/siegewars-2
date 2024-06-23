package me.dragonl.siegewars.player;

import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.events.PlayerSelectKitEvent;
import me.dragonl.siegewars.game.kit.KitManager;
import me.dragonl.siegewars.game.kit.SiegeWarsKit;
import me.dragonl.siegewars.game.shop.ShopMenu;
import me.dragonl.siegewars.player.data.PlayerDataManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@InjectableComponent
public class PlayerKitManager {
    private final KitManager kitManager;
    private final TeamManager teamManager;
    private final NameGetter nameGetter;
    private final ShopMenu shopMenu;
    private final PlayerDataManager playerDataManager;
    private Map<UUID, SiegeWarsKit> playerKits = new HashMap<>();

    public PlayerKitManager(KitManager kitManager, TeamManager teamManager, NameGetter nameGetter, ShopMenu shopMenu, PlayerDataManager playerDataManager) {
        this.kitManager = kitManager;
        this.teamManager = teamManager;
        this.nameGetter = nameGetter;
        this.shopMenu = shopMenu;
        this.playerDataManager = playerDataManager;
    }

    public void setPlayerKit(Player player, SiegeWarsKit kit) {
        playerKits.put(player.getUniqueId(), kit);
    }

    public SiegeWarsKit getPlayerKit(Player player) {
        return playerKits.getOrDefault(player.getUniqueId(), kitManager.getKit("none"));
    }

    public void playerSelectKit(Player player, SiegeWarsKit kit) {
        UUID uuid = player.getUniqueId();
        playerKits.put(uuid, kit);
        shopMenu.open(player);

        teamManager.getPlayerTeam(player).getPlayers().forEach(u -> {
            Player p = Bukkit.getPlayer(u);
            p.sendMessage("§e[職業] " + nameGetter.getNameWithTeamColor(player) + " §a選擇了職業: §e" + kit.getKitName());
            p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, 1.2f);
        });

        //give items
        player.getInventory().setArmorContents(reverseArray(kit.getArmors(player)));
        player.getInventory().setContents(kit.getItems());
        player.getInventory().setItem(8, kit.getAbilityItem(player));
        playerDataManager.getPlayerData(player).getBuyCountsMap().forEach((k,v) -> {
            for(int i = 0; i < v; i++){
                player.getInventory().addItem(k.getItemStack());
            }
        });

        //call event
        Bukkit.getPluginManager().callEvent(new PlayerSelectKitEvent(player, kit));
    }

    private ItemStack[] reverseArray(Object[] array){
        List<Object> list = Arrays.asList(array);
        Collections.reverse(list);
        return list.toArray(new ItemStack[0]);
    }
}
