package net.mauki.maukiseasonpl.features.crosschat;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.mauki.maukiseasonpl.core.Boot;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

/**
 * Configuration for crosschatting
 */
public class Configuration extends ListenerAdapter implements Listener {

    /**
     * Reload the webserver
     */
    public void reload() {
        Boot.getLOGGER().info("Reloading the server...");
        Boot.getDISCORD_CLIENT().getJDA().shutdownNow();
        Boot.getPLUGIN().getServer().reload();
    }

    /**
     * Listener to prevent the server from reloading due to networking issues
     * @param event The event that will be triggered before a command will be performed
     */
    @EventHandler
    public void onReloadCommand(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage().split(" ")[0].replaceAll("(?i)bukkit", "");
        if(!((cmd.equalsIgnoreCase("/reload") || cmd.equalsIgnoreCase("/rl")))) return;
        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.RED + "Befehl deaktiviert!");
/*        event.getPlayer().sendMessage(ChatColor.GREEN + "Server wird neu geladen!");
        reload();*/
    }

    /**
     * Listener to prevent the server from reloading due to networking issues
     * @param event The event that will be triggered before a command will be performed
     */
    @EventHandler
    public void onReloadCommand(ServerCommandEvent event) {
        String cmd = event.getCommand().split(" ")[0].replaceAll("(?i)bukkit", "");
        if(!((cmd.equalsIgnoreCase("reload") || cmd.equalsIgnoreCase("rl")))) return;
        event.setCancelled(true);
        event.getSender().sendMessage(ChatColor.RED + "Befehl deaktiviert!");
/*        event.getSender().sendMessage(ChatColor.GREEN + "Server wird neu geladen!");
        reload();*/
    }

}
