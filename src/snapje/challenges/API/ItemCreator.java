package snapje.challenges.API;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import snapje.challenges.Utils.Chat;

public class ItemCreator {

	
	public static ItemStack createItem(Material mat) {
	ItemStack item = new ItemStack(mat);
		
	return item;
	}
	
	public static ItemStack createItem(Material mat, String displayname) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Chat.format(displayname));
		item.setItemMeta(meta);
			
		return item;
	}
	
	public static ItemStack createItem(Material mat, String displayname, int amount) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Chat.format(displayname));
		item.setItemMeta(meta);
		item.setAmount(amount);
			
		return item;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack createItem(Material mat, String displayname,ArrayList<String> lore) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Chat.format(displayname));
		meta.setLore(lore);
		item.setItemMeta(meta);
			
		return item;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack createItem(Material mat, String displayname, int amount, int type) {
		ItemStack item = new ItemStack(mat, amount, (short) 0, (byte) type);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Chat.format(displayname));
		item.setItemMeta(meta);
			
		return item;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack createItem(Material mat, String displayname,ArrayList<String> lore, int amount, int type) {
		ItemStack item = new ItemStack(mat, amount, (short) 0, (byte) type);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Chat.format(displayname));
		meta.setLore(lore);
		item.setItemMeta(meta);
			
		return item;
	}
}
