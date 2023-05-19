package net.mauki.maukiseasonpl.commands;

import net.mauki.maukiseasonpl.core.MessageConstants;
import org.bukkit.Bukkit;
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
        receiver.sendMessage("§6[§4"+sender.getName()+" §6-> §aDu§6]: §r"+ message);
        sender.sendMessage("§6[§4Du §6-> §a"+receiver.getName()+"§6]: §r"+ message);
        return true;
    }
}
