package me.rampen88.credits.menu;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CreditsMenuHolder implements InventoryHolder {

	private Menu menu;

	CreditsMenuHolder(Menu menu) {
		this.menu = menu;
	}

	// No idea what this does, but it works.
	@Override
	public Inventory getInventory() {
		return Bukkit.createInventory(null, 54);
	}

	// used to get the Menu instance by the Listener.
	public Menu getMenu() {
		return menu;
	}
}
