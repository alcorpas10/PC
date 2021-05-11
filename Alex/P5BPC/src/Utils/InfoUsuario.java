package Utils;

import java.util.ArrayList;

public class InfoUsuario {
	String id;
	String ip;
	ArrayList<String> info; //O set quizas
	
	public InfoUsuario(String id, String ip, ArrayList<String> info) {
		this.id = id;
		this.ip = ip;
		this.info = info;
	}

	public String getId() {
		return id;
	}

	public String getIp() {
		return ip;
	}

	public ArrayList<String> getInfo() {
		return info;
	}
}
