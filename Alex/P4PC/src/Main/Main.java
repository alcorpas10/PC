package Main;

import java.util.ArrayList;
import Threads.Decrementador;
import Threads.Incrementador;
import Utils.Entero;

public class Main {
	public static void main(String[] args) {
		Entero ent = new Entero();
		int M = Integer.parseInt(args[0]);
		ArrayList<Thread> array = new ArrayList<>(2 * M);
		int n = 10000;
		for (int i = 1; i <= M; i++) {
			Thread h1 = new Incrementador(n, ent);
			Thread h2 = new Decrementador(n, ent);
			array.add(h1);
			array.add(h2);
			h1.start();
			h2.start();
		}
		for (Thread h : array) {
			try {
				h.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(ent);
	}
}
