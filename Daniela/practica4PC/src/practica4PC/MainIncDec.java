package practica4PC;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class HebraInc extends Thread{
	//private Entero ent;
	private int id;
	private int n;
	private miMonitorEntero miMonitor;
	//private AlgoritmoCerrojos cerrojo;
	public HebraInc(int i, int n, miMonitorEntero m) {
		this.id = i;
		///this.ent=e;
		this.n =n;
		miMonitor=m;
		//this.cerrojo=c;
	}

	public void run() {
		System.out.println("Hebra Ic: " +this.id);
		for (int i=0; i<n; i++)
			miMonitor.incrementarMonitor();
	}
}
class HebraDec extends Thread{
	//private Entero ent;
	private int id;
	private int n;
	private miMonitorEntero miMonitor;
	
	public HebraDec(int i,  int n,  miMonitorEntero m) {
		this.id = i;
		//this.ent=e;
		this.n = n;
		miMonitor=m;
	}

	public void run() {
		System.out.println("Hebra Dc: " +this.id);
		for (int i=0; i<n; i++)
			miMonitor.decrementarMonitor();
	}
}
public class MainIncDec {

	public static void main(String[] args) {
		ArrayList<Thread> list = new ArrayList<Thread>(); 
        Random ran = new Random(); 
        int m = ran.nextInt(10000); 
        int n = ran.nextInt(10000);
        
    
        System.out.println("Numero de threads M :"+m);
        System.out.println("Numero de incrementos/decremenetos N:"+n);
        
        miMonitorEntero monitor = new miMonitorEntero();

		for(int i=1; i<=m ; i++) {
			
			Thread h1 = new HebraInc(i, n, monitor);
			list.add(h1);
			h1.start();
			
		}
		for(int i=m+1; i<=2*m ; i++) {
			
			Thread h2 = new HebraDec(i, n, monitor);
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
		System.out.println("Result: " + monitor.print());
	}
	
	
}
