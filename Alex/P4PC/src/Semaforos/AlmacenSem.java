package Semaforos;

import Utils.Producto;

public class AlmacenSem implements Almacen {

	Producto buf[];
	int cont;
	int N, ini, fin, contCreados, contExtraidos;
	
	public AlmacenSem(int N) {
		this.N = N;
		buf = new Producto[N];
		cont = 0;
		ini = 0;
		fin = 0;
		contCreados = 0;
		contExtraidos = 0;
	}
	
	@Override
	public synchronized void almacenar(Producto producto) {
		while(cont == N) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		buf[fin] = producto;
		fin = (fin + 1) % N;
		contCreados++;
		System.out.println(producto + " creado");
		cont++;
		notifyAll();
	}

	@Override
	public synchronized Producto extraer() {
		while(cont == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Producto producto = buf[ini];
		ini = (ini + 1) % N;
		contExtraidos++;
		System.out.println(producto + " extraido");
		cont--;
		notifyAll();
		return producto;
	}

	@Override
	public String toString() {
		return "AlmacenSem [contCreados=" + contCreados + ", contExtraidos=" + contExtraidos + "]";
	}
}
