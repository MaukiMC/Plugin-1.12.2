package net.mauki.maukiseasonpl.features.linking;

import net.kyori.adventure.text.Component;
import net.mauki.maukiseasonpl.core.LiteSQL;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LinkingListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) throws SQLException {
        String code;
        ResultSet rs = LiteSQL.onQuery("SELECT * FROM connections WHERE uuid = '" + event.getPlayer().getUniqueId() + "'");
        assert rs != null;
        if(rs.next()) {
            if(rs.getString("discord_id") != null) return;
            code = rs.getString("code");
        } else {
            String newCode = new RandomStringGenerator(6).nextString();
            code = newCode;
            LiteSQL.onUpdate("INSERT INTO connections(uuid, code) VALUES ('" + event.getPlayer().getUniqueId() + "', '" + newCode + "')");
        }
        if(code == null) return;
        event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        event.kickMessage(Component.text(ChatColor.RED + "Bitte verifiziere dich auf Discord mit " + ChatColor.GOLD + "/link <code>\n" +
                ChatColor.RED + "Verwende dabei folgenden Code: " + ChatColor.GREEN + code + "\n" +
                ChatColor.RED + "Verbinde dich danach erneut auf " + ChatColor.GOLD + "mc.mauki.net" + ChatColor.RED + "!"));
/*        event.getPlayer().kick(Component.text(ChatColor.RED + "Bitte verifiziere dich auf Discord mit " + ChatColor.GOLD + "/link <code>\n" +
                ChatColor.RED + "Verwende dabei folgenden Code: " + ChatColor.GREEN + code + "\n" +
                ChatColor.RED + "Verbinde dich danach erneut auf " + ChatColor.GOLD + "mc.mauki.net" + ChatColor.RED + "!"));*/
    }

}
