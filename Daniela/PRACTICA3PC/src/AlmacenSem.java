import java.util.concurrent.Semaphore;

public class AlmacenSem implements Almacen{

	private  Semaphore empty;
	private  Semaphore full;
	private int cantidad_producida=0;
	private int cantidad_consumida=0;
	private Producto buf ;
	
	public AlmacenSem() {
		empty = new Semaphore(1);
		full = new Semaphore(0);
		cantidad_producida=0;
		cantidad_consumida=0;
	}

	
	
	@Override
	public void almacenar(Producto producto) {
		try {
			empty.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.buf = producto;
		System.out.println("Almacenamos: " +producto.toString());
		full.release();
		cantidad_producida+=1;
	}

	@Override
	public Producto extraer() {
		Producto sol=null;
		
		try {
			full.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sol = buf;
		System.out.println("Obtenemos: " +sol.toString());
		empty.release();
		cantidad_consumida+=1;
		return sol;
	}
	
	public int cantidadProducida() {
		return this.cantidad_producida;
	}
	
	public int cantidadConsumida() {
		return this.cantidad_consumida;
	}

}
