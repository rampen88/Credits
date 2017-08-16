package me.rampen88.credits.menu;

import me.rampen88.credits.Credits;
import me.rampen88.credits.hooks.McmmoHook;
import me.rampen88.credits.menu.items.InventoryItem;
import me.rampen88.credits.menu.items.InventoryRefreshItem;
import me.rampen88.credits.menu.items.ItemAction;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public class MenuMaster {

	private Credits plugin;
	private Menu mcmmoMenu;

	public MenuMaster(Credits plugin) {
		this.plugin = plugin;
		reload();
	}

	public void openMcmmoMenu(Player p){
		if(mcmmoMenu != null) mcmmoMenu.open(p);
	}

	public void reload(){
		// Only load McMMO inventory if McMMOHook is not null.
		if(plugin.getMcmmoHook() != null){
			ConfigurationSection section = plugin.getConfig().getConfigurationSection("McmmoInventory");
			mcmmoMenu = loadMenu(section);
		}

	}

	private Menu loadMenu(ConfigurationSection configurationSection){
		String title = configurationSection.getString("Title");
		int rows = configurationSection.getInt("Rows");
		Menu menu = new Menu(title, rows, plugin);

		ConfigurationSection items = configurationSection.getConfigurationSection("Items");

		for(String s : items.getKeys(false)){

			InventoryItem item = loadItem(items.getConfigurationSection(s));
			int pos = (items.getInt(s + ".Position-X") - 1) + ((items.getInt(s + ".Position-Y") - 1) * 9);

			menu.addItem(pos, item);
		}

		return menu;
	}


	private InventoryItem loadItem(ConfigurationSection section){
		Material mat = Material.getMaterial(section.getString("Material").toUpperCase());

		if(mat == null){
			plugin.getLogger().info("Invalid material: " + section.getString("Material") + ", Skipping item.");
			return null;
		}

		String name = section.getString("Name");
		List<String> lore = section.getStringList("Lore");
		int damage = section.getInt("Damage", 0);
		boolean ench = section.getBoolean("FakeEnchant", false);
		boolean closeInv = section.getBoolean("CloseInventory", false);

		ItemAction itemAction = getItemAction(section.getString("ItemAction","").split("::"));

		List<String> flagz = section.getStringList("ItemFlags");
		List<ItemFlag> flags = null;
		if(flagz != null){
			flags = new ArrayList<>();
			for(String s : flagz){
				try{
					flags.add(ItemFlag.valueOf(s));
				}catch (IllegalArgumentException | NullPointerException e){
					plugin.getLogger().info("Invalid ItemFlag: " + s);
				}
			}
		}

		return new InventoryRefreshItem(itemAction, closeInv, mat, name, damage, ench, lore, flags);
	}

	private ItemAction getItemAction(String[] itemAction){
		if(itemAction.length < 2) return null;

		// Use switch to make it easy to add new things if wanted.
		switch (itemAction[0]){
			case "mmolvl":
				McmmoHook hook = plugin.getMcmmoHook();
				return hook != null ? hook.getMcmmoItemAction(itemAction[1]) : null;
			default:
				return null;
		}

	}

}
