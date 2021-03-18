import java.util.ArrayList;
import java.util.Random;

class Entero {
	private int i;
	
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

class Incrementador extends Thread{
	private int n;
	private Entero ent;
	public Incrementador(int n, Entero ent) {
		this.n = n;
		this.ent = ent;
	}
	public void run() {
		for (int i = 0; i < n; i++)
			ent.incrementar();
	}
}

class Decrementador extends Thread{
	private int n;
	private Entero ent;
	public Decrementador(int n, Entero ent) {
		this.n = n;
		this.ent = ent;
	}
	public void run() {
		for (int i = 0; i < n; i++)
			ent.decrementar();
	}
}

public class Main {
	
	public static void main(String[] args) {
		Entero ent = new Entero();
		int n = new Random().nextInt(100);
		ArrayList<Thread> array = new ArrayList<>();
		for (int i = 0; i < Integer.parseInt(args[0]); i++) {
			Thread h1 = new Incrementador(n, ent);
			Thread h2 = new Decrementador(n, ent);
			array.add(h1);
			array.add(h2);
			h1.start();
			h2.start();
		}
		for (Thread h : array) {
			try {
				h.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(ent);
	}

}
