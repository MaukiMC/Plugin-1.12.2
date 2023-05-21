package net.mauki.maukiseasonpl.commands.overwirtten;

import net.mauki.maukiseasonpl.core.Boot;
import net.mauki.maukiseasonpl.core.MessageConstants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to overwrite /stop
 */
public class StopCMD implements CommandExecutor {

    /**
     * Variable to check if the server is already in shutdown process
     */
    public static boolean shutting_down = false;

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
        if(sender instanceof Player player && !player.isOp()) {
            sender.sendMessage(MessageConstants.NOT_AN_OPERATOR());
            return false;
        }
        if(args.length != 1 && args.length != 0) {
            sender.sendMessage(MessageConstants.INVALID_SYNTAX());
            return false;
        }
        if(args.length == 1 && (!Boot.isNumeric(args[0]) || args[0].equalsIgnoreCase("now"))) {
            if(args[0].equalsIgnoreCase("now")) {
                Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(ChatColor.RED + "Der Server musste aus technischen Gründen sofort heruntergefahren werden. Versuche es zu einem späterem Zeitpunkt erneut."));
                Boot.getPLUGIN().getServer().shutdown();
                return true;
            }
            sender.sendMessage(MessageConstants.INVALID_SYNTAX());
            return false;
        }
        if(shutting_down) {
            sender.sendMessage(ChatColor.RED + "Der Server befindet sich bereits im Herunterfahrmodus.");
            return false;
        }
        long time = (args.length == 1 ? Long.parseLong(args[0]) : 3);
        if(time <= 0) {
            sender.sendMessage(ChatColor.RED + "Fehlgeschlagen! Die Zeitperiode muss mindestens eine Minute bertragen.");
            return false;
        }
        shutting_down = true;
        new Thread(() -> {
            try {
                long i = time;
                if(i != 1) Bukkit.broadcastMessage(ChatColor.RED + "Server fährt in " + ChatColor.YELLOW + i + ChatColor.RED + " Minuten herunter");
                if(i == 1) oneMinute();
                do {
                    Thread.sleep(60000);
                    i = i-1;
                    if(i == 30 || i == 20 || i == 10 || i == 5 || i == 3 || i == 2)
                        Bukkit.broadcastMessage(ChatColor.RED + "Server fährt in " + ChatColor.YELLOW + i + ChatColor.RED + " Minuten herunter");
                    if(i == 1) oneMinute();
                } while(i>0);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }).start();
        return true;
    }

    /**
     * Manage the sopping-process when only one minute is left
     * @throws InterruptedException Will be thrown when there was an error during stopping
     */
    public void oneMinute() throws InterruptedException {
        int sec = 60;
        Bukkit.broadcastMessage(ChatColor.RED + "Server fährt in " + ChatColor.YELLOW + "1" + ChatColor.RED + " Minute herunter");
        do {
            Thread.sleep(1000);
            sec = sec-1;
            if(sec <= 10)
                Bukkit.broadcastMessage(ChatColor.RED + "Server wird in " + ChatColor.YELLOW + sec + " Sekunde" + (sec != 1 ? "n" : "") + ChatColor.RED + " herunterfahren.");
        } while(sec > 0);
        Boot.getPLUGIN().getServer().shutdown();
    }

}
