import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

class HebraConsumidor extends Thread{
	private int id;
	private int n;
	private AlmacenSem alm;
	
	public HebraConsumidor(int i, int n, AlmacenSem a) {
		this.id = i;
		this.n =n;
		this.alm =a;
	}

	public void run() {
		System.out.println("Consumidor: " +this.id);
		Producto p = null;
		for (int i=0; i<n; i++) {
			p = alm.extraer();
			
		}
	}
}
class HebraProductor extends Thread{
	private int id;
	private int n;
	private AlmacenSem alm;
	
	public HebraProductor(int i,int n, AlmacenSem a) {
		this.id = i;
		this.n = n;
		this.alm =a;
	}

	public void run() {
		System.out.println("Productor: " +this.id);
		Producto p = null;
		for (int i=0; i<n; i++) {
			p = new Producto();
			alm.almacenar(p);
			
		}
	}
}

public class productorConsumidor1P {

	 public static void main(String[] args) {
	        Random ran = new Random();
		 	AlmacenSem almacemS = new  AlmacenSem();
	        int m = ran.nextInt(20); 
	        int n = ran.nextInt(20); //Productos a producir
	        ArrayList<Thread> array = new ArrayList<Thread>(); 
	        
	        for (int i = 1; i <= m; i++) {
	            Thread h1 = new HebraConsumidor(i,n, almacemS);
	            Thread h2 = new HebraProductor(i,n, almacemS);
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
	        
	        System.out.println("Producidos: " + almacemS.cantidadProducida());
	        System.out.println("Consumidos: " + almacemS.cantidadConsumida());
	        System.out.println("Cantidad de Productos totales: " + m*n);
	    }
}
