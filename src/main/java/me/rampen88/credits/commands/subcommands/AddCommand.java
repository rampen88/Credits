package me.rampen88.credits.commands.subcommands;

import me.rampen88.credits.Credits;
import me.rampen88.credits.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class AddCommand extends SubCommand{

	public AddCommand(Credits plugin) {
		super(plugin, Arrays.asList("add", "give"));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!messageUtil.hasPerm(sender, "credits.command.add", false))
			return;

		if (args.length != 3) {
			sender.sendMessage(messageUtil.getMessage("Commands.Add.Usage"));
			return;
		}

		Integer amount = getIntegerFromInput(args[2]);
		if (amount == null) {
			sender.sendMessage(messageUtil.getMessage("Commands.Add.Usage"));
			return;
		}

		// Check that target player is online
		Player p = getPlayerCheckOnline(sender, args[1]);
		if (p == null) return;

		if(plugin.getStorage().addCredits(p.getUniqueId(), amount)){
			sender.sendMessage(messageUtil.getMessage("Commands.Add.Added").replace("%player%", p.getName()).replace("%amount%", amount.toString()));
			p.sendMessage(messageUtil.getMessage("Commands.Add.Target").replace("%amount%", amount.toString()));
		}else{
			sender.sendMessage(messageUtil.getMessage("Commands.NotLoaded").replace("%player%", p.getName()));
		}
	}
}
