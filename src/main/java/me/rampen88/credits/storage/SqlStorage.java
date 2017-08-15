package me.rampen88.credits.storage;

import com.zaxxer.hikari.HikariDataSource;
import me.rampen88.credits.Credits;
import me.rampen88.credits.storage.importer.FlatFileImporter;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SqlStorage extends CachedStorage {

	private HikariDataSource dataSource;
	private Credits plugin;
	private String table;

	SqlStorage(Credits plugin, HikariDataSource dataSource, String table) {
		super(plugin);
		this.plugin = plugin;
		this.dataSource = dataSource;
		this.table = table;
		createTablesAsync();
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
		FlatFileImporter fileImporter = new FlatFileImporter(plugin, this);
		fileImporter.importCredits();
	}
}
