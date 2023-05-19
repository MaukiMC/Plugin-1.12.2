package net.mauki.maukiseasonpl.commands.sign;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Management for item signing
 */
public class SignManager {

    /**
     * The item that will be signed/unsigned
     */
    private final ItemStack itemStack;

    /**
     * Initialise the manager for item signing
     * @param itemStack The item you want to sign
     */
    public SignManager(final ItemStack itemStack){
        this.itemStack = itemStack;
    }

    /**
     * Sign the item
     * @param name The name of the player who signed the item
     * @param message The message that belongs to the item
     * @return The signed item
     */
    public ItemStack sign(final String name, final String message){
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<String> lore;

        assert itemMeta != null;
        if(itemMeta.getLore() == null){
            lore = Lists.newArrayList();
        }else{
            lore = itemMeta.getLore();
        }

        lore.add(ChatColor.GRAY + "");
        lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------------------------");
        lore.add(ChatColor.GRAY + "Signiert von " + ChatColor.RED + name + ChatColor.GRAY + " am " + ChatColor.RED + formatTime(System.currentTimeMillis()));

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Check if the item is signed
     * @return If the item is signed
     */
    public boolean isSigned() {
        return (itemStack.getItemMeta().getLore() != null);
    }

    /**
     * Unsign an item
     * @return The item which has been unsigned
     */
    public ItemStack unSign(){
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        List<String> lore = itemMeta.getLore();

        for(int i = 0;i < 3;i++){
            assert lore != null;
            lore.remove(lore.size()-1);
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Format the time to a specific format
     * @param millis The timestamp on milliseconds
     * @return The formatted string
     */
    private String formatTime(final Long millis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return simpleDateFormat.format(millis);
    }

}
