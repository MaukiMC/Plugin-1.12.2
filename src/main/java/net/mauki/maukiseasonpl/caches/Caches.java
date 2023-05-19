package net.mauki.maukiseasonpl.caches;

import net.dv8tion.jda.api.entities.User;
import net.mauki.maukiseasonpl.caches.handler.Cache;
import net.mauki.maukiseasonpl.entities.InternalPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * All caches of the plugin
 */
public class Caches {

    /**
     * The cache for all {@link User}s
     */
    public static Cache<String, User> discordUserCache = new Cache<>();
    /**
     * The cache of all {@link InternalPlayer}s
     */
    public static Cache<UUID, InternalPlayer> internalPlayerCache = new Cache<>();
    /**
     * The cache for the latest message of a {@link Player}
     */
    public static Cache<Player, Player> latestMessageCache = new Cache<>();

}
