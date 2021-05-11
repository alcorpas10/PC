package Cliente;

import static Utils.Constantes.MENSAJE_CONFIRMACION_CERRAR_CONEXION;
import static Utils.Constantes.MENSAJE_CONFIRMACION_CONEXION;
import static Utils.Constantes.MENSAJE_CONFIRMACION_LISTA_USUARIOS;
import static Utils.Constantes.MENSAJE_EMITIR_FICHERO;
import static Utils.Constantes.MENSAJE_PREPARADO_CLIENTESERVIDOR;
import static Utils.Constantes.MENSAJE_PREPARADO_SERVIDORCLIENTE;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Mensajes.Informacion;
import Mensajes.Mensaje;

public class OyenteServidor extends Thread {
	//private Cliente c;
	private Socket s;
	private ObjectInputStream in;
	private ObjectOutputStream out;

    public OyenteServidor(/*Cliente c, */Socket s, ObjectInputStream in, ObjectOutputStream out) {
        super("OyenteServidor");
        //this.c = c;
        this.s = s;
        this.in = in;
        this.out = out;
    }
    
    public void run() {
		try {
			in = new ObjectInputStream(s.getInputStream());
			out = new ObjectOutputStream(s.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Mensaje m;
		int tipo;
		while (true) {
			try {
				m = (Mensaje) in.readObject();
				tipo = m.getTipo();
				switch (tipo) {
					case MENSAJE_CONFIRMACION_CONEXION:
						System.out.println("Conexion con el servidor establecida correctamente\n Bienvenido :D");
						break;
					case MENSAJE_CONFIRMACION_LISTA_USUARIOS:
						System.out.println(m.getLista());
						break;
					case MENSAJE_CONFIRMACION_CERRAR_CONEXION:
						System.out.println("Desconexion realizada correctamente\n Adios :)");
						System.exit(0);
						break;
					case MENSAJE_EMITIR_FICHERO:
						String archivo = m.getNomArchivo();
		    			out.writeObject(new Informacion(MENSAJE_PREPARADO_CLIENTESERVIDOR, m.getId()));
		    			new Emisor(m.getPuerto(), archivo).run();
						break;
					case MENSAJE_PREPARADO_SERVIDORCLIENTE:
						new Receptor(m.getIP(), m.getPuerto()).run();
						break;
					default:
						System.err.println("Mensaje recibido no valido");
						break;
				}
			} catch (IOException | ClassNotFoundException e) {
				System.err.println("Error al leer mensaje");
				e.printStackTrace();
			}
		}
    }
}
