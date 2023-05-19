package net.mauki.maukiseasonpl.dashboard;

import de.mp.kwsb.internal.Request;
import de.mp.kwsb.internal.Response;
import de.mp.kwsb.internal.handlers.errors.HttpException;
import net.dv8tion.jda.api.entities.User;
import net.mauki.maukiseasonpl.core.LiteSQL;
import net.mauki.maukiseasonpl.features.linking.DiscordUser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class Authenticator {

    public static ArrayList<Object> authenticate(Request request, Response response) throws HttpException, SQLException {
        //Access-Token
        String access_token = request.getHeader("Authorization");
        if(access_token == null) {
            response.send(new JSONObject()
                    .put("code", 401)
                    .put("message", "No access-token given").toString());
            return null;
        }

        //Discord-User
        User discord_user = DiscordUser.retrieveUserByAccessToken(access_token);
        if(discord_user == null) {
            response.send(new JSONObject()
                    .put("code", 401)
                    .put("message", "Invalid access-token").toString());
            return null;
        }

        //Database Entry
        ResultSet rs = LiteSQL.onQuery("SELECT * FROM connections WHERE discord_id = '" + discord_user.getId() + "'");
        if(rs == null) {
            response.send(new JSONObject()
                    .put("code", 500)
                    .put("message", "Internal Server Error").toString());
            return null;
        }

        if(!rs.next()) {
            response.send(new JSONObject()
                    .put("code", 401)
                    .put("message", "Couldn't find the associated minecraft account").toString());
            return null;
        }

        //Succeeded request
        UUID uuid = UUID.fromString(rs.getString("uuid"));
        OfflinePlayer minecraft_player = Bukkit.getOfflinePlayer(uuid);
        if(minecraft_player == null) {
            response.send(new JSONObject()
                    .put("code", 401)
                    .put("message", "Couldn't find the associated minecraft account").toString());
            return null;
        }
        ArrayList<Object> al = new ArrayList<>();
        al.add(discord_user);
        al.add(minecraft_player);
        return al;
    }

}
