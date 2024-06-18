package me.dragonl.siegewars.game.mapSetup;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.util.Position;
import me.dragonl.siegewars.WorldSetup;
import me.dragonl.siegewars.itemStack.items.SetupWand;
import me.dragonl.siegewars.yaml.MainConfig;
import me.dragonl.siegewars.yaml.MapConfig;
import me.dragonl.siegewars.yaml.element.DestroyableWallElement;
import me.dragonl.siegewars.yaml.element.DestroyableWindowElement;
import me.dragonl.siegewars.yaml.element.MapConfigElement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Command(value = {"setup","su"},permissionNode = "siegewars.admin")
@InjectableComponent
public class SetupCommand extends BaseCommand {
    private final MainConfig mainConfig;
    private final MapConfig mapConfig;
    private final WorldSetup worldSetup;
    private final SetupWand setupWand;
    private final SetupWandManager setupWandManager;

    public SetupCommand(MainConfig mainConfig, MapConfig mapConfig, WorldSetup worldSetup, SetupWand setupWand, SetupWandManager setupWandManager) {
        this.mainConfig = mainConfig;
        this.mapConfig = mapConfig;
        this.worldSetup = worldSetup;
        this.setupWand = setupWand;
        this.setupWandManager = setupWandManager;
    }

    @Command("getWand")
    public void getWand(BukkitCommandContext ctx) {
        Player p = ctx.getPlayer();
        p.getInventory().addItem(setupWand.get(p));
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

    @Command("setMap")
    public void setMap(BukkitCommandContext ctx, @Arg("name") MapSetupArgument arg){
        Player player = ctx.getPlayer();
        if(!mapConfig.getMaps().containsKey(player.getWorld().getName())){
            player.sendMessage("§c你所在的世界並不是一張地圖!");
            return;
        }

        MapConfigElement mapElement = mapConfig.getMaps().get(player.getWorld().getName());

        switch (arg){
            case specSpawn:{
                player.sendMessage("§a已設置 §e" + player.getWorld().getName() + " §a的觀戰玩家出生點!");
                mapElement.setSpecSpawn(BukkitPos.toMCPos(player.getLocation()));
                break;
            }
            case attackSpawn:{
                player.sendMessage("§a已設置 §e" + player.getWorld().getName() + " §a的攻擊方初始點!");
                mapElement.setAttackSpawn(BukkitPos.toMCPos(player.getLocation()));
                break;
            }
            case defendSpawn:{
                player.sendMessage("§a已設置 §e" + player.getWorld().getName() + " §a的防守方初始點!");
                mapElement.setDefendSpawn(BukkitPos.toMCPos(player.getLocation()));
                break;
            }
            case attackSpot:{
                mapElement.getAttackSpot().add(BukkitPos.toMCPos(player.getLocation()));
                player.sendMessage("§a已新增1個 §e" + player.getWorld().getName() + " §a的攻擊方點位! §7(" + mapElement.getAttackSpot().size() + ")");
                break;
            }
            case defendSpot:{
                mapElement.getDefendSpot().add(BukkitPos.toMCPos(player.getLocation()));
                player.sendMessage("§a已新增1個 §e" + player.getWorld().getName() + " §a的防守方點位! §7(" + mapElement.getDefendSpot().size() + ")");
                break;
            }
            case destroyableWall:{
                if(setupWandManager.getPlayerSelection1().containsKey(player.getUniqueId()) && setupWandManager.getPlayerSelection2().containsKey(player.getUniqueId())){
                    Position pos1 = BukkitPos.toMCPos(setupWandManager.getPlayerSelection1().get(player.getUniqueId()));
                    Position pos2 = BukkitPos.toMCPos(setupWandManager.getPlayerSelection2().get(player.getUniqueId()));
                    mapElement.getDestroyableWallList().add(new DestroyableWallElement(pos1, pos2));

                    player.sendMessage("§a已新增1個 §e" + player.getWorld().getName() + " §a的可破牆! §7(" + mapElement.getDestroyableWallList().size() + ")");
                    break;
                }
                else {
                    player.sendMessage("§c你需要圈選一個區域來創建可破牆, 使用 §e/setup getWand §c獲得圈選工具!");
                    return;
                }
            }
            case destroyableWindow:{
                if(setupWandManager.getPlayerSelection1().containsKey(player.getUniqueId()) && setupWandManager.getPlayerSelection2().containsKey(player.getUniqueId())){
                    Position pos1 = BukkitPos.toMCPos(setupWandManager.getPlayerSelection1().get(player.getUniqueId()));
                    Position pos2 = BukkitPos.toMCPos(setupWandManager.getPlayerSelection2().get(player.getUniqueId()));
                    mapElement.getDestroyableWindowList().add(new DestroyableWindowElement(pos1, pos2));

                    player.sendMessage("§a已新增1個 §e" + player.getWorld().getName() + " §a的可破窗! §7(" + mapElement.getDestroyableWindowList().size() + ")");
                    break;
                }
                else {
                    player.sendMessage("§c你需要圈選一個區域來創建可破牆, 使用 §e/setup getWand §c獲得圈選工具!");
                    return;
                }
            }
        }
        mapConfig.save();
    }

    private Boolean isInMap(Player player){
        List<World> mapList = new ArrayList<>();
        mapConfig.getMaps().forEach((m, element) -> {
            mapList.add(Bukkit.getWorld(m));
        });

        return mapList.contains(player.getWorld());
    }
}
