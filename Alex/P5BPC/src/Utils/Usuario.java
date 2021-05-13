package Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Usuario {
	String id;
	String ip;
	ObjectInputStream in;
	ObjectOutputStream out;
	boolean descargando;
	
	public Usuario(String id, String ip, ObjectInputStream in, ObjectOutputStream out) {
		this.id = id;
		this.ip = ip;
		this.in = in;
		this.out = out;
		this.descargando = false;
	}

	public String getId() {
		return id;
	}

	public String getIp() {
		return ip;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public boolean isDescargando() {
		return descargando;
	}

	public void setDescargando(boolean descargando) {
		this.descargando = descargando;
	}
	
	public void desconectarUsuario() {
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
