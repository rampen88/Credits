package me.rampen88.credits.storage.cache;

import me.rampen88.credits.Credits;
import me.rampen88.credits.storage.Storage;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerCache {

	private final Map<UUID, CachedPlayer> playerCache = new ConcurrentHashMap<>();
	private Storage storage;
	private Credits plugin;

	public PlayerCache(Storage storage, Credits plugin) {
		this.storage = storage;
		this.plugin = plugin;
	}

	public void loadPlayer(UUID uuid){
		new BukkitRunnable() {
			@Override
			public void run() {
				playerCache.computeIfAbsent(uuid, key -> new CachedPlayer(key, storage.loadCredits(key), storage, plugin));
			}
		}.runTaskAsynchronously(plugin);
	}

	public void unloadPlayer(UUID uuid){
		playerCache.remove(uuid);
	}

	public CachedPlayer getPlayer(UUID uuid) throws PlayerNotLoadedException {
		CachedPlayer cachedPlayer = playerCache.get(uuid);
		if(cachedPlayer == null)
			throw new PlayerNotLoadedException();
		return cachedPlayer;
	}

	public boolean isLoaded(UUID uuid){
		return playerCache.get(uuid) != null;
	}

}
