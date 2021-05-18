import java.util.ArrayList;
import java.util.concurrent.Semaphore;

class HebraInc extends Thread{
	private Entero ent;
	private int id;
	private int n;
	private Semaphore sem;
	public HebraInc(int i, Entero e, int n,Semaphore s) {
		this.id = i;
		this.ent=e;
		this.n =n;
		this.sem=s;
	}

	public void run() {
		System.out.println("Hebra Ic: " +this.id);
		for (int i=0; i<n; i++)
			{
				try {
					sem.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ent.incrementar();
				sem.release();
			
			}
	}
}
class HebraDec extends Thread{
	private Entero ent;
	private int id;
	private int n;
	private  Semaphore sem;
	
	public HebraDec(int i, Entero e, int n, Semaphore s) {
		this.id = i;
		this.ent=e;
		this.n = n;
		this.sem=s;
	}

	public void run() {
		System.out.println("Hebra Dc: " +this.id);
		for (int i=0; i<n; i++)
			{
			try {
				sem.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				ent.decrementar();
				sem.release();
			}
	}
}

public class carrera {

	 public static void main(String[] args) {
	        Entero ent =  new Entero(0);
	        int M = Integer.parseInt(args[0]);
	        Semaphore sem = new Semaphore(1);
	        ArrayList<Thread> array = new ArrayList<>(2 * M);
	        int n = 10000;
	        for (int i = 1; i <= M; i++) {
	            Thread h1 = new HebraInc(i,ent, n, sem);
	            Thread h2 = new HebraDec(i,ent, n, sem);
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
