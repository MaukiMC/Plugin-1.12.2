package net.mauki.maukiseasonpl.commands;

import net.mauki.maukiseasonpl.caches.Caches;
import net.mauki.maukiseasonpl.core.MessageConstants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command for sending a private-message to another player
 */
public class MessageCMD implements CommandExecutor {

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
        if(args.length < 2) {
            sender.sendMessage(MessageConstants.INVALID_SYNTAX());
            return false;
        }
        String name = args[0];
        Player receiver = Bukkit.getPlayer(name);
        if(receiver == null) {
            sender.sendMessage(MessageConstants.USER_NOT_VALID(args[0]));
            return false;
        }
        if(receiver.getUniqueId() == ((Player) sender).getUniqueId()) {
            sender.sendMessage(ChatColor.RED + "Fehlgeschlagen! Inwiefern soll es funktionieren, dir selbst zu schreiben?");
            return false;
        }
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
        Caches.latestMessageCache.addOrUpdate((Player) sender, receiver);
        Caches.latestMessageCache.addOrUpdate(receiver, (Player) sender);
        return true;
    }
}
