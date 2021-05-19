package Mensajes;

import java.util.ArrayList;

public class Lista extends Mensaje {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fila;
	private int numFila;
	private ArrayList<String> lista;
	
	public Lista(int tipo, String origen, String destino, ArrayList<String> lista) {
		super(tipo, origen, destino);
		this.lista = lista;
	}
	public Lista(int tipo, int  num, String fila) {
		super(tipo, "", "");
		this.fila = fila;
		this.numFila = num;
	}
	
	public Lista(int tipo, int  num) {
		super(tipo, "", "");
		this.numFila = num;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public int getNumero() {
		return numFila;
	}

	@Override
	public String getString() {
		return fila;
	}
	
	@Override
	public ArrayList<String> getLista() {
		return lista;
	}
	@Override
	public byte[] getBuffer() {
		return null;
	}
}
