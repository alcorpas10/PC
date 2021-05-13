package Threads;

import Semaforos.AlmacenSem;

public class Consumidor extends Thread {

	private int numProductos;
	private AlmacenSem alm;
	
	public Consumidor(int numProductos, AlmacenSem alm) {
		this.numProductos = numProductos;
		this.alm = alm;
	}
	
	public void run() {
		for (int i = 0; i < numProductos; i++) {
			alm.extraer();
		}
	}
}
