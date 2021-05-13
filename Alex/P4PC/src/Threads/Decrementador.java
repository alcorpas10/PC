package Threads;

import Utils.Entero;

public class Decrementador extends Thread {
	private int n;
	private Entero ent;

	public Decrementador(int n, Entero ent) {
		this.n = n;
		this.ent = ent;
	}
	
	public void run() {
		for (int i = 0; i < n; i++) {
			ent.decrementar();
		}
	}
}
