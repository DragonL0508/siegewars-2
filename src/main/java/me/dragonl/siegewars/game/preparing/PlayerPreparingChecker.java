package me.dragonl.siegewars.game.preparing;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.Titles;
import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.mc.scheduler.MCSchedulers;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.SoundPlayer;
import me.dragonl.siegewars.game.ingame.InGamePreparingExecuter;
import me.dragonl.siegewars.player.data.PlayerDataManager;
import me.dragonl.siegewars.team.TeamManager;
import me.dragonl.siegewars.yaml.element.MapConfigElement;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@InjectableComponent
public class PlayerPreparingChecker extends BukkitRunnable {
    private final GameStateManager gameStateManager;
    private final PlayerPreparingManager playerPreparingManager;
    private final InGamePreparingExecuter inGamePreparingExecuter;
    private final TeamManager teamManager;
    private final SoundPlayer soundPlayer;
    private final PlayerDataManager playerDataManager;

    public PlayerPreparingChecker(GameStateManager gameStateManager, PlayerPreparingManager playerPreparingManager, InGamePreparingExecuter inGamePreparingExecuter, TeamManager teamManager, SoundPlayer soundPlayer, PlayerDataManager playerDataManager) {
        this.gameStateManager = gameStateManager;
        this.playerPreparingManager = playerPreparingManager;
        this.inGamePreparingExecuter = inGamePreparingExecuter;
        this.teamManager = teamManager;
        this.soundPlayer = soundPlayer;
        this.playerDataManager = playerDataManager;
    }

    @PostInitialize
    public void init() {
        this.runTaskTimer(BukkitPlugin.INSTANCE, 0, 1);
    }

    @Override
    public void run() {
        if (gameStateManager.isCurrentGameState(GameState.PREPARING)) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                Titles.sendTitle(player, 0, 30, 0, "§a玩家準備中", "§7(點選綠寶石進行準備)");
            });
            if (!playerPreparingManager.playerPrepareMap.containsValue(false)) {
                teamManager.swTeamSplits(teamManager.getTeam("lobby").getOnlineBukkitPlayers());
                Bukkit.getOnlinePlayers().forEach(Titles::clearTitle);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.getInventory().clear();
                });
                this.cancel();

                final int[] timer = {10};
                MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
                    List<UUID> uuids = Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toList());
                    if (timer[0] == 10) {
                        Bukkit.getServer().broadcastMessage("§a玩家準備完成，遊戲即將開始...");
                        soundPlayer.playSound(uuids, Sound.CLICK, 1, 1);
                    } else if (timer[0] > 0 && timer[0] <= 5) {
                        Bukkit.getServer().broadcastMessage("§e遊戲將在§c" + timer[0] + "§e秒後開始!");
                        soundPlayer.playSound(uuids, Sound.CLICK, 1, 0.5f);
                    }
                    if (timer[0] == 0) {
                        Bukkit.getServer().broadcastMessage("§a遊戲啟動中,請稍後...");
                        gameStateManager.setCurrentGameState(GameState.IN_GAME);
                        gameStartLogic();

                        this.cancel();
                    }
                    timer[0]--;
                },0,20);
            }
        }
    }

    private void gameStartLogic() {
        //choose attack or defend
//        if (Math.random() < 0.5)
//            gameStateManager.setAttackTeam(teamManager.getTeam("A"));
//        else
//            gameStateManager.setAttackTeam(teamManager.getTeam("B"));
        gameStateManager.setAttackTeam(teamManager.getTeam("A"));

        //players loop
        //send players to map
        MapConfigElement element = gameStateManager.getSelectedMap();
        Bukkit.getOnlinePlayers().forEach(p -> {
            Random r = new Random();
            ItemStack air = ItemBuilder.of(XMaterial.AIR).build();
            p.getInventory().clear();
            p.getInventory().setArmorContents(new ItemStack[]{air,air,air,air});
            p.updateInventory();
            p.setGameMode(GameMode.SPECTATOR);
            p.teleport(BukkitPos.toBukkitLocation(element.getSpecSpawn()));
            playerDataManager.getPlayerData(p).getBuyCountsMap().clear();

            if (teamManager.getPlayerTeam(p) == gameStateManager.getAttackTeam())
                Titles.sendTitle(p, 10, 70, 20, "§c攻擊方", "§e你的隊伍將先擔任");
            else if (teamManager.swGetAnotherTeam(p) == gameStateManager.getAttackTeam())
                Titles.sendTitle(p, 10, 70, 20, "§a防守方", "§e你的隊伍將先擔任");

            p.playSound(p.getLocation(), Sound.PORTAL_TRAVEL, 1, 1);
        });

        MCSchedulers.getGlobalScheduler().schedule(inGamePreparingExecuter::start, 20 * 5);
    }
}
