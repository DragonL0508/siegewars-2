package me.dragonl.siegewars.game.ingame;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.Titles;
import com.github.retrooper.packetevents.protocol.world.chunk.TileEntity;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCGameProfile;
import io.fairyproject.mc.util.Property;
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

import java.util.List;
import java.util.UUID;

@InjectableComponent
public class BombManager {
    private final InGameTimerManager timerManager;
    private final InGameRunTime inGameRunTime;
    private Location bombLocation;

    public Location getBombLocation() {
        return bombLocation;
    }

    public BombManager(InGameTimerManager timerManager, InGameRunTime inGameRunTime) {
        this.timerManager = timerManager;
        this.inGameRunTime = inGameRunTime;
    }

    public void plant(Player player, Location location) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            Titles.sendTitle(p, 0, 50, 10, "", "§c炸藥包已被放置");
            p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1, 1.25f);
        });

        this.bombLocation = location;
        Block bomb = location.getBlock();
        bomb.setType(Material.SKULL);
        Skull skullData = (Skull)bomb.getState();
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
}
