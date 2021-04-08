package Threads;

import Semaforos.AlmacenSem;
import Utils.Producto;

public class Productor extends Thread {
	
	private int numProductos;
	private AlmacenSem alm;
	
	public Productor(int numProductos, AlmacenSem alm) {
		this.numProductos = numProductos;
		this.alm = alm;
	}
	
	public void run() {
		for (int i = 0; i < numProductos; i++) {
			alm.almacenar(new Producto());
		}
	}
}
