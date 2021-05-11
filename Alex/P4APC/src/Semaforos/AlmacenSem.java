package Semaforos;

import java.util.concurrent.Semaphore;

import Utils.Producto;

public class AlmacenSem implements Almacen {

	Producto buf[];
	Semaphore empty, full, mutexP, mutexC;
	int N, ini, fin, contCreados, contExtraidos;
	
	public AlmacenSem(int N) {
		this.N = N;
		buf = new Producto[N];
		/*empty = new Semaphore(N);
		full = new Semaphore(0);
		mutexP = new Semaphore(1);
		mutexC = new Semaphore(1);*/
		ini = 0;
		fin = 0;
		contCreados = 0;
		contExtraidos = 0;
	}
	
	@Override
	synchronized public void almacenar(Producto producto) {
		/*try {
			empty.acquire();
			mutexP.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		buf[fin] = producto;
		fin = (fin + 1) % N;
		contCreados++;
		System.out.println(producto + " creado");
		/*mutexP.release();
		full.release();*/
	}

	@Override
	synchronized public Producto extraer() {
		/*try {
			full.acquire();
			mutexC.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		Producto producto = buf[ini];
		ini = (ini + 1) % N;
		contExtraidos++;
		System.out.println(producto + " extraido");
		/*mutexC.release();
		empty.release();*/
		return producto;
	}

	@Override
	public String toString() {
		return "AlmacenSem [contCreados=" + contCreados + ", contExtraidos=" + contExtraidos + "]";
	}
}
