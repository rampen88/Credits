package me.rampen88.credits.commands.subcommands;

import me.rampen88.credits.Credits;
import me.rampen88.credits.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ViewCommand extends SubCommand{

	public ViewCommand(Credits plugin) {
		super(plugin, Arrays.asList("view", "look"));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player target;
		String msg = "Self";
		if(args.length == 0){
			if(!messageUtil.hasPerm(sender, "credits.command.view", true))
				return;
			target = (Player) sender;
		}else{
			if(!messageUtil.hasPerm(sender, "credits.command.view.other", false))
				return;
			target = getPlayerCheckOnline(sender, args[0]);
			if(target == null) return;
			msg = "Other";
		}
		int credits = plugin.getStorage().getCredits(target.getUniqueId());
		sender.sendMessage(messageUtil.getMessage("CreditsCmd.View." + msg).replace("%player%", target.getName()).replace("%amount%", Integer.toString(credits)));
	}
}
