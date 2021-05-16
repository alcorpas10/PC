package Mensajes;

import java.util.ArrayList;

import static Utils.Constantes.ERROR;

public class Error extends Mensaje {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String error;

	public Error(String error) {
		super(ERROR, "", "");
		this.error = error;
	}

	@Override
	public ArrayList<String> getLista() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public int getNumero() {
		return 0;
	}

	@Override
	public String getString() {
		return error;
	}

}
