package me.rampen88.credits.storage.cache;

public class PlayerNotLoadedException extends RuntimeException {

	PlayerNotLoadedException() {
		super("Tried to access credits of a player that was not loaded.");
	}
}
