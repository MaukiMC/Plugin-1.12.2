package net.mauki.maukiseasonpl.commands.sign;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.List;

public class SignManager {

    private final ItemStack itemStack;

    public SignManager(final ItemStack itemStack){
        this.itemStack = itemStack;
    }

    public ItemStack sign(final String name, final String message){
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<String> lore;

        assert itemMeta != null;
        if(itemMeta.getLore() == null){
            lore = Lists.newArrayList();
        }else{
            lore = itemMeta.getLore();
        }

        lore.add(ChatColor.GRAY + message.replace('&', '§'));
        lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------------------------");
        lore.add(ChatColor.GRAY + "Signiert von " + ChatColor.RED + name + ChatColor.GRAY + " am " + ChatColor.RED + formatTime(System.currentTimeMillis()));

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public boolean isSigned() {
        return (itemStack.getItemMeta().getLore() != null);
    }

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

    private String formatTime(final Long millis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return simpleDateFormat.format(millis);
    }

}
