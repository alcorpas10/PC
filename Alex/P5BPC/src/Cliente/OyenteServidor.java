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
import Mensajes.Informacion;
import Mensajes.Mensaje;

public class OyenteServidor extends Thread {
	private ObjectInputStream in;
	private ObjectOutputStream out;

    public OyenteServidor(ObjectInputStream in, ObjectOutputStream out) {
        super("OyenteServidor");
        this.out = out;
        this.in = in;
    }
    
    public void run() {
		Mensaje m;
		int tipo;
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
						System.out.println(m.getLista());
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
						String archivo = m.getNomArchivo();
		    			out.writeObject(new Informacion(MENSAJE_PREPARADO_CLIENTESERVIDOR, m.getId()));
		    			new Emisor(m.getPuerto(), archivo).run();
						break;
					case MENSAJE_PREPARADO_SERVIDORCLIENTE:
						new Receptor(m.getOrigen(), m.getPuerto()).run();
						break;
					default:
						System.err.println("Mensaje recibido no valido");
						break;
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error al leer mensaje");
			e.printStackTrace();
		}
    }
}
