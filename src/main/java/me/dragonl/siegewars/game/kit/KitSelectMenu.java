package me.dragonl.siegewars.game.kit;

import io.fairyproject.bukkit.menu.Button;
import io.fairyproject.bukkit.menu.Menu;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.dragonl.siegewars.game.Kit;
import me.dragonl.siegewars.player.PlayerKitManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class KitSelectMenu extends Menu {
    private final PlayerKitManager playerKitManager;

    public KitSelectMenu(PlayerKitManager playerKitManager) {
        this.playerKitManager = playerKitManager;
    }

    @Override
    public void draw(boolean firstInitial) {
        for(int i = 0; i < 10; i++){
            this.set(i,new border());
        }
        for(int i = 17; i < 27; i++){
            this.set(i,new border());
        }
        this.set(10,new Attacker());
        this.set(11,new Archer());
        this.set(12,new Healer());
        this.set(13,new Special());
    }

    @Override
    public String getTitle() {
        return "§e選擇職業:";
    }

    private class Attacker extends Button{

        @Override
        public ItemStack getButtonItem(Player player) {
            return ItemBuilder.of(Material.IRON_SWORD)
                    .name("&e" + playerKitManager.getKitString(Kit.ATTACKER))
                    .lore("&8輸出"
                            ,""
                            ,"&7傷害 &a|||||||||||||||||||||||||&7|||||"
                            ,"&7血量 &c|||||&7|||||||||||||||||||||||||"
                            ,""
                            ,"&e身為一位近戰的輸出角色"
                            ,"&e你必須用最快的速度埋伏並擊殺對手"
                            ,"&e在血量並不高的情況下"
                            ,"&e能夠適時的進攻與撤退便成為重要的戰術之一"
                            ,""
                            ,"&7點擊選擇")
                    .itemFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .build();
        }
    }

    private class Archer extends Button{

        @Override
        public ItemStack getButtonItem(Player player) {
            return ItemBuilder.of(Material.BOW)
                    .name("&e" + playerKitManager.getKitString(Kit.ARCHER))
                    .lore("&8輸出"
                            ,""
                            ,"&7傷害 &a||||||||||||||||||||&7||||||||||"
                            ,"&7血量 &6||||||||||&7||||||||||||||||||||"
                            ,""
                            ,"&e一位強力的遠程角色"
                            ,"&e你必須善用地形的優勢"
                            ,"&e盡快在遠處擊殺敵人"
                            ,"&e因為只要戰線被拉進就會對你十分不利"
                            ,""
                            ,"&7點擊選擇")
                    .build();
        }
    }

    private class Healer extends Button{

        @Override
        public ItemStack getButtonItem(Player player) {
            return ItemBuilder.of(Material.POTION)
                    .name("&e" + playerKitManager.getKitString(Kit.HEALER))
                    .lore("&8輔助"
                            ,""
                            ,"&7傷害 &c||||||||||&7||||||||||||||||||||"
                            ,"&7血量 &6|||||||||||||||&7|||||||||||||||"
                            ,""
                            ,"&e治療師是相當重要的角色之一"
                            ,"&e能夠在關鍵時刻回復隊友的血量"
                            ,"&e相對的你也更容易成為敵人的目標"
                            ,"&e請注意好自身的安全"
                            ,""
                            ,"&7點擊選擇")
                    .data(8261)
                    .itemFlag(ItemFlag.HIDE_POTION_EFFECTS)
                    .build();
        }
    }

    private class Special extends Button{

        @Override
        public ItemStack getButtonItem(Player player) {
            return ItemBuilder.of(Material.BLAZE_POWDER)
                    .name("&e" + playerKitManager.getKitString(Kit.SPECIAL))
                    .lore("&8先鋒"
                            ,""
                            ,"&7傷害 &c||||||||||&7||||||||||||||||||||"
                            ,"&7血量 &a||||||||||||||||||||&7||||||||||"
                            ,""
                            ,"&e特種兵的能力值很平均"
                            ,"&e在探勘與實戰中很具優勢"
                            ,"&e在大多數的情況下能夠自由穿梭戰場"
                            ,"&e並給與隊友戰術協助"
                            ,""
                            ,"&7點擊選擇")
                    .build();
        }
    }

    private class Tank extends Button{

        @Override
        public ItemStack getButtonItem(Player player) {
            return null;
        }
    }

    private class border extends Button{
        @Override
        public ItemStack getButtonItem(Player player) {
            return ItemBuilder.of(Material.STAINED_GLASS_PANE)
                    .data(7)
                    .name("")
                    .build();
        }
    }
}
