
public class ProductoVolatil {

private volatile Producto p;
	
	public ProductoVolatil(Producto nuevo) {
		p = nuevo;
	}
	
	public void changeValue(Producto valor) {
		p = valor;
	}
	public Producto getValue() {
		return p;
	}
}
