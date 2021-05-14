package Cliente;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import Mensajes.Conexion;
import Mensajes.Informacion;
import static Utils.Constantes.MENSAJE_CONEXION;
import static Utils.Constantes.MENSAJE_CERRAR_CONEXION;
import static Utils.Constantes.MENSAJE_PEDIR_FICHERO;
import static Utils.Constantes.MENSAJE_LISTA_USUARIOS;

public class Cliente {
	private String usuario;
	private String ip;
	private Socket s;
	private ArrayList<String> archivos;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private BufferedReader stdIn;
	
	public Cliente() {
		stdIn = new BufferedReader(new InputStreamReader(System.in));
		try {
	    	System.out.print("Introduzca su nombre de usuario: ");
			usuario = stdIn.readLine();
		} catch (IOException e) {
			System.err.println("Error al leer nombre de usuario");
			e.printStackTrace();
	        System.exit(1);
		}
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			System.err.println("No pudo obtenerse la IP de la maquina");
			e.printStackTrace();
	        System.exit(1);
		}
		s = null;
		archivos = new ArrayList<>();
		in = null;
		out = null;
	}
	
	private void init(String IPservidor, int puerto) {
		cargarArchivo();
		try {
			s = new Socket(IPservidor, puerto);
			out = new ObjectOutputStream(s.getOutputStream());
	        in = new ObjectInputStream(s.getInputStream());
	    	System.out.println("Usuario " + usuario + " conectandose al servidor " + IPservidor + "...");
			out.writeObject(new Conexion(MENSAJE_CONEXION, usuario, ip, archivos));
		} catch (UnknownHostException e) {
			System.err.println("No pudo encontrarse un servidor en " + IPservidor);
			e.printStackTrace();
	        System.exit(1);
        } catch (IOException e) {
            System.err.println("No se pudieron establecer canales de comunicacion con " + IPservidor);
			e.printStackTrace();
	        System.exit(1);
		}
		
        new OyenteServidor(in, out).start();

        try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {}
        
        while (true) {
        	menu();
        }
	}
	
	private int opcion() {
		System.out.print("Opcion: ");
		try {
			String s = stdIn.readLine();
			return Integer.parseInt(s);
		} catch (Exception e) {
			System.err.println("Opcion no valida. Introduzca un numero.");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {}
		}
		return -1;
	}
	
	private void menu() {
		System.out.println("Seleccione la opcion que desee ejecutar:");
		System.out.println("1.- Consultar informacion sobre los usuarios conectados.");
		System.out.println("2.- Descargar archivo.");
		System.out.println("0.- Finalizar sesion.");

		int n = 0;
		n = opcion(); 
		while (n < 0 || n > 2) {
			System.err.println("Opcion no valida. Debe ser un numero ");
			n = opcion();
		}
		switch (n) {
		case 1:
			listaUsuarios();
			break;
		case 2:
			descargarArchivo();
			break;
		case 0:
			cerrarSesion();
			break;
		default:
			System.err.println("La accion no se pudo realizar");
			break;
		}
	}
	
	private void cargarArchivo() {
		try {
			InputStream inputStream = new FileInputStream("files.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			
			String linea = br.readLine();
			for (String a : linea.split(";"))
				archivos.add(a);
			br.close();
        	System.out.println("Lista de archivos cargada correctamente");
		} catch(Exception e) {
			System.err.println("Archivo files.txt no existente.");
			e.printStackTrace();
		}
	}
	
	private void cerrarSesion() {
		try {
			System.out.println("Desconectando del servidor...");
			out.writeObject(new Conexion(MENSAJE_CERRAR_CONEXION, usuario, ip));
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {}
		} catch (IOException e) {
			System.err.println("No se pudo enviar la solicitud de cierre de sesion");
			e.printStackTrace();
		}
	}

	private void descargarArchivo() {
		try {
			out.writeObject(new Informacion(MENSAJE_PEDIR_FICHERO, ip));
		} catch (IOException e) {
			System.err.println("No se pudo enviar la solicitud de descarga");
			e.printStackTrace();
		}
	}

	private void listaUsuarios() {
		try {
			out.writeObject(new Informacion(MENSAJE_LISTA_USUARIOS));
		} catch (IOException e) {
			System.err.println("No se pudo enviar la solicitud de informacion");
			e.printStackTrace();
		}
	}
	
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Uso: java Cliente <IP servidor> <puerto>");
            System.exit(1);
        }

        String IPservidor = args[0];
        int puerto = Integer.parseInt(args[1]);
        
        Cliente cliente = new Cliente();
        cliente.init(IPservidor, puerto);        
    }
}