package Mensajes;

import java.util.ArrayList;

import Utils.InfoUsuario;

public class Lista extends Mensaje {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<InfoUsuario> lista;
	
	public Lista(int tipo, String origen, String destino, ArrayList<InfoUsuario> lista) {
		super(tipo, origen, destino);
		this.lista = lista;
	}
	
	public Lista(int tipo) {
		super(tipo, "", "");
	}

	@Override
	public ArrayList<InfoUsuario> getLista() {
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
	public String getIP() {
		return null;
	}

	@Override
	public int getPuerto() {
		return 0;
	}
}
