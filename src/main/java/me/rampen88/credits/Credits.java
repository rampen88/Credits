package me.rampen88.credits;

import me.rampen88.credits.commands.CreditsCommand;
import me.rampen88.credits.commands.ImportCommand;
import me.rampen88.credits.commands.RedeemCommand;
import me.rampen88.credits.hooks.McmmoHook;
import me.rampen88.credits.hooks.PlaceholderAPIHook;
import me.rampen88.credits.listener.InventoryListener;
import me.rampen88.credits.listener.PlayerListener;
import me.rampen88.credits.menu.MenuMaster;
import me.rampen88.credits.storage.Storage;
import me.rampen88.credits.storage.StorageFactory;
import me.rampen88.credits.util.ItemBuilder;
import me.rampen88.credits.util.MessageUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Credits extends JavaPlugin {

	private static ItemBuilder itemBuilder;
	private PlaceholderAPIHook placeholderAPIHook;
	private MenuMaster menuMaster;
	private McmmoHook mcmmoHook;
	private Storage storage;

	private InventoryListener inventoryListener;
	private PlayerListener playerListener;
	private MessageUtil messageUtil;

	@Override
	public void onEnable() {
		saveDefaultConfig();

		itemBuilder = new ItemBuilder(this);
		messageUtil = new MessageUtil(this);
		storage = setupStorage();

		registerListener();
		registerCommands();

		if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") && getConfig().getBoolean("UsePlaceholderAPI")){
			placeholderAPIHook = new PlaceholderAPIHook(this);
			placeholderAPIHook.hook();
		}
		if(getServer().getPluginManager().isPluginEnabled("mcMMO"))
			mcmmoHook = new McmmoHook(this);

		menuMaster = new MenuMaster(this);
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

	public String applyPlaceholders(Player p, String s){
		return placeholderAPIHook != null ? placeholderAPIHook.applyPlaceholders(p, s) : s;
	}

	public List<String> applyPlaceholders(Player p, List<String> s){
		return placeholderAPIHook != null ? placeholderAPIHook.applyPlaceholders(p, s) : s;
	}

	public McmmoHook getMcmmoHook() {
		return mcmmoHook;
	}

	public Storage getStorage() {
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

	private Storage setupStorage(){
		String storageType = getConfig().getString("StorageType", "h2");
		return StorageFactory.getStorage(storageType, this);
	}

}
