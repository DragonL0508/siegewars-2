package me.dragonl.siegewars.game.mapSetup;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.WorldSetup;
import me.dragonl.siegewars.yaml.MainConfig;
import me.dragonl.siegewars.yaml.MapConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Command(value = {"setup","su"},permissionNode = "siegewars.admin")
@InjectableComponent
public class SetupCommand {
    private final MainConfig mainConfig;
    private final MapConfig mapConfig;
    private final WorldSetup worldSetup;

    public SetupCommand(MainConfig mainConfig, MapConfig mapConfig, WorldSetup worldSetup) {
        this.mainConfig = mainConfig;
        this.mapConfig = mapConfig;
        this.worldSetup = worldSetup;
    }

    @Command("setLobbySpawn")
    public void setSpawn(BukkitCommandContext ctx) {
        Player p = ctx.getPlayer();
        Location l = p.getLocation();
        mainConfig.setLobbySpawnLoc(l);
        mainConfig.save();

        p.sendMessage(ChatColor.GREEN + "已設定大廳重生點!");
    }

    @Command("createMap")
    public void createMap(BukkitCommandContext ctx, @Arg("name") String name) {
        mapConfig.addMap(name);
        worldSetup.bukkitCreateWorld(name);
        worldSetup.worldSetup(Bukkit.getWorld(name));
    }

    @Command("setAttackSpawn")
    public void setAttackSpawn(BukkitCommandContext ctx){

    }

    @Command("addAttackSpot")
    public void addAttackSpot(BukkitCommandContext ctx){

    }

    @Command("setDefendSpawn")
    public void setDefendSpawn(BukkitCommandContext ctx){

    }

    @Command("addDefendSpot")
    public void addDefendSpot(BukkitCommandContext ctx){

    }

    private Boolean isInMap(Player player){
        List<World> mapList = new ArrayList<>();
        mapConfig.getMaps().forEach((m, element) -> {
            mapList.add(Bukkit.getWorld(m));
        });

        return mapList.contains(player.getWorld());
    }
}
