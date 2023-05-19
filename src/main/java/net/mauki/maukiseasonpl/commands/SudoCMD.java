package net.mauki.maukiseasonpl.commands;

import net.mauki.maukiseasonpl.core.MessageConstants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SudoCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.isOp()) {
            sender.sendMessage(MessageConstants.NOT_AN_OPERATOR());
            return false;
        }
        if(args.length < 2) {
            sender.sendMessage(MessageConstants.INVALID_SYNTAX());
            return false;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            sender.sendMessage(MessageConstants.USER_NOT_VALID(args[0]));
            return false;
        }
        if(!target.isOnline()) {
            sender.sendMessage(MessageConstants.USER_NOT_ONLINE(args[0]));
            return false;
        }
        StringBuilder input = new StringBuilder();
        for(int i = 1; i < args.length; i++) input.append(args[i]).append(" ");
        if(input.toString().startsWith("/")) {
            Bukkit.dispatchCommand(target, input.substring(1));
            sender.sendMessage(ChatColor.GREEN + "Erfolgreich! Befehl " + ChatColor.GOLD + input.substring(1).split(" ")[0] + ChatColor.GREEN + " ausgeführt als " + ChatColor.GOLD + target.getName());
            return true;
        }
        target.chat(input.toString());
        sender.sendMessage(ChatColor.GREEN + "Erfolgreich! " + ChatColor.GOLD + input + ChatColor.GREEN + " gesagt als " + ChatColor.GOLD + target.getName());
        return true;
    }
}
