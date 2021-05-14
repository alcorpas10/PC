package Mensajes;

import java.util.ArrayList;

public class Conexion extends Mensaje {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private ArrayList<String> lista;

	public Conexion(int tipo, String id, String ip, ArrayList<String> lista) {
		super(tipo, ip, "");
		this.id = id;
		this.lista = lista;
	}
	
	public Conexion(int tipo, String id, String ip) {
		super(tipo, ip, "");
		this.id = id;
	}

	@Override
	public ArrayList<String> getLista() {
		return lista;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getNomArchivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPuerto() {
		// TODO Auto-generated method stub
		return 0;
	}

}
