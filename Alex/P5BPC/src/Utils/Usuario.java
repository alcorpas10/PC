package Utils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Usuario {
	String id;
	String ip;
	ObjectInputStream in;
	ObjectOutputStream out;
	
	public Usuario(String id, String ip, ObjectInputStream in, ObjectOutputStream out) {
		this.id = id;
		this.ip = ip;
		this.in = in;
		this.out = out;
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
}
