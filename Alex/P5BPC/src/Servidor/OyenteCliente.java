package Servidor;

import Mensajes.Informacion;
import Mensajes.Mensaje;

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
    	while (true) {
    		try {
				m = (Mensaje) in.readObject();
				tipo = m.getTipo();
				switch (tipo) {
					case MENSAJE_CONEXION:
						servidor.guardarInfo();
		    			out.writeObject(new Informacion(MENSAJE_CONFIRMACION_CONEXION));
						break;
					case MENSAJE_LISTA_USUARIOS:
						servidor.listaUsuarios();
		    			out.writeObject(new Informacion(MENSAJE_CONFIRMACION_LISTA_USUARIOS));
						break;
					case MENSAJE_CERRAR_CONEXION:
						servidor.finSesion();
		    			out.writeObject(new Informacion(MENSAJE_CONFIRMACION_CERRAR_CONEXION));
						break;
					case MENSAJE_PEDIR_FICHERO:
						servidor.buscarFichero();
		    			out.writeObject(new Informacion(MENSAJE_EMITIR_FICHERO));
						break;
					case MENSAJE_PREPARADO_CLIENTESERVIDOR:
						servidor.buscarUsuario();
		    			out.writeObject(new Informacion(MENSAJE_PREPARADO_SERVIDORCLIENTE));
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