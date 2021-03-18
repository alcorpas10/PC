package Cerrojos;
import java.util.concurrent.atomic.AtomicInteger;

public class LockTicket implements Cerrojo {
	private int turn[]; //No hace falta volatile
	private AtomicInteger number;
	private volatile int next;
	
	public LockTicket(int N) {
		turn = new int[N + 1];
		number = new AtomicInteger(1);
		next = 1;
	}
	
	@Override
	public void takeLock(int i) {
		turn[i] = number.getAndIncrement();
		while(turn[i] != next);
	}

	@Override
	public void releaseLock(int i) {
		next = next + 1;
	}	
}
