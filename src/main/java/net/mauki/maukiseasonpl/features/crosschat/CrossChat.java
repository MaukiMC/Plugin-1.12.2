package net.mauki.maukiseasonpl.features.crosschat;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.mauki.maukiseasonpl.core.Boot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CrossChat extends ListenerAdapter implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncChat(AsyncPlayerChatEvent event) throws IOException {
        event.setCancelled(true);
        Boot.getPLUGIN().getServer().broadcastMessage(ChatColor.GRAY + event.getPlayer().getName() + ": " + ChatColor.RESET + event.getMessage());

        Boot.getD_WEBHOOK().setAvatarUrl("https://crafatar.com/avatars/" + event.getPlayer().getUniqueId() + "?overlay");
        Boot.getD_WEBHOOK().setUsername(event.getPlayer().getName());
        Boot.getD_WEBHOOK().setContent(event.getMessage());
        Boot.getD_WEBHOOK().execute();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(!event.isFromGuild()) return;
        if(event.getGuild().getIdLong() != 947597599513411615L) return;
        if(event.getChannel().asTextChannel().getIdLong() != 983789134407663646L) return;
        if(event.getAuthor().isBot() || event.getAuthor().isSystem()) return;
        Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.RESET + ChatColor.AQUA + "Discord - " + event.getAuthor().getAsTag() + ChatColor.RESET + ChatColor.GRAY + "]: " +
                event.getMessage().getContentRaw());
    }

}
