package Mensajes;

import java.util.ArrayList;

public class Informacion extends Mensaje {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	
	public Informacion(int tipo, String destino) {
		super(tipo, "", destino);
	}
	
	public Informacion(int tipo) {
		super(tipo, "", "");
	}

	@Override
	public ArrayList<String> getLista() {
		return null;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public int getPuerto() {
		return 0;
	}

	@Override
	public String getString() {
		return null;
	}
}
