package me.rampen88.credits.menu.items;

import me.rampen88.credits.Credits;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryItem {

	private ItemStack item;
	private ItemAction clickAction;
	private boolean closeInv;
	
	public InventoryItem(ItemStack item, ItemAction clickAction, boolean closeInv){
		this.item = item;
		this.clickAction = clickAction;
		this.closeInv = closeInv;
	}
	
	public ItemStack getItem(Player p){
		return item;
	}
	
	public boolean executeClick(Player p, Credits plugin){
		if(clickAction != null){
			clickAction.executeAction(p, plugin);
		}
		return closeInv;
	}
	
}
