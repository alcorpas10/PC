package Cliente;

import static Utils.Constantes.MENSAJE_CONEXION;
import static Utils.Constantes.MENSAJE_CONFIRMACION_CONEXION;
import static Utils.Constantes.MENSAJE_LINEA_ENVIADA;
import static Utils.Constantes.MENSAJE_LINEA_RECIBIDA;
import static Utils.Constantes.MENSAJE_FINAL_SECUENCIA;
import static Utils.Constantes.MENSAJE_CONFIRMACION_FINAL_SECUENCIA;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

import Mensajes.Conexion;
import Mensajes.Informacion;
import Mensajes.Mensaje;

public class Receptor extends Thread {
	private Socket s;
	private Cliente c;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String ip;
	private int puerto;
	private String nomArchivo;
	private Semaphore sem;
	
	public Receptor(Semaphore sem, Cliente c, String ip, int puerto, String nomArchivo) {
        super("Receptor");
		this.c = c;
		this.s = null;
		this.in = null;
		this.out = null;
		this.ip = ip;
		this.puerto = puerto;
		this.nomArchivo = nomArchivo;
		this.sem = sem;
	}
	
	@Override
	public void run() {
		try {
			s = new Socket(ip, puerto);
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
	    	System.out.println("Cliente iniciando descarga P2P...");
		} catch (UnknownHostException e) {
			System.err.println("No pudo encontrarse un servidor abierto en " + ip);
			e.printStackTrace();
	        System.exit(1);
        } catch (IOException e) {
            System.err.println("No se pudieron establecer canales de comunicacion con " + ip);
			e.printStackTrace();
	        System.exit(1);
		}
		Mensaje m;
		try {
			m = (Mensaje) in.readObject();
			if (m.getTipo() == MENSAJE_CONEXION) {
				out.writeObject(new Conexion(MENSAJE_CONFIRMACION_CONEXION));
				PrintWriter pw = new PrintWriter(nomArchivo);
				m = (Mensaje) in.readObject();
				while (m.getTipo() == MENSAJE_LINEA_ENVIADA) {
					pw.println(m.getString());
	    			out.writeObject(new Informacion(MENSAJE_LINEA_RECIBIDA));
	    			m = (Mensaje) in.readObject();
				}
				pw.flush();
				pw.close();
				if (m.getTipo() == MENSAJE_FINAL_SECUENCIA) {
	    			out.writeObject(new Informacion(MENSAJE_CONFIRMACION_FINAL_SECUENCIA));
					System.out.println("\nDescarga finalizada correctamente");
					
					sem.acquire();
					c.descargaTerminada(nomArchivo);
					sem.release();
				}
				else
					System.err.println("\nDescarga finalizada incorrectamente");
			}
			else
				System.err.println("\nDescarga finalizada incorrectamente");

			out.close();
			in.close();
			s.close();
		} catch (ClassNotFoundException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
