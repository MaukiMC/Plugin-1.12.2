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

public class GamemodeCMD implements CommandExecutor {

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
            sender.sendMessage(ChatColor.RED + "Fehlgeschlagen! " + ChatColor.GOLD + args[1] + ChatColor.RED + " ist kein valider Spieler.");
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
            sender.sendMessage(ChatColor.RED + "Fehlgeschlagen! " + ChatColor.GOLD + args[1] + ChatColor.RED + " ist nicht online.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "0", "survival" -> {
                player.setGameMode(GameMode.SURVIVAL);
                sender.sendMessage(ChatColor.GREEN + "Erfolgreich! " + (selfExecution ? "Dein" : ChatColor.GOLD + args[1] + "'s" + ChatColor.RED) + " Spielmodus wurde zu " + ChatColor.GOLD + "Überleben" + ChatColor.GREEN + " geändert.");
                if(!selfExecution) player.sendMessage(ChatColor.GREEN + "Dein Spielmodus wurde zu " + ChatColor.GOLD + "Überleben" + ChatColor.GREEN + " geändert.");
                return true;
            }
            case "1", "creative" -> {
                player.setGameMode(GameMode.CREATIVE);
                sender.sendMessage(ChatColor.GREEN + "Erfolgreich! " + (selfExecution ? "Dein" : ChatColor.GOLD + args[1] + "'s" + ChatColor.RED) + " Spielmodus wurde zu " + ChatColor.GOLD + "Kreativ" + ChatColor.GREEN + " geändert.");
                if(!selfExecution) player.sendMessage(ChatColor.GREEN + "Dein Spielmodus Spielmodus wurde zu " + ChatColor.GOLD + "Kreativ" + ChatColor.GREEN + " geändert.");
                return true;
            }
            case "2", "adventure" -> {
                player.setGameMode(GameMode.ADVENTURE);
                sender.sendMessage(ChatColor.GREEN + "Erfolgreich! " + (selfExecution ? "Dein" : ChatColor.GOLD + args[1] + "'s" + ChatColor.RED) + " Spielmodus wurde zu " + ChatColor.GOLD + "Abenteuer" + ChatColor.GREEN + " geändert.");
                if(!selfExecution) player.sendMessage(ChatColor.GREEN + "Dein Spielmodus " + ChatColor.GOLD + "Abenteuer" + ChatColor.GREEN + " geändert.");
                return true;
            }
            case "3", "spectator" -> {
                player.setGameMode(GameMode.SPECTATOR);
                sender.sendMessage(ChatColor.GREEN + "Erfolgreich! " + (selfExecution ? "Dein" : ChatColor.GOLD + args[1] + "'s" + ChatColor.RED) + " Spielmodus wurde zu " + ChatColor.GOLD + "Zuschauer" + ChatColor.GREEN + " geändert.");
                if(!selfExecution) player.sendMessage(ChatColor.GREEN + "Dein Spielmodus wurde zu " + ChatColor.GOLD + "Zuschauer" + ChatColor.GREEN + " geändert.");
                return true;
            }
            default -> {
                sender.sendMessage(ChatColor.RED + "Fehlgeschlagen! " + ChatColor.GOLD + args[0] + ChatColor.RED + " ist kein valider Spielmodus.");
                return true;
            }
        }
    }

}
