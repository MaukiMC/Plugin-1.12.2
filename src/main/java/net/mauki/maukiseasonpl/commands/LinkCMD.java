package net.mauki.maukiseasonpl.commands;

import net.mauki.maukiseasonpl.core.LiteSQL;
import net.mauki.maukiseasonpl.core.MessageConstants;
import net.mauki.maukiseasonpl.features.linking.RandomStringGenerator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Command to generate a code for linking your discord account
 */
public class LinkCMD implements CommandExecutor {

    /**
     * The code which will be executed when command is being called
     * @param sender The sender of the command
     * @param command The command itself
     * @param label The label of the command
     * @param args The arguments which were sent with the command
     * @return boolean if the command were performed successfully
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if(!(sender instanceof Player)) {
                sender.sendMessage(MessageConstants.NEED_TO_BE_A_PLAYER());
                return false;
            }
            Player player = (Player) sender;
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
