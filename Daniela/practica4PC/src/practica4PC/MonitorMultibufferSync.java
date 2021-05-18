package practica4PC;

public class MonitorMultibufferSync {


	private Producto buf [];
	private int k, ini, fin;
	private int count; 
	private int cantidad_producida;
	private int cantidad_consumida;
	
	public MonitorMultibufferSync(int k) {
		this.k = k;
		buf = new Producto[k];
		ini = 0;
		fin=0;
		count=0;
		cantidad_producida=0;
		cantidad_consumida=0;

	}

	
	public  synchronized int producir( int num) {
		
		int producidos=0;
		if(count==k) {
		while (count + num > k)
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(count + num>k) //quiere producir mas de lo que puede
		{	producidos = k -count;
				num = producidos;
		}
			
		
		
		for(int i=0;i< num; i++) {
			Producto p = new Producto();
			buf[fin]= p;
			cantidad_producida++;
			fin = (fin+1)%k;
			count++;
			System.out.println("Producimos : " +p.toString());
		}
		notifyAll();
		return num;
	}


	public synchronized Producto[]  consumir(int num) {
		
		int cosumidos=0;
		if(count==0) {
		while (count - num < 0)
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(count - num < 0) //quiere consumir mas de lo que puede
		{	cosumidos = count;
			num = cosumidos;
		}
		
		Producto pr [] = new Producto[num ];

		for(int i=0;i< num; i++) {
			pr[i] = buf[ini];
			cantidad_consumida++;
			ini =(ini+1)%k;
			count--;
			System.out.println("Consumimos : " +pr[i].toString());
		}
		notifyAll();
		
		return pr;
	}
	
	public synchronized int cantidadProducida() {
		return this.cantidad_producida;
	}
	
	public synchronized int cantidadConsumida() {
		return this.cantidad_consumida;
	}
}
