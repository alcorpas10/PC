package Utils;

public class Entero {
	private int i;
	private int p = 1, m = 1;
	public Entero() {
		i = 0;
	}
	
	private synchronized void inc() {
		i++;
	}
	
	private synchronized void dec() {
		i--;
	}
	
	public synchronized void incrementar() {
		while(p == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		p--;
		inc();
		p++;
		notify();
	}
	public synchronized void decrementar() {
		while(m == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		m--;
		dec();
		m++;
		notify();
	}

	@Override
	public String toString() {
		return "" + i;
	}
}
