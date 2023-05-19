package net.mauki.maukiseasonpl.entities;

import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class InternalPlayer {

    private User user;
    private String code;
    private Player player;

    /**
     * Set the {@link User} object to the player
     * @param user The {@link User} object of the player
     * @return The current object
     */
    public InternalPlayer setUser(User user) {
        this.user = user;
        return this;
    }

    /**
     * Set the (linking-)code to the player
     * @param code The (linking-)code of the player
     * @return The current object
     */
    public InternalPlayer setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Set the {@link Player} object of the player
     * @param player The {@link Player} object of the player
     * @return The current object
     */
    public InternalPlayer setPlayer(Player player) {
        this.player = player;
        return this;
    }

    /**
     * Get the {@link Player} object of the player
     * @return The {@link Player} object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the (linking-)code of the player
     * @return The code object
     */
    public String getCode() {
        return code;
    }

    /**
     * Get the {@link User} object of the player
     * @return The {@link User} object
     */
    public User getUser() {
        return user;
    }

    /**
     * Parse the object to an {@link JSONObject}
     * @return The parsed {@link JSONObject} of the object
     */
    public JSONObject toJSON() {
        return new JSONObject()
                .put("minecraft_uuid", player.getUniqueId())
                .put("discord_id", user.getId())
                .put("code", code);
    }

    /**
     * Parse the object to an {@link String} (JSON-format)
     * @return The parsed string of the object (JSON-format)
     */
    public String toString() {
        return toJSON().toString();
    }
}
