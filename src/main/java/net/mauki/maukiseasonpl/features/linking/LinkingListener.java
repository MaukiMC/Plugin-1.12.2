package net.mauki.maukiseasonpl.features.linking;

import net.mauki.maukiseasonpl.core.LiteSQL;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LinkingListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        ResultSet rs = LiteSQL.onQuery("SELECT * FROM connections WHERE uuid = '" + event.getPlayer().getUniqueId() + "'");
        assert rs != null;
        if(!rs.next() || rs.getString("discord_id") == null || rs.getString("discord_id").equalsIgnoreCase("")) {
            String code = "Ein Fehler ist augetreten! Melde dich im Support!";

            if(rs.next()) code = rs.getString("code");
            else {
                String newCode = new RandomStringGenerator(6).nextString();
                LiteSQL.onUpdate("INSERT INTO connections(uuid, code) VALUES ('" + event.getPlayer().getUniqueId() + "', '" + newCode + "')");
                code = newCode;
            }

            event.getPlayer().kickPlayer(ChatColor.RED + "Bitte verifiziere dich auf Discord mit " + ChatColor.GOLD + "/link <code>\n" +
                    ChatColor.RED + "Verwende dabei folgenden Code: " + ChatColor.GREEN + code + "\n" +
                    ChatColor.RED + "Verbinde dich danach erneut auf " + ChatColor.GOLD + "mc.mauki.net" + ChatColor.RED + "!");
        }
    }

}
