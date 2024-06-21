package me.dragonl.siegewars.player;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import io.fairyproject.bukkit.util.LegacyAdventureUtil;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTag;
import io.fairyproject.mc.nametag.NameTagAdapter;
import me.dragonl.siegewars.team.NametagVisibility;
import me.dragonl.siegewars.team.Team;
import me.dragonl.siegewars.team.TeamManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

@InjectableComponent
public class PlayerNameTag extends NameTagAdapter {

    private final TeamManager teamManager;
    private final NameTagTemporaryManager nameTagTemporaryManager;

    public PlayerNameTag(TeamManager teamManager, NameTagTemporaryManager nameTagTemporaryManager) {
        super("nametag", 0);
        this.teamManager = teamManager;
        this.nameTagTemporaryManager = nameTagTemporaryManager;
    }

    @Override
    public NameTag fetch(MCPlayer player, MCPlayer target) {
        Player bukkitTarget = target.as(Player.class);
        Player bukkitPlayer = player.as(Player.class);

        if(teamManager.getPlayerTeam(bukkitPlayer) == null)
            return new NameTag(LegacyAdventureUtil.decode("NoTeam "), Component.empty(), TextColor.color(255, 255, 255), WrapperPlayServerTeams.NameTagVisibility.ALWAYS);

        WrapperPlayServerTeams.NameTagVisibility nameTagVisibility = WrapperPlayServerTeams.NameTagVisibility.ALWAYS;
        //nametag visibility option
        if (teamManager.getPlayerTeam(bukkitPlayer) == teamManager.getPlayerTeam(bukkitTarget) && teamManager.getPlayerTeam(bukkitTarget).getNametagVisibility() == NametagVisibility.hideForOwnTeams)
            nameTagVisibility = WrapperPlayServerTeams.NameTagVisibility.NEVER;
        else if (teamManager.getPlayerTeam(bukkitPlayer) != teamManager.getPlayerTeam(bukkitTarget) && teamManager.getPlayerTeam(bukkitTarget).getNametagVisibility() == NametagVisibility.hideForOtherTeams)
            nameTagVisibility = WrapperPlayServerTeams.NameTagVisibility.NEVER;
        else if (teamManager.getPlayerTeam(bukkitPlayer).getGroupID() == teamManager.getPlayerTeam(bukkitTarget).getGroupID() && teamManager.getPlayerTeam(bukkitTarget).getNametagVisibility() == NametagVisibility.hideForGroupTeam) {
            if (teamManager.getPlayerTeam(bukkitPlayer) != teamManager.getPlayerTeam(bukkitTarget))
                nameTagVisibility = WrapperPlayServerTeams.NameTagVisibility.NEVER;
        }

        DecimalFormat format = new DecimalFormat("#.#");
        //check if force display
        if (nameTagTemporaryManager.getPlayerSetMap().containsKey(bukkitTarget.getUniqueId())) {
            if (nameTagTemporaryManager.getPlayerSetMap().get(bukkitTarget.getUniqueId()).contains(bukkitPlayer.getUniqueId())) {
                return new NameTag(Component.text("§c")
                        , Component.text(" §4-" + format.format(new Double(bukkitTarget.getLastDamageCause().getFinalDamage())))
                        , TextColor.color(0xFFFFFF)
                        , WrapperPlayServerTeams.NameTagVisibility.ALWAYS);
            }
        }

        //output
        if (teamManager.isInTeam(bukkitTarget)) {
            Team team = teamManager.getPlayerTeam(bukkitTarget);
            Component prefix = LegacyAdventureUtil.decode(team.getPrefix());
            Component suffix = LegacyAdventureUtil.decode(team.getSuffix());
            TextColor color = LegacyComponentSerializer.parseChar(team.getColor().getChar()).color();
            return new NameTag(prefix, suffix, color, nameTagVisibility);
        }

        return new NameTag(LegacyAdventureUtil.decode("NoTeam "), Component.empty(), TextColor.color(255, 255, 255), nameTagVisibility);
    }
}