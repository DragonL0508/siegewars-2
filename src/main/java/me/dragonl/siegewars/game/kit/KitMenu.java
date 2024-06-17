package me.dragonl.siegewars.game.kit;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.Kit;
import me.dragonl.siegewars.game.events.PlayerSelectKitEvent;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.UUID;

@InjectableComponent
public class KitMenu {
    private final GuiFactory guiFactory;
    private final PlayerKitManager playerKitManager;
    private final TeamManager teamManager;
    private final NameGetter nameGetter;
    private final KitInfoGetter kitInfoGetter;
    private final KitSelectLogic kitSelectLogic;

    public KitMenu(GuiFactory guiFactory, PlayerKitManager playerKitManager, TeamManager teamManager, NameGetter nameGetter, KitInfoGetter kitInfoGetter, KitSelectLogic kitSelectLogic) {
        this.guiFactory = guiFactory;
        this.playerKitManager = playerKitManager;
        this.teamManager = teamManager;
        this.nameGetter = nameGetter;
        this.kitInfoGetter = kitInfoGetter;
        this.kitSelectLogic = kitSelectLogic;
    }

    public void open(Player player){
        Gui gui = guiFactory.create(Component.text("選擇職業:"));
        NormalPane pane = Pane.normal(9,6);
        pane.fillEmptySlots(GuiSlot.of(ItemBuilder.of(XMaterial.GRAY_STAINED_GLASS_PANE)
                .name(" ")
                .build()));
        pane.setSlot(1,1,7,2,GuiSlot.of(XMaterial.AIR));
        pane.setSlot(0,4,8,5,GuiSlot.of(ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE)
                .name(" ")
                .build()));
        pane.setSlot(2,4,6,4,GuiSlot.of(ItemBuilder.of(XMaterial.BARRIER)
                .name("&c空")
                .build()));
        pane.setSlot(2,5,6,5,GuiSlot.of(ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE)
                .name("&c沒有職業")
                .build()));
        pane.setSlot(1,1,GuiSlot.of(ItemBuilder.of(XMaterial.IRON_SWORD)
                .name("&e" + kitInfoGetter.getKitString(Kit.ATTACKER))
                .lore("&8輸出"
                        ,""
                        ,"&7傷害 &a|||||||||||||||||||||||||&7|||||"
                        ,"&7防禦 &c|||||&7|||||||||||||||||||||||||"
                        ,""
                        ,"&e身為一位近戰的輸出角色"
                        ,"&e你必須用最快的速度埋伏並擊殺對手"
                        ,"&e在防禦並不高的情況下"
                        ,"&e能夠適時的進攻與撤退便成為重要的戰術之一"
                        ,""
                        ,"&7點擊選擇")
                .itemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .build(), clickedPlayer -> {
            kitSelectLogic.kitSelect(clickedPlayer, Kit.ATTACKER);
        }));
        pane.setSlot(2,1,GuiSlot.of(ItemBuilder.of(XMaterial.BOW)
                .name("&e" + kitInfoGetter.getKitString(Kit.ARCHER))
                .lore("&8輸出"
                        ,""
                        ,"&7傷害 &a||||||||||||||||||||&7||||||||||"
                        ,"&7防禦 &6||||||||||&7||||||||||||||||||||"
                        ,""
                        ,"&e一位強力的遠程角色"
                        ,"&e你必須善用地形的優勢"
                        ,"&e盡快在遠處擊殺敵人"
                        ,"&e因為只要戰線被拉進就會對你十分不利"
                        ,""
                        ,"&7點擊選擇")
                .build(), clickedPlayer -> {
            kitSelectLogic.kitSelect(clickedPlayer, Kit.ARCHER);
        }));
        pane.setSlot(3,1,GuiSlot.of(ItemBuilder.of(XMaterial.POTION)
                .name("&e" + kitInfoGetter.getKitString(Kit.HEALER))
                .lore("&8輔助"
                        ,""
                        ,"&7傷害 &c||||||||||&7||||||||||||||||||||"
                        ,"&7防禦 &6|||||||||||||||&7|||||||||||||||"
                        ,""
                        ,"&e治療師是相當重要的角色之一"
                        ,"&e能夠在關鍵時刻回復隊友的防禦"
                        ,"&e相對的你也更容易成為敵人的目標"
                        ,"&e請注意好自身的安全"
                        ,""
                        ,"&7點擊選擇")
                .data(8261)
                .itemFlag(ItemFlag.HIDE_POTION_EFFECTS)
                .build(), clickedPlayer -> {
            kitSelectLogic.kitSelect(clickedPlayer, Kit.HEALER);
        }));
        pane.setSlot(4,1,GuiSlot.of(ItemBuilder.of(Material.BLAZE_POWDER)
                .name("&e" + kitInfoGetter.getKitString(Kit.SPECIAL))
                .lore("&8先鋒"
                        ,""
                        ,"&7傷害 &c||||||||||&7||||||||||||||||||||"
                        ,"&7防禦 &a||||||||||||||||||||&7||||||||||"
                        ,""
                        ,"&e特種兵的能力值很平均"
                        ,"&e在探勘與實戰中很具優勢"
                        ,"&e在大多數的情況下能夠自由穿梭戰場"
                        ,"&e並給與隊友戰術協助"
                        ,""
                        ,"&7點擊選擇")
                .build(), clickedPlayer -> {
            kitSelectLogic.kitSelect(clickedPlayer, Kit.SPECIAL);
        }));
        pane.setSlot(5,1,GuiSlot.of(ItemBuilder.of(Material.IRON_CHESTPLATE)
                .name("&e" + kitInfoGetter.getKitString(Kit.TANK))
                .lore("&8輔助"
                        ,""
                        ,"&7傷害 &c|||||&7|||||||||||||||||||||||||"
                        ,"&7防禦 &a||||||||||||||||||||&7||||||||||"
                        ,""
                        ,"&e身為團戰最堅強的後盾"
                        ,"&e你必須跟緊你的隊友並適時幫助他們"
                        ,"&e雖然沒辦法對敵人造成太多傷害"
                        ,"&e但使用恰當可以為團隊帶來莫大的利益"
                        ,""
                        ,"&7點擊選擇")
                .build(), clickedPlayer -> {
            kitSelectLogic.kitSelect(clickedPlayer, Kit.TANK);
        }));
        pane.setSlot(6,1,GuiSlot.of(ItemBuilder.of(Material.STONE_HOE)
                .name("&e" + kitInfoGetter.getKitString(Kit.REAPER))
                .lore("&8先鋒"
                        ,""
                        ,"&7傷害 &c||||||||||&7||||||||||||||||||||"
                        ,"&7防禦 &6|||||||||||||||&7|||||||||||||||"
                        ,""
                        ,"&e死神是一個適合擾亂敵方節奏的角色"
                        ,"&e身為先鋒，你可以最先探勘敵情"
                        ,"&e並通知隊友敵方的狀態"
                        ,"&e無論是進攻或防守都有很大的發揮空間"
                        ,""
                        ,"&7點擊選擇")
                .build(), clickedPlayer -> {
            kitSelectLogic.kitSelect(clickedPlayer, Kit.REAPER);
        }));
        gui.onOpenCallback($ -> {
            gui.getEventNode().addListener(PlayerSelectKitEvent.class, event -> {
                gui.update(player);
            });
        });
        //show every player selected
        gui.onDrawCallback($ -> {
            List<UUID> playerList = teamManager.getPlayerTeam(player).getPlayers();
            playerList.forEach(uuid -> {
                Player p = Bukkit.getPlayer(uuid);
                Kit kit = playerKitManager.getPlayerKit(p);
                int slot = playerList.indexOf(uuid);
                pane.setSlot(slot + 2,4,GuiSlot.of(ItemBuilder.of(XMaterial.PLAYER_HEAD)
                        .skull(p.getName())
                        .name(nameGetter.getChatName(p))
                        .build()));
                if(kit != Kit.NONE)
                    pane.setSlot(slot + 2, 5, GuiSlot.of(ItemBuilder.of(kitInfoGetter.getKitIcon(kit))
                            .name("&a已選擇: &e" + kitInfoGetter.getKitString(kit))
                            .itemFlag(ItemFlag.HIDE_ATTRIBUTES)
                            .build()));
            });
        });

        gui.addPane(pane);
        gui.open(player);
    }
}
