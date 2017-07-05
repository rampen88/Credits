package me.rampen88.credits.commands;

import me.rampen88.credits.Credits;
import me.rampen88.credits.storage.IStorage;
import me.rampen88.credits.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CreditsCommand implements CommandExecutor{

	private Credits plugin;
	private MessageUtil messageUtil;

	public CreditsCommand(Credits plugin) {
		this.plugin = plugin;
		this.messageUtil = plugin.getMessageUtil();
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
		if(args.length == 0){
			if(!messageUtil.hasPerm(commandSender, "credits.command.view.", true)) return true;

			Player p = (Player) commandSender;
			int credits = plugin.getStorage().getCredits(p.getUniqueId());

			p.sendMessage(messageUtil.getMessage("CreditsCmd.View.Self").replace("%amount%", Integer.toString(credits)));
			return true;
		}else{
			switch (args[0].toLowerCase()){
				case "add":
					addCredits(commandSender, args);
					return true;

				case "take":
					takeCredits(commandSender, args);
					return true;

				case "send":
					sendCredits(commandSender, args);
					return true;

				case "reload":
					if(!messageUtil.hasPerm(commandSender, "credits.command.reload", false)) return true;

					plugin.reload();
					commandSender.sendMessage(messageUtil.getMessage("CreditsCmd.Reload"));

					return true;

				case "help":
					if(!messageUtil.hasPerm(commandSender, "credits.command.help", false)) return true;

					List<String> help = plugin.getConfig().getStringList("Messages.CreditsCmd.Help");
					help.add("&7" + plugin.getDescription().getName() + " version "+ plugin.getDescription().getVersion() + " made by rampen88");

					help = Credits.getItemBuilder().translateColors(help);
					help.forEach(commandSender::sendMessage);
					return true;
				default:
					if(!messageUtil.hasPerm(commandSender, "credits.command.view.other", false)) return true;

					if(args.length < 2){
						commandSender.sendMessage(messageUtil.getMessage("CreditsCmd.View.OtherUsage"));
						return true;
					}

					Player p = getPlayerCheckOnline(commandSender, args[1]);
					if(p == null) return true;

					int credits = plugin.getStorage().getCredits(p.getUniqueId());

					commandSender.sendMessage(messageUtil.getMessage("CreditsCmd.View.Other").replace("%player%", p.getName()).replace("%amount%", Integer.toString(credits)));
					return true;
			}
		}
	}

	private void sendCredits(CommandSender commandSender, String[] args){
		if(!messageUtil.hasPerm(commandSender, "credits.command.send", true)) return;

		if(args.length != 3){
			commandSender.sendMessage(messageUtil.getMessage("CreditsCmd.Send.Usage"));
			return;
		}

		Player p = (Player) commandSender;

		// Integer instead of int to allow for null in case input is not valid.
		Integer amount = getIntegerFromInput(args[2]);
		if(amount == null){
			commandSender.sendMessage(messageUtil.getMessage("CreditsCmd.Send.Usage"));
			return;
		}else if(amount <= 0){
			commandSender.sendMessage(messageUtil.getMessage("CreditsCmd.Amount"));
			return;
		}

		IStorage storage = plugin.getStorage();

		// Make sure player has enough credits.
		int current = storage.getCredits(p.getUniqueId());
		if(amount > current){
			p.sendMessage(messageUtil.getMessage("CreditsCmd.Send.NotEnough"));
			return;
		}

		// Check that target player is online
		Player target = getPlayerCheckOnline(commandSender, args[1]);
		if(target == null) return;

		// Make sure you cant sent credits to yourself.
		if(target.getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString())){
			p.sendMessage(messageUtil.getMessage("CreditsCmd.Send.NotSelf"));
			return;
		}

		if(storage.takeCredits(p.getUniqueId(), amount)){
			if(!storage.addCredits(p.getUniqueId(), amount)){
				// if it for some mysterious reason fails, log to console. Should never happen though.
				plugin.getLogger().info("Something went wrong trying to add " + amount + " credits to " + target.getName() + " sent by " + p.getName());
				return;
			}
			p.sendMessage(messageUtil.getMessage("CreditsCmd.Send.Sent").replace("%player%", args[1]).replace("%amount%", amount.toString()));
			target.sendMessage(messageUtil.getMessage("CreditsCmd.Send.Received").replace("%player%", args[1]).replace("%amount%", amount.toString()));
		}else{
			plugin.getLogger().info("Something went wrong trying to take " + amount + " credits from " + p.getName());
			// Assume its because of not enough credits.
			p.sendMessage(messageUtil.getMessage("CreditsCmd.Send.NotEnough"));
		}
	}

	private void addCredits(CommandSender commandSender, String[] args) {
		if (!messageUtil.hasPerm(commandSender, "credits.command.add", false)) return;

		if (args.length != 3) {
			commandSender.sendMessage(messageUtil.getMessage("CreditsCmd.Add.Usage"));
			return;
		}

		Integer amount = getIntegerFromInput(args[2]);
		if (amount == null) {
			commandSender.sendMessage(messageUtil.getMessage("CreditsCmd.Add.Usage"));
			return;
		}

		// Check that target player is online
		Player p = getPlayerCheckOnline(commandSender, args[1]);
		if (p == null) return;

		if (plugin.getStorage().addCredits(p.getUniqueId(), amount)){
			commandSender.sendMessage(messageUtil.getMessage("CreditsCmd.Add.Added").replace("%player%", args[1]).replace("%amount%", amount.toString()));
			p.sendMessage(messageUtil.getMessage("CreditsCmd.Add.Target").replace("%player%", args[1]).replace("%amount%", amount.toString()));
		}else {
			// Should never happen, but just in case.
			commandSender.sendMessage(messageUtil.getMessage("CreditsCmd.Add.Error").replace("%player%", args[1]));
			plugin.getLogger().info("Something went wrong trying to add " + amount.toString() + " credits to player " + p.getName());
		}

	}

	private void takeCredits(CommandSender commandSender, String[] args){
		if(!messageUtil.hasPerm(commandSender, "credits.command.take", false)) return;

		if(args.length != 3){
			commandSender.sendMessage(messageUtil.getMessage("CreditsCmd.Take.Usage"));
			return;
		}

		Integer amount = getIntegerFromInput(args[2]);
		if(amount == null){
			commandSender.sendMessage(messageUtil.getMessage("CreditsCmd.Take.Usage"));
			return;
		}

		// Make sure target player is online.
		Player p = getPlayerCheckOnline(commandSender, args[1]);
		if(p == null) return;

		if(plugin.getStorage().takeCredits(p.getUniqueId(), amount)) {
			commandSender.sendMessage(messageUtil.getMessage("CreditsCmd.Take.Removed").replace("%player%", args[1]).replace("%amount%", amount.toString()));
			p.sendMessage(messageUtil.getMessage("CreditsCmd.Take.Target").replace("%player%", args[1]).replace("%amount%", amount.toString()));
		}else
			commandSender.sendMessage(messageUtil.getMessage("CreditsCmd.Take.Error").replace("%player%", args[1]));

	}

	private Player getPlayerCheckOnline(CommandSender sender, String name){
		Player p = Bukkit.getPlayer(name);

		// Send message to the person executing the command if player is not online.
		if(p == null){
			sender.sendMessage(messageUtil.getMessage("CreditsCmd.PlayerNotOnline").replace("%player%", name));
			return null;
		}

		return p;
	}

	private Integer getIntegerFromInput(String input){
		try{
			return Integer.valueOf(input);

		}catch(NumberFormatException ignored){}

		return null;
	}

}
