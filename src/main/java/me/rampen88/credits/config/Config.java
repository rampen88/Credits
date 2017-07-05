package me.rampen88.credits.config;

import me.rampen88.credits.Credits;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

	private File file;
	private YamlConfiguration config;
	
	public Config(Credits plugin, String fileName){
		file = ConfigurationCreator.loadFile(plugin, fileName);
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public YamlConfiguration getConfig(){
		return config;
	}
	
	public void saveConfig(){
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reloadConfig(){
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	
	
}
