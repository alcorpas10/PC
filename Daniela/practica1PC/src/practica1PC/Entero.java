package practica1PC;

public class Entero {

	private int ent;
	
	public Entero(int valor) {
		this.ent=valor;
	}
	
	public void incrementar() {
		this.ent++;
	}
	public void decrementar() {
		this.ent--;
	}
	
	public String print() {
		return Integer.toString(ent);
	}
}
