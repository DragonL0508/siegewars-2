package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.mc.util.Position;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;
import me.dragonl.siegewars.game.MapObjectCatcher;
import me.dragonl.siegewars.game.MapObjectDestroyer;
import me.dragonl.siegewars.game.kit.SiegeWarsKit;
import me.dragonl.siegewars.game.mapSetup.SetupWandManager;
import me.dragonl.siegewars.itemStack.items.ability.TankAbilityItem;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class TankKit implements SiegeWarsKit {
    private final TeamManager teamManager;
    private final TankAbilityItem tankAbilityItem;
    private final MapObjectCatcher mapObjectCatcher;
    private final MapObjectDestroyer mapObjectDestroyer;

    public TankKit(TeamManager teamManager, TankAbilityItem tankAbilityItem, MapObjectCatcher mapObjectCatcher, MapObjectDestroyer mapObjectDestroyer) {
        this.teamManager = teamManager;
        this.tankAbilityItem = tankAbilityItem;
        this.mapObjectCatcher = mapObjectCatcher;
        this.mapObjectDestroyer = mapObjectDestroyer;
    }

    @Override
    public String getID() {
        return "tank";
    }

    @Override
    public String getKitName() {
        return "衝撞者";
    }

    @Override
    public String getKitChar() {
        return "§7§l衝";
    }

    @Override
    public ItemStack getKitIcon() {
        return ItemBuilder.of(XMaterial.IRON_CHESTPLATE).build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "&8輔助"
                , ""
                , "&7傷害 &c|||||&7|||||||||||||||||||||||||"
                , "&7防禦 &a||||||||||||||||||||&7||||||||||"
                , ""
                , "&e身為團戰最堅強的後盾"
                , "&e你必須跟緊你的隊友並適時幫助他們"
                , "&e雖然沒辦法對敵人造成太多傷害"
                , "&e但使用恰當可以為團隊帶來莫大的利益"
                , ""
                , "&7點擊選擇"
        };
    }

    @Override
    public ItemStack[] getArmors(Player player) {
        return new ItemStack[]{
                ItemBuilder.of(XMaterial.LEATHER_HELMET)
                        .color(teamManager.getPlayerTeam(player).getBukkitColor())
                        .enchantment(XEnchantment.PROTECTION_PROJECTILE, 2)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .build(),
                ItemBuilder.of(XMaterial.IRON_CHESTPLATE)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .build(),
                ItemBuilder.of(XMaterial.LEATHER_LEGGINGS)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .color(Color.GRAY)
                        .build(),
                ItemBuilder.of(XMaterial.LEATHER_BOOTS)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .color(Color.WHITE)
                        .build()
        };
    }

    @Override
    public ItemStack[] getItems() {
        return new ItemStack[]{
                ItemBuilder.of(XMaterial.GOLDEN_PICKAXE)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .enchantment(XEnchantment.KNOCKBACK, 1)
                        .build(),
                ItemBuilder.of(XMaterial.FISHING_ROD)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .build(),
                ItemBuilder.of(XMaterial.BOW)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .build(),
                ItemBuilder.of(XMaterial.ARROW)
                        .amount(8)
                        .build()
        };
    }

    @Override
    public ItemStack getAbilityItem(Player player) {
        return tankAbilityItem.get(player);
    }

    @Override
    public Boolean useAbility(Player player) {
        Location location = player.getLocation();

        player.getWorld().playSound(location, Sound.COW_HURT, 1, 0.5f);
        sprint(player, location.getYaw());
        return true;
    }

    private void sprint(Player player, float yaw) {
        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            doSprintVelocity(player, yaw);

            Location location = player.getLocation().add(player.getVelocity().setY(0));
            Block blockAt = player.getWorld().getBlockAt(location);

            if (blockAt.getType() != Material.AIR) {
                hitBlock(player, blockAt);
                return TaskResponse.failure("");
            }

            return TaskResponse.continueTask();
        }, 0, 1, RepeatPredicate.length(Duration.ofSeconds(3))).getFuture();
        future.thenRun(() -> onSprintEnded(player));
    }

    private void doSprintVelocity(Player player, float yaw) {
        Location location = player.getLocation();
        Location to = new Location(player.getWorld(), location.getX(), location.getY(), location.getZ(), yaw, 0);

        player.teleport(to);

        Vector dir = location.getDirection().normalize().multiply(0.65);
        player.setVelocity(new Vector(dir.getX(), -1, dir.getZ()));
        player.getWorld().spigot().playEffect(location.clone().add(0, 1.8, 0), Effect.VILLAGER_THUNDERCLOUD, 1, 0, 0.1f, 0.1f, 0.1f, 0, 1, 32);
    }

    private static void onSprintEnded(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.FIZZ, 1, 0.85f);
        player.getWorld().spigot().playEffect(player.getLocation().add(0,2,0), Effect.EXTINGUISH, 1, 0, 0, 0, 0, 0, 10, 32);
    }

    private void hitBlock(Player player, Block block) {
        Location location = block.getLocation();
        Position mcPos = BukkitPos.toMCPos(location.clone());
        player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1, 0.85f);
        player.getWorld().spigot().playEffect(player.getLocation().add(0,1,0), Effect.EXPLOSION_HUGE, 1, 0, 0, 0, 0, 0, 1, 32);

        if(mapObjectCatcher.isBaffle(location.clone()))
            mapObjectDestroyer.destroyBaffle(location);
        if(mapObjectCatcher.isDestroyableWall(mcPos))
            mapObjectDestroyer.destroyWall(location);
        if(mapObjectCatcher.isDestroyableWindow(mcPos))
            mapObjectDestroyer.destroyWindow(location);
    }
}
