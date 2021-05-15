package Servidor;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import Mensajes.Archivo;
import Mensajes.Informacion;
import Mensajes.Mensaje;
//import Utils.InfoUsuario;
import Utils.Pair;
import Utils.Usuario;

import static Utils.Constantes.MENSAJE_CONFIRMACION_FINAL_SECUENCIA;
import static Utils.Constantes.MENSAJE_FINAL_SECUENCIA;
import static Utils.Constantes.MENSAJE_LINEA_ENVIADA;
import static Utils.Constantes.MENSAJE_LINEA_RECIBIDA;

import java.io.*;

public class Servidor {
	ServerSocket ss;
	//ArrayList<InfoUsuario> infoUsuarios;
	HashMap<String, ArrayList<String>> mapaInfoUsuarios;
	HashMap<String, ArrayList<Pair<String, String>>> mapaArchivos;
	HashMap<String, Usuario> mapaUsuarios;	
	
	public Servidor(int puerto) {
		mapaInfoUsuarios = new HashMap<>();
		mapaArchivos = new HashMap<>();
		mapaUsuarios = new HashMap<>();
		try {
			ss = new ServerSocket(puerto);
	    	System.out.println("Servidor iniciado en puerto " + puerto);
		} catch (IOException e) {
			System.err.println("No pudo iniciarse el servidor");
			e.printStackTrace();
	        System.exit(1);
		}
	}
	
	public void init() {
		cargarArchivo();
		while (true) {
        	try {
        		Socket s = ss.accept();
    			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
    			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
	        	System.out.println("Nueva conexion creada");
	        	
	            new OyenteCliente(this, in, out).start();
			} catch (IOException e) {
				System.err.println("No pudo aceptarse una solicitud de conexion");
				e.printStackTrace();
			}
        }
	}
	
	private void cargarArchivo() {
		try {
			InputStream inputStream = new FileInputStream("users.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String linea = br.readLine();
			String[] users = linea.split(";");
			for (String s : users) {
				String[] atributos = s.split(",");
				ArrayList<String> archivos = new ArrayList<String>();
				for (String a : atributos[2].split(" ")) {
					archivos.add(a);
					if (mapaArchivos.containsKey(a))
						mapaArchivos.get(a).add(new Pair<String, String>(atributos[0], atributos[1]));
					else {
						ArrayList<Pair<String, String>> usuarios = new ArrayList<>();
						usuarios.add(new Pair<String, String>(atributos[0], atributos[1]));
						mapaArchivos.put(a, usuarios);
					}
				}
				mapaInfoUsuarios.put(atributos[0] + "," + atributos[1], archivos);
			}
			br.close();
			inputStream.close();
        	System.out.println("Base de datos cargada correctamente");
		} catch(Exception e) {
			System.err.println("Archivo users.txt no existente.");
			e.printStackTrace();
		}
	}
	
	private void guardarArchivo() {
		try {
			OutputStream outputStream = new FileOutputStream("users.txt");
			BufferedWriter br = new BufferedWriter(new OutputStreamWriter(outputStream));
			
			for (Entry<String, ArrayList<String>> u : mapaInfoUsuarios.entrySet()) {
				br.append(u.getKey());
				br.append(',');
				ArrayList<String> lista = u.getValue();
				for (int i = 0; i < lista.size()-1; i++) {
					br.append(lista.get(i));
					br.append(' ');
				}
				br.append(lista.get(lista.size()-1));
				br.append(';');
			}
			br.close();
			outputStream.flush();
			outputStream.close();
        	System.out.println("Base de datos guardada correctamente");
		} catch(Exception e) {
			System.err.println("Error al guardar en users.txt.");
			e.printStackTrace();
		}
	}
	
	public void guardarUsuario(String id, String ip, ArrayList<String> archivos, ObjectInputStream in, ObjectOutputStream out) {
		String par = id + "," + ip;
		if (mapaInfoUsuarios.containsKey(par))
			mapaInfoUsuarios.replace(par, archivos);
		else
			mapaInfoUsuarios.put(par, archivos);
		mapaUsuarios.put(par, new Usuario(id, ip, in, out));
		for (String s : archivos) {
			if (mapaArchivos.containsKey(s))
				mapaArchivos.get(s).add(new Pair<>(id, ip));
			else {
				ArrayList<Pair<String, String>> lista = new ArrayList<>();
				lista.add(new Pair<>(id, ip));
				mapaArchivos.put(s, lista);
			}
		}
	}
	
	public void listaUsuarios(ObjectInputStream in, ObjectOutputStream out) {
		String s;
		Mensaje m;
		try {
			for (Entry<String, ArrayList<String>> info : mapaInfoUsuarios.entrySet()) {
				s = info.getKey() + ": " + info.getValue().toString();
				out.writeObject(new Archivo(MENSAJE_LINEA_ENVIADA, s));
				m = (Mensaje) in.readObject();
				if (m.getTipo() != MENSAJE_LINEA_RECIBIDA)
						out.writeObject(new Error("La lista no pudo imprimirse de forma correcta"));	
			}
			out.writeObject(new Informacion(MENSAJE_FINAL_SECUENCIA));
			m = (Mensaje) in.readObject();
			if (m.getTipo() == MENSAJE_CONFIRMACION_FINAL_SECUENCIA)
				System.err.println("Descarga finalizada correctamente");
			else
				System.out.println("Descarga finalizada incorrectamente");
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Usuario buscarFichero(String nomArchivo) {
		if (mapaArchivos.containsKey(nomArchivo)) {
			ArrayList<Pair<String, String>> lista = mapaArchivos.get(nomArchivo);
			for (Pair<String, String> par : lista) {
				String s = par.getFirst() + "," + par.getSecond();
				if (mapaUsuarios.containsKey(s))
					return mapaUsuarios.get(s);
			}
			return null;
		}
		else return null;
	}
	
	public Usuario buscarUsuario(String id, String ip) {
		String key = id + "," + ip;
		if (mapaUsuarios.containsKey(key))
			return mapaUsuarios.get(key);
		return null;
	}
	
	public void descargaTerminada(String id, String ip, String nomArchivo) {
		String key = id + "," + ip;
		if (mapaUsuarios.containsKey(key))
			mapaUsuarios.get(key).setDescargando(false);
		mapaArchivos.get(nomArchivo).add(new Pair<>(id, ip));
		mapaInfoUsuarios.get(key).add(nomArchivo);
	}
	
	public void finSesion(String id, String ip) {
    	System.out.println("Cliente desconectandose del servidor...");
		guardarArchivo();
		String par = id + "," + ip;
		if (mapaUsuarios.containsKey(par))
			mapaUsuarios.remove(par);
		else
			System.err.println("Esto no deberia imprimirse nunca, F por ti");
    	System.out.println("Cliente desconectado");
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