package net.mauki.maukiseasonpl.features;

import de.mp.jdwc.internal.entities.EmbedObject;
import net.mauki.maukiseasonpl.core.Boot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class ChestLog implements Listener {

    @EventHandler
    public void onChestOpen(InventoryClickEvent event) throws IOException {
        if(event.getClickedInventory() == null || event.getClickedInventory().getType() == InventoryType.PLAYER) return;
        Boot.getdWebhook().setAvatarUrl(Boot.getDISCORD_CLIENT().getJDA().getSelfUser().getEffectiveAvatarUrl());
        Boot.getdWebhook().setUsername(event.getWhoClicked().getName());
        Boot.getdWebhook().addEmbed(new EmbedObject()
                .setTitle("Inventory Event")
                .setAuthor(event.getWhoClicked().getName(), null, "https://crafatar.com/avatars/" + event.getWhoClicked().getUniqueId() + "?overlay")
                .setColor(Color.YELLOW)
                .setDescription("**__Item Name:__** " + Objects.requireNonNull(event.getCurrentItem()).getItemMeta().getLocalizedName() + "\\n" +
                        "**__Material:__** " + event.getCurrentItem().getType() + "\\n" +
                        "**__Amount:__** " + event.getCurrentItem().getAmount() +
                        "**__Location:__** (`" + Objects.requireNonNull(event.getClickedInventory().getLocation()).getBlockX() + "`, `" + event.getClickedInventory().getLocation().getBlockY() + "`, `" + event.getClickedInventory().getLocation().getBlockZ() + "`)\\n" +
                        "**__Player:__** " + event.getWhoClicked().getName()));
        Boot.getdWebhook().execute();
    }

}
