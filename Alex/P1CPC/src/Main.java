import java.util.ArrayList;
import java.util.Random;

class Matriz {
	private int M [][];
	int n;
	
	public Matriz(int n) {
		M = new int[n][n];
		Random rnd = new Random();
		this.n = n;
		for (int i = 0; i < n; i++) {
			for (int e = 0; e < n; e++)
				M[i][e] = rnd.nextInt(10);
		}
	}
	
	public Matriz(int n, int u) {
		M = new int[n][n];
		this.n = n;
		for (int i = 0; i < n; i++) {
			for (int e = 0; e < n; e++)
				M[i][e] = u;
		}
	}
	
	public int getElem(int i, int e) {
		return M[i][e];
	}
	
	public int setElem(int i, int e, int num) {
		return M[i][e] = num;
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < n; i++) {
			for (int e = 0; e < n; e++)
				s += M[i][e] + " ";
			s += "\n";
		}
		return s;
	}
}

class Calculador extends Thread{
	private int i, n;
	private Matriz A, B, C;
	public Calculador(int i, int n, Matriz A, Matriz B, Matriz C) {
		this.i = i;
		this.n = n;
		this.A = A;
		this.B = B;
		this.C = C;
	}
	public void run() {
		for (int j = 0; j < n; j++)
			for (int k = 0; k < n; k++)
				C.setElem(i, j, C.getElem(i, j) + A.getElem(i, k) * B.getElem(k, j));
	}
}

public class Main {
	public static void main(String[] args) {
		int n = new Random().nextInt(4) + 1;
		Matriz A = new Matriz(n), B = new Matriz(n), C = new Matriz(n, 0);
		ArrayList<Thread> array = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			Thread h = new Calculador(i, n, A, B, C);
			array.add(h);
			h.start();
		}
		for (Thread h : array) {
			try {
				h.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(C);
	}
}
