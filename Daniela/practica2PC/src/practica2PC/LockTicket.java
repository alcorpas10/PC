package practica2PC;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class LockTicket implements AlgoritmoCerrojos{

	private  int [] turn;
	private AtomicInteger number;
	private volatile int next;
	private int n;
	public LockTicket(int n) {
		this.n = n;
		turn = new int[n+1];
		Arrays.fill(turn, 0);
		number= new AtomicInteger(1);
		next= 1;
	}
	
	
	public void takeLock(int i) {
		turn[i] = number.getAndIncrement();
		while(turn[i]!=next);

	}
	
	public void releaseLock(int i) {
		next=next+1;
	}
	
}
