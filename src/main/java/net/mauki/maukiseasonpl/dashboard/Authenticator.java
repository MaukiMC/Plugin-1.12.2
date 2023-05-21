package net.mauki.maukiseasonpl.dashboard;

import net.dv8tion.jda.api.entities.User;
import net.mauki.maukiseasonpl.core.LiteSQL;
import net.mauki.maukiseasonpl.entities.DiscordUser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * methods for authentication
 */
public class Authenticator {

    /**
     * Authenticates a user
     * @param access_token The provided access_token of the user
     * @return An {@link ArrayList} with a {@link User} object on index 0 and {@link OfflinePlayer} on index 1
     * @throws SQLException Will be thrown when there was an error with the database
     */
    public static ArrayList<Object> authenticate(String access_token) throws Exception {

        ArrayList<Object> al = new ArrayList<>();

        //Access-Token
        if(access_token == null) {
            al.add(new JSONObject()
                    .put("code", 401)
                    .put("message", "No access-token given").toString());
            return al;
        }

        //Discord-User
        User discord_user = DiscordUser.retrieveUserByAccessToken(access_token);
        if(discord_user == null) {
            al.add(new JSONObject()
                    .put("code", 401)
                    .put("message", "Invalid access-token").toString());
            return al;
        }

        //Database Entry
        ResultSet rs = LiteSQL.onQuery("SELECT * FROM connections WHERE discord_id = '" + discord_user.getId() + "'");
        if(rs == null) {
            al.add(new JSONObject()
                    .put("code", 500)
                    .put("message", "Internal Server Error").toString());
            return al;
        }

        if(!rs.next()) {
            al.add(new JSONObject()
                    .put("code", 401)
                    .put("message", "Couldn't find the associated minecraft account").toString());
            return al;
        }

        //Succeeded request
        UUID uuid = UUID.fromString(rs.getString("uuid"));
        OfflinePlayer minecraft_player = Bukkit.getOfflinePlayer(uuid);
        if(minecraft_player == null) {
            al.add(new JSONObject()
                    .put("code", 401)
                    .put("message", "Couldn't find the associated minecraft account").toString());
            return al;
        }

        al.add(discord_user);
        al.add(minecraft_player);
        return al;
    }

}
