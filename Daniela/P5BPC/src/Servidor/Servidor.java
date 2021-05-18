package Servidor;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import Concurrencia.MonitorReadersWritersLC;
import Concurrencia.Cerrojos.Cerrojo;
import Concurrencia.Cerrojos.LockBakery;
import Concurrencia.Cerrojos.LockTicket;
import Utils.InfoUsuario;
import Utils.Usuario;

import java.io.*;

public class Servidor {
	public static final int MAX_CLIENTES = 200;
	
	private ServerSocket ss;
	private ArrayList<InfoUsuario> infoUsuarios;
	private HashMap<String, ArrayList<String>> mapaInfoUsuarios;
	private HashMap<String, ArrayList<String>> mapaArchivos;
	private HashMap<String, Usuario> mapaUsuarios;
	private int clientes;
	
	
	//Monitores
	private MonitorReadersWritersLC monitorRW;
	
	public Servidor(int puerto) {
		infoUsuarios = new ArrayList<>();
		mapaInfoUsuarios = new HashMap<>();
		mapaArchivos = new HashMap<>();
		mapaUsuarios = new HashMap<>();
		clientes = 1;
		monitorRW = new MonitorReadersWritersLC();
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
		Cerrojo lock1 = new LockTicket(MAX_CLIENTES);
		Cerrojo lock2 = new LockBakery(MAX_CLIENTES);

		while (/*true*/clientes <= MAX_CLIENTES) {
        	try {
        		Socket s = ss.accept();
    			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
    			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
	        	System.out.println("Nueva conexion creada. Cliente numero " + clientes);
	        	
	            new OyenteCliente(clientes, this, in, out, lock1, lock2).start();
	            clientes++;
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
					String usuario = atributos[0] + "," + atributos[1];
					if (mapaArchivos.containsKey(a))
						mapaArchivos.get(a).add(usuario);
					else {
						ArrayList<String> usuarios = new ArrayList<>();
						usuarios.add(usuario);
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
	
	public void guardarUsuario(String id, String ip, ArrayList<String> archivos, ObjectInputStream in, ObjectOutputStream out) throws InterruptedException {
		
		
		String par = id + "," + ip;
		if (mapaInfoUsuarios.containsKey(par)) {
			ArrayList<String> array = mapaInfoUsuarios.get(par);
			for (String s : array)
				mapaArchivos.get(s).remove(par);
			mapaInfoUsuarios.remove(par);
			mapaInfoUsuarios.put(par, archivos);
		} else {
			mapaInfoUsuarios.put(par, archivos);
		}
		
		monitorRW.realese_write();
		infoUsuarios.add(new InfoUsuario(id, ip, archivos));
		monitorRW.realese_write();
		
		mapaUsuarios.put(par, new Usuario(id, ip, in, out));
		for (String s : archivos) {
			if (mapaArchivos.containsKey(s))
				mapaArchivos.get(s).add(par);
			else {
				ArrayList<String> lista = new ArrayList<>();
				lista.add(par);
				mapaArchivos.put(s, lista);
			}
		}
	}
	
	public String listaUsuarios(int i) throws InterruptedException {
		monitorRW.request_read();
		String r;
		if (i < infoUsuarios.size())
			r= infoUsuarios.get(i).toString() + "\n";
		else
			r= "";
		monitorRW.realese_read();
		return r;
	}
	
	public Usuario buscarFichero(String id, String ip, String nomArchivo) {
		if (mapaArchivos.containsKey(nomArchivo)) {
			String key = id + "," + ip;
			ArrayList<String> lista = mapaArchivos.get(nomArchivo);
			for (String par : lista) {
				if (!key.equals(par)) {
					if (mapaUsuarios.containsKey(par))
						return mapaUsuarios.get(par);
				}
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
	
	public void descargaTerminada(String id, String ip, String nomArchivo) throws InterruptedException {
		String key = id + "," + ip;
		if (mapaUsuarios.containsKey(key))
			mapaUsuarios.get(key).setDescargando(false);
		mapaArchivos.get(nomArchivo).add(key);
		mapaInfoUsuarios.get(key).add(nomArchivo);
		
		
		monitorRW.request_read();
		for (InfoUsuario user : infoUsuarios) {
			if (user.getId().equals(id) && user.getIp().equals(ip))
				user.getInfo().add(nomArchivo);
		}
		monitorRW.realese_read();
	}
	
	public void finSesion(String id, String ip) throws InterruptedException {
    	System.out.println("Cliente desconectandose del servidor...");
    	
		guardarArchivo();
		String par = id + "," + ip;
		if (mapaUsuarios.containsKey(par)) {
			mapaUsuarios.remove(par);
			
			monitorRW.request_write();
			for (int i = 0; i < infoUsuarios.size(); i++) {
				InfoUsuario user = infoUsuarios.get(i);
				if (user.getId().equals(id) && user.getIp().equals(ip))
					infoUsuarios.remove(user);
			}
			monitorRW.realese_write();
		}
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