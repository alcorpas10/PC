package Cliente;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Emisor extends Thread {
	ServerSocket ss;
	
	public Emisor(int puerto, String archivo) {
		try {
			ss = new ServerSocket(puerto);
	    	System.out.println("Servidor P2P iniciado en puerto " + puerto);
		} catch (IOException e) {
			System.err.println("No pudo iniciarse el servidor");
			e.printStackTrace();
	        System.exit(1);
		}
	}
	
	@Override
	public void run() {
		Socket s;
		while (true) {
        	try {
				s = ss.accept();
	        	System.out.println("Cliente conectado para descarga");
			} catch (IOException e) {
				System.err.println("No pudo aceptarse una solicitud de conexion");
				e.printStackTrace();
			}
        }
	}
}
