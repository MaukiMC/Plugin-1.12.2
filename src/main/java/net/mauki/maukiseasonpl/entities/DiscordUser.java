package net.mauki.maukiseasonpl.entities;

import io.mokulu.discord.oauth.DiscordAPI;
import net.dv8tion.jda.api.entities.User;
import net.mauki.maukiseasonpl.caches.Caches;
import net.mauki.maukiseasonpl.core.Boot;

import java.io.IOException;

/**
 * The discord user of authentication
 */
public class DiscordUser {

    /**
     * Get the {@link User} of their access-token
     * @param accessToken The access-token
     * @return The {@link User} object
     * @throws IOException Will be thrown when there was an error
     */
    public static User retrieveUserByAccessToken(String accessToken) throws IOException {
        String id = new DiscordAPI(accessToken).fetchUser().getId();
        User u = Caches.discordUserCache.getOrDefault(id, Boot.getDISCORD_CLIENT().getJDA().retrieveUserById(id).complete());
        Caches.discordUserCache.addOrUpdate(id, u);
        return u;
    }

    /**
     * Get the {@link User} of their id
     * @param id The id of the user
     * @return The {@link User} object
     */
    public static User retrieveUserById(String id) {
        User u = Caches.discordUserCache.getOrDefault(id, Boot.getDISCORD_CLIENT().getJDA().retrieveUserById(id).complete());
        Caches.discordUserCache.addOrUpdate(id, u);
        return u;
    }

}
