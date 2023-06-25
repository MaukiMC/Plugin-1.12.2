package net.mauki.maukiseasonpl.features.linking;

import net.mauki.maukiseasonpl.core.LiteSQL;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LinkingListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        ResultSet rs = LiteSQL.onQuery("SELECT * FROM connections WHERE uuid = '" + event.getPlayer().getUniqueId() + "'");
        assert rs != null;
        if(!rs.next() || rs.getString("discord_id") == null || rs.getString("discord_id").equalsIgnoreCase("")) {
            event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), 34.5, 1, -2901.5));
            event.getPlayer().sendMessage(ChatColor.GOLD + "Bitte verifiziere dich mit Discord mit " + ChatColor.GRAY + "/link" + ChatColor.GOLD + "!");
            return;
        }
        if(!(event.getPlayer().getLocation().getBlockX() == 34.5 && event.getPlayer().getLocation().getBlockY() == 1 && event.getPlayer().getLocation().getBlockZ() == -2901.5)) {
            return;
        }
        event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), 204, 63, 221));
        event.getPlayer().sendMessage(ChatColor.GREEN + "Du wurdest erfolgreich verifiziert!");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) throws SQLException {
        ResultSet rs = LiteSQL.onQuery("SELECT * FROM connections WHERE uuid = '" + event.getPlayer().getUniqueId() + "'");
        assert rs != null;
        if(!rs.next() || rs.getString("discord_id") == null || rs.getString("discord_id").equalsIgnoreCase("")) {
            event.setCancelled(true);
        }
    }

}
