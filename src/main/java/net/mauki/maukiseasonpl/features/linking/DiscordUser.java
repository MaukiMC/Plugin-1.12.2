package net.mauki.maukiseasonpl.features.linking;

import io.mokulu.discord.oauth.DiscordAPI;
import net.dv8tion.jda.api.entities.User;
import net.mauki.maukiseasonpl.caches.Caches;
import net.mauki.maukiseasonpl.core.Boot;

public class DiscordUser {

    /**
     * Get the {@link User} of their access-token
     * @param accessToken The access-token
     * @return The {@link User} object
     */
    public static User retrieveUserByAccessToken(String accessToken) {
        if(Caches.discordUserCache.contains(accessToken)) return Caches.discordUserCache.get(accessToken);
        DiscordAPI api = new DiscordAPI(accessToken);
        try {
            User u = Boot.getDISCORD_CLIENT().getJDA().retrieveUserById(api.fetchUser().getId()).complete();
            Caches.discordUserCache.add(accessToken, u);
            return u;
        } catch(Exception ex) {
            return null;
        }
    }

}
