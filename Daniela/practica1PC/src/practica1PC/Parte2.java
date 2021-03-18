package practica1PC;

import java.util.ArrayList;
import java.util.Random;

class HebraIn extends Thread{
	private Entero ent;
	private int id;
	private int n;
	public HebraIn(int i, Entero e, int n) {
		this.id = i;
		this.ent=e;
		this.n =n;
	}

	public void run() {
		System.out.println("Hebra Ic: " +this.id);
		for (int i=0; i<n; i++)
			ent.incrementar();
	}
}
class HebraDc extends Thread{
	private Entero ent;
	private int id;
	private int n;
	public HebraDc(int i, Entero e, int n) {
		this.id = i;
		this.ent=e;
		this.n = n;
	}

	public void run() {
		System.out.println("Hebra Dc: " +this.id);
		for (int i=0; i<n; i++)
			ent.decrementar();
	}
}

public class Parte2 {

	private static ArrayList<Thread> list;
	public static void main(String[] args) {
		list = new ArrayList<Thread>(); 
        Random ran = new Random(); 
        int m = ran.nextInt(10); 
        int n = ran.nextInt(1000);

        Entero ent = new Entero(0);
		for(int i=1; i<=m ; i++) {
			
			Thread h1 = new HebraIn(i, ent,n);
			Thread h2 = new HebraDc(i, ent, n);
			list.add(h1);
			h1.start();
			list.add(h2);
			h2.start();
			
		}
		
		for(Thread h: list ) {

			try {
				h.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Main is done");
		System.out.println("Result: " + ent.print());
	}
}
