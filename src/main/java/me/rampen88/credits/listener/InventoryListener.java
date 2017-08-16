package me.rampen88.credits.listener;

import me.rampen88.credits.Credits;
import me.rampen88.credits.menu.CreditsMenuHolder;
import me.rampen88.credits.menu.Menu;
import me.rampen88.credits.menu.items.InventoryItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryListener implements Listener{

	private Credits plugin;
	private long minDelay;

	public InventoryListener(Credits plugin) {
		this.plugin = plugin;
		reload();
	}

	public void reload(){
		minDelay = plugin.getConfig().getLong("InventoryClickDelay", 1000);
	}

	private Map<UUID, Long> playerCooldown = new HashMap<>();

	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent e){
		if(e.getInventory().getHolder() instanceof CreditsMenuHolder){
			e.setCancelled(true);

			if(e.getClickedInventory() == null){
				return;
			}

			Menu menu = ((CreditsMenuHolder)e.getInventory().getHolder()).getMenu();
			InventoryItem item = menu.getItemAtPosition(e.getRawSlot());

			if(item != null){
				Player p = (Player) e.getWhoClicked();

				// Make sure player cant spam click items in inventory.
				if (minDelay > 0) {
					Long last = playerCooldown.get(p.getUniqueId());
					long now = System.currentTimeMillis();
					if(last != null && last > now)
						return;

					playerCooldown.put(p.getUniqueId(), now + minDelay);
				}

				if(item.executeClick(p, plugin)){
					delayClose(p);
				}
			}
		}
	}

	void removePlayer(UUID id){
		playerCooldown.remove(id);
	}

	/*
	   * Using a 0 tick delay to make getting items out of the GUI in creative more difficult (at 0 ping at least).
	   */
	private void delayClose(Player p){
		new BukkitRunnable(){

			@Override
			public void run() {
				if(p.isOnline()){
					p.closeInventory();
				}
			}

		}.runTaskLater(plugin, 0L);
	}


}
