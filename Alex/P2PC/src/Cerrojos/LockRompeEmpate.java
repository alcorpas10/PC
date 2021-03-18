package Cerrojos;

public class LockRompeEmpate implements Cerrojo {
	private volatile int in [], last [];
	private int N;
	
	public LockRompeEmpate(int N) {
		this.N = N;
		in = new int [N + 1];
		last = new int [N + 1];
	}
	
	@Override
	public void takeLock(int i) {
		for (int j = 1; j <= N; j++) {
			in[i] = j;
			last[j] = i;
			for (int k = 1; k <= N; k++) {
				if (k != i)
					while ((in[k] >= in[i]) && (last[j] == i));
			}
		}
	}

	@Override
	public void releaseLock(int i) {
		in[i] = 0;
	}
}
