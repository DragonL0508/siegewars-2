package me.dragonl.siegewars;

import com.google.common.collect.Lists;
import io.fairyproject.bukkit.util.LegacyAdventureUtil;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.tablist.TabColumn;
import io.fairyproject.mc.tablist.TablistAdapter;
import io.fairyproject.mc.tablist.util.Skin;
import io.fairyproject.mc.tablist.util.TabSlot;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.team.TeamManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.*;

@InjectableComponent
public class TabList implements TablistAdapter {
    private final TeamManager teamManager;
    private final NameGetter nameGetter;
    private final GameStateManager gameStateManager;

    public TabList(TeamManager teamManager, NameGetter nameGetter, GameStateManager gameStateManager) {
        this.teamManager = teamManager;
        this.nameGetter = nameGetter;
        this.gameStateManager = gameStateManager;
    }

    @Override
    public @Nullable Set<TabSlot> getSlots(MCPlayer player) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = dateFormat.format(currentDate);
        //Lobby Tab List
        if(gameStateManager.isCurrentState(GameState.IN_LOBBY)){
            //default texts
            TabSlot headBorder = new TabSlot().column(TabColumn.LEFT).slot(1).text(LegacyAdventureUtil.decode("§7§m----------------------------------------------------------------------------------------------------------------------------"));
            TabSlot footBorder = new TabSlot().column(TabColumn.LEFT).slot(20).text(LegacyAdventureUtil.decode("§7§m---------------------------------------------------------------------------------------------------------------------------"));
            TabSlot statTitle = new TabSlot().column(TabColumn.LEFT).slot(2).text(LegacyAdventureUtil.decode("§e§l個人資訊:"));
            TabSlot playersTitle = new TabSlot().column(TabColumn.MIDDLE).slot(2).text(LegacyAdventureUtil.decode("§e§l大廳玩家:"));
            TabSlot spectatorsTitle = new TabSlot().column(TabColumn.RIGHT).slot(2).text(LegacyAdventureUtil.decode("§e§l觀戰玩家:"));
            TabSlot dateTitle = new TabSlot().column(TabColumn.FAR_RIGHT).slot(2).text(LegacyAdventureUtil.decode("§e§l日期:"));
            TabSlot nameInfo = new TabSlot().column(TabColumn.LEFT).slot(3).text(LegacyAdventureUtil.decode("§6ID: §f" + player.getName()));
            TabSlot pingInfo = new TabSlot().column(TabColumn.LEFT).slot(4).text(LegacyAdventureUtil.decode("§6Ping: §f" + player.getPing() + "ms"));
            TabSlot dateInfo = new TabSlot().column(TabColumn.FAR_RIGHT).slot(3).text(LegacyAdventureUtil.decode("§f" + formattedDate));
            Set<TabSlot> tabSlots = new HashSet<>(Set.of(
                    headBorder, footBorder, statTitle, playersTitle, spectatorsTitle
                    , dateTitle, nameInfo, pingInfo, dateInfo));
            //information
            //lobby player list
            List<UUID> lobbyPlayersList = new ArrayList<>();
            lobbyPlayersList.addAll(teamManager.getTeam("lobby").getPlayers());
            lobbyPlayersList.addAll(teamManager.getTeam("A").getPlayers());
            lobbyPlayersList.addAll(teamManager.getTeam("B").getPlayers());
            lobbyPlayersList.forEach(uuid -> {
                Player p = Bukkit.getPlayer(uuid);
                tabSlots.add(new TabSlot().column(TabColumn.MIDDLE).slot(lobbyPlayersList.indexOf(uuid) + 3).text(LegacyAdventureUtil.decode(nameGetter.getNameWithTeamColor(p))).skin(Skin.load(uuid)));
            });
            //spectators list
            teamManager.getTeam("spectator").getPlayers().forEach(uuid -> {
                Player p = Bukkit.getPlayer(uuid);
                tabSlots.add(new TabSlot().column(TabColumn.RIGHT).slot(teamManager.getTeam("spectator").getPlayers().indexOf(uuid) + 3).text(LegacyAdventureUtil.decode(nameGetter.getNameWithTeamColor(p))).skin(Skin.load(uuid)));
            });
            return tabSlots;
        }
        return null;
    }

    @Override
    public @Nullable Component getHeader(MCPlayer player) {
        return LegacyAdventureUtil.decode("&eSiege&6Wars &f&lII");
    }

    @Override
    public @Nullable Component getFooter(MCPlayer player) {
        return LegacyAdventureUtil.decode("&7Made By DragonL");
    }
}
