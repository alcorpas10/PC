package Main;

import java.util.ArrayList;
import java.util.Scanner;

import Cerrojos.Cerrojo;
import Cerrojos.LockBakery;
import Cerrojos.LockRompeEmpate;
import Cerrojos.LockTicket;
import Threads.Decrementador;
import Threads.Incrementador;
import Utils.Entero;

public class Main {
	public static void main(String[] args) {
		Entero ent = new Entero();
		int M = Integer.parseInt(args[0]);
		Cerrojo lock = creaCerrojo(M);
		ArrayList<Thread> array = new ArrayList<>(2 * M);
		int n = 10000;
		for (int i = 1; i <= M; i++) {
			Thread h1 = new Incrementador(i, n, ent, lock);
			Thread h2 = new Decrementador(i + M, n, ent, lock);
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
	
	public static Cerrojo creaCerrojo(int M) {
		System.out.print("Introduce el nombre del tipo de cerrojo que quieres crear (Ticket - Bakery - RompeEmpate): ");
        Scanner entrada = new Scanner(System.in);
        String tipo = entrada.nextLine();
        entrada.close();
        if (tipo.equals("Ticket"))
        	return new LockTicket(2 * M);
        else if (tipo.equals("Bakery"))
            return new LockBakery(2 * M);
        else 
            return new LockRompeEmpate(2 * M);
    }
}
