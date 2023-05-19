package net.mauki.maukiseasonpl.commands;

import net.mauki.maukiseasonpl.core.MessageConstants;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to see your enderchest or the enderchest of someone else
 */
public class EndseeCMD implements CommandExecutor {
    
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
        if(!(sender instanceof Player player)) {
            sender.sendMessage(MessageConstants.NEED_TO_BE_A_PLAYER());
            return false;
        }
        if(args.length != 0) {
            if(!player.isOp()) {
                player.sendMessage(MessageConstants.NOT_AN_OPERATOR());
                return false;
            }
        }
        if(args.length != 1) {
            player.sendMessage(MessageConstants.INVALID_SYNTAX());
            return false;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            player.sendMessage();
            return false;
        }
        player.openInventory(target.getEnderChest());
        return true;
    }
}
