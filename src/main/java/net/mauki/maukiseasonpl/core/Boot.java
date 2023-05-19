package net.mauki.maukiseasonpl.core;

import de.mp.jdwc.internal.JavaDiscordWebhookClient;
import de.mp.kwsb.internal.KWSB;
import de.mp.kwsb.internal.Request;
import de.mp.kwsb.internal.Response;
import de.mp.kwsb.internal.handlers.GetRequestHandler;
import net.mauki.maukiseasonpl.commands.*;
import net.mauki.maukiseasonpl.dashboard.PlayersOnlineHandler;
import net.mauki.maukiseasonpl.discord.DiscordClient;
import net.mauki.maukiseasonpl.features.crosschat.ChatEvents;
import net.mauki.maukiseasonpl.features.crosschat.Configuration;
import net.mauki.maukiseasonpl.features.crosschat.CrossChat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Boot extends JavaPlugin implements Listener {

    private static Logger LOGGER;
    private static Plugin PLUGIN;
    private static final Pattern PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final String SERVER_NAME = System.getProperty("user.dir").toLowerCase().substring(System.getProperty("user.dir").toLowerCase().length() - 4);
    private static DiscordClient DISCORD_CLIENT;
    private static final KWSB kwsb = new KWSB();
    private static final JavaDiscordWebhookClient D_WEBHOOK = new JavaDiscordWebhookClient.Builder()
            .setToken("OXkwyHTxHTudU-Dg7NiP2Ao4N9CjN28wSuvOne2-Xc_mDAanye3HmEyrjg2KBQT-D_7B")
            .setID(1059912656997331055L)
            .build();

    @Override
    public void onEnable() {
        //Constants
        LOGGER = getLogger();
        PLUGIN = Boot.getPlugin(Boot.class);
        DISCORD_CLIENT = new DiscordClient("MTEwODA3MzE5NjY2NzgwMTYwMQ.GqOmbO.cBQ1NG2dtul5l8l7W18kcwLdnsn7YV8j5iPLw4");

        //Database
        LiteSQL.connect();
        LiteSQL.createTables();

        //Commands
        registerCommand("gamemode", new GamemodeCMD());
        registerCommand("gm", new GamemodeCMD());
        registerCommand("link", new LinkCMD());
        registerCommand("invsee", new InvseeCMD());
        registerCommand("endsee", new EndseeCMD());
        registerCommand("sudo", new SudoCMD());
        registerCommand("msg", new MessageCMD());
        registerCommand("message", new MessageCMD());
        registerCommand("heal", new HealCMD());

        //Listeners
        registerEvent(new CrossChat());
        registerEvent(new ChatEvents());
        registerEvent(new Configuration());

        //REST-API
        kwsb.addRequestHandler("/", new GetRequestHandler() {
            @Override
            public void onRequest(Request request, Response response) throws Exception {
                response.send(new JSONObject().put("code", 200).put("message", "OK!").toString());
            }
        });
        kwsb.addRequestHandler("/online_players", new PlayersOnlineHandler());

        new Thread(() -> {
            try {
                kwsb.listen(8080).whenComplete((event, throwable) -> {
                    if(throwable != null) throwable.printStackTrace();
                    Boot.getLOGGER().info("API started");
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

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

    public static Pattern getPATTERN() {
        return PATTERN;
    }

    public static Plugin getPLUGIN() {
        return PLUGIN;
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public static JavaDiscordWebhookClient getD_WEBHOOK() {
        return D_WEBHOOK;
    }

    public static String getSERVER_NAME() {
        return SERVER_NAME;
    }

    public static DiscordClient getDISCORD_CLIENT() {
        return DISCORD_CLIENT;
    }

    public static KWSB getKWSB() {
        return kwsb;
    }
}
