package me.rampen88.credits.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import me.rampen88.credits.Credits;
import me.rampen88.credits.storage.Storage;
import org.bukkit.entity.Player;

import java.util.List;

public class PlaceholderAPIHook extends EZPlaceholderHook{

	private Credits plugin;

	public PlaceholderAPIHook(Credits plugin) {
		super(plugin, "credits");
		this.plugin = plugin;
	}

	@Override
	public String onPlaceholderRequest(Player p, String s) {
		if(s.equalsIgnoreCase("credits")){
			if(p == null)
				return "";

			Storage storage = plugin.getStorage();
			return Integer.toString(storage.getCredits(p.getUniqueId()));

		}else if(s.contains("mcmmocap_")){

			String[] placeholderThing = s.split("_");
			if(placeholderThing.length < 2)
				return "";

			int levelCap = plugin.getMcmmoHook().getLevelCap(placeholderThing[1]);
			return levelCap == Integer.MAX_VALUE ? plugin.getMessageUtil().getMessage("NoLevelCap") : Integer.toString(levelCap);
		}

		return null;
	}

	// Methods to apply placeholders.
	public String applyPlaceholders(Player p, String s){
		return PlaceholderAPI.setPlaceholders(p, s);
	}

	public List<String> applyPlaceholders(Player p, List<String> s){
		return PlaceholderAPI.setPlaceholders(p, s);
	}



}
