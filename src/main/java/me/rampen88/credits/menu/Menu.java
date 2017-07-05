package me.rampen88.credits.menu;

import me.rampen88.credits.Credits;
import me.rampen88.credits.menu.items.InventoryItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Menu {

	private String title;
	private InventoryItem[] items;
	private Credits plugin;

	Menu(String title, int rows, Credits plugin){
		this.plugin = plugin;
		this.title = ChatColor.translateAlternateColorCodes('&', title);
		items = new InventoryItem[rows * 9];
	}

	void addItem(int position, InventoryItem item){
		if(position <= items.length && position >= 0){
			// TODO: Log to console if there is already an item in that position.
			items[position] = item;
		}
	}

	public InventoryItem getItemAtPosition(int pos){
		if(pos >= 0 && pos < items.length){
			return items[pos];
		}else{
			return null;
		}
	}

	void open(Player p){
		Inventory inv = Bukkit.createInventory(new CreditsMenuHolder(this), items.length, plugin.applyPlaceholders(p, title));
		for(int x = 0; x < items.length; x++){
			if(items[x] != null){
				inv.setItem(x, items[x].getItem(p));
			}
		}
		p.openInventory(inv);
	}

}
