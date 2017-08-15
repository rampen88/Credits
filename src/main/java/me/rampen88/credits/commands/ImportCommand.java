package me.rampen88.credits.commands;

import me.rampen88.credits.Credits;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ImportCommand implements CommandExecutor{

	private Credits plugin;

	public ImportCommand(Credits plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if(!plugin.getMessageUtil().hasPerm(commandSender, "credits.import", false)) return true;

		plugin.getStorage().importCredits();
		commandSender.sendMessage(plugin.getMessageUtil().getMessage("Commands.Import.Attempted"));
		return true;
	}
}
