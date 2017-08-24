package me.rampen88.credits.listener;

import me.rampen88.credits.Credits;
import me.rampen88.credits.player.WatchedPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerListener implements Listener {

	private Credits plugin;

	public PlayerListener(Credits plugin) {
		this.plugin = plugin;
	}

	private HashMap<UUID, WatchedPlayer> players = new HashMap<>();

	@EventHandler(priority = EventPriority.LOWEST)
	public void chatEvent(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();

		// Check if player is being watched for input.
		WatchedPlayer wp = players.get(p.getUniqueId());
		if (wp == null) return;

		e.setCancelled(true);
		players.remove(p.getUniqueId());

		String msg = e.getMessage();
		try{
			int input = Integer.valueOf(msg);
			if(input <= 0){
				p.sendMessage(plugin.getMessageUtil().getMessage("McMMO.Cancel"));
				return;
			}

			wp.doTheThing(input, plugin);
		}catch (NumberFormatException ignored){
			e.getPlayer().sendMessage(plugin.getMessageUtil().getMessage("McMMO.Cancel"));
		}
	}

	@EventHandler
	public void leaveEvent(PlayerQuitEvent e){
		UUID id = e.getPlayer().getUniqueId();
		players.remove(id);
		plugin.getInventoryListener().removePlayer(id);
		plugin.getStorage().unloadPlayer(id);
	}

	@EventHandler
	public void joinEvent(PlayerJoinEvent event){
		plugin.getStorage().loadPlayer(event.getPlayer().getUniqueId());
	}

	public boolean containsPlayer(Player p){
		return players.containsKey(p.getUniqueId());
	}

	public void addPlayer(Player p, WatchedPlayer wp){
		players.put(p.getUniqueId(), wp);
	}


}
