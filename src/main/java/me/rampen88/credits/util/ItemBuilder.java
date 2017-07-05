package me.rampen88.credits.util;

import me.rampen88.credits.Credits;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

	private Credits pl;

	public ItemBuilder(Credits pl){
		this.pl = pl;
	}

	public ItemStack buildItem(Material m, String name, int damage, Boolean enchant, List<String> lore, Player p, List<ItemFlag>  flags){

		ItemStack item = new ItemStack(m, 1, (short)damage);

		ItemMeta meta = item.getItemMeta();

		// Attempt to add color and placeholders to ItemName and lore
		if(name != null) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', pl.applyPlaceholders(p, name)));
		if(lore != null){
			lore = pl.applyPlaceholders(p, lore);
			meta.setLore(translateColors(lore));
		}

		if(enchant){
			meta.addEnchant(Enchantment.DURABILITY, 0, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}

		if(flags != null){
			flags.forEach(meta::addItemFlags);
		}

		item.setItemMeta(meta);
		return item;
	}

	// TODO: Move to MessageUtil?
	public List<String> translateColors(List<String> lore){
		List<String> list = new ArrayList<>();
		for(String s : lore){
			list.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		return list;
	}

}
