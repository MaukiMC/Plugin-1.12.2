package net.mauki.maukiseasonpl.commands;

import net.mauki.maukiseasonpl.caches.Caches;
import net.mauki.maukiseasonpl.core.MessageConstants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCMD implements CommandExecutor {
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
        Caches.latestMessageCache.add((Player) sender, receiver);
        return true;
    }
}