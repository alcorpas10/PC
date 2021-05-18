import java.util.ArrayList;
import java.util.Random;

class HebraConsumidor2 extends Thread{
	private int id;
	private int n;
	private AlmacenSem2 alm;
	
	public HebraConsumidor2(int i, int n, AlmacenSem2 a) {
		this.id = i;
		this.n =n;
		this.alm =a;
	}

	public void run() {
		System.out.println("Consumidor: " +this.id);
		Producto p = null;
		for (int i=0; i<n; i++) {
			p = alm.extraer();
			System.out.println("Obtenemos: " +p.toString());
		}
	}
}
class HebraProductor2 extends Thread{
	private int id;
	private int n;
	private AlmacenSem2 alm;
	
	public HebraProductor2(int i,int n, AlmacenSem2 a) {
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
			System.out.println("Almacenamos: " +p.toString());
		}
	}
}
public class productorConsumidor2p {

	public static void main(String[] args) {
        Random ran = new Random();
        int m = ran.nextInt(20); 
        int n = ran.nextInt(20); //Productos a producir
        int k = ran.nextInt(10);
        if(k>=n)k=n/2;
        
	 	AlmacenSem2 almacemS = new  AlmacenSem2(k);

        ArrayList<Thread> array = new ArrayList<Thread>(); 
        
        for (int i = 1; i <= m; i++) {
            Thread h1 = new HebraConsumidor2(i,n, almacemS);
            Thread h2 = new HebraProductor2(i,n, almacemS);
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
        System.out.println("Tamano de k: " + k);
        System.out.println("Producidos: " + almacemS.cantidadProducida());
        System.out.println("Consumidos: " + almacemS.cantidadConsumida());
        System.out.println("Cantidad de Productos totales: " + m*n);
    }
}
