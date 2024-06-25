package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.Titles;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TankKit implements SiegeWarsKit {
    private final TeamManager teamManager;
    private final TankAbilityItem tankAbilityItem;
    private final MapObjectCatcher mapObjectCatcher;
    private final MapObjectDestroyer mapObjectDestroyer;
    private Map<UUID, List<UUID>> hitPlayersList = new HashMap<>();

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

            if (blockAt.getType() != Material.AIR)
                if (hitBlock(player, blockAt))
                    return TaskResponse.failure("");

            for (Entity nearbyEntity : player.getNearbyEntities(0.65, 0.65, 0.65)) {
                if (nearbyEntity.getType() == EntityType.PLAYER)
                    if (teamManager.getPlayerTeam((Player) nearbyEntity) == teamManager.swGetAnotherTeam(player)
                    && !hitPlayersList.getOrDefault(player.getUniqueId(), Arrays.asList()).contains(nearbyEntity.getUniqueId()))
                        hitPlayer(player, (Player) nearbyEntity);
            }

            return TaskResponse.continueTask();
        }, 0, 1, RepeatPredicate.length(Duration.ofSeconds(2))).getFuture();
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

    private void onSprintEnded(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.FIZZ, 1, 0.85f);
        player.getWorld().spigot().playEffect(player.getLocation().add(0, 2, 0), Effect.SMOKE, 1, 0, 0.1f, 0.1f, 0.1f, 0.05f, 30, 32);
        hitPlayersList.remove(player.getUniqueId());
    }

    private void hitPlayer(Player player, Player target) {
        PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 20, 1),
                blind = new PotionEffect(PotionEffectType.BLINDNESS, 40, 0),
                slowMining = new PotionEffect(PotionEffectType.SLOW_DIGGING, 20, 100);

        target.addPotionEffect(slowness);
        target.addPotionEffect(blind);
        target.addPotionEffect(slowMining);
        target.getWorld().playSound(target.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.5f, 0.85f);
        target.damage(3, player);
        Titles.sendTitle(target, 0, 20, 10, " ", "§c你受到了 §e" + player.getName() + " §c的撞擊");

        List<UUID> hitPlayers = new ArrayList<>(hitPlayersList.getOrDefault(player.getUniqueId(), Arrays.asList()));
        hitPlayers.add(target.getUniqueId());
        hitPlayersList.put(player.getUniqueId(), hitPlayers);
    }

    private Boolean hitBlock(Player player, Block block) {
        Location location = block.getLocation();
        Position mcPos = BukkitPos.toMCPos(location.clone());
        player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1, 0.85f);
        player.getWorld().spigot().playEffect(player.getLocation().add(0, 1, 0), Effect.EXPLOSION_HUGE, 1, 0, 0, 0, 0, 0, 1, 32);

        if (mapObjectCatcher.isBaffle(location.clone())) {
            mapObjectDestroyer.destroyBaffle(location);
            return false;
        }
        if (mapObjectCatcher.isDestroyableWall(mcPos)) {
            mapObjectDestroyer.destroyWall(location);
            return false;
        }
        if (mapObjectCatcher.isDestroyableWindow(mcPos)) {
            mapObjectDestroyer.destroyWindow(location);
            return false;
        }

        return true;
    }
}
