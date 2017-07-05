package me.rampen88.credits.util;

import me.rampen88.credits.Credits;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtil {

	private Credits plugin;

	public MessageUtil(Credits plugin) {
		this.plugin = plugin;
	}

	public String getMessage(String path){
		return ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Messages." + path, "&4Error: Failed to find message. Please inform staff."));
	}

	private String getPermissionMessage(){
		return ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Messages.NoPermission", "&4Error: Failed to find message. Please inform staff."));
	}

	public boolean hasPerm(CommandSender sender, String perm, boolean needsPlayer){
		if(sender.hasPermission(perm)){
			if(needsPlayer){
				if(sender instanceof Player)
					return true;
				else{
					sender.sendMessage(getPermissionMessage());
					return false;
				}
			}else return true;
		}else{
			sender.sendMessage(getPermissionMessage());
			return false;
		}
	}

}
