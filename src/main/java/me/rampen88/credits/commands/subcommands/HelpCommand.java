package me.rampen88.credits.commands.subcommands;

import me.rampen88.credits.Credits;
import me.rampen88.credits.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class HelpCommand extends SubCommand{

	public HelpCommand(Credits plugin) {
		super(plugin, Arrays.asList("help", "?", "howdoesthiswork"));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		StringBuilder stringBuilder = new StringBuilder();

		if(sender.hasPermission("credits.command.help"))
			stringBuilder.append(messageUtil.getMessage("Commands.Help.All"));

		if(sender.hasPermission("credits.command.help.admin"))
			stringBuilder.append(messageUtil.getMessage("Commands.Help.Admin"));

		stringBuilder.append(ChatColor.GRAY).append(plugin.getDescription().getName()).append(" version ").append(plugin.getDescription().getVersion()).append(" made by rampen88");
		sender.sendMessage(stringBuilder.toString());
	}
}
