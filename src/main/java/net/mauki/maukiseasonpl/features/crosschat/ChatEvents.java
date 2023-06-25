package net.mauki.maukiseasonpl.features.crosschat;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.mauki.maukiseasonpl.caches.Caches;
import net.mauki.maukiseasonpl.core.Boot;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.util.Objects;

/**
 * Manager for all events which can happen in the chat
 */
public class ChatEvents extends ListenerAdapter implements Listener {

    /**
     * The listener to inform the discord about when a player joins
     * @param event The event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(event.getPlayer().isOp())
            event.setJoinMessage(ChatColor.RED + event.getPlayer().getName() + ChatColor.GRAY + " ist dem Spiel beigetreten.");
        else
            event.setJoinMessage(ChatColor.GRAY + event.getPlayer().getName() + " ist dem Spiel beigetreten.");
        if(!Boot.getSERVER_NAME().equalsIgnoreCase("pa01")) return;
        Objects.requireNonNull(Boot.getDISCORD_CLIENT().getJDA().getTextChannelById(1109934419508203641L)).sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(Color.green)
                        .setAuthor(event.getPlayer().getName() + " ist dem Spiel beigetreten.",null,  "https://crafatar.com/avatars/" + event.getPlayer().getUniqueId() + "?overlay")
                        .build()
        ).queue();
    }

    /**
     * The listener to inform the discord about when a player left
     * @param event The event
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if(event.getPlayer().isOp())
            event.setQuitMessage(ChatColor.RED + event.getPlayer().getName() + ChatColor.GRAY + " hat das Spiel verlassen.");
        else
            event.setQuitMessage(ChatColor.GRAY + event.getPlayer().getName() + " hat das Spiel verlassen.");
        if(!Boot.getSERVER_NAME().equalsIgnoreCase("pa01")) return;
        Objects.requireNonNull(Boot.getDISCORD_CLIENT().getJDA().getTextChannelById(1109934419508203641L)).sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(Color.red)
                        .setAuthor(event.getPlayer().getName() + " hat das Spiel verlassen.",null,  "https://crafatar.com/avatars/" + event.getPlayer().getUniqueId() + "?overlay")
                        .build()
        ).queue();
        if(Caches.latestMessageCache.contains(event.getPlayer())) Caches.latestMessageCache.remove(event.getPlayer());
    }

}
