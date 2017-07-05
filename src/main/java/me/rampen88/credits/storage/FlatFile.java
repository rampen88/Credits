package me.rampen88.credits.storage;

import me.rampen88.credits.Credits;
import me.rampen88.credits.config.Config;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.yaml.snakeyaml.Yaml;

import java.util.UUID;

public class FlatFile implements IStorage{

	private Config config;
	private Credits plugin;
	private boolean attemptSaveAsync;

	public FlatFile(Credits plugin){
		this.plugin = plugin;
		config = new Config(plugin, "storage.yml");
		attemptSaveAsync = plugin.getConfig().getBoolean("FlatFile.AttemptAsyncSave", false);
	}

	@Override
	public int getCredits(UUID uuid) {
		YamlConfiguration cfg = config.getConfig();

		ConfigurationSection section = cfg.getConfigurationSection(uuid.toString());
		// If section is null, player does not exist. No reason to create either, as the player would not have any credits.
		if(section == null){
			return 0;
		}

		// Return int at 'Credits: ' with 0 as fallback in case it cant find it. That should never happen though.
		return section.getInt("Credits", 0);
	}

	@Override
	public boolean addCredits(UUID uuid, int amount) {
		YamlConfiguration cfg = config.getConfig();

		// check if player exist, if not, create section in config
		ConfigurationSection section = cfg.getConfigurationSection(uuid.toString());
		if(section == null){
			section = cfg.createSection(uuid.toString());
		}

		// Add current credits.
		amount += section.getInt("Credits", 0);

		// set new amount
		section.set("Credits", amount);

		// save
		saveFile();
		return true;
	}

	@Override
	public boolean takeCredits(UUID uuid, int amount) {
		YamlConfiguration cfg = config.getConfig();

		ConfigurationSection section = cfg.getConfigurationSection(uuid.toString());
		// if section is null, player does not have any credits, therefor, return false as it cannot take them.
		if(section == null){
			return false;
		}

		// check if player has enough credits.
		int current = section.getInt("Credits", 0);
		if(amount > current) return false;

		// set new amount
		current -= amount;
		section.set("Credits", current);

		// save
		saveFile();
		return true;
	}

	@Override
	public void importCredits() {
		// Get all import settings
		String file = plugin.getConfig().getString("Import.File");
		String sectionPath = plugin.getConfig().getString("Import.UuidSection");
		String path = plugin.getConfig().getString("Import.CreditsPath");

		if(file == null || file.isEmpty()){
			plugin.getLogger().info("Import file name is empty. Stopping import.");
			return;
		}

		// Create or load the file
		Config cfg = new Config(plugin, file);

		// Get the config
		YamlConfiguration config = cfg.getConfig();

		// Get and validate that the section does exist.
		ConfigurationSection section = config.getConfigurationSection(sectionPath);
		if(section == null){
			plugin.getLogger().info("Unable to find section '" + sectionPath + "' in the config. Make sure your settings are correct!");
			return;
		}

		// Get the current flatfile storage.
		YamlConfiguration current = this.config.getConfig();

		int total = 0, skipped =0;
		// Loop over all the shallow keys in the section (UUIDs).
		for(String s : section.getKeys(false)){
			total++;
			int credits = section.getInt(s + path, 0);
			if(credits == 0) {
				plugin.getLogger().info("UUID: " + s + " had 0 credits, or CreditsPath is wrong. Skipping player.");
				skipped++;
				continue;
			}
			// Set the credits.
			current.set(s + ".Credits", credits);
		}

		// Inform how many were imported
		plugin.getLogger().info("Attempted to import a total of " + total + " players, skipped " + skipped + " due to 0 credits or invalid credits path in config.");

		// Save the file.
		saveFile();
	}

	private void saveFile(){
		if(attemptSaveAsync){
			config.saveConfigAsync(plugin);
		}else{
			config.saveConfig();
		}
	}

}
