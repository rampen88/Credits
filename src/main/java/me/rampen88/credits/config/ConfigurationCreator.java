package me.rampen88.credits.config;

import com.google.common.io.ByteStreams;
import me.rampen88.credits.Credits;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

class ConfigurationCreator {

	static File loadFile(Credits plugin, String file) {

		File folder = plugin.getDataFolder();
		if (!folder.exists()) folder.mkdir();

		File resourceFile = new File(folder, file);
		try {
			if (!resourceFile.exists() && resourceFile.createNewFile()) {
				try(InputStream in = plugin.getResource(file); OutputStream out = new FileOutputStream(resourceFile)){
					if(in != null){
						ByteStreams.copy(in, out);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resourceFile;
	}
	
}
