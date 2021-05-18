package Concurrencia;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorReadersWritersLC {

	//Readers-Writers
	private final Lock l = new ReentrantLock(true);
	private final Condition okToRead = l.newCondition();
	private final Condition okWrite = l.newCondition();
	
	private int nr, nw;
	
	public MonitorReadersWritersLC() {
		nr = 0;
		nw=0;
	}
	
	public void request_read() throws InterruptedException {
		l.lock();
		while(nw>0) 
			okToRead.await();
		nr = nr +1;
		l.unlock();
	}
	
	public void realese_read() {
		l.lock();
		nr = nr -1;
		if(nr==0)okWrite.signal(); //notify solo 1 escritor
		l.unlock();
	}
	
	public void request_write() throws InterruptedException {
		l.lock();
		
		while(nr>0 || nw >0)
			okWrite.wait();
		
		nw = nw+1;
		l.unlock();
	}
	
	public void realese_write() {
		l.lock();
		nw = nw-1;
		okWrite.signal();
		okToRead.signalAll();
		l.unlock();
	}
}
