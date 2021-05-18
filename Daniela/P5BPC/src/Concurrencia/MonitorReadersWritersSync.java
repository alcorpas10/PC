package Concurrencia;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorReadersWritersSync {

	
	private int nr, nw;
	
	public MonitorReadersWritersSync() {
		//this.k = k;
		//infoUsuarios = new ArrayList<InfoUsuario>();
		nr = 0;
		nw=0;
		//count=0;
		//cantidad_producida=0;
		//cantidad_consumida=0;

	}
	
	public synchronized void request_read() throws InterruptedException {
		while(nw>0) 
			wait();
		nr = nr +1;
		
	}
	
	public synchronized void realese_read() {
		nr = nr -1;
		if(nr==0)
			notifyAll(); //notify solo 1 escritor????
	}
	
	public synchronized void request_write() throws InterruptedException {
		
		while(nr>0 || nw >0)
			wait();
		
		nw = nw+1;
		
	}
	
	public synchronized void realese_write() {
		
		nw = nw-1;
		notifyAll();
	}


}
