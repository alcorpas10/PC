package practica1PC;

import java.util.Random;

public class MatrizCompartida {

	private int [][] matriz;
	private int n;
	
	public MatrizCompartida(int [][] ini,int n, boolean crear, Random r) {
		
		this.n=n;
		
		if(!crear)
			this.matriz=ini;
		else
			this.matriz = generateRandomMatriz(n, r);
	}
	
	public void modificar(int i, int j, int valor) {
		matriz[i][j] = valor;
	}
	
	public int valor(int i, int j) {
		return matriz[i][j];
		
	}
	
	public String toString() {
		String s="";
		for(int i=0; i<n ; i++) {
			
			for(int j=0; j<n ; j++)
				s += matriz[i][j] + " ";
			
			s+="\n";
		}
		return s;
	}
	
	private int [][] generateRandomMatriz(int n, Random r) {
		  int[][] m =new int[n][n];
		  for(int i=0;i<n;i++)
		      for(int j=0;j<n;j++)
		         m[i][j]=r.nextInt(10);     
		 return m;
	}
}
