package Concurrencia;

import java.util.concurrent.Semaphore;

public class ReadersArchivos extends Thread {

	//Paso de testigo -->lectura entre clientes
	
	private Semaphore r;
	private Semaphore w;
	private Semaphore e;
	
	private InfoReadersWriters info;

	public ReadersArchivos( Semaphore r,Semaphore w,Semaphore e, InfoReadersWriters info) { 

		this.r=r; this.w=w; this.e=e;
		this.info=info;
	}
	
	public void run(){
		
		try {
			
			//no while, solo quiere leer archivos ==> tamano de la lista de archviso???
			e.acquire(); //Consultar
			if(info.numWriters()>0) {
				info.addWriterWaiting();
				e.release();
				r.acquire();
			}
			
			info.addReader();
			if(info.numReadersWaiting()>0) {
				info.subReaderWaiting();
				r.release();
			}
			else e.release();
			
			
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}
