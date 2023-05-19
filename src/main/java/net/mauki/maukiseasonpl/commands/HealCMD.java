package net.mauki.maukiseasonpl.commands;

import net.mauki.maukiseasonpl.core.MessageConstants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCMD implements CommandExecutor {

    CommandSender author;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            if(!sender.isOp()) {
                sender.sendMessage(MessageConstants.NOT_AN_OPERATOR());
                return false;
            }
            if(args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if(target == null) {
                    sender.sendMessage(MessageConstants.USER_NOT_VALID(args[0]));
                    return false;
                }
                if(!target.isOnline()) {
                    sender.sendMessage(MessageConstants.USER_NOT_ONLINE(args[0]));
                    return false;
                }
                author = sender;
                heal(target);
                return true;
            }
            sender.sendMessage(MessageConstants.INVALID_SYNTAX());
            return false;
        }
        if(args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                sender.sendMessage(MessageConstants.USER_NOT_VALID(args[0]));
                return false;
            }
            if(!target.isOnline()) {
                sender.sendMessage(MessageConstants.USER_NOT_ONLINE(args[0]));
                return false;
            }
            heal(target);
            return true;
        }
        heal((Player) sender);
        return true;
    }

    private void heal(Player player) {
        player.setHealth(20);
        player.setFoodLevel(20);
        author.sendMessage(ChatColor.GREEN + "Erfolgreich! Du hast " + ChatColor.GOLD + player.getName() + ChatColor.GREEN + "geheilt.");
        player.sendMessage(ChatColor.GOLD + "Du wurdest geheilt.");
    }

}
