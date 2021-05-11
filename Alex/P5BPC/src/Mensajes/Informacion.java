package Mensajes;

import java.util.ArrayList;

import Utils.InfoUsuario;

public class Informacion extends Mensaje {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Informacion(int tipo, String destino) {
		super(tipo, "", destino);
	}
	
	public Informacion(int tipo) {
		super(tipo, "", "");
	}

	@Override
	public ArrayList<InfoUsuario> getLista() {
		return null;
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
