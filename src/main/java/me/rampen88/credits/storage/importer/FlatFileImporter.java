package me.rampen88.credits.storage.importer;

import me.rampen88.credits.Credits;
import me.rampen88.credits.config.Config;
import me.rampen88.credits.storage.Storage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class FlatFileImporter {

	private Storage storage;
	private Credits plugin;

	public FlatFileImporter(Credits plugin, Storage storage) {
		this.plugin = plugin;
		this.storage = storage;
	}

	public void importCredits(){
		String file = plugin.getConfig().getString("Import.File");
		String sectionPath = plugin.getConfig().getString("Import.UuidSection");
		String path = plugin.getConfig().getString("Import.CreditsPath");

		if(file == null || file.isEmpty()){
			plugin.getLogger().info("Import file name is empty. Stopping import.");
			return;
		}
		Config cfg = new Config(plugin, file);
		YamlConfiguration config = cfg.getConfig();

		ConfigurationSection section = getSection(config, sectionPath);
		if(section == null){
			plugin.getLogger().info("Unable to find section '" + sectionPath + "' in the config. Make sure your settings are correct!");
			return;
		}

		int total = 0, skipped =0;
		for(String s : section.getKeys(false)){
			total++;
			int credits = section.getInt(s + path, 0);
			if(credits == 0) {
				plugin.getLogger().info("UUID: " + s + " had 0 credits, or CreditsPath is wrong. Skipping player.");
				skipped++;
				continue;
			}
			UUID uuid = getUUID(s);
			if(uuid != null)
				setCredits(uuid, credits);

		}
		plugin.getLogger().info("Attempted to import a total of " + total + " players, skipped " + skipped + ".");
	}

	private ConfigurationSection getSection(FileConfiguration config, String path){
		if(path.isEmpty())
			return config;
		else
			return config.getConfigurationSection(path);
	}

	private UUID getUUID(String s){
		try{
			return UUID.fromString(s);
		}catch (IllegalArgumentException e){
			return null;
		}
	}

	private void setCredits(UUID uuid, int amount){
		new BukkitRunnable(){
			@Override
			public void run() {
				storage.setCredits(uuid, amount);
			}
		}.runTaskAsynchronously(plugin);
	}

}
