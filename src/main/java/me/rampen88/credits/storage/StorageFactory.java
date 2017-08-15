package me.rampen88.credits.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.rampen88.credits.Credits;
import org.bukkit.configuration.file.FileConfiguration;

public class StorageFactory {

	public static Storage getStorage(String type, Credits plugin){
		switch (type.toLowerCase()){
			case "mysql":
			case "h2":
				return getSqlStorage(type, plugin);
			default:
				plugin.getLogger().warning(type + " is not a valid storage type, defaulting to h2.");
				return getSqlStorage("h2", plugin);
		}
	}

	private static Storage getSqlStorage(String type, Credits plugin){
		HikariConfig hikariConfig = new HikariConfig();
		FileConfiguration config = plugin.getConfig();
		String table = "users";

		if(type.equalsIgnoreCase("mysql")){
			hikariConfig = setupMysqlConfig(config, hikariConfig);
			table = plugin.getConfig().getString("MySql.TablePrefix") + "users";

		}else{ // assume its h2 if its not mysql.
			hikariConfig = setupH2Config(plugin, config, hikariConfig);
		}

		HikariDataSource dataSource = new HikariDataSource(hikariConfig);
		return new SqlStorage(plugin, dataSource, table);
	}

	private static HikariConfig setupMysqlConfig(FileConfiguration config, HikariConfig hikariConfig){
		String user = config.getString("MySql.Username");
		String pass = config.getString("MySql.Password");
		String database = config.getString("MySql.Database");
		String host = config.getString("MySql.Host");
		String port = config.getString("MySql.Port");
		boolean ssl = config.getBoolean("MySql.UseSSL");

		hikariConfig.setUsername(user);
		hikariConfig.setPassword(pass);
		hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);

		hikariConfig.addDataSourceProperty("useSSL", ssl);
		hikariConfig = setConnectionPoolSettings(hikariConfig, config);
		return hikariConfig;
	}

	private static HikariConfig setupH2Config(Credits plugin, FileConfiguration config, HikariConfig hikariConfig){
		boolean useMVStore = config.getBoolean("H2.MV_Store", false);

		String path = plugin.getDataFolder().getAbsolutePath();
		hikariConfig.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
		hikariConfig.addDataSourceProperty("URL", "jdbc:h2:file:"+path+"/database;mode=MySQL;mv_store=" + useMVStore);
		hikariConfig.setUsername("sa");
		hikariConfig.setPassword("sa");

		hikariConfig = setConnectionPoolSettings(hikariConfig, config);
		return new HikariDataSource(hikariConfig);
	}

	private static HikariConfig setConnectionPoolSettings(HikariConfig hikariConfig, FileConfiguration config){
		int maxPoolSize = config.getInt("ConnectionPool.MaxPoolSize", 10);
		long connectionTimeout = config.getLong("ConnectionPool.ConnectionTimeout", 30000);
		long idleTimeout = config.getLong("ConnectionPool.IdleTimeout", 600000);
		long maxLifetime = config.getLong("ConnectionPool.MaxLifetime", 1800000);

		hikariConfig.setMaximumPoolSize(maxPoolSize);
		hikariConfig.setMaxLifetime(maxLifetime);
		hikariConfig.setIdleTimeout(idleTimeout);
		hikariConfig.setConnectionTimeout(connectionTimeout);
		return hikariConfig;
	}
}
