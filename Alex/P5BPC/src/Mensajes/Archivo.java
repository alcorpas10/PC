package Mensajes;

import java.util.ArrayList;

public class Archivo extends Mensaje {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] buffer;
	private String string;
	private String id;
	private int puerto;

	public Archivo(int tipo, String origen, String destino, String id, String nomArchivo) {
		super(tipo, origen, destino);
		this.id = id;
		this.string = nomArchivo;
	}
	
	public Archivo(int tipo, String origen, String destino, String id, int puerto, String nomArchivo) {
		super(tipo, origen, destino);
		this.id = id;
		this.puerto = puerto;
		this.string = nomArchivo;
	}
	
	public Archivo(int tipo, String origen, String id, String nomArchivo) {
		super(tipo, origen, "");
		this.id = id;
		this.string = nomArchivo;
	}
	
	public Archivo(int tipo, byte[] buffer) {
		super(tipo, "", "");
		this.buffer = buffer;
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
	public String getString() {
		return string;
	}

	@Override
	public int getNumero() {
		return puerto;
	}

	@Override
	public byte[] getBuffer() {
		return buffer;
	}
}
