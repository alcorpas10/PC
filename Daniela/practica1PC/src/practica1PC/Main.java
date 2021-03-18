package practica1PC;

import java.util.ArrayList;

class Hebra extends Thread{
	private String id;
	private int t;

	public Hebra(String id, int T) {
		this.id = id;
		this.t = T;
	}

	public void run() {
		System.out.println("Hebra: " +this.id);
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Hebra: " +this.id);
	}
}

/*
public class Main {

	private static ArrayList<Thread> list;
	public static void main(String[] args) {
		list = new ArrayList<Thread>(); 
		int T =  Integer.valueOf(args[1]);
		int n =  Integer.valueOf(args[0]);
		for(int i=1; i<= n; i++) {
		
			
			Thread h1 = new Hebra("Hebra "+i, T);
			list.add(h1);
			h1.start();
			
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
	}
}
*/
