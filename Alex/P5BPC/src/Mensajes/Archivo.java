package Mensajes;

import static Utils.Constantes.ARCHIVO;

import java.util.ArrayList;

import Utils.InfoUsuario;

public class Archivo extends Mensaje {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Archivo(String origen, String destino) {
		super(ARCHIVO, origen, destino);
	}

	@Override
	public ArrayList<InfoUsuario> getLista() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNomArchivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIP() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPuerto() {
		// TODO Auto-generated method stub
		return 0;
	}
}
