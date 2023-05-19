package net.mauki.maukiseasonpl.core;

import org.bukkit.ChatColor;

/**
 * Default messages for commands
 */
public class MessageConstants {

    /**
     * The message of you are not an operator
     * @return The {@link String} of the message
     */
    public static String NOT_AN_OPERATOR() {
        return ChatColor.RED + "Fehlgeschlagen! Ungenügen Rechte.";
    }

    /**
     * The message of you need to be a player
     * @return The {@link String} of the message
     */
    public static String NEED_TO_BE_A_PLAYER() {
        return "Fehlgeschlagen! Du musst ein Spieler sein, um diesen Befehl verwenden zu können.";
    }

    /**
     * The message of the syntax is invalid
     * @return The {@link String} of the message
     */
    public static String INVALID_SYNTAX() {
        return ChatColor.RED + "Fehlgeschlagen! Invalider Syntax.";
    }

    /**
     * The message of unexpected errors
     * @return The {@link String} of the message
     */
    public static String UNEXPECTED_ERROR() {
        return ChatColor.RED + "Fehlgeschlagen! Ein unerwarteter Fehler ist aufgetreten.";
    }

    /**
     * The message of the user is not valid
     * @param name The name of the player
     * @return The {@link String} of the message
     */
    public static String USER_NOT_VALID(String name) {
        return ChatColor.RED + "Fehlgeschlagen! " + ChatColor.GOLD + name + ChatColor.RED + " ist kein valider Spieler.";
    }

    /**
     * The message of if the user is not online
     * @param name The name of the player
     * @return The {@link String} of the message
     */
    public static String USER_NOT_ONLINE(String name) {
        return ChatColor.RED + "Fehlgeschlagen! " + ChatColor.GOLD + name + ChatColor.RED + " ist nicht online.";
    }

}
