package Servidor;

import Mensajes.Archivo;
import Mensajes.Informacion;
import Mensajes.Mensaje;
import Mensajes.Error;
import Utils.Usuario;

import java.io.*;

import static Utils.Constantes.MENSAJE_CONEXION;
import static Utils.Constantes.MENSAJE_CONFIRMACION_CONEXION;
import static Utils.Constantes.MENSAJE_LISTA_USUARIOS;
import static Utils.Constantes.MENSAJE_CONFIRMACION_LISTA_USUARIOS;
import static Utils.Constantes.MENSAJE_CERRAR_CONEXION;
import static Utils.Constantes.MENSAJE_CONFIRMACION_CERRAR_CONEXION;
import static Utils.Constantes.MENSAJE_PEDIR_FICHERO;
import static Utils.Constantes.MENSAJE_EMITIR_FICHERO;
import static Utils.Constantes.MENSAJE_PREPARADO_CLIENTESERVIDOR;
import static Utils.Constantes.MENSAJE_PREPARADO_SERVIDORCLIENTE;
import static Utils.Constantes.MENSAJE_FINAL_DESCARGA;


public class OyenteCliente extends Thread {
	private Servidor servidor;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public OyenteCliente(Servidor servidor, ObjectInputStream in, ObjectOutputStream out) {
        super("OyenteCliente");
        this.servidor = servidor;
        this.out = out;
        this.in = in;
    }
    
    public void run() {
		Mensaje m;
		int tipo;
		try {
			while (true && in != null && out != null) {
				m = (Mensaje) in.readObject();
				tipo = m.getTipo();
				switch (tipo) {
					case MENSAJE_CONEXION:
						servidor.guardarUsuario(m.getId(), m.getOrigen(), m.getLista(), in, out);
		    			out.writeObject(new Informacion(MENSAJE_CONFIRMACION_CONEXION));
						break;
					case MENSAJE_LISTA_USUARIOS:
		    			out.writeObject(new Informacion(MENSAJE_CONFIRMACION_LISTA_USUARIOS));
						servidor.listaUsuarios(in, out);
						break;
					case MENSAJE_CERRAR_CONEXION:
						servidor.finSesion(m.getId(), m.getOrigen());
		    			out.writeObject(new Informacion(MENSAJE_CONFIRMACION_CERRAR_CONEXION));
		    			out.close();
		    			in.close();
		    			in = null;
		    			out = null;
						break;
					case MENSAJE_PEDIR_FICHERO:
						String nomArchivo = m.getString();
						Usuario usuario1 = servidor.buscarFichero(nomArchivo);
						if (usuario1 != null)
							usuario1.getOut().writeObject(new Archivo(MENSAJE_EMITIR_FICHERO, m.getOrigen(), m.getId(), nomArchivo));
						else 
							out.writeObject(new Error("No hay ningun usuario con ese archivo conectado actualmente :("));
						break;
					case MENSAJE_PREPARADO_CLIENTESERVIDOR:
						Usuario usuario2 = servidor.buscarUsuario(m.getId(), m.getDestino());
						if (usuario2 != null)
							usuario2.getOut().writeObject(new Archivo(MENSAJE_PREPARADO_SERVIDORCLIENTE, m.getOrigen(), m.getDestino(), m.getId(), m.getPuerto(), m.getString()));
						else 
							out.writeObject(new Error("El usuario que habia pedido el archivo se ha desconectado :("));
						break;
					case MENSAJE_FINAL_DESCARGA:
						servidor.descargaTerminada(m.getId(), m.getOrigen(), m.getString());
						break;
					default:
						System.err.println("Mensaje recibido no valido");
						break;
				}
			}
    	} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error al leer mensaje");
		}
    }
}