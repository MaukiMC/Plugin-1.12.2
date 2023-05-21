package net.mauki.maukiseasonpl.entities;

import net.dv8tion.jda.api.entities.User;
import net.mauki.maukiseasonpl.caches.Caches;
import net.mauki.maukiseasonpl.core.LiteSQL;
import net.mauki.maukiseasonpl.dashboard.Authenticator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * An object with all for the plugin necessary information
 */
public interface InternalPlayer {

    /**
     * Get the {@link Player} object of the player
     * @return The {@link Player} object
     */
    Player getPlayer() throws Exception;

    /**
     * Get the (linking-)code of the player
     * @return The code object
     */
    String getCode() throws Exception;

    /**
     * Get the {@link User} object of the player
     * @return The {@link User} object
     */
    User getUser() throws Exception;

    /**
     * Parse the object to an {@link JSONObject}
     * @return The parsed {@link JSONObject} of the object
     */
    default JSONObject toJSON() throws Exception {
        return new JSONObject()
                .put("minecraft_uuid", getPlayer().getUniqueId())
                .put("discord_id", getUser().getId())
                .put("code", getCode());
    }

    /**
     * Parse the object to an {@link String} (JSON-format)
     * @return The parsed string of the object (JSON-format)
     */
    default String asString() throws Exception {
        return toJSON().toString();
    }

    /**
     * Get an {@link InternalPlayer} object from the users access_token
     * @param accessToken The access-token of the user
     * @return The {@link InternalPlayer} object of the user
     * @throws Exception Will be thrown when there was an error while parsing
     */
    static InternalPlayer getFromAccessToken(String accessToken) throws Exception {
        ArrayList<Object> al = Authenticator.authenticate(accessToken);
        if(al.size() == 1) return null;
        User user = (User) Objects.requireNonNull(Authenticator.authenticate(accessToken)).get(0);
        ResultSet rs = LiteSQL.onQuery("SELECT * FROM connections WHERE discord_id = '" + user.getId() + "'");
        if(rs == null) return null;
        if(!rs.next()) return null;
        return new InternalPlayer() {
            @Override
            public Player getPlayer() throws Exception {
                return Bukkit.getPlayer(UUID.fromString(rs.getString("uuid")));
            }

            @Override
            public String getCode() throws Exception {
                return rs.getString("code");
            }

            @Override
            public User getUser() throws Exception {
                return DiscordUser.retrieveUserByAccessToken(accessToken);
            }
        };
    }

    /**
     * Get an {@link InternalPlayer} object from the users uuid
     * @param uuid The uuid of the user
     * @return The {@link InternalPlayer} object of the user
     */
    static InternalPlayer getFromUUID(UUID uuid) {
        return Caches.internalPlayerCache.getOrDefault(uuid, new InternalPlayer() {

            final ResultSet rs = LiteSQL.onQuery("SELECT * FROM connections WHERE uuid = '" + uuid + "'");

            @Override
            public Player getPlayer() throws Exception {
                return Bukkit.getPlayer(uuid);
            }

            @Override
            public String getCode() throws Exception {
                if(rs == null) return null;
                if(!rs.next()) return null;
                if(rs.getString("code") == null || rs.getString("code").equalsIgnoreCase("")) return null;
                return rs.getString("code");
            }

            @Override
            public User getUser() throws Exception {
                if(rs == null) return null;
                if(!rs.next()) return null;
                if(rs.getString("discord_id") == null || rs.getString("discord_id").equalsIgnoreCase("")) return null;
                return DiscordUser.retrieveUserById(rs.getString("discord_id"));
            }
        });
    }

    /**
     * Get an {@link InternalPlayer} object from the users id
     * @param id The id of the user
     * @return The {@link InternalPlayer} object of the user
     * @throws Exception Will be thrown when there was an error
     */
    static InternalPlayer getFromId(String id) throws Exception {
        final ResultSet rs = LiteSQL.onQuery("SELECT * FROM connections WHERE discord_id = '" + id + "'");
        if(rs == null) return null;
        if(!rs.next()) return null;
        return Caches.internalPlayerCache.getOrDefault(UUID.fromString(rs.getString("uuid")), new InternalPlayer() {
            @Override
            public Player getPlayer() throws Exception {
                return Bukkit.getPlayer(UUID.fromString(rs.getString("uuid")));
            }

            @Override
            public String getCode() throws Exception {
                return rs.getString("code");
            }

            @Override
            public User getUser() throws Exception {
                return DiscordUser.retrieveUserById(id);
            }
        });
    }

}
