package me.rampen88.credits.commands;

import me.rampen88.credits.Credits;
import me.rampen88.credits.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RedeemCommand implements CommandExecutor{

	private Credits plugin;
	private MessageUtil messageUtil;

	public RedeemCommand(Credits plugin) {
		this.plugin = plugin;
		this.messageUtil = plugin.getMessageUtil();
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if(!messageUtil.hasPerm(commandSender,"credits.redeem.mcmmo", true)) return true;

		Player p = (Player) commandSender;
		plugin.getMenuMaster().openMcmmoMenu(p);
		return true;
	}
}
