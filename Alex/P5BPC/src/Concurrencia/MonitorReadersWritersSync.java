package Concurrencia;

public class MonitorReadersWritersSync {
	private int nr, nw;
	
	public MonitorReadersWritersSync() {
		nr = 0;
		nw=0;
	}
	
	public synchronized void request_read() throws InterruptedException {
		while(nw>0) 
			wait();
		nr = nr +1;
		
	}
	
	public synchronized void release_read() {
		nr = nr -1;
		if(nr==0)
			notify();
	}
	
	public synchronized void request_write() throws InterruptedException {
		
		while(nr>0 || nw >0)
			wait();
		
		nw = nw+1;
		
	}
	
	public synchronized void release_write() {
		
		nw = nw-1;
		notifyAll();
	}


}
