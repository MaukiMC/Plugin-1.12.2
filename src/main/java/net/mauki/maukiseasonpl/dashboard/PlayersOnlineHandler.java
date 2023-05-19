package net.mauki.maukiseasonpl.dashboard;

import de.mp.kwsb.internal.Request;
import de.mp.kwsb.internal.Response;
import de.mp.kwsb.internal.handlers.GetRequestHandler;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Endpoint for all online players
 */
public class PlayersOnlineHandler extends GetRequestHandler {

    /**
     * The code which will be executed when endpoint is called
     * @param request The {@link Request} object of the request
     * @param response The {@link Response} object of the request
     * @throws Exception Will be thrown when there was an error
     */
    @Override
    public void onRequest(Request request, Response response) throws Exception {
        ArrayList<Object> al = Authenticator.authenticate(request, response);
        User discord_user = (User) al.get(0);
        OfflinePlayer minecraft_player = (OfflinePlayer) al.get(1);

        Collection<JSONObject> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> {
            players.add(new JSONObject()
                    .put("uuid", player.getUniqueId())
                    .put("name", player.getName()));
        });

        JSONObject output = new JSONObject()
                .put("online_players", players)
                .put("requester", new JSONObject()
                        .put("minecraft_user", new JSONObject()
                                .put("uuid", minecraft_player.getUniqueId().toString())
                                .put("name", minecraft_player.getName()))
                        .put("discord_id", discord_user.getId()))
                .put("code", 200)
                .put("message", "Success");
        response.send(output.toString());
    }
}
