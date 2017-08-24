package me.rampen88.credits.storage.cache;

import java.util.concurrent.atomic.AtomicInteger;

public class QueuedPlayerImpl implements QueuedPlayer{

	private final AtomicInteger credits = new AtomicInteger(0);

	public int getCredits(){
		return credits.get();
	}

	public void addCredits(int amount){
		credits.addAndGet(amount);
	}
}
