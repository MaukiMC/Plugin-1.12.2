package net.mauki.maukiseasonpl.core;

import de.mp.jdwc.internal.JavaDiscordWebhookClient;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import net.mauki.maukiseasonpl.commands.*;
import net.mauki.maukiseasonpl.commands.overwirtten.StopCMD;
import net.mauki.maukiseasonpl.commands.sign.SignCMD;
import net.mauki.maukiseasonpl.commands.sign.UnsignCMD;
import net.mauki.maukiseasonpl.dashboard.wss.BaseWebsocketServer;
import net.mauki.maukiseasonpl.discord.DiscordClient;
import net.mauki.maukiseasonpl.features.ChestLog;
import net.mauki.maukiseasonpl.features.crosschat.ChatEvents;
import net.mauki.maukiseasonpl.features.crosschat.Configuration;
import net.mauki.maukiseasonpl.features.crosschat.CrossChat;
import net.mauki.maukiseasonpl.features.linking.LinkingListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Main-file of the entire plugin. Manager for basically everything
 */
public class Boot extends JavaPlugin implements Listener {

    /**
     * The {@link Logger} of the plugin
     */
    @Getter
    private static Logger LOGGER;
    /**
     * The {@link Dotenv} object with environment variables for the project
     */
    @Getter
    private static final Dotenv dotenv = Dotenv.load();
    /**
     * The {@link Plugin} object of the plugin
     */
    @Getter
    private static Plugin PLUGIN;
    /**
     * Pattern for something (I forgot what it's for)
     */
    @Getter
    private static final Pattern PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");
    /**
     * The name of the server the plugin is running on
     */
    @Getter
    private static final String SERVER_NAME = System.getProperty("user.dir").toLowerCase().substring(System.getProperty("user.dir").toLowerCase().length() - 4);
    /**
     * The discord client of the current session
     */
    @Getter
    private static DiscordClient DISCORD_CLIENT;
    /**
     * The {@link BaseWebsocketServer} for the API
     */
    @Getter
    private static BaseWebsocketServer wss;
    /**
     * The webhook instance for crosschatting
     */
    @Getter
    private static final JavaDiscordWebhookClient D_WEBHOOK = new JavaDiscordWebhookClient.Builder()
            .setToken(dotenv.get("WEBHOOK_TOKEN"))
            .setID(Long.parseLong(dotenv.get("WEBHOOK_ID")))
            .build();

    /**
     * Will be triggered when the plugin gets enabled
     */
    @Override
    public void onEnable() {
        //Constants
        LOGGER = getLogger();
        PLUGIN = Boot.getPlugin(Boot.class);
        DISCORD_CLIENT = new DiscordClient(dotenv.get("BOT_TOKEN"));

        //Database
        LiteSQL.connect();
        LiteSQL.createTables();

        //Commands
        registerCommand("gamemode", new GamemodeCMD());
        //registerCommand("link", new LinkCMD());
        registerCommand("invsee", new InvseeCMD());
        registerCommand("endsee", new EndseeCMD());
        registerCommand("sudo", new SudoCMD());
        registerCommand("message", new MessageCMD());
        registerCommand("heal", new HealCMD());
        registerCommand("sign", new SignCMD());
        registerCommand("unsign", new UnsignCMD());
        registerCommand("reply", new ReplyCMD());
        registerCommand("sleep", new SleepCMD());
        registerCommand("downfall", new WeatherCMD());
        registerCommand("stop", new StopCMD());

        //Listeners
        registerEvent(new CrossChat());
        registerEvent(new ChatEvents());
        registerEvent(new Configuration());
        registerEvent(new SleepCMD());
        registerEvent(new ChestLog());
        registerEvent(new LinkingListener());

        //WSS-API
        wss = new BaseWebsocketServer(dotenv.get("WS_HOST"), 8887);
        wss.setConnectionLostTimeout(60);
        new Thread(() -> wss.run()).start();

        //Discord
        new Thread(() -> {
            try {
                getDISCORD_CLIENT().start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        getLogger().info("Plugin enabled");
    }

    /**
     * Will be triggered when the plugin gets disabled
     */
    @Override
    public void onDisable() {
        LiteSQL.disconnect();
        getLogger().info("Plugin disabled");
    }

    /**
     * Register an event listener
     * @param listener The {@link Listener} which should be added
     */
    private void registerEvent(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * Register a command
     * @param invoke The invoke of the command (the word to call the command)
     * @param commandExecutor The {@link CommandExecutor} of the command
     */
    private void registerCommand(String invoke, CommandExecutor commandExecutor) {
        this.getCommand(invoke).setExecutor(commandExecutor);
    }

    /**
     * Get a file in the server's dictionary
     * @param name The name of the file
     * @return returns the file
     */
    private File getPluginFile(String name) {
        return new File(Bukkit.getWorlds().get(0).getWorldFolder().getPath().replace(Bukkit.getWorlds().get(0).getName(), "")+"/plugins/"+name);
    }

    /**
     * Check if String content is only numbers
     * @param strNum The String which you want to check
     * @return return if the string is only made of numbers
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return PATTERN.matcher(strNum).matches();
    }

    /**
     * Get content of an URL
     * @param url the URL you want the content from
     * @return returns the content as a string
     * @throws Exception will be thrown when there is an error
     */
    public static String readStringFromURL(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) response.append(inputLine);
        in.close();
        return response.toString();
    }
}
