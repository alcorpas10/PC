package practica4PC;

public class miMonitorEntero {

private int x, y;
private int entero;

	public miMonitorEntero() {
		x=1;y=1;
		entero=0;
	}
	
	private synchronized void inc() {
		entero++;
	}
	
	private synchronized void dec() {
		entero--;
	}
	
	public synchronized void incrementarMonitor() {
		while(x==0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		x--;
		inc();
		x++;
		notifyAll();
	} 
	public synchronized  void decrementarMonitor() {
		while(y==0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		y--;
		dec();
		y++;
		notifyAll();
	}
	
	public synchronized String print() {
		return Integer.toString(entero);
	}
}
