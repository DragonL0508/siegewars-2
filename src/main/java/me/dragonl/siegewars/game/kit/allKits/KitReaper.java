package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.kit.KitInfoGetter;
import me.dragonl.siegewars.game.kit.SiegeWarsAbstractKit;
import me.dragonl.siegewars.itemStack.items.ability.ReaperAbilityItem;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@InjectableComponent
public class KitReaper extends SiegeWarsAbstractKit {
    private final ReaperAbilityItem reaperAbilityItem;
    private final KitInfoGetter kitInfoGetter;
    public KitReaper(
            PlayerKitManager playerKitManager
            , TeamManager teamManager
            , NameGetter nameGetter, ReaperAbilityItem reaperAbilityItem, KitInfoGetter kitInfoGetter) {
        super(playerKitManager, teamManager, nameGetter, kitInfoGetter);
        this.reaperAbilityItem = reaperAbilityItem;
        this.kitInfoGetter = kitInfoGetter;
    }

    @Override
    public Boolean useAbility(Player player) {
        PotionEffect resistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 140, 1, false, false);
        PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, 140, 0, false, false);
        PotionEffect weekness = new PotionEffect(PotionEffectType.WEAKNESS, 140, 100, false, false);
        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 140, 0, false, false);
        PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false, false);
        PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 1, 0, false, false);
        PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 20, 0, false, false);
        player.addPotionEffect(resistance);
        player.addPotionEffect(invisibility);
        player.addPotionEffect(speed);
        player.addPotionEffect(weekness);
        BukkitTask debuffTask = new BukkitRunnable(){
            @Override
            public void run() {
                player.getWorld().getNearbyEntities(player.getLocation(),2,2,2).forEach(e -> {
                    if(e.getType() == EntityType.PLAYER){
                        Player target = (Player) e;
                        if(teamManager.swGetAnotherTeam(player) == teamManager.getPlayerTeam(target)){
                            target.addPotionEffect(blind);
                            target.addPotionEffect(slowness);
                            target.addPotionEffect(wither);
                        }
                    }
                });
                //player visual effects
                player.getWorld().spigot().playEffect(player.getLocation().add(0,1,0), Effect.LARGE_SMOKE, 1, 0, 0.15f, 0.4f, 0.15f, 0, 10, 32);
            }
        }.runTaskTimer(BukkitPlugin.INSTANCE, 0, 1);
        new BukkitRunnable(){
            @Override
            public void run() {
                player.getWorld().playSound(player.getLocation(), Sound.FIZZ, 1, 0.5f);
                player.getWorld().spigot().playEffect(player.getLocation().add(0,1,0), Effect.LARGE_SMOKE, 1, 0, 0.15f, 0.4f, 0.15f, 0.5f, 50, 32);
                debuffTask.cancel();
            }
        }.runTaskLater(BukkitPlugin.INSTANCE, 140);

        player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_SCREAM, 1, 0.35f);
        player.getWorld().playSound(player.getLocation(), Sound.PORTAL_TRIGGER, 1, 1.75f);

        return true;
    }

    @Override
    protected void giveKitItems(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.setHelmet(ItemBuilder.of(XMaterial.LEATHER_HELMET)
                .color(teamManager.getPlayerTeam(player).getBukkitColor())
                .enchantment(XEnchantment.PROTECTION_PROJECTILE, 2)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setChestplate(ItemBuilder.of(XMaterial.CHAINMAIL_CHESTPLATE)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setLeggings(ItemBuilder.of(XMaterial.LEATHER_LEGGINGS)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .color(Color.BLACK)
                .build());
        inv.setBoots(ItemBuilder.of(XMaterial.LEATHER_BOOTS)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(0, ItemBuilder.of(XMaterial.WOODEN_SWORD)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(1, ItemBuilder.of(XMaterial.FISHING_ROD)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(2, ItemBuilder.of(XMaterial.BOW)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(8, reaperAbilityItem.get(player));
        inv.setItem(9, ItemBuilder.of(XMaterial.ARROW)
                .amount(8)
                .build());
    }
}
