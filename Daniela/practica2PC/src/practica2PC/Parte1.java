package practica2PC;

import java.util.ArrayList;
import java.util.Random;

class Hebra1 extends Thread{
	private Entero ent;
	private int id;
	private int n;
	private VariableSicronizacion vs;
	public Hebra1(int i, Entero e, int n,VariableSicronizacion v) {
		this.id = i;
		this.ent=e;
		this.n =n;
		this.vs = v;
	}

	public void run() {
		System.out.println("Hebra Ic: " +this.id);
		for (int i=0; i<n; i++)
			{
				this.vs.lock1();
				ent.incrementar();
				this.vs.unlock1();
			
			}
	}
}
class Hebra2 extends Thread{
	private Entero ent;
	private int id;
	private int n;
	private VariableSicronizacion vs;
	
	public Hebra2(int i, Entero e, int n, VariableSicronizacion v) {
		this.id = i;
		this.ent=e;
		this.n = n;
		this.vs = v;
	}

	public void run() {
		System.out.println("Hebra Dc: " +this.id);
		for (int i=0; i<n; i++)
			{
				this.vs.lock2();
				ent.decrementar();
				this.vs.unlock2();
			}
	}
}

public class Parte1 {

	public static void main(String[] args) {
		ArrayList<Thread> list = new ArrayList<Thread>(); 
        Random ran = new Random(); 
        //int m = ran.nextInt(10); 
        int n = ran.nextInt(100000000);
        
        VariableSicronizacion vs = new VariableSicronizacion (false,false, 0);
        Entero ent = new Entero(0);
		for(int i=1; i<=1 ; i++) {
			
			Thread h1 = new Hebra1(i, ent,n, vs);
			Thread h2 = new Hebra2(i, ent, n, vs);
			list.add(h1);
			h1.start();
			list.add(h2);
			h2.start();
			
		}
		
		for(Thread h: list ) {

			try {
				h.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Main is done");
		System.out.println("Result: " + ent.print());
	}
}
