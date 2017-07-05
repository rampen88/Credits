package me.rampen88.credits.listener;

import me.rampen88.credits.Credits;
import me.rampen88.credits.player.IWatchedPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerListener implements Listener {

	private Credits plugin;

	public PlayerListener(Credits plugin) {
		this.plugin = plugin;
	}

	private HashMap<UUID, IWatchedPlayer> players = new HashMap<>();

	// Use .LOWEST event priority to get called before other events.
	@EventHandler(priority = EventPriority.LOWEST)
	public void chatEvent(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();

		// Check if player is being watched for input.
		IWatchedPlayer wp = players.get(p.getUniqueId());
		if (wp == null) return;

		// Cancel the event.
		e.setCancelled(true);

		// Remove player from map.
		players.remove(p.getUniqueId());

		String msg = e.getMessage();
		try{
			int input = Integer.valueOf(msg);

			// check that input is not 0 or below
			if(input <= 0){
				p.sendMessage(plugin.getMessageUtil().getMessage("McMMO.Cancel"));
				return;
			}

			// Does the thing
			wp.doTheThing(input, plugin);

		}catch (NumberFormatException ignored){
			// If input number is not a valid number, just send the cancel message.
			e.getPlayer().sendMessage(plugin.getMessageUtil().getMessage("McMMO.Cancel"));
		}
	}

	@EventHandler
	public void leaveEvent(PlayerQuitEvent e){
		// Remove player from both HashMaps upon leaving.
		// No need to check if map already contains player, as it will just silently fail
		// if it does not contain the player UUID.
		UUID id = e.getPlayer().getUniqueId();
		players.remove(id);
		plugin.getInventoryListener().removePlayer(id);
	}

	public boolean containsPlayer(Player p){
		return players.containsKey(p.getUniqueId());
	}

	public void addPlayer(Player p, IWatchedPlayer wp){
		players.put(p.getUniqueId(), wp);
	}


}
