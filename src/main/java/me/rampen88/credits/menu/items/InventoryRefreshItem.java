package me.rampen88.credits.menu.items;

import me.rampen88.credits.Credits;
import me.rampen88.credits.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryRefreshItem extends InventoryItem{

	private static ItemBuilder ib = Credits.getItemBuilder();

	private Material mat;
	private String name;
	private int damage;
	private boolean ench;
	private List<String> lore;
	private List<ItemFlag> flags;

	public InventoryRefreshItem(ItemAction clickAction, boolean closeInv, Material m, String name, int damage, boolean enchant, List<String> lore, List<ItemFlag> flags) {
		super(null, clickAction, closeInv);
		this.mat = m;
		this.name = name;
		this.damage = damage;
		this.ench = enchant;
		this.lore = lore;
		this.flags = flags;
	}

	@Override
	public ItemStack getItem(Player p){
		return ib.buildItem(mat, name, damage, ench, lore, p, flags);
	}

}
