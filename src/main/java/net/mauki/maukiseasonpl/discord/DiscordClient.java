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

public class DiscordClient extends ListenerAdapter {

    private boolean reload = false;
    private final String token;
    private JDABuilder jdaBuilder;
    private JDA JDA;

    public DiscordClient(String token) {
        Boot.getLOGGER().info("Initialising DiscordClient...");
        this.token = token;
    }

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

    public net.dv8tion.jda.api.JDA getJDA() {
        return JDA;
    }

    public JDABuilder getJdaBuilder() {
        return jdaBuilder;
    }

    public String getToken() {
        return token;
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }
}
