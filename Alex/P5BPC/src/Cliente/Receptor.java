package Cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Receptor extends Thread {
	Socket s;
	ObjectInputStream in;
	
	public Receptor(String ip, int puerto) {
		try {
			s = new Socket(ip, puerto);
			in = new ObjectInputStream(s.getInputStream());
	    	System.out.println("Cliente iniciando conexion P2P...");
		} catch (UnknownHostException e) {
			System.err.println("No pudo encontrarse un servidor abierto en " + ip);
			e.printStackTrace();
	        System.exit(1);
        } catch (IOException e) {
            System.err.println("No se pudieron establecer canales de comunicacion con " + ip);
			e.printStackTrace();
	        System.exit(1);
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}
}
