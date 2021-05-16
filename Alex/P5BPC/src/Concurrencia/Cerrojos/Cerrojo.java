package Concurrencia.Cerrojos;

public interface Cerrojo {
	public void takeLock(int i);
	public void releaseLock(int i);
}
