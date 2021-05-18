package Servidor;

import Mensajes.Archivo;
import Mensajes.Informacion;
import Mensajes.Lista;
import Mensajes.Mensaje;
import Mensajes.Error;
import Utils.Usuario;

import java.io.*;

import Concurrencia.Cerrojos.Cerrojo;

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
import static Utils.Constantes.MENSAJE_LINEA_RECIBIDA;
import static Utils.Constantes.MENSAJE_LINEA_ENVIADA;
import static Utils.Constantes.MENSAJE_FINAL_SECUENCIA;
import static Utils.Constantes.MENSAJE_CONFIRMACION_FINAL_SECUENCIA;


public class OyenteCliente extends Thread {
	private int numID;
	private Servidor servidor;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Cerrojo lock1;
    private Cerrojo lock2;

    public OyenteCliente(int numID, Servidor servidor, ObjectInputStream in, ObjectOutputStream out, Cerrojo lock1, Cerrojo lock2) {
        super("OyenteCliente");
        this.numID = numID;
        this.servidor = servidor;
        this.out = out;
        this.in = in;
        this.lock1 = lock1;
        this.lock2 = lock2;
    }
    
    public void run() {
		Mensaje m;
		int tipo;
		String s = "";
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
						s = servidor.listaUsuarios(0);
						if (s.equals(""))
							out.writeObject(new Informacion(MENSAJE_FINAL_SECUENCIA));
						else
							out.writeObject(new Lista(MENSAJE_LINEA_ENVIADA, 0, s));
						break;
					case MENSAJE_LINEA_RECIBIDA:
						s = servidor.listaUsuarios(m.getNumero()+1);
						if (s.equals(""))
							out.writeObject(new Informacion(MENSAJE_FINAL_SECUENCIA));
						else
							out.writeObject(new Lista(MENSAJE_LINEA_ENVIADA, m.getNumero()+1, s));
						break;
					case MENSAJE_CONFIRMACION_FINAL_SECUENCIA:
						System.out.println("Lista enviada exitosamente");
						break;
					case MENSAJE_CERRAR_CONEXION:
						
						lock1.takeLock(numID);
						boolean b = servidor.finSesion(m.getId(), m.getOrigen());
						lock1.releaseLock(numID);
						
						servidor.actualizarUsuarios(b, m.getId(), m.getOrigen());
		    			out.writeObject(new Informacion(MENSAJE_CONFIRMACION_CERRAR_CONEXION));
		    			out.close();
		    			in.close();
		    			in = null;
		    			out = null;
						break;
					case MENSAJE_PEDIR_FICHERO:
						String nomArchivo = m.getString();
						Usuario usuario1 = servidor.buscarFichero(m.getId(), m.getOrigen(), nomArchivo);						
						if (usuario1 != null)
							usuario1.getOut().writeObject(new Archivo(MENSAJE_EMITIR_FICHERO, m.getOrigen(), m.getId(), nomArchivo));
						else 
							out.writeObject(new Error("No hay ningun usuario con ese archivo conectado actualmente :(\n"));
						break;
					case MENSAJE_PREPARADO_CLIENTESERVIDOR:
						lock2.takeLock(numID);
						Usuario usuario2 = servidor.buscarUsuario(m.getId(), m.getDestino());
						if (usuario2 != null) {
							usuario2.setDescargando(true);
							ObjectOutputStream o = usuario2.getOut();
							lock2.releaseLock(numID);

							o.writeObject(new Archivo(MENSAJE_PREPARADO_SERVIDORCLIENTE, m.getOrigen(), m.getDestino(), m.getId(), m.getNumero(), m.getString()));
						} else {
							lock2.releaseLock(numID);
							out.writeObject(new Error("El usuario que habia pedido el archivo se ha desconectado :("));
						}
						break;
					case MENSAJE_FINAL_DESCARGA:
						lock2.takeLock(numID);
						Usuario usuario3 = servidor.buscarUsuario(m.getId(), m.getDestino());
						usuario3.setDescargando(false);
						lock2.releaseLock(numID);
						
						servidor.descargaTerminada(m.getId(), m.getOrigen(), m.getString());
						break;
					default:
						System.err.println("Mensaje recibido no valido");
						break;
				}
			}
    	} catch (IOException | ClassNotFoundException | InterruptedException e) {
			System.err.println("Error al leer mensaje");
		}
    }
}