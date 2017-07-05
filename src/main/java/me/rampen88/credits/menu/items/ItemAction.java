package me.rampen88.credits.menu.items;

import me.rampen88.credits.Credits;
import org.bukkit.entity.Player;

public interface ItemAction {

	void executeAction(Player p, Credits plugin);

}
