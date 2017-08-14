package me.rampen88.credits.commands.subcommands;

import me.rampen88.credits.Credits;
import me.rampen88.credits.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SendCommand extends SubCommand{

	public SendCommand(Credits plugin) {
		super(plugin, Collections.singletonList("send"));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!messageUtil.hasPerm(sender, "credits.command.send", true))
			return;

		if(args.length != 3){
			sender.sendMessage(messageUtil.getMessage("CreditsCmd.Send.Usage"));
			return;
		}

		Player p = (Player) sender;

		// Integer instead of int to allow for null in case input is not valid.
		Integer amount = getIntegerFromInput(args[2]);
		if(amount == null){
			sender.sendMessage(messageUtil.getMessage("CreditsCmd.Send.Usage"));
			return;
		}else if(amount <= 0){
			sender.sendMessage(messageUtil.getMessage("CreditsCmd.Amount"));
			return;
		}

		// Make sure player has enough credits.
		int current = plugin.getStorage().getCredits(p.getUniqueId());
		if(amount > current){
			p.sendMessage(messageUtil.getMessage("CreditsCmd.Send.NotEnough"));
			return;
		}

		Player target = getPlayerCheckOnline(sender, args[1]);
		if(target == null) return;

		// Make sure you cant sent credits to yourself.
		if(target.getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString())){
			p.sendMessage(messageUtil.getMessage("CreditsCmd.Send.NotSelf"));
			return;
		}

		if(!plugin.getStorage().isLoaded(target.getUniqueId())){
			p.sendMessage(messageUtil.getMessage("CreditsCmd.NotLoaded").replace("%player%", target.getName()));
			return;
		}

		if(takeCredits(p.getUniqueId(), amount)){
			plugin.getStorage().addCredits(target.getUniqueId(), amount);
			p.sendMessage(messageUtil.getMessage("CreditsCmd.Send.Sent").replace("%player%", target.getName()).replace("%amount%", amount.toString()));
			target.sendMessage(messageUtil.getMessage("CreditsCmd.Send.Received").replace("%player%", p.getName()).replace("%amount%", amount.toString()));
		}else{
			plugin.getLogger().info("Something went wrong trying to take " + amount + " credits from " + p.getName());
			// Assume its because of not enough credits.
			p.sendMessage(messageUtil.getMessage("CreditsCmd.Send.NotEnough"));
		}
	}
}
