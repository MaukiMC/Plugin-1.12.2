package net.mauki.maukiseasonpl.entities;

import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class InternalPlayer {

    private User user;
    private String code;
    private Player player;

    public InternalPlayer setUser(User user) {
        this.user = user;
        return this;
    }

    public InternalPlayer setCode(String code) {
        this.code = code;
        return this;
    }

    public InternalPlayer setPlayer(Player player) {
        this.player = player;
        return this;
    }


    public Player getPlayer() {
        return player;
    }

    public String getCode() {
        return code;
    }

    public User getUser() {
        return user;
    }

    public JSONObject toJSON() {
        return new JSONObject()
                .put("minecraft_uuid", player.getUniqueId())
                .put("discord_id", user.getId())
                .put("code", code);
    }

    public String toString() {
        return toJSON().toString();
    }
}
