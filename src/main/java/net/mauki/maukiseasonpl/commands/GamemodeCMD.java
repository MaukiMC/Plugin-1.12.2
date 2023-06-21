package net.mauki.maukiseasonpl.commands;

import net.mauki.maukiseasonpl.core.MessageConstants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Command to change your or someone else gamemode
 */
public class GamemodeCMD implements CommandExecutor {

    /**
     * The code which will be executed when command is being called
     * @param sender The sender of the command
     * @param command The command itself
     * @param label The label of the command
     * @param args The arguments which were sent with the command
     * @return boolean if the command were performed successfully
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        boolean selfExecution = false;
        Player player;

        if((args.length != 2) && args.length != 1) {
            sender.sendMessage(MessageConstants.INVALID_SYNTAX());
            return true;
        }

        if((args.length == 1)) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Fehlgeschlagen! Als Konsole musst du einen Spieler auswählen, dessen Spielmodus geändert werden soll.");
                return true;
            }
            player = (Player) sender;
            selfExecution = true;
        } else if((Bukkit.getPlayer(args[1]) == null)) {
            sender.sendMessage(MessageConstants.USER_NOT_VALID(args[1]));
            return true;
        } else {
            player = Bukkit.getPlayer(args[1]);
        }

        if(!sender.isOp()) {
            sender.sendMessage(MessageConstants.NOT_AN_OPERATOR());
            return true;
        }

        if(player == null) {
            sender.sendMessage(MessageConstants.UNEXPECTED_ERROR());
            return true;
        }

        if(!player.isOnline()) {
            sender.sendMessage(MessageConstants.USER_NOT_ONLINE(args[1]));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "0":
            case "survival":
                player.setGameMode(GameMode.SURVIVAL);
                sender.sendMessage(ChatColor.GREEN + "Erfolgreich! " + (selfExecution ? "Dein" : ChatColor.GOLD + args[1] + "'s" + ChatColor.RED) + " Spielmodus wurde zu " + ChatColor.GOLD + "Überleben" + ChatColor.GREEN + " geändert.");
                if(!selfExecution) player.sendMessage(ChatColor.GREEN + "Dein Spielmodus wurde zu " + ChatColor.GOLD + "Überleben" + ChatColor.GREEN + " geändert.");
                return true;
            case "1":
            case "creative":
                player.setGameMode(GameMode.CREATIVE);
                sender.sendMessage(ChatColor.GREEN + "Erfolgreich! " + (selfExecution ? "Dein" : ChatColor.GOLD + args[1] + "'s" + ChatColor.RED) + " Spielmodus wurde zu " + ChatColor.GOLD + "Kreativ" + ChatColor.GREEN + " geändert.");
                if(!selfExecution) player.sendMessage(ChatColor.GREEN + "Dein Spielmodus Spielmodus wurde zu " + ChatColor.GOLD + "Kreativ" + ChatColor.GREEN + " geändert.");
                return true;

            case "2":
            case "adventure":
                player.setGameMode(GameMode.ADVENTURE);
                sender.sendMessage(ChatColor.GREEN + "Erfolgreich! " + (selfExecution ? "Dein" : ChatColor.GOLD + args[1] + "'s" + ChatColor.RED) + " Spielmodus wurde zu " + ChatColor.GOLD + "Abenteuer" + ChatColor.GREEN + " geändert.");
                if(!selfExecution) player.sendMessage(ChatColor.GREEN + "Dein Spielmodus " + ChatColor.GOLD + "Abenteuer" + ChatColor.GREEN + " geändert.");
                return true;
            case "3":
            case "spectator":
                player.setGameMode(GameMode.SPECTATOR);
                sender.sendMessage(ChatColor.GREEN + "Erfolgreich! " + (selfExecution ? "Dein" : ChatColor.GOLD + args[1] + "'s" + ChatColor.RED) + " Spielmodus wurde zu " + ChatColor.GOLD + "Zuschauer" + ChatColor.GREEN + " geändert.");
                if(!selfExecution) player.sendMessage(ChatColor.GREEN + "Dein Spielmodus wurde zu " + ChatColor.GOLD + "Zuschauer" + ChatColor.GREEN + " geändert.");
                return true;
            default:
                sender.sendMessage(ChatColor.RED + "Fehlgeschlagen! " + ChatColor.GOLD + args[0] + ChatColor.RED + " ist kein valider Spielmodus.");
                return true;
        }
    }

}
