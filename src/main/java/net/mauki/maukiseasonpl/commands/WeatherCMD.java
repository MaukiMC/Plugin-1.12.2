package net.mauki.maukiseasonpl.commands;

import net.mauki.maukiseasonpl.caches.Caches;
import net.mauki.maukiseasonpl.core.Boot;
import net.mauki.maukiseasonpl.core.MessageConstants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to skip a thunder
 */
public class WeatherCMD implements CommandExecutor {

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
        if(!(sender instanceof Player)) {
            sender.sendMessage(MessageConstants.NEED_TO_BE_A_PLAYER());
            return false;
        }
        Player player = (Player) sender;
        if(player.getPlayerWeather() == WeatherType.CLEAR) {
            player.sendMessage(ChatColor.RED + "Es scheint bereits die Sonne...");
            return false;
        }
        if(Caches.playerWeatherState.contains(player)) {
            player.sendMessage(ChatColor.RED + "Du tanzt bereits den Sonnentanz...");
            return false;
        }
        Caches.playerWeatherState.add(player);
        Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.GREEN + "tanzt den Sonnentanz um das Unwetter zu vertreiben. " + ChatColor.YELLOW + "(" + Caches.playerWeatherState.size() + "/" + ((Bukkit.getOnlinePlayers().size()/2)+1) + ")");
        if(Caches.playerWeatherState.size() >= (Bukkit.getOnlinePlayers().size()/2)+1) {
            Boot.getPLUGIN().getServer().dispatchCommand(Boot.getPLUGIN().getServer().getConsoleSender(), "weather clear");
            Caches.playerWeatherState.clear();
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Euer Sonnentanz war erfolgreich! Ich kann die Sonne schon sehen!");
        }
        return true;
    }
}
