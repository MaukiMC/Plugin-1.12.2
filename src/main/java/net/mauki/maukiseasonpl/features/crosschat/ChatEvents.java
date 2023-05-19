package net.mauki.maukiseasonpl.features.crosschat;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.mauki.maukiseasonpl.caches.Caches;
import net.mauki.maukiseasonpl.core.Boot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class ChatEvents extends ListenerAdapter implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(ChatColor.GRAY + event.getPlayer().getName() + " ist dem Spiel beigetreten.");
        if(!Boot.getSERVER_NAME().equalsIgnoreCase("pa01")) return;
        Objects.requireNonNull(Boot.getDISCORD_CLIENT().getJDA().getTextChannelById(983789134407663646L)).sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(Color.green)
                        .setAuthor(event.getPlayer().getName() + " ist dem Spiel beigetreten.",null,  "https://crafatar.com/avatars/" + event.getPlayer().getUniqueId() + "?overlay")
                        .build()
        ).queue();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.GRAY + event.getPlayer().getName() + " hat das Spiel verlassen.");
        if(!Boot.getSERVER_NAME().equalsIgnoreCase("pa01")) return;
        Objects.requireNonNull(Boot.getDISCORD_CLIENT().getJDA().getTextChannelById(983789134407663646L)).sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(Color.red)
                        .setAuthor(event.getPlayer().getName() + " hat das Spiel verlassen.",null,  "https://crafatar.com/avatars/" + event.getPlayer().getUniqueId() + "?overlay")
                        .build()
        ).queue();
        Caches.latestMessageCache.remove(event.getPlayer());
    }

}
