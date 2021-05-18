import java.util.concurrent.Semaphore;


public class AlmacenSem2 implements Almacen{

	private Semaphore empty;
	private Semaphore full;
	private Semaphore mutexP;
	private Semaphore mutexC;

	private ProductoVolatil buf [];
	private int k, ini, fin;
	
	private int cantidad_producida=0;
	private int cantidad_consumida=0;
	
	public AlmacenSem2(int k) {
		this.k = k;
		buf = new ProductoVolatil[k+1];
		ini = 0;
		fin=0;
		
		empty = new Semaphore(k);
		full = new Semaphore(0);
		mutexP = new Semaphore(1);
		mutexC = new Semaphore(1);
	}
	
	
	
	@Override
	public void almacenar(Producto producto) {
		
		try {
			empty.acquire();
			mutexP.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		buf[fin] = new ProductoVolatil(producto);
		System.out.println("Almacenamos: " +producto.toString());
		cantidad_producida+=1;
		
		fin = (fin+1)%k;
		mutexP.release();
		
		full.release();
	}

	@Override
	public Producto extraer() {
		Producto pr;
		try {
			full.acquire();
			
			mutexC.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		pr = buf[ini].getValue();
		System.out.println("Obtenemos: " +pr.toString());
		cantidad_consumida+=1;
		
		ini =(ini+1)%k;
		mutexC.release();
		
		empty.release();
		
		return pr;
	}
	
	public int cantidadProducida() {
		return this.cantidad_producida;
	}
	
	public int cantidadConsumida() {
		return this.cantidad_consumida;
	}

}
