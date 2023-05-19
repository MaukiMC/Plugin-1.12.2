package net.mauki.maukiseasonpl.core;

import org.bukkit.ChatColor;

public class MessageConstants {

    public static String NOT_AN_OPERATOR() {
        return ChatColor.RED + "Fehlgeschlagen! Ungenügen Rechte.";
    }

    public static String NEED_TO_BE_A_PLAYER() {
        return "Fehlgeschlagen! Du musst ein Spieler sein, um diesen Befehl verwenden zu können.";
    }

    public static String INVALID_SYNTAX() {
        return ChatColor.RED + "Fehlgeschlagen! Invalider Syntax.";
    }

    public static String UNEXPECTED_ERROR() {
        return ChatColor.RED + "Fehlgeschlagen! Ein unerwarteter Fehler ist aufgetreten.";
    }

    public static String USER_NOT_VALID(String name) {
        return ChatColor.RED + "Fehlgeschlagen! " + ChatColor.GOLD + name + ChatColor.RED + " ist kein valider Spieler.";
    }

    public static String USER_NOT_ONLINE(String name) {
        return ChatColor.RED + "Fehlgeschlagen! " + ChatColor.GOLD + name + ChatColor.RED + " ist nicht online.";
    }

}
