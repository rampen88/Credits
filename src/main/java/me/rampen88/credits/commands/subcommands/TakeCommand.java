package me.rampen88.credits.commands.subcommands;

import me.rampen88.credits.Credits;
import me.rampen88.credits.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TakeCommand extends SubCommand{
	
	public TakeCommand(Credits plugin) {
		super(plugin, Arrays.asList("take", "remove"));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!messageUtil.hasPerm(sender, "credits.command.take", false))
			return;

		if(args.length != 3){
			sender.sendMessage(messageUtil.getMessage("Commands.Take.Usage"));
			return;
		}

		Integer amount = getIntegerFromInput(args[2]);
		if(amount == null){
			sender.sendMessage(messageUtil.getMessage("Commands.Take.Usage"));
			return;
		}

		Player p = getPlayerCheckOnline(sender, args[1]);
		if(p == null) return;

		if(takeCredits(p.getUniqueId(), amount)) {
			sender.sendMessage(messageUtil.getMessage("Commands.Take.Removed").replace("%player%", p.getName()).replace("%amount%", amount.toString()));
			p.sendMessage(messageUtil.getMessage("Commands.Take.Target").replace("%amount%", amount.toString()));
		}else
			sender.sendMessage(messageUtil.getMessage("Commands.Take.Error").replace("%player%", p.getName()));

	}
}
