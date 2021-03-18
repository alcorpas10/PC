package practica1PC;

import java.util.ArrayList;
import java.util.Random;

class miHebra extends Thread{
	private MatrizCompartida matrizC;
	private MatrizCompartida matrizA;
	private MatrizCompartida matrizB;
	private int i;
	private int n;
	public miHebra(int i, MatrizCompartida a, MatrizCompartida b, MatrizCompartida c, int n) {
		this.i = i;
		this.matrizA =a;
		this.matrizB =b;
		this.matrizC =c;
		this.n = n;
	}

	public void run() {
		//System.out.println("Hebra: " +this.i);
		int v;
		
		for (int j=0; j<n; j++)
		{
			matrizC.modificar(i, j, 0);
			
			for (int k=0; k<n;k++) {
				
				v=matrizC.valor(i, j);
				matrizC.modificar(i, j, v + (matrizA.valor(i, k) *matrizB.valor(k,j)));
			}
		}
	}
}


public class Parte3 {

	private static ArrayList<Thread> list;
	public static void main(String[] args) {
		list = new ArrayList<Thread>(); 
        Random ran = new Random();  
        int n = ran.nextInt(10); 

        MatrizCompartida a = new MatrizCompartida(null,n, true, ran);
        MatrizCompartida b = new MatrizCompartida(null,n, true, ran);
        MatrizCompartida c = new MatrizCompartida(new int[n][n],n, false, ran);
		for(int i=0; i<n ; i++) {
			Thread h = new miHebra(i,a,b, c, n);
			list.add(h);
			h.start();
			
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
		System.out.println("Result: " + "\n"+ c);
	}
	
	

}