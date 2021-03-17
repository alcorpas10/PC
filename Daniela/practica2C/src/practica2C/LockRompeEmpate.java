package practica2C;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class LockRompeEmpate {

private volatile int ini [];
private volatile int last[] ;
private int n;
	public LockRompeEmpate(int n) {
		this.n = n;
		ini = new int[n+1];
		last = new int[n+1];
		Arrays.fill(ini, 0);
		Arrays.fill(last, 0);
	}
	

	
	public void takeLock(int i) {
		for(int j =1; j<=n; j++) {
			ini[i]=j;
			last[j]=i;
			for(int k=1; k<=n; k++) 
				if(k!=i) 
					while(ini[k]>=ini[i] && last[j]==i);
				
			
		}
	}
	
	public void releaseLock(int i) {
		ini[i]=0;
	}
	
}
