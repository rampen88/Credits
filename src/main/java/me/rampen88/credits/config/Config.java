package me.rampen88.credits.config;

import me.rampen88.credits.Credits;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config {

	private YamlConfiguration config;
	
	public Config(Credits plugin, String fileName){
		File file = ConfigurationCreator.loadFile(plugin, fileName);
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public YamlConfiguration getConfig(){
		return config;
	}

}
