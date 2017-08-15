package me.rampen88.credits.commands;

import me.rampen88.credits.Credits;
import me.rampen88.credits.storage.cache.PlayerNotLoadedException;
import me.rampen88.credits.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public abstract class SubCommand {

	protected MessageUtil messageUtil;
	protected Credits plugin;
	private List<String> aliases;

	protected SubCommand(Credits plugin, List<String> aliases) {
		this.aliases = aliases;
		this.plugin = plugin;
		messageUtil = plugin.getMessageUtil();
	}

	public abstract void execute(CommandSender sender, String[] args);

	boolean isAlias(String cmd){
		return aliases.contains(cmd);
	}

	protected Player getPlayerCheckOnline(CommandSender sender, String name){
		Player p = Bukkit.getPlayer(name);
		// Send message to the person executing the command if player is not online.
		if(p == null){
			sender.sendMessage(messageUtil.getMessage("Commands.PlayerNotOnline").replace("%player%", name));
			return null;
		}

		return p;
	}

	protected Integer getIntegerFromInput(String input){
		try{
			return Integer.valueOf(input);

		}catch(NumberFormatException ignored){}

		return null;
	}

	protected boolean takeCredits(UUID uuid, int amount){
		try{
			return plugin.getStorage().takeCredits(uuid, amount);
		} catch (PlayerNotLoadedException e) {
			return false;
		}
	}

}
