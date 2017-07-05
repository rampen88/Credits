package me.rampen88.credits;

import me.rampen88.credits.commands.CreditsCommand;
import me.rampen88.credits.commands.ImportCommand;
import me.rampen88.credits.commands.RedeemCommand;
import me.rampen88.credits.hooks.McmmoHook;
import me.rampen88.credits.hooks.PlaceholderAPIHook;
import me.rampen88.credits.listener.InventoryListener;
import me.rampen88.credits.listener.PlayerListener;
import me.rampen88.credits.menu.MenuMaster;
import me.rampen88.credits.storage.FlatFile;
import me.rampen88.credits.storage.IStorage;
import me.rampen88.credits.util.ItemBuilder;
import me.rampen88.credits.util.MessageUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Credits extends JavaPlugin {

	private PlaceholderAPIHook placeholderAPIHook;
	private MenuMaster menuMaster;
	private McmmoHook mcmmoHook;
	private IStorage storage;

	private PlayerListener playerListener;
	private InventoryListener inventoryListener;

	private MessageUtil messageUtil;
	private static ItemBuilder itemBuilder;

	@Override
	public void onEnable() {
		// Save config with comments.
		saveDefaultConfig();

		itemBuilder = new ItemBuilder(this);
		messageUtil = new MessageUtil(this);

		registerListener();
		registerCommands();

		menuMaster = new MenuMaster(this);

		if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") && getConfig().getBoolean("UsePlaceholderAPI")){
			placeholderAPIHook = new PlaceholderAPIHook(this);
			placeholderAPIHook.hook();
		}

		if(getServer().getPluginManager().isPluginEnabled("mcMMO")){
			mcmmoHook = new McmmoHook(this);
		}

		reload();
		storage = setupStorage();
	}

	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
	}

	private void registerCommands(){
		getCommand("redeem").setExecutor(new RedeemCommand(this));
		getCommand("credits").setExecutor(new CreditsCommand(this));
		getCommand("importcredits").setExecutor(new ImportCommand(this));
	}

	private void registerListener(){
		PluginManager pm = getServer().getPluginManager();

		playerListener = new PlayerListener(this);
		inventoryListener = new InventoryListener(this);

		pm.registerEvents(playerListener, this);
		pm.registerEvents(inventoryListener, this);
	}

	public void reload(){
		reloadConfig();
		menuMaster.reload();
		inventoryListener.reload();
	}

	// Apply placeholders if placeholderAPIHook is not null.
	public String applyPlaceholders(Player p, String s){
		return placeholderAPIHook != null ? placeholderAPIHook.applyPlaceholders(p, s) : s;
	}

	public List<String> applyPlaceholders(Player p, List<String> s){
		return placeholderAPIHook != null ? placeholderAPIHook.applyPlaceholders(p, s) : s;
	}

	public McmmoHook getMcmmoHook() {
		return mcmmoHook;
	}

	public IStorage getStorage() {
		return storage;
	}

	public PlayerListener getPlayerListener() {
		return playerListener;
	}

	public InventoryListener getInventoryListener() {
		return inventoryListener;
	}

	public MenuMaster getMenuMaster() {
		return menuMaster;
	}

	public MessageUtil getMessageUtil() {
		return messageUtil;
	}

	public static ItemBuilder getItemBuilder() {
		return itemBuilder;
	}

	// Gets a instance of storage type.
	private IStorage setupStorage(){
		switch (getConfig().getString("StorageType", "flatfile").toLowerCase()){
			case "flatfile":
				return new FlatFile(this);
			default:
				getLogger().info(getConfig().getString("StorageType") + " is not a valid storage type.");
				break;
		}
		return null;
	}

}
