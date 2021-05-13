package Cerrojos;

public class LockBakery implements Cerrojo {
	private int N;
	private volatile int turn[];
	
	public LockBakery(int N) {
		this.N = N;
		turn = new int [N + 1];
	}
	
	@Override
	public void takeLock(int i) {
		turn[i] = 1;
		turn[i] = max(turn) + 1;
		for (int j = 1; j <= N; j++) {
			if (j != i)
				while (turn[j] != 0 && mayorMayor(turn[i], i, turn[j], j));
		}
	}

	@Override
	public void releaseLock(int i) {
		turn[i] = 0;
	}
	
	private boolean mayorMayor(int t1, int i, int t2, int j) {
		return (t1 > t2) || (t1 == t2 && i > j);
	}
	
	private int max(int[] array) {
		int maximo = 0;
		for (int i: array) {
			if (i > maximo)
				maximo = i;
		}
		return maximo;
	}
}
