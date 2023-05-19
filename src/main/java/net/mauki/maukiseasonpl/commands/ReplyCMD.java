package net.mauki.maukiseasonpl.commands;

import net.mauki.maukiseasonpl.caches.Caches;
import net.mauki.maukiseasonpl.core.MessageConstants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command for replying the player with the latest correspondence
 */
public class ReplyCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player receiver = Caches.latestMessageCache.get((Player) sender);
        if(!receiver.isOnline()) {
            sender.sendMessage(MessageConstants.USER_NOT_ONLINE(args[0]));
            return false;
        }
        StringBuilder message = new StringBuilder();
        for(int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        receiver.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + sender.getName() + ChatColor.GOLD + " -> " + ChatColor.GRAY + "Du" + ChatColor.GOLD + "]: " + ChatColor.RESET + message);
        sender.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "Du " + ChatColor.GOLD +"-> " + ChatColor.GRAY + receiver.getName() + ChatColor.GOLD + "]: " + ChatColor.RESET + message);
        return true;
    }
}
