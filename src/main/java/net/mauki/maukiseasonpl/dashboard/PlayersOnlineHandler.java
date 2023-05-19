package net.mauki.maukiseasonpl.dashboard;

import de.mp.kwsb.internal.Request;
import de.mp.kwsb.internal.Response;
import de.mp.kwsb.internal.handlers.GetRequestHandler;
import net.dv8tion.jda.api.entities.User;
import net.mauki.maukiseasonpl.core.LiteSQL;
import net.mauki.maukiseasonpl.features.linking.DiscordUser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class PlayersOnlineHandler extends GetRequestHandler {
    @Override
    public void onRequest(Request request, Response response) throws Exception {
        //Access-Token
        String access_token = request.getHeader("Authorization");
        if(access_token == null) {
            response.send(new JSONObject()
                    .put("code", 401)
                    .put("message", "No access-token given").toString());
            return;
        }

        //Discord-User
        User discord_user = DiscordUser.retrieveUserByAccessToken(access_token);
        if(discord_user == null) {
            response.send(new JSONObject()
                    .put("code", 401)
                    .put("message", "Invalid access-token").toString());
            return;
        }

        //Database Entry
        ResultSet rs = LiteSQL.onQuery("SELECT * FROM connections WHERE discord_id = '" + discord_user.getId() + "'");
        if(rs == null) {
            response.send(new JSONObject()
                    .put("code", 500)
                    .put("message", "Internal Server Error").toString());
            return;
        }

        if(!rs.next()) {
            response.send(new JSONObject()
                    .put("code", 401)
                    .put("message", "Couldn't find the associated minecraft account").toString());
            return;
        }

        //Succeeded request
        UUID uuid = UUID.fromString(rs.getString("uuid"));
        Collection<JSONObject> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> {
            players.add(new JSONObject()
                    .put("uuid", player.getUniqueId())
                    .put("name", player.getName()));
        });
        OfflinePlayer minecraft_player = Bukkit.getOfflinePlayer(uuid);
        if(minecraft_player == null) {
            response.send(new JSONObject()
                    .put("code", 401)
                    .put("message", "Couldn't find the associated minecraft account").toString());
            return;
        }

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
