package Threads;

import java.util.concurrent.Semaphore;

import Utils.Entero;

public class Decrementador extends Thread {
	//private int id;
	private int n;
	private Entero ent;
	private Semaphore sem;

	public Decrementador(/*int id, */int n, Entero ent, Semaphore sem) {
		//this.id = id;
		this.n = n;
		this.ent = ent;
		this.sem = sem;
	}
	
	public void run() {
		for (int i = 0; i < n; i++) {
			try {
				sem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ent.decrementar();
			sem.release();
		}
	}
}
