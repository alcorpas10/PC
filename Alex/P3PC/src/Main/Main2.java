package Main;

import java.util.ArrayList;
import Semaforos.AlmacenSem;
import Threads.Consumidor;
import Threads.Productor;

public class Main2 {
	public static void main(String[] args) {
		int N = Integer.parseInt(args[0]);
		int P = Integer.parseInt(args[1]);
		int C = Integer.parseInt(args[2]);
		int numProductos = Integer.parseInt(args[3]);
		AlmacenSem alm = new AlmacenSem(N);
		ArrayList<Thread> array = new ArrayList<>(P + C);
		int numProductoHilo = numProductos / P;
		int numDeSobra = numProductos % P;
		for (int i = 0; i < P; i++) {
			Thread t;
			if (i < numDeSobra)
				t = new Productor(numProductoHilo + 1, alm);
			else
				t = new Productor(numProductoHilo, alm);
			array.add(t);
			t.start();
		}
		numProductoHilo = numProductos / C;
		numDeSobra = numProductos % C;
		for (int i = 0; i < C; i++) {
			Thread t;
			if (i < numDeSobra)
				t = new Consumidor(numProductoHilo + 1, alm);
			else
				t = new Consumidor(numProductoHilo, alm);
			array.add(t);
			t.start();
		}
		for (Thread t : array) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(alm);
	}
}
