package net.mauki.maukiseasonpl.caches;

import net.dv8tion.jda.api.entities.User;
import net.mauki.maukiseasonpl.caches.handler.Cache;
import net.mauki.maukiseasonpl.entities.InternalPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Caches {

    public static Cache<String, User> discordUserCache = new Cache<>();
    public static Cache<UUID, InternalPlayer> internalPlayerCache = new Cache<>();
    public static Cache<Player, Player> latestMessageCache = new Cache<>();

}
