package me.rampen88.credits.storage;

import me.rampen88.credits.Credits;
import me.rampen88.credits.storage.cache.CachedPlayer;
import me.rampen88.credits.storage.cache.PlayerCache;
import me.rampen88.credits.storage.cache.PlayerNotLoadedException;
import me.rampen88.credits.storage.cache.QueuedPlayer;

import java.util.UUID;

public abstract class CachedStorage implements Storage{

	private final PlayerCache playerCache;

	CachedStorage(Credits plugin) {
		playerCache = new PlayerCache(this, plugin);
	}

	@Override
	public boolean isLoaded(UUID uuid) {
		return playerCache.isLoaded(uuid);
	}

	@Override
	public void loadPlayer(UUID uuid) {
		playerCache.loadPlayer(uuid);
	}

	@Override
	public void unloadPlayer(UUID uuid) {
		playerCache.unloadPlayer(uuid);
	}

	@Override
	public int getCredits(UUID uuid){
		try {
			CachedPlayer cachedPlayer = playerCache.getPlayer(uuid);
			return cachedPlayer.getCredits();
		} catch (PlayerNotLoadedException e) {
			return 0;
		}
	}

	@Override
	public boolean addCredits(UUID uuid, int amount){
		QueuedPlayer queuedPlayer = playerCache.getQueuedPlayer(uuid);
		if(queuedPlayer != null) {
			queuedPlayer.addCredits(amount);
			return true;
		}
		return false;
	}

	@Override
	public boolean takeCredits(UUID uuid, int amount) throws PlayerNotLoadedException {
		CachedPlayer cachedPlayer = playerCache.getPlayer(uuid);
		return cachedPlayer.takeCredits(amount);
	}

}
