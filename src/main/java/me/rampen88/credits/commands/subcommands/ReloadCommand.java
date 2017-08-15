package me.rampen88.credits.commands.subcommands;

import me.rampen88.credits.Credits;
import me.rampen88.credits.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class ReloadCommand extends SubCommand{

	public ReloadCommand(Credits plugin) {
		super(plugin, Collections.singletonList("reload"));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!messageUtil.hasPerm(sender, "credits.command.reload", false))
			return;

		plugin.reload();
		sender.sendMessage(messageUtil.getMessage("Commands.Reload"));
	}
}
