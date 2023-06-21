package net.mauki.maukiseasonpl.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.mauki.maukiseasonpl.core.Boot;
import net.mauki.maukiseasonpl.discord.commands.handler.SlashCommandHandler;
import net.mauki.maukiseasonpl.features.crosschat.ChatEvents;
import net.mauki.maukiseasonpl.features.crosschat.Configuration;
import net.mauki.maukiseasonpl.features.crosschat.CrossChat;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

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
    public void onReady(@NotNull ReadyEvent event) {
/*        Objects.requireNonNull(Boot.getDISCORD_CLIENT().getJDA().getTextChannelById(1109253583523807313L)).sendMessageEmbeds(new EmbedBuilder()
                .setTitle("Minecraft-Übersicht")
                .setDescription("Um auf dem Minecraft-Server spielen zu können, trete dem **[Mauki Community Server](https://discord.gg/7fVXR2g7DG)** bei!")
                .setColor(Color.decode("#5b8731"))
                .setFooter("https://mauki.net")
                .setThumbnail(Boot.getDISCORD_CLIENT().getJDA().getSelfUser().getEffectiveAvatarUrl())
                .build()).addActionRow(Button.link("https://discord.gg/7fVXR2g7DG", "Mauki Community Server")).queue();*/
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getChannel().getIdLong() != 904701719848566804L) return;
        if(!event.getMessage().getContentRaw().equalsIgnoreCase("resend")) return;
        event.getMessage().delete().queue();
        event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                .setTitle("Minecraft-Übersicht")
                .setDescription("Um auf dem Minecraft Server über `mc.mauki.net` spielen zu können, benötigst du einen [`1.16.5`-Forge Client](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.16.5.html)\n\n" +
                        "Im Web-Dashbaord könnt ihr euch unter https://web.mc.mauki.net via Discord einloggen, nachdem ihr euren Account mit `/link` verlinkt habt.\n" +
                        "In besagtem Dashboard gibt es einige coole Funktionen und Statistiken für euch ;)\n\n" +
                        "Die Regeln des Minecraft-Servers findest du unter https://web.mc.mauki.net/regeln\n\n" +
                        "Wer sich für den Code interessiert, sollte auf https://github.com/MaukiNet vorbeischauen!\n\n" +
                        "Die Mods zum download gibt es unter https://web.mc.mauki.net/mods\n")
                .addField("Zusammenfassung", "IP (Java): `mc.mauki.net`\n" +
                        "Version: `1.16.5-Forge`\n" +
                        "Dashboard: https://web.mc.mauki.net\n" +
                        "Mods: https://web.mc.mauki.net/mods", false)
                .setColor(Color.decode("#5b8731"))
                .setFooter("https://mauki.net")
                .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                .build()).addActionRow(Button.link("https://web.mc.maukinet", "Dashboard"),
                Button.link("https://web.mc.mauki.net/regeln", "Regeln"),
                Button.link("https://github.com/MaukiNet", "GitHub"),
                Button.link("https://web.mc.mauki.net/mods", "Mods")).queue();
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
