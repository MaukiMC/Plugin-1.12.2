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

/**
 * The crosschat itself
 */
public class CrossChat extends ListenerAdapter implements Listener {

    /**
     * The listener to catch every chat message to use it for crosschatting
     * @param event The event
     * @throws IOException If there was an error
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncChat(AsyncPlayerChatEvent event) throws IOException {
        event.setCancelled(true);
        if(event.getPlayer().isOp()) {
            Boot.getPLUGIN().getServer().broadcastMessage(ChatColor.RED + event.getPlayer().getName() + ": " + ChatColor.RESET + event.getMessage());
            return;
        }
        Boot.getPLUGIN().getServer().broadcastMessage(ChatColor.DARK_AQUA + event.getPlayer().getName() + ": " + ChatColor.RESET + event.getMessage());

        Boot.getD_WEBHOOK().setAvatarUrl("https://crafatar.com/avatars/" + event.getPlayer().getUniqueId() + "?overlay");
        Boot.getD_WEBHOOK().setUsername(event.getPlayer().getName());
        Boot.getD_WEBHOOK().setContent(event.getMessage());
        Boot.getD_WEBHOOK().execute();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(!event.isFromGuild()) return;
        if(event.getChannel().asTextChannel().getIdLong() != 1109934419508203641L) return;
        if(event.getAuthor().isBot() || event.getAuthor().isSystem()) return;
        Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.RESET + ChatColor.BLUE + "Discord - " + event.getAuthor().getName() + ChatColor.RESET + ChatColor.GRAY + "]: " +
                event.getMessage().getContentRaw());
    }

}
