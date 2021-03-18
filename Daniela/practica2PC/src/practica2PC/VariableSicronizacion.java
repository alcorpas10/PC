package practica2PC;

public class VariableSicronizacion {

private volatile  boolean  ini1, ini2;
private volatile  int last;

	public VariableSicronizacion(boolean ini1, boolean ini2, int last) {
		this.ini1=ini1;
		this.ini2=ini2;
		this.last=last;
	}
	
	private void setIni1(boolean v) {
		this.ini1=v;
	}
	private void setIni2(boolean v) {
		this.ini2=v;
	}
	
	private void setLast(int v) {
		this.last=v;
	}
	
	private boolean getIni1() {
		return this.ini1;
	}
	private boolean getIni2() {
		return this.ini2;
	}
	
	private int getLast() {
		return this.last;
	}
	
	public void lock1() {
		setIni1(true);
		setLast(1);
		while(getIni2() && getLast()==1);
	}
	
	public void unlock1() {
		setIni1(false);
	}
	public void lock2() {
		setIni2(true);
		setLast(2);
		while(getIni1() && getLast()==2);
	}
	
	public void unlock2() {
		setIni2(false);
	}
}
