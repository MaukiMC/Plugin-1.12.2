package net.mauki.maukiseasonpl.features;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.mp.jdwc.internal.JavaDiscordWebhookClient;
import de.mp.jdwc.internal.entities.EmbedObject;
import lombok.Getter;
import net.mauki.maukiseasonpl.core.Boot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.awt.*;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ChestLog implements Listener {

    /**
     * Cache for checking whether the event should be logged or not
     */
    @Getter
    private static final Cache<UUID, Integer> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(20, TimeUnit.SECONDS)
            .build();

    /**
     * Event which will be triggered when a player clicks into an inventory
     * @param event The {@link InventoryClickEvent}
     * @throws IOException will be thrown when there was an error with the webhook
     */
    @EventHandler
    public void onChestOpen(InventoryClickEvent event) throws IOException {
        if(event.getClickedInventory() == null || event.getClickedInventory().getType() == InventoryType.PLAYER) return;
        if(getCache().asMap().getOrDefault(event.getWhoClicked().getUniqueId(), 0) >= 5) return;

        getCache().put(event.getWhoClicked().getUniqueId(), getCache().asMap().getOrDefault(event.getWhoClicked().getUniqueId(), 0)+1);

        JavaDiscordWebhookClient hook = new JavaDiscordWebhookClient.Builder()
                .setID(Long.parseLong(Boot.getDotenv().get("ADMIN_LOG_HOOK_ID")))
                .setToken(Boot.getDotenv().get("ADMIN_LOG_HOOK_TOKEN"))
                .build();

        hook.setAvatarUrl(Boot.getDISCORD_CLIENT().getJDA().getSelfUser().getEffectiveAvatarUrl());
        hook.setUsername(event.getWhoClicked().getName());

        boolean currItNull = (event.getCurrentItem() == null);
        boolean hasName = event.getCurrentItem().getItemMeta().hasLocalizedName();
        boolean clickInvNull = (event.getClickedInventory() == null);
        boolean invLocNull = (clickInvNull || event.getClickedInventory().getLocation() == null);

        hook.addEmbed(new EmbedObject()
                .setTitle("Inventory Event")
                .setAuthor(event.getWhoClicked().getName(), null, "https://crafatar.com/avatars/" + event.getWhoClicked().getUniqueId() + "?overlay")
                .setColor(Color.YELLOW)
                .setDescription("**__Item Name:__** " + (currItNull || !hasName ? "_Could not be loaded/No name given_" : event.getCurrentItem().getItemMeta().getLocalizedName()) + "\\n" +
                        "**__Material:__** " + (currItNull ? "_Could not be loaded_" : event.getCurrentItem().getType()) + "\\n" +
                        "**__Amount:__** " + (currItNull ? "_Could not be loaded_" : event.getCurrentItem().getAmount()) + "\\n" +
                        "**__Location:__** " + (invLocNull ? "_Could not be loaded_" : "(`" + event.getClickedInventory().getLocation().getBlockX() + "`, `" + event.getClickedInventory().getLocation().getBlockY() + "`, `" + event.getClickedInventory().getLocation().getBlockZ() + "`)") + "\\n" +
                        "**__Player:__** " + event.getWhoClicked().getName()));
        hook.execute();
    }

}
