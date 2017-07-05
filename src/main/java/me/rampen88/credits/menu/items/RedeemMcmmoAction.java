package me.rampen88.credits.menu.items;

import com.gmail.nossr50.datatypes.skills.SkillType;
import me.rampen88.credits.Credits;
import me.rampen88.credits.player.IWatchedPlayer;
import me.rampen88.credits.player.McmmoWatchedPlayer;
import org.bukkit.entity.Player;

public class RedeemMcmmoAction implements ItemAction{

	private SkillType type;

	public RedeemMcmmoAction(SkillType type) {
		this.type = type;
	}

	@Override
	public void executeAction(Player p, Credits plugin) {
		if(plugin.getPlayerListener().containsPlayer(p)) return;

		// Create a watched player object and add it to the player listener.
		IWatchedPlayer wp = new McmmoWatchedPlayer(p, type);
		plugin.getPlayerListener().addPlayer(p, wp);
		wp.sendInfoMessage(plugin);
	}
}
