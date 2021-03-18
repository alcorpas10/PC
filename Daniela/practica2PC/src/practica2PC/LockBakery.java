package practica2PC;

import java.util.Arrays;

public class LockBakery implements AlgoritmoCerrojos {

	private EnteroVolatil [] turn;
	private int n;
	public LockBakery(int n) {
		this.n = n;
		turn = new EnteroVolatil[n+1];
		fillEntero();
	}
	
	public void fillEntero() {
		for(int i=0; i<= n; i++)
				turn[i]=new EnteroVolatil(0);
	}
	
	public void takeLock(int i) {
		turn[i].changeValue(1);
		turn[i].changeValue(getMaxTurn()+1);
		for(int j =1; j<=n; j++) 
			if(j!=i) 
				while(turn[j].getValue()!=0 && opMayorMayor(turn[i].getValue(),i, turn[j].getValue(), j));
			
		
	}
	
	public void releaseLock(int i) {
		turn[i].changeValue(0);
	}
	
	public boolean opMayorMayor(int a,int b, int c, int d ){
		if(a>c)return true;
		else if(a==c && b>d)return true;
		else return false;
	}
	public int getMaxTurn() {
		int maxV = turn[1].getValue();
		for(int i=1; i<=n;i++) 
			if(maxV<turn[i].getValue())maxV=turn[i].getValue();
		return maxV;
	}
}
