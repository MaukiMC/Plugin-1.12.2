package net.mauki.maukiseasonpl.features.linking;

import io.mokulu.discord.oauth.DiscordAPI;
import net.dv8tion.jda.api.entities.User;
import net.mauki.maukiseasonpl.caches.handler.Cache;
import net.mauki.maukiseasonpl.core.Boot;

public class DiscordUser {

    public static Cache<Long, User> discordUserCache = new Cache<>();

    /**
     * Get the {@link User} of their access-token
     * @param accessToken The access-token
     * @return The {@link User} object
     */
    public static User retrieveUserByAccessToken(String accessToken) {
        DiscordAPI api = new DiscordAPI(accessToken);
        try {
            User u = Boot.getDISCORD_CLIENT().getJDA().retrieveUserById(api.fetchUser().getId()).complete();
            discordUserCache.add(u.getIdLong(), u);
            return u;
        } catch(Exception ex) {
            return null;
        }
    }

}
