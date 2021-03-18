package practica2PC;

public class EnteroVolatil {

	private volatile int ent;
	
	public EnteroVolatil(int valor) {
		this.ent=valor;
	}
	
	public void changeValue(int v) {
		this.ent=v;
	}
	public int getValue() {
		return ent;
	}
	

}
