package me.rampen88.credits.storage;

import com.zaxxer.hikari.HikariDataSource;
import me.rampen88.credits.Credits;
import me.rampen88.credits.storage.cache.PlayerCache;
import me.rampen88.credits.storage.cache.PlayerNotLoadedException;
import me.rampen88.credits.storage.cache.CachedPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SqlStorage implements Storage {

	private HikariDataSource dataSource;
	private PlayerCache playerCache;
	private Credits plugin;
	private String table;

	SqlStorage(Credits plugin, HikariDataSource dataSource, String table) {
		this.plugin = plugin;
		this.dataSource = dataSource;
		this.table = table;
		createTablesAsync();
		playerCache = new PlayerCache(this, plugin);
	}

	private void createTablesAsync(){
		new BukkitRunnable() {
			@Override
			public void run() {
				createTables();
			}
		}.runTaskAsynchronously(plugin);
	}

	private void createTables(){
		try(Connection connection = getConnection()){

			PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + table + " (uuid VARCHAR(36) UNIQUE NOT NULL, credits INT NOT NULL);");
			statement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	@Override
	public boolean isLoaded(UUID uuid) {
		return playerCache.isLoaded(uuid);
	}

	@Override
	public int loadCredits(UUID uuid){
		try(Connection connection = getConnection()){

			PreparedStatement statement = connection.prepareStatement("SELECT credits FROM " + table + " WHERE uuid=?;");
			statement.setString(1, uuid.toString());

			ResultSet resultSet = statement.executeQuery();
			if(resultSet.isBeforeFirst()){ // if there is no rows this returns false,
				resultSet.next();
				return resultSet.getInt("credits");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void loadPlayer(UUID uuid) {
		playerCache.loadPlayer(uuid);
	}

	@Override
	public void unloadPlayer(UUID uuid) {
		playerCache.unloadPlayer(uuid);
	}

	@Override
	public int getCredits(UUID uuid){
		try {
			CachedPlayer cachedPlayer = playerCache.getPlayer(uuid);
			return cachedPlayer.getCredits();
		} catch (PlayerNotLoadedException e) {
			return 0;
		}
	}

	@Override
	public boolean addCredits(UUID uuid, int amount){
		try {
			CachedPlayer cachedPlayer = playerCache.getPlayer(uuid);
			cachedPlayer.addCredits(amount);
			return true;
		} catch (PlayerNotLoadedException e) {
			return false;
		}
	}

	@Override
	public boolean takeCredits(UUID uuid, int amount) throws PlayerNotLoadedException {
		CachedPlayer cachedPlayer = playerCache.getPlayer(uuid);
		return cachedPlayer.takeCredits(amount);
	}

	@Override
	public void setCredits(UUID uuid, int amount) {
		try(Connection connection = getConnection()){

			PreparedStatement statement = connection.prepareStatement("INSERT INTO " + table + " (uuid, credits) VALUES (?,?) ON DUPLICATE KEY UPDATE credits=?;");
			statement.setString(1, uuid.toString());
			statement.setInt(2, amount);
			statement.setInt(3, amount);
			statement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void importCredits() {
		// TODO
	}
}
