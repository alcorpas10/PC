package practica4PC;

import java.util.ArrayList;
import java.util.Random;


class HebraConsumidor2 extends Thread{
private int id;
private int n;
private MonitorAlmacen monitorAlm;

public HebraConsumidor2(int i, int n, MonitorAlmacen a) {
	this.id = i;
	this.n =n;
	this.monitorAlm =a;
}

public void run() {
	System.out.println("Consumidor: " +this.id);
	Producto p = null;
	for (int i=0; i<n; i++) {
		p = monitorAlm.consumir();
	}
}
}
class HebraProductor2 extends Thread{
private int id;
private int n;
private MonitorAlmacen monitorAlm;


public HebraProductor2(int i,int n, MonitorAlmacen a) {
	this.id = i;
	this.n = n;
	this.monitorAlm =a;
}

public void run() {
	System.out.println("Productor: " +this.id);
	for (int i=0; i<n; i++) {
		monitorAlm.producir();
		
	}
}
}
public class MainProdCon {

	public static void main(String[] args) {
        Random ran = new Random();
        int m = ran.nextInt(20000); 
        int n = ran.nextInt(1000); //Productos a producir
        int k = ran.nextInt(10);
        if(k>=n)k=n/2;
        
        MonitorAlmacen monitorAlm = new MonitorAlmacen(k);

        ArrayList<Thread> array = new ArrayList<Thread>(); 
        
        for (int i = 1; i <= m; i++) {
            Thread h1 = new HebraConsumidor2(i,n, monitorAlm);
            Thread h2 = new HebraProductor2(i,n, monitorAlm);
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
        System.out.println("Producidos: " + monitorAlm.cantidadProducida());
        System.out.println("Consumidos: " + monitorAlm.cantidadConsumida());
        System.out.println("Cantidad de Productos totales: " + m*n);
    }
}
