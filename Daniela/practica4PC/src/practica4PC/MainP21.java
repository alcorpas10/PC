package practica4PC;

import java.util.ArrayList;
import java.util.Random;

class HebraConsumidor3 extends Thread{
private int id;
private int n, k;
private MonitorMultibufferSync monitorAlm;
private Random r;

public HebraConsumidor3(int i, int n, MonitorMultibufferSync a, Random ran, int k) {
	this.id = i;
	this.n =n;
	this.monitorAlm =a;
	this.k =k;
	r = ran;
}

public void run() {
	System.out.println("Consumidor: " +this.id);
	Producto[] p = null;
	int num =  0;
	int consumidos =0, res=0;
	for (int i=0; i<n; i++) {
		 num =  r.nextInt(10);
		 if(num==0) num=1;
		 if( consumidos + num <=n && num<=k)
			 consumidos+= num;
		 else
		 {
			 num = 1;
			 consumidos++;
		 }
		 
		if(consumidos<=n && num<=k)
		{	p = monitorAlm.consumir(num);
			res= p.length;
		}
		
		 if(res!=num) {
			 consumidos-=num;
			 consumidos+=res;
		 }
		
	}
}
}
class HebraProductor3 extends Thread{
private int id;
private int n, k;
private MonitorMultibufferSync monitorAlm;
private Random r;


public HebraProductor3(int i,int n, MonitorMultibufferSync a, Random ran, int k) {
	this.id = i;
	this.n = n;
	this.monitorAlm =a;
	this.k=k;
	r = ran;
}

public void run() {
	System.out.println("Productor: " +this.id);
	Producto [] p = null;
	int producidos=0, res=0;
	int num=0;
	for (int i=0; i<n; i++) {
		 num =  r.nextInt(10);
		 if(num==0) num=1;
		 if( producidos + num <=n && num<=k)
			 producidos+= num;
		 else
		 {
			 num = 1;
			 producidos++;
		 }
		 
		 if(producidos<=n && num<=k)
			res = monitorAlm.producir(num);
		 
		 if(res!=num) {
			 producidos-=num;
			 producidos+=res;
		 }
	}
}
}

public class MainP21 {

	public static void main(String[] args) {
        Random ran = new Random();
        int m = ran.nextInt(100); 
        int n = ran.nextInt(100); //Productos a producir
        int k = ran.nextInt(100); 
        if(k>=n)k=n/2;
        else if(k==0) k=1;
        
        
        
        MonitorMultibufferSync monitorAlm = new MonitorMultibufferSync(k);

        ArrayList<Thread> array = new ArrayList<Thread>(); 
        
        for (int i = 1; i <= m; i++) {
            Thread h1 = new HebraConsumidor3(i,n, monitorAlm, ran, k);
            Thread h2 = new HebraProductor3(i,n, monitorAlm, ran, k);
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
        System.out.println("Numero de threads M: " + m);
        System.out.println("Productos : " + n);
        System.out.println("Tamano de k: " + k);
        System.out.println("Producidos: " + monitorAlm.cantidadProducida());
        System.out.println("Consumidos: " + monitorAlm.cantidadConsumida());
        System.out.println("Cantidad de Productos totales: " + m*n);
    }
}
