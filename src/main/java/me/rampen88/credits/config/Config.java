package me.rampen88.credits.config;

import me.rampen88.credits.Credits;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;

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

	public void saveConfigAsync(Credits plugin){
		synchronized (config){

			final String data = config.saveToString();

			new BukkitRunnable(){
				@Override
				public void run() {
					save(data);
				}
			}.runTaskAsynchronously(plugin);

		}
	}

	private synchronized void save(String data){

		try(Writer writer = new OutputStreamWriter(new FileOutputStream(file))){
			writer.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void reloadConfig(){
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	
	
}
