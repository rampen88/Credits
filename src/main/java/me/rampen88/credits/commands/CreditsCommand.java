package me.rampen88.credits.commands;

import me.rampen88.credits.Credits;
import me.rampen88.credits.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

public class CreditsCommand implements CommandExecutor{

	private Set<SubCommand> subCommands = new HashSet<>();
	private SubCommand viewCommand;

	public CreditsCommand(Credits plugin) {
		viewCommand = new ViewCommand(plugin);
		subCommands.add(viewCommand);
		subCommands.add(new AddCommand(plugin));
		subCommands.add(new TakeCommand(plugin));
		subCommands.add(new SendCommand(plugin));
		subCommands.add(new ReloadCommand(plugin));
		subCommands.add(new HelpCommand(plugin));
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
		SubCommand subCommand = getSubCommand(args.length == 0 ? "view" : args[0].toLowerCase());
		if(subCommand != null)
			subCommand.execute(commandSender, args);
		return true;
	}

	private SubCommand getSubCommand(String cmd){
		for (SubCommand subCommand : subCommands) {
			if(subCommand.isAlias(cmd))
				return subCommand;
		}
		return viewCommand;
	}

}
