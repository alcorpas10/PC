package Cliente;

import static Utils.Constantes.MENSAJE_CONFIRMACION_CERRAR_CONEXION;
import static Utils.Constantes.MENSAJE_CONFIRMACION_CONEXION;
import static Utils.Constantes.MENSAJE_CONFIRMACION_FINAL_SECUENCIA;
import static Utils.Constantes.MENSAJE_CONFIRMACION_LISTA_USUARIOS;
import static Utils.Constantes.MENSAJE_EMITIR_FICHERO;
import static Utils.Constantes.MENSAJE_FINAL_SECUENCIA;
import static Utils.Constantes.MENSAJE_LINEA_ENVIADA;
import static Utils.Constantes.MENSAJE_LINEA_RECIBIDA;
import static Utils.Constantes.MENSAJE_PREPARADO_CLIENTESERVIDOR;
import static Utils.Constantes.MENSAJE_PREPARADO_SERVIDORCLIENTE;
import static Utils.Constantes.ERROR;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Mensajes.Archivo;
import Mensajes.Informacion;
import Mensajes.Lista;
import Mensajes.Mensaje;

public class OyenteServidor extends Thread {
	private Cliente c;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String ip;
	private int puertoCont;

    public OyenteServidor(Cliente c, String ip, ObjectInputStream in, ObjectOutputStream out) {
        super("OyenteServidor");
        this.c = c;
        this.ip = ip;
        this.out = out;
        this.in = in;
        this.puertoCont = 1234;
    }
    
    public void run() {
		Mensaje m;
		int tipo;
		String lista = "";
		try {
			while (true) {
				m = (Mensaje) in.readObject();
				tipo = m.getTipo();
				switch (tipo) {
					case MENSAJE_CONFIRMACION_CONEXION:
						System.out.println("Conexion con el servidor establecida correctamente\n"
								+ ".----. .-..----..-. .-..-. .-..----..-. .-..-..----.  .----. \r\n"
								+ "| {}  }| || {_  |  `| || | | || {_  |  `| || || {}  \\/  {}  \\\r\n"
								+ "| {}  }| || {__ | |\\  |\\ \\_/ /| {__ | |\\  || ||     /\\      /\r\n"
								+ "`----' `-'`----'`-' `-' `---' `----'`-' `-'`-'`----'  `----' ");
						break;
					case MENSAJE_CONFIRMACION_LISTA_USUARIOS:
						Mensaje nuevoM;
						try {
							nuevoM = (Mensaje) in.readObject();
							if (nuevoM.getTipo() == MENSAJE_LINEA_ENVIADA) {
								lista += nuevoM.getString();
				    			out.writeObject(new Lista(MENSAJE_LINEA_RECIBIDA, nuevoM.getNumero()));
							}
							else {
								out.writeObject(new Informacion(MENSAJE_CONFIRMACION_FINAL_SECUENCIA));
								System.out.println(lista);
								lista = "";
							}
						} catch (ClassNotFoundException | IOException e) {
							e.printStackTrace();
						}
						break;
					case MENSAJE_LINEA_ENVIADA:
						lista += m.getString();
		    			out.writeObject(new Lista(MENSAJE_LINEA_RECIBIDA, m.getNumero()));
		    			break;
					case MENSAJE_FINAL_SECUENCIA:
						out.writeObject(new Informacion(MENSAJE_CONFIRMACION_FINAL_SECUENCIA));
						System.out.println(lista);
						lista = "";
						break;
					case MENSAJE_CONFIRMACION_CERRAR_CONEXION:
						System.out.println("Desconexion realizada correctamente\n"
								+ "  ,---.     ,--.,--.               \r\n"
								+ " /  O  \\  ,-|  |`--' ,---.  ,---.  \r\n"
								+ "|  .-.  |' .-. |,--.| .-. |(  .-'  \r\n"
								+ "|  | |  |\\ `-' ||  |' '-' '.-'  `) \r\n"
								+ "`--' `--' `---' `--' `---' `----' ");
						System.exit(0);
						break;
					case MENSAJE_EMITIR_FICHERO:
						String nomArchivo = m.getString();
		    			out.writeObject(new Archivo(MENSAJE_PREPARADO_CLIENTESERVIDOR, ip, m.getOrigen(), m.getId(), puertoCont, nomArchivo));
		    			new Emisor(puertoCont, nomArchivo).start();
		    			puertoCont++;
						break;
					case MENSAJE_PREPARADO_SERVIDORCLIENTE:
						new Receptor(c, m.getOrigen(), m.getNumero(), m.getString()).start();
						break;
					case ERROR:
						System.err.println(m.getString());
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
