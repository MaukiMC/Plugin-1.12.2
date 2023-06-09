package net.mauki.maukiseasonpl.commands.sign;

import net.mauki.maukiseasonpl.core.MessageConstants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Command to unsign an item
 */
public class UnsignCMD implements CommandExecutor {

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
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        ItemStack currentHand = player.getItemInHand();
        if(currentHand.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "Fehlgeschlagen! Du hast kein Item in deiner Hand.");
            return false;
        }
        if(currentHand.getAmount() != 1) {
            player.sendMessage(ChatColor.RED + "Fehlgeschlagen! Du kannst nur ein Item designieren.");
            return false;
        }
        SignManager signManager = new SignManager(currentHand);
        if(!player.isOp()) {
            player.sendMessage(MessageConstants.NOT_AN_OPERATOR());
            return false;
        }
        if(!signManager.isSigned()) {
            player.sendMessage(ChatColor.RED + "Fehlgeschlagen! Dieses Item ist nicht signiert.");
            return false;
        }
        player.setItemInHand(signManager.unSign());
        player.sendMessage(ChatColor.GREEN + "Designiert!");
        return true;
    }
}
