package me.dragonl.siegewars;

import io.fairyproject.bukkit.util.LegacyAdventureUtil;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.tablist.TabColumn;
import io.fairyproject.mc.tablist.TablistAdapter;
import io.fairyproject.mc.tablist.util.Skin;
import io.fairyproject.mc.tablist.util.TabSlot;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.game.preparing.PlayerPreparingManager;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.player.data.PlayerData;
import me.dragonl.siegewars.player.data.PlayerDataManager;
import me.dragonl.siegewars.team.Team;
import me.dragonl.siegewars.team.TeamManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.*;

@InjectableComponent
public class TabList implements TablistAdapter {
    private final TeamManager teamManager;
    private final NameGetter nameGetter;
    private final GameStateManager gameStateManager;
    private final PlayerPreparingManager playerPreparingManager;
    private final PlayerDataManager playerDataManager;
    private final PlayerKitManager playerKitManager;

    public TabList(TeamManager teamManager, NameGetter nameGetter, GameStateManager gameStateManager, PlayerPreparingManager playerPreparingManager, PlayerDataManager playerDataManager, PlayerKitManager playerKitManager) {
        this.teamManager = teamManager;
        this.nameGetter = nameGetter;
        this.gameStateManager = gameStateManager;
        this.playerPreparingManager = playerPreparingManager;
        this.playerDataManager = playerDataManager;
        this.playerKitManager = playerKitManager;
    }

    @Override
    public @Nullable Set<TabSlot> getSlots(MCPlayer player) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = dateFormat.format(currentDate);
        //Lobby Tab List
        if (gameStateManager.isCurrentGameState(GameState.IN_LOBBY) || gameStateManager.isCurrentGameState(GameState.PREPARING)) {
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
            lobbyPlayersList.addAll(teamManager.getTeam("lobby").getOnlinePlayers());
            lobbyPlayersList.addAll(teamManager.getTeam("A").getOnlinePlayers());
            lobbyPlayersList.addAll(teamManager.getTeam("B").getOnlinePlayers());
            lobbyPlayersList.forEach(uuid -> {
                Player p = Bukkit.getPlayer(uuid);
                String nameTag = nameGetter.getNameWithTeamColor(p);
                if (gameStateManager.isCurrentGameState(GameState.PREPARING) && playerPreparingManager.isPlayerPrepared(p))
                    nameTag += " §a§l已準備";
                else
                    nameTag += " §c§l未準備";
                tabSlots.add(new TabSlot().column(TabColumn.MIDDLE).slot(lobbyPlayersList.indexOf(uuid) + 3).text(LegacyAdventureUtil.decode(nameTag)).skin(Skin.load(uuid)));
            });
            //spectators list
            teamManager.getTeam("spectator").getPlayers().forEach(uuid -> {
                Player p = Bukkit.getPlayer(uuid);
                tabSlots.add(new TabSlot().column(TabColumn.RIGHT).slot(teamManager.getTeam("spectator").getPlayers().indexOf(uuid) + 3).text(LegacyAdventureUtil.decode(nameGetter.getNameWithTeamColor(p))).skin(Skin.load(uuid)));
            });
            return tabSlots;
        } else if (gameStateManager.isCurrentGameState(GameState.IN_GAME) || gameStateManager.isCurrentGameState(GameState.GAME_END)) {
            String[] titles = {"§6§l隊伍A:", "§b§l隊伍B:"};
            Player bukkitPlayer = player.as(Player.class);
            PlayerData playerData = playerDataManager.getPlayerData(bukkitPlayer);
            Set<TabSlot> tabSlots = new HashSet<>();

            for (int i = 0; i < 2; i++) {
                String title = titles[i];
                if (teamManager.isInTeam(bukkitPlayer, teamManager.getTeam(i == 0 ? "A" : "B")))
                    title += " §r§7(你的隊伍)";

                TabSlot teamTitle = new TabSlot().column(i == 0 ? TabColumn.MIDDLE : TabColumn.RIGHT).slot(2).text(LegacyAdventureUtil.decode(title));
                tabSlots.add(teamTitle);
            }

// default texts
            TabSlot headBorder = new TabSlot().column(TabColumn.LEFT).slot(1).text(LegacyAdventureUtil.decode("§7§m----------------------------------------------------------------------------------------------------------------------------"));
            TabSlot footBorder = new TabSlot().column(TabColumn.LEFT).slot(20).text(LegacyAdventureUtil.decode("§7§m---------------------------------------------------------------------------------------------------------------------------"));
            TabSlot statTitle = new TabSlot().column(TabColumn.LEFT).slot(2).text(LegacyAdventureUtil.decode("§e§l個人資訊:"));
            TabSlot teamAScore = new TabSlot().column(TabColumn.MIDDLE).slot(3).text(LegacyAdventureUtil.decode("§6得分: §f" + gameStateManager.getScoreA()));
            TabSlot teamBScore = new TabSlot().column(TabColumn.RIGHT).slot(3).text(LegacyAdventureUtil.decode("§b得分: §f" + gameStateManager.getScoreB()));
            TabSlot dateTitle = new TabSlot().column(TabColumn.FAR_RIGHT).slot(2).text(LegacyAdventureUtil.decode("§e§l日期:"));
            TabSlot nameInfo = new TabSlot().column(TabColumn.LEFT).slot(3).text(LegacyAdventureUtil.decode("§6ID: §f" + player.getName()));
            TabSlot pingInfo = new TabSlot().column(TabColumn.LEFT).slot(4).text(LegacyAdventureUtil.decode("§6Ping: §f" + player.getPing() + "ms"));
            TabSlot killsInfo = new TabSlot().column(TabColumn.LEFT).slot(6).text(LegacyAdventureUtil.decode("§6擊殺數: §f" + playerData.getKills()));
            TabSlot deathInfo = new TabSlot().column(TabColumn.LEFT).slot(7).text(LegacyAdventureUtil.decode("§6死亡數: §f" + playerData.getDeaths()));
            TabSlot assistInfo = new TabSlot().column(TabColumn.LEFT).slot(8).text(LegacyAdventureUtil.decode("§6助攻數: §f" + playerData.getAssist()));
            TabSlot damageInfo = new TabSlot().column(TabColumn.LEFT).slot(9).text(LegacyAdventureUtil.decode("§6傷害量: §f" + playerData.getTotalDamage()));
            TabSlot dateInfo = new TabSlot().column(TabColumn.FAR_RIGHT).slot(3).text(LegacyAdventureUtil.decode("§f" + formattedDate));

            tabSlots.addAll(Set.of(
                    headBorder, footBorder, statTitle, teamAScore, teamBScore,
                    dateTitle, nameInfo, pingInfo, killsInfo, deathInfo, assistInfo, damageInfo, dateInfo));

            //player list
            addPlayerTabSlots(bukkitPlayer, teamManager.getTeam("A"), TabColumn.MIDDLE, tabSlots);
            addPlayerTabSlots(bukkitPlayer, teamManager.getTeam("B"), TabColumn.RIGHT, tabSlots);
            //information
            return tabSlots;
        }
        return null;
    }

    private void addPlayerTabSlots(Player target, Team team, TabColumn tabColumn, Set<TabSlot> tabSlots) {
        team.getOnlinePlayers().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            PlayerData data = playerDataManager.getPlayerData(player);
            String nametag = nameGetter.getNameWithTeamColor(player) + " " + playerKitManager.getPlayerKitText(player);
            String info = ChatColor.GRAY + " (" + data.getKills() + "/" + data.getDeaths() + "/" + data.getAssist() + ") " + ChatColor.RED + data.getTotalDamage() + "⚡ ";
            if(teamManager.getPlayerTeam(target) == team)
                info += "" + ChatColor.YELLOW + data.getMoney() + "$";
            int index = team.getPlayers().indexOf(uuid);
            tabSlots.add(new TabSlot().column(tabColumn).slot(index * 2 + 5).text(LegacyAdventureUtil.decode(nametag)).skin(Skin.load(uuid)));
            tabSlots.add(new TabSlot().column(tabColumn).slot(index * 2 + 6).text(LegacyAdventureUtil.decode(info)));
        });
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
