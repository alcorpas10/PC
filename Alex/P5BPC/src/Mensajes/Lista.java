package Mensajes;

import java.util.ArrayList;

public class Lista extends Mensaje {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> lista;
	
	public Lista(int tipo, String origen, String destino, ArrayList<String> lista) {
		super(tipo, origen, destino);
		this.lista = lista;
	}
	
	public Lista(int tipo) {
		super(tipo, "", "");
	}

	@Override
	public ArrayList<String> getLista() {
		return lista;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public String getNomArchivo() {
		return null;
	}

	@Override
	public int getPuerto() {
		return 0;
	}
}
