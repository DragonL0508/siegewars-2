package me.dragonl.siegewars.game.ingame;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.Titles;
import com.github.retrooper.packetevents.protocol.world.chunk.TileEntity;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCGameProfile;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.mc.util.Property;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.game.ingame.ingameTimer.BombTimer;
import me.dragonl.siegewars.game.ingame.ingameTimer.InGameTimerManager;
import me.dragonl.siegewars.game.ingame.ingameTimer.Timer;
import me.dragonl.siegewars.game.ingame.ingameTimer.TimerMap;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@InjectableComponent
public class BombManager {
    private final InGameTimerManager timerManager;
    private final InGameRunTime inGameRunTime;
    private Location bombLocation = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
    private Boolean isPlanting = false;
    private Boolean isDefusing = false;

    public Boolean getDefusing() {
        return isDefusing;
    }

    public void setDefusing(Boolean defusing) {
        isDefusing = defusing;
    }

    public Boolean getPlanting() {
        return isPlanting;
    }

    public void setPlanting(Boolean planting) {
        isPlanting = planting;
    }

    public Location getBombLocation() {
        return bombLocation;
    }

    public BombManager(InGameTimerManager timerManager, InGameRunTime inGameRunTime) {
        this.timerManager = timerManager;
        this.inGameRunTime = inGameRunTime;
    }

    public void plant(Player player, Location location) {
        isPlanting = false;

        Bukkit.getOnlinePlayers().forEach(p -> {
            Titles.sendTitle(p, 0, 50, 10, "", "§c炸藥包已被放置");
            p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1, 1.25f);
        });

        this.bombLocation = location;
        Block bomb = location.getBlock();
        bomb.setType(Material.SKULL);
        Skull skullData = (Skull) bomb.getState();
        skullData.setSkullType(SkullType.PLAYER);
        skullData.setRawData((byte) 1);
        skullData.update(true);

        SkullUtils.setSkin(bomb, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGY4NzRiYjViNmVmN2IzNDM0YTU4YjMxNDk2MjUyMTg2Mjk1YzM3OWJlOTk2OTM0ZDEwM2QxNWVhMjI1Y2JhMyJ9fX0=");

        location.getWorld().spigot().playEffect(location.add(0.5, 0, 0.5), Effect.LAVA_POP, 1, 0, 0, 0, 0, 0.05f, 10, 16);

        //timers
        inGameRunTime.setBomblocation(location);
        Timer running = timerManager.getTimerRunningNow();
        List<TimerMap> timerMaps = timerManager.getTimers();
        Timer bombTimer = timerManager.registerTimer(new BombTimer(55, inGameRunTime), timerMaps.indexOf(timerManager.getTimerMap(running.getID())) + 1);

        timerManager.stopTimer(timerManager.getTimerRunningNow());
        timerManager.startTimer(bombTimer);
    }

    public void defuse(Player player) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            Titles.sendTitle(p, 0, 50, 10, "§a炸藥包已被拆除", "§e拆除者: " + player.getName());
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1.25f);
        });

        Block bomb = bombLocation.getBlock();
        bomb.getWorld().spigot().playEffect(bomb.getLocation().add(0.5, 0, 0.5), Effect.LAVA_POP, 1, 0, 0, 0, 0, 0.05f, 10, 16);
        bomb.setType(Material.AIR);

        bombLocation = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
        isDefusing = false;

        //timer
        Timer running = timerManager.getTimerRunningNow();
        timerManager.stopAndUnregisterTimer(running);
        timerManager.startTimer(timerManager.getTimerMap("roundEnd").getValue());

    }
}
