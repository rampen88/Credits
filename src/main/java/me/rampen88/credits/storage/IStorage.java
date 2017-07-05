package me.rampen88.credits.storage;

import java.util.UUID;

public interface IStorage {

	int getCredits(UUID uuid);

	boolean addCredits(UUID uuid, int amount);

	boolean takeCredits(UUID uuid, int amount);

	void importCredits();


}
