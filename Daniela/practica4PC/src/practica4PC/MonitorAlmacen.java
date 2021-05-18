package practica4PC;

import java.util.concurrent.Semaphore;

public class MonitorAlmacen {


	private Producto buf [];
	private int k, ini, fin;
	private int count; 
	private int cantidad_producida;
	private int cantidad_consumida;
	
	public MonitorAlmacen(int k) {
		this.k = k;
		buf = new Producto[k+1];
		ini = 0;
		fin=0;
		count=0;
		cantidad_producida=0;
		cantidad_consumida=0;

	}

	
	public  synchronized void producir() {
		
		while (count == k)
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		Producto p = new Producto();
		buf[fin]= p;
		cantidad_producida++;
		fin = (fin+1)%k;
		count++;
		System.out.println("Almacenamos: " +p.toString());
		notifyAll();
	
	}


	public synchronized Producto  consumir() {
		Producto pr=null;
		while (count == 0)
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		pr = buf[ini];
		cantidad_consumida++;
		ini =(ini+1)%k;
		count--;
		System.out.println("Obtenemos: " +pr.toString());
		notifyAll();
		
		return pr;
	}
	
	public synchronized int cantidadProducida() {
		return this.cantidad_producida;
	}
	
	public synchronized int cantidadConsumida() {
		return this.cantidad_consumida;
	}

}
