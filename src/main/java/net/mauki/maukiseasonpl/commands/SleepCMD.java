package net.mauki.maukiseasonpl.commands;

import net.mauki.maukiseasonpl.caches.Caches;
import net.mauki.maukiseasonpl.core.MessageConstants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

/**
 * Command for skipping the night
 */
public class SleepCMD implements CommandExecutor, Listener {

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
        nightSkipState(player);
        return true;
    }

    /**
     * Do the same thing when a player enters the bed
     * @param event The event
     */
    @EventHandler
    public void onPlayerBetEnter(PlayerBedEnterEvent event) {
        nightSkipState(event.getPlayer());
    }

    /**
     * Add the player to the belonging state
     * @param player The player who wants to skip the night
     */
    public void nightSkipState(Player player) {
        if(player.getWorld().getTime() < 12500) {
            player.sendMessage(ChatColor.RED + "Es ist noch nicht Nacht...");
            return;
        }
        if(Caches.playerSleepState.contains(player)) {
            player.sendMessage(ChatColor.RED + "Du schläfst bereits.");
            return;
        }
        Caches.playerSleepState.add(player);
        Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.GREEN + " möchte schlafen. " + ChatColor.YELLOW + "(" + Caches.playerSleepState.size() + "/" + ((Bukkit.getOnlinePlayers().size()/2)+1) + ")");
        if(Caches.playerSleepState.size() >= (Bukkit.getOnlinePlayers().size()/2)+1) {
            player.getWorld().setTime(1000);
            Caches.playerSleepState.clear();
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Die Nacht wird übersprungen. Ein neuer Tag bricht an...");
        }
    }

}
