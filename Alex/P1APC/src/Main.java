import java.util.ArrayList;
import java.util.Random;

class Hebra extends Thread {
	private int id;
	public Hebra(int id) {
		this.id = id;
	}
	public void run() {
		System.out.println("Inicia: " + id);
		try {
			sleep(new Random().nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Fin: " + id);
	}
}

public class Main {
	public static void main(String[] args) {
		ArrayList<Hebra> array = new ArrayList<>();
		for (int i = 0; i < Integer.parseInt(args[0]); i++) {
			Hebra h = new Hebra(i);
			array.add(h);
			h.start();
		}
		for (Hebra h : array) {
			try {
				h.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Fin del programa");
	}
}
