package Cliente;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Mensajes.Archivo;
import Mensajes.Conexion;
import Mensajes.Informacion;
import Mensajes.Mensaje;

import static Utils.Constantes.MENSAJE_CONEXION;
import static Utils.Constantes.MENSAJE_CONFIRMACION_CONEXION;
import static Utils.Constantes.MENSAJE_LINEA_ENVIADA;
import static Utils.Constantes.MENSAJE_LINEA_RECIBIDA;
import static Utils.Constantes.MENSAJE_FINAL_SECUENCIA;
import static Utils.Constantes.MENSAJE_CONFIRMACION_FINAL_SECUENCIA;

public class Emisor extends Thread {
	private ServerSocket ss;
	private String nomArchivo;
	private int puerto;
	
	public Emisor(String archivo) {
        super("Emisor");
		this.nomArchivo = archivo;
		try {
			ss = new ServerSocket(0);
			this.puerto = ss.getLocalPort();
	    	System.out.println("\nServidor P2P iniciado en puerto " + puerto);
		} catch (IOException e) {
			System.err.println("\nNo pudo iniciarse el servidor");
			e.printStackTrace();
	        System.exit(1);
		}
	}
	
	public int getPuerto() {
		return puerto;
	}
	
	@Override
	public void run() {
    	Socket s = null;
    	ObjectOutputStream out = null;
    	ObjectInputStream in = null;
		try {
			s = ss.accept();
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
	    	System.out.println("\nCliente conectado para descarga");
			out.writeObject(new Conexion(MENSAJE_CONEXION));
		} catch (IOException e) {
			System.err.println("\nNo pudo aceptarse una solicitud de conexion para descarga");
			e.printStackTrace();
		}
		
		try {
			Mensaje m = (Mensaje) in.readObject();
			if (m.getTipo() == MENSAJE_CONFIRMACION_CONEXION) {
				InputStream inputStream = new FileInputStream(nomArchivo);
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
				String linea = br.readLine();
				while(linea != null) {
					out.writeObject(new Archivo(MENSAJE_LINEA_ENVIADA, linea));
					m = (Mensaje) in.readObject();
					if (m.getTipo() == MENSAJE_LINEA_RECIBIDA)
						linea = br.readLine();
				}
				br.close();
				out.writeObject(new Informacion(MENSAJE_FINAL_SECUENCIA));
				m = (Mensaje) in.readObject();
				if (m.getTipo() == MENSAJE_CONFIRMACION_FINAL_SECUENCIA)
					System.out.println("\nDescarga finalizada correctamente");
				else
					System.err.println("\nDescarga finalizada incorrectamente");
			}
			else
				System.err.println("\nConexion finalizada incorrectamente");
			
			out.close();
			in.close();
			s.close();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
}
