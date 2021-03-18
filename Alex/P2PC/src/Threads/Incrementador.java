package Threads;

import Cerrojos.Cerrojo;
import Utils.Entero;

public class Incrementador extends Thread {
	private int id;
	private int n;
	private Entero ent;
	private Cerrojo alg;
	
	public Incrementador(int id, int n, Entero ent, Cerrojo alg) {
		this.id = id;
		this.n = n;
		this.ent = ent;
		this.alg = alg;
	}
	
	public void run() {
		for (int i = 0; i < n; i++) {
			alg.takeLock(id);
			ent.incrementar();
			alg.releaseLock(id);
		}
	}
}
