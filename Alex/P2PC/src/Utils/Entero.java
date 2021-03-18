package Utils;

public class Entero {
	private volatile int i;
	
	public Entero() {
		i = 0;
	}
	
	public void incrementar() {
		i++;
	}
	public void decrementar() {
		i--;
	}

	@Override
	public String toString() {
		return "" + i;
	}
}
