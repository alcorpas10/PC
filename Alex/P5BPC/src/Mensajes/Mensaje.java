package Mensajes;

import java.io.Serializable;
import java.util.ArrayList;

import Utils.InfoUsuario;

public abstract class Mensaje implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int tipo;
	private String origen;
	private String destino;
	
	public Mensaje(int tipo, String origen, String destino) {
		super();
		this.tipo = tipo;
		this.origen = origen;
		this.destino = destino;
	}
	
	public int getTipo() {
		return tipo;
	}
	
	public String getOrigen() {
		return origen;
	}
	
	public String getDestino() {
		return destino;
	}
	
	public abstract ArrayList<InfoUsuario> getLista();
	
	public abstract String getId();
	
	public abstract String getNomArchivo();
	
	public abstract String getIP();
	
	public abstract int getPuerto();
}
