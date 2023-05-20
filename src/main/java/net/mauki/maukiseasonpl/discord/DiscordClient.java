package net.mauki.maukiseasonpl.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.mauki.maukiseasonpl.core.Boot;
import net.mauki.maukiseasonpl.discord.commands.handler.SlashCommandHandler;
import net.mauki.maukiseasonpl.features.crosschat.ChatEvents;
import net.mauki.maukiseasonpl.features.crosschat.Configuration;
import net.mauki.maukiseasonpl.features.crosschat.CrossChat;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Manager for the discord client
 */
public class DiscordClient extends ListenerAdapter {

    /**
     * If you want to reload the bot
     * @deprecated
     */
    @Deprecated
    private boolean reload = false;
    /**
     * The token of the bot
     */
    private final String token;
    /**
     * The {@link JDABuilder} of the bot
     */
    private JDABuilder jdaBuilder;
    /**
     * The {@link JDA} object of the website
     */
    private JDA JDA;


    /**
     * Initialise the client
     * @param token The discord token of the bot
     */
    public DiscordClient(String token) {
        Boot.getLOGGER().info("Initialising DiscordClient...");
        this.token = token;
    }

    /**
     * Starts the discord bot
     * @throws InterruptedException Will be thrown when there was an error while starting the bot
     */
    public void start() throws InterruptedException {
        Boot.getLOGGER().info("Booting DiscordClient...");
        if(!Boot.getSERVER_NAME().equalsIgnoreCase("pa01")) {
            Boot.getLOGGER().info("Booting process cancelled.");
            return;
        }
        jdaBuilder = JDABuilder.createDefault(this.token);
        jdaBuilder.setStatus(OnlineStatus.ONLINE);
        jdaBuilder.setActivity(Activity.playing("Minecraft auf mc.mauki.net!"));
        jdaBuilder.setAutoReconnect(true);
        jdaBuilder.enableIntents(Arrays.asList(GatewayIntent.values()));
        jdaBuilder.addEventListeners(this);
        jdaBuilder.addEventListeners(new Configuration());
        jdaBuilder.addEventListeners(new ChatEvents());
        jdaBuilder.addEventListeners(new CrossChat());
        jdaBuilder.addEventListeners(new SlashCommandHandler());
        JDA = jdaBuilder.build();

        JDA.awaitReady();
        SlashCommandHandler.loadCommands();
        Boot.getLOGGER().info("DiscordClient launched.");
    }


    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        Boot.getLOGGER().info("Shutting down the DiscordClient.");
    }

    /**
     * Get the {@link JDA} object
     * @return The {@link JDA} object
     */
    public net.dv8tion.jda.api.JDA getJDA() {
        return JDA;
    }

    /**
     * Get the {@link JDABuilder} object
     * @return The {@link JDABuilder} object
     */
    public JDABuilder getJdaBuilder() {
        return jdaBuilder;
    }

    /**
     * Get the token of the bot
     * @return The token of the bot
     */
    public String getToken() {
        return token;
    }

    /**
     * Check if the bot should be reloaded
     * @return If the bot should be reloaded
     * @deprecated
     */
    @Deprecated
    public boolean isReload() {
        return reload;
    }

    /**
     * Set if the bot should be reloaded
     * @param reload If the bot should be reloaded
     * @deprecated
     */
    @Deprecated
    public void setReload(boolean reload) {
        this.reload = reload;
    }
}
