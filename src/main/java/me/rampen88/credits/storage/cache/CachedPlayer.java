package me.rampen88.credits.storage.cache;

import me.rampen88.credits.Credits;
import me.rampen88.credits.storage.Storage;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class CachedPlayer {

	private final AtomicInteger credits;
	private final Storage storage;
	private Credits plugin;
	private UUID uuid;

	CachedPlayer(UUID uuid, int amount, Storage storage, Credits plugin) {
		this.storage = storage;
		this.plugin = plugin;
		this.uuid = uuid;
		credits = new AtomicInteger(amount);
	}

	public int getCredits() {
		return credits.get();
	}

	public void addCredits(int amount){
		credits.addAndGet(amount);
		saveCredits();
	}

	public boolean takeCredits(int amount) {
		int currentValue = credits.get();
		int newValue = currentValue - amount;
		if(newValue >= 0 && credits.compareAndSet(currentValue, newValue)){
			saveCredits();
			return true;
		}
		return false;
	}

	private void saveCredits(){
		new BukkitRunnable() {
			@Override
			public void run() {
				storage.setCredits(uuid, credits.get());
			}
		}.runTaskAsynchronously(plugin);
	}


}
