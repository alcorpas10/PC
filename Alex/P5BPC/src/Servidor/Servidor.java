package Servidor;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import Concurrencia.AlmacenArchivosSem;
import Concurrencia.MonitorReadersWritersLC;
import Concurrencia.MonitorReadersWritersSync;
import Concurrencia.Cerrojos.Cerrojo;
import Concurrencia.Cerrojos.LockTicket;
import Utils.InfoUsuario;
import Utils.Usuario;

import java.io.*;


/*
 * 
 ALUMNOS: DANIELA ALEJANDRA CORDOVA PORTA, ALEJANDRO CORPAS CALVO
 
 Diseños de este servidor y diseño basico de la practica en las imagenes PNG
 */
public class Servidor {
	public static final int MAX_CLIENTES = 200;
	
	private ServerSocket ss;
	private ArrayList<InfoUsuario> infoUsuarios; //Lista de Usuarios conectados, mayor concurrencia al enviar usuario por usuario al cliente
	private HashMap<String, ArrayList<String>> mapaInfoUsuarios; //mapa de usuarios con su informacion
	private HashMap<String, Usuario> mapaUsuarios; //mapa de los usuarios con el objeto Usuario (para obtener su in y out mas facilmente) y sincronizar sus descargas
	private int clientes;
	
	
	//Monitores, Readers-Writers
	private MonitorReadersWritersLC monitorRW_LC;
	private MonitorReadersWritersSync monitorRW_Synch;
	
	//Almacen de Archivos y sus usuarios:
	private AlmacenArchivosSem almacenArchivos; //almacen de los archivos con la lista de los usuarios que lo poseen
	
	
	public Servidor(int puerto) {
		infoUsuarios = new ArrayList<>();
		mapaInfoUsuarios = new HashMap<>();
		almacenArchivos = new AlmacenArchivosSem();
		mapaUsuarios = new HashMap<>();
		clientes = 1;
		monitorRW_LC = new MonitorReadersWritersLC();
		monitorRW_Synch = new MonitorReadersWritersSync();
		try {
			ss = new ServerSocket(puerto);
	    	System.out.println("Servidor iniciado en puerto " + puerto + " con IP: " + InetAddress.getLocalHost().getHostAddress()); //getIP()); //Para usar IP publica
		} catch (IOException e) {
			System.err.println("No pudo iniciarse el servidor");
			e.printStackTrace();
	        System.exit(1);
		}
	}
	
	public void init() {
		cargarArchivo();
		Cerrojo lock = new LockTicket(MAX_CLIENTES);

		while (/*true*/clientes <= MAX_CLIENTES) {
        	try {
        		Socket s = ss.accept();
    			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
    			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
	        	System.out.println("Nueva conexion creada. Cliente numero " + clientes);
	        	
	            new OyenteCliente(clientes, this, in, out, lock).start();
	            clientes++;
			} catch (IOException e) {
				System.err.println("No pudo aceptarse una solicitud de conexion");
				e.printStackTrace();
			}
        }
	}
	
	private String getIP() { //Para usar IP publica
        BufferedReader in = null;
        try {
    		URL whatismyip = new URL("http://checkip.amazonaws.com");
            in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            String ip = in.readLine();
            in.close();
            return ip;
        } catch (IOException e) {
			e.printStackTrace();
		}
        return "";
    }	
	
	//Cargar Base de datos
	private void cargarArchivo() { 
		try {
			InputStream inputStream = new FileInputStream("users.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String linea = br.readLine();
			if (linea != null) {
				String[] users = linea.split(";");
				for (String s : users) {
					String[] atributos = s.split(",");
					ArrayList<String> archivos = new ArrayList<String>();
					if (atributos.length == 3) {
						for (String a : atributos[2].split(" ")) {
							archivos.add(a);
							String usuario = atributos[0] + "," + atributos[1];
							
							almacenArchivos.modify(a, usuario, true);
							
						}
					}
					monitorRW_Synch.request_write();
					mapaInfoUsuarios.put(atributos[0] + "," + atributos[1], archivos);
					monitorRW_Synch.release_write();
				}
			}
			br.close();
			inputStream.close();
        	System.out.println("Base de datos cargada correctamente");
		} catch(Exception e) {
			System.err.println("Archivo users.txt no existente.");
		}
	}
	
	//Actualizar txt de Base de Datos
	private void guardarArchivo() {
		try {
			OutputStream outputStream = new FileOutputStream("users.txt");
			BufferedWriter br = new BufferedWriter(new OutputStreamWriter(outputStream));
			
			monitorRW_Synch.request_read();
			for (Entry<String, ArrayList<String>> u : mapaInfoUsuarios.entrySet()) {
				br.append(u.getKey());
				br.append(',');
				ArrayList<String> lista = u.getValue();
				for (int i = 0; i < lista.size(); i++) {
					br.append(' ');
					br.append(lista.get(i));
				}
				br.append(';');
			}
			monitorRW_Synch.release_read();
			br.close();
			outputStream.flush();
			outputStream.close();
        	System.out.println("Base de datos guardada correctamente");
		} catch(Exception e) {
			System.err.println("Error al guardar en users.txt.");
		}
	}
	
	//METODOS PARA GUARDAR NUEVO USUARIO:
	
	public void guardarUsuario(String id, String ip, ArrayList<String> archivos, ObjectInputStream in, ObjectOutputStream out) throws InterruptedException {
		
		
		String par = id + "," + ip;
		monitorRW_Synch.request_write();
		if (mapaInfoUsuarios.containsKey(par)) {
			ArrayList<String> array = mapaInfoUsuarios.get(par);
			for (String s : array)
				 almacenArchivos.modify(s, par, false);
			
				
			mapaInfoUsuarios.remove(par);
			mapaInfoUsuarios.put(par, archivos);
		} else {
			mapaInfoUsuarios.put(par, archivos);
		}
		monitorRW_Synch.release_write();
		
		monitorRW_LC.request_write();
		infoUsuarios.add(new InfoUsuario(id, ip, new ArrayList<String>(archivos)));
		monitorRW_LC.release_write();

		for (String s : archivos) {
			 almacenArchivos.modify(s, par, true);
		}
	}
	
	public void nuevoUsuario(String id, String ip, ObjectInputStream in, ObjectOutputStream out) {
		String par = id + "," + ip;
		mapaUsuarios.put(par, new Usuario(id, ip, in, out));
	}
	
	//Lista de usuarios conectados
	public String listaUsuarios(int i) throws InterruptedException {
		monitorRW_LC.request_read();
		String r;
		if (i < infoUsuarios.size())
			r= infoUsuarios.get(i).toString() + "\n";
		else
			r= "";
		monitorRW_LC.release_read();
		return r;
	}
	
	//Dado archivo, que usuarios tienen archivo para descargar
	public  ArrayList<String> listaUsuarios (String id, String ip, String nomArchivo){
		return almacenArchivos.read(nomArchivo);
	}
	
	//Obtener usuario dado ip, id que este en la lista de posibles Clientes disponible para descargar
	//Se revisa que esten conectados actualmente
	public Usuario buscarUsuario(String id, String ip,ArrayList<String> lista) {
		String key = id + "," + ip;
		if(lista != null) {
			for (String par : lista) {
				if (!key.equals(par)) {
					if (mapaUsuarios.containsKey(par) ) //existe en mapa if(!mapaUsuarios.get(par).isDescargando())??			
							return mapaUsuarios.get(par);
				}
			}
		}
		else
			return null;
		return null;
	}
	
	//Dado ip, id, obtener usuario
	public Usuario getUsuario(String id, String ip) {
		String key = id + "," + ip;
		if (mapaUsuarios.containsKey(key))
			return mapaUsuarios.get(key);
		return null;
	}
	
	//Un usuario ya no esta descargando, se hace para que pueda actualizar ahora su BD de archivos privada
	public void desocuparUsuario(String id, String ip) {
		String key = id + "," + ip;
		if (mapaUsuarios.containsKey(key))
			mapaUsuarios.get(key).setDescargando(false);
	}
	
	//Recorrer un array list y encontrar un string
	public boolean hasString(ArrayList<String> lista, String s) {
		for (String cadena: lista) {
			if(cadena.equals(s))
				return true;
		}
		return false;
	}
	
	//Se termino una descarga, actualizar la base de datos
	public void descargaTerminada(String id, String ip, String nomArchivo) throws InterruptedException {
		String key = id + "," + ip;
		almacenArchivos.modify(nomArchivo, key, true);
		
		monitorRW_Synch.request_write();
		boolean contiene = hasString(mapaInfoUsuarios.get(key), nomArchivo);
		if (!contiene)
			mapaInfoUsuarios.get(key).add(nomArchivo);
		monitorRW_Synch.release_write();
		
		if (!contiene) {
			monitorRW_LC.request_write();
			for (InfoUsuario user : infoUsuarios) {
				if (user.getId().equals(id) && user.getIp().equals(ip))
					user.getInfo().add(nomArchivo);
			}
			monitorRW_LC.release_write();
		}
		
	}
	
	//Usuario cierra sesion, actualiza BD de usuarios conectados
	public boolean finSesion(String id, String ip) {
    	System.out.println("Cliente desconectandose del servidor...");
    	
		guardarArchivo();
		String par = id + "," + ip;
		if (mapaUsuarios.containsKey(par)) {
			mapaUsuarios.remove(par);
	    	System.out.println("Cliente desconectado");
			return true;
		}
		else return false;
	}
	//Usuario cierra sesion, actualiza BD de usuarios conectados
	public void actualizarUsuarios(boolean b, String id, String ip) throws InterruptedException {
		if (b) {
			monitorRW_LC.request_write();
			for (int i = 0; i < infoUsuarios.size(); i++) {
				InfoUsuario user = infoUsuarios.get(i);
				if (user.getId().equals(id) && user.getIp().equals(ip))
					infoUsuarios.remove(user);
			}
			monitorRW_LC.release_write();
		}
		else
			System.err.println("Esto no deberia imprimirse nunca");
	}
	
    public static void main(String[] args) throws IOException {
	    if (args.length != 1) {
	        System.err.println("Uso: java Servidor <puerto>");
	        System.exit(1);
	    }
	    
	    int puerto = Integer.parseInt(args[0]);
	    
	    Servidor servidor = new Servidor(puerto);
	    servidor.init();
    }
}