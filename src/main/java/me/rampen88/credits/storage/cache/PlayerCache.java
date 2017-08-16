package me.rampen88.credits.storage.cache;

import me.rampen88.credits.Credits;
import me.rampen88.credits.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerCache {

	private final Map<UUID, QueuedPlayer> queuedPlayers = new ConcurrentHashMap<>();
	private final Map<UUID, CachedPlayer> playerCache = new ConcurrentHashMap<>();
	private final Storage storage;
	private final Credits plugin;

	public PlayerCache(Storage storage, Credits plugin) {
		this.storage = storage;
		this.plugin = plugin;
	}

	public void loadPlayer(UUID uuid){
		new BukkitRunnable() {
			@Override
			public void run() {
				CachedPlayer cachedPlayer = playerCache.computeIfAbsent(uuid, key -> new CachedPlayer(key, storage.loadCredits(key), storage, plugin));
				QueuedPlayer queuedPlayer = queuedPlayers.get(uuid);
				if(queuedPlayer != null)
					cachedPlayer.addCredits(queuedPlayer.getCredits());

				queuedPlayers.remove(uuid);
				removeIfOnline(uuid);
			}
		}.runTaskAsynchronously(plugin);
	}

	private void removeIfOnline(UUID uuid){
		new BukkitRunnable() {
			@Override
			public void run() {
				if(!isOnline(uuid))
					playerCache.remove(uuid);
			}
		}.runTask(plugin);
	}

	private boolean isOnline(UUID uuid){
		return Bukkit.getPlayer(uuid) != null;
	}

	public boolean isLoaded(UUID uuid){
		return playerCache.get(uuid) != null;
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

	public QueuedPlayer getQueuedPlayer(UUID uuid){
		QueuedPlayer cachedPlayer = playerCache.get(uuid);
		if(cachedPlayer != null)
			return cachedPlayer;
		// Only create a QueuedPlayer if a CachedPlayer does not exists.
		return queuedPlayers.computeIfAbsent(uuid, key -> new QueuedPlayerImpl());
	}

}
