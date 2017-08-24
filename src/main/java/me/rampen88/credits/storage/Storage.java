package me.rampen88.credits.storage;

import me.rampen88.credits.storage.cache.PlayerNotLoadedException;

import java.util.UUID;

public interface Storage {

	boolean isLoaded(UUID uuid);

	void loadPlayer(UUID uuid);

	void unloadPlayer(UUID uuid);

	int loadCredits(UUID uuid);

	int getCredits(UUID uuid);

	boolean addCredits(UUID uuid, int amount);

	boolean takeCredits(UUID uuid, int amount) throws PlayerNotLoadedException;

	void setCredits(UUID uuid, int amount);

	void importCredits();

}
