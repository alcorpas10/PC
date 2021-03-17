package practica2C;

import java.util.ArrayList;
import java.util.Random;

class HebraInc extends Thread{
	private Entero ent;
	private int id;
	private int n;
	private LockRompeEmpate cerrojo;
	public HebraInc(int i, Entero e, int n,LockRompeEmpate c) {
		this.id = i;
		this.ent=e;
		this.n =n;
		this.cerrojo=c;
	}

	public void run() {
		System.out.println("Hebra Ic: " +this.id);
		for (int i=0; i<n; i++)
			{
				cerrojo.takeLock(id);
				ent.incrementar();
				cerrojo.releaseLock(id);
			
			}
	}
}
class HebraDec extends Thread{
	private Entero ent;
	private int id;
	private int n;
	private  LockRompeEmpate cerrojo;
	
	public HebraDec(int i, Entero e, int n, LockRompeEmpate c) {
		this.id = i;
		this.ent=e;
		this.n = n;
		this.cerrojo=c;
	}

	public void run() {
		System.out.println("Hebra Dc: " +this.id);
		for (int i=0; i<n; i++)
			{
				cerrojo.takeLock(id);
				ent.decrementar();
				cerrojo.releaseLock(id);
			}
	}
}

public class Parte2 {

	public static void main(String[] args) {
		ArrayList<Thread> list = new ArrayList<Thread>(); 
        Random ran = new Random(); 
        int m = ran.nextInt(100); 
        int n = ran.nextInt(20);
        
        System.out.println("M:"+m);
        System.out.println("N:"+n);
        //ACTUALIZAR
        LockRompeEmpate cerrojo = new LockRompeEmpate (2*m);
        Entero ent = new Entero(0);
		for(int i=1; i<=m ; i++) {
			
			Thread h1 = new HebraInc(i, ent, n, cerrojo);
			list.add(h1);
			h1.start();
			
		}
		for(int i=m+1; i<=2*m ; i++) {
			
			Thread h2 = new HebraDec(i, ent, n, cerrojo);
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
