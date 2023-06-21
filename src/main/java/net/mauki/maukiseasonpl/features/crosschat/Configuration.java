package net.mauki.maukiseasonpl.features.crosschat;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.mauki.maukiseasonpl.core.Boot;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

/**
 * Configuration for crosschatting
 */
public class Configuration extends ListenerAdapter implements Listener {

    /**
     * Reload the webserver
     * @deprecated
     */
    @Deprecated
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
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage().split(" ")[0].replaceAll("(?i)bukkit", "").replace("/", "");
        switch (cmd) {
            case "reload":
            case "rl": {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Befehl deaktiviert! Verwende /stop <zeit/now>");
            }
//                event.getPlayer().sendMessage(ChatColor.GREEN + "Server wird neu geladen!");
//                reload();
        }
    }

    /**
     * Listener to prevent the server from reloading due to networking issues
     * @param event The event that will be triggered before a command will be performed
     */
    @EventHandler
    public void onCommand(ServerCommandEvent event) {
        String cmd = event.getCommand().split(" ")[0].replaceAll("(?i)bukkit", "");
        switch (cmd) {
            case "reload":
            case "rl": {
                event.setCancelled(true);
                event.getSender().sendMessage(ChatColor.RED + "Befehl deaktiviert! Verwende /stop <zeit/now>");
            }
//                event.getSender().sendMessage(ChatColor.GREEN + "Server wird neu geladen!");
//                reload();
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Objects.requireNonNull(event.getJDA().getTextChannelById(1109253583523807313L)).sendMessageEmbeds(new EmbedBuilder()
                .setTitle("Minecraft-Übersicht")
                .setDescription("Um auf dem Minecraft-Server spielen zu können, trete dem **[Mauki Community Server](https://discord.gg/7fVXR2g7DG) bei!**")
                .setColor(Color.decode("#"))
                .setFooter("https://mauki.net")
                .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                .build()).addActionRow(Button.link("https://discord.gg/7fVXR2g7DG", "Mauki Community Server")).queue();
    }
}
