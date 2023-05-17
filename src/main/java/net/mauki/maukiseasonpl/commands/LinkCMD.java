package net.mauki.maukiseasonpl.commands;

import net.mauki.maukiseasonpl.core.LiteSQL;
import net.mauki.maukiseasonpl.features.linking.RandomStringGenerator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LinkCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if(!(sender instanceof Player player)) {
                sender.sendMessage("Fehlgeschlagen! Du musst ein Spieler sein, um diesen Befehl verwenden zu können.");
                return false;
            }
            ResultSet rs = LiteSQL.onQuery("SELECT * FROM connections WHERE uuid = '" + player.getUniqueId() + "'");
            assert rs != null;
            if(rs.next()) {
                player.sendMessage(ChatColor.GRAY + "Dein Verlinkungscode ist: " + ChatColor.GOLD + rs.getString("code"));
                return true;
            } else {
                String newCode = new RandomStringGenerator(6).nextString();
                LiteSQL.onUpdate("INSERT INTO connections(uuid, code) VALUES ('" + player.getUniqueId() + "', '" + newCode + "')");
                player.sendMessage(ChatColor.GRAY + "Dein Verlinkungscode ist: " + ChatColor.GOLD + newCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
