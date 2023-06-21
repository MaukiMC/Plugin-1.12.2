package net.mauki.maukiseasonpl.commands.overwirtten;

import net.mauki.maukiseasonpl.core.MessageConstants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to overwrite /reload and /rl
 * @deprecated
 */
@Deprecated
public class ReloadCMD implements CommandExecutor {

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
        if(sender instanceof Player && !sender.isOp()) {
            sender.sendMessage(MessageConstants.NOT_AN_OPERATOR());
            return false;
        }
        sender.sendMessage(ChatColor.RED + "Befehl deaktiviert. Verwende /stop <zeit>");
        return true;
    }
}
