package Servidor;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import Concurrencia.AlmacenArchivosSem;
import Concurrencia.MonitorReadersWritersLC;
import Concurrencia.MonitorReadersWritersSync;
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
	private HashMap<String, Usuario> mapaUsuarios;
	private int clientes;
	
	
	//Monitores
	private MonitorReadersWritersLC monitorRW_LC;
	private MonitorReadersWritersSync monitorRW_Synch;
	
	//Almacen de Archivos y sus usuarios:
	private AlmacenArchivosSem almacenArchivos;
	
	
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
	
	
	private void cargarArchivo() { //hace falta concurrencia? mejor borrar?
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
					
					almacenArchivos.modify(a, usuario, true);
					
				}
				monitorRW_Synch.request_write();
				mapaInfoUsuarios.put(atributos[0] + "," + atributos[1], archivos);
				monitorRW_Synch.realese_write();
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
			
			monitorRW_Synch.request_read();
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
			monitorRW_Synch.realese_read();
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
		monitorRW_Synch.realese_write();
		
		monitorRW_LC.request_write();
		infoUsuarios.add(new InfoUsuario(id, ip, archivos));
		monitorRW_LC.realese_write();

		for (String s : archivos) {
			 almacenArchivos.modify(s, par, true);
		}
	}
	
	public void nuevoUsuario(String id, String ip, ObjectInputStream in, ObjectOutputStream out) {
		String par = id + "," + ip;
		mapaUsuarios.put(par, new Usuario(id, ip, in, out));
	}
	public String listaUsuarios(int i) throws InterruptedException {
		monitorRW_LC.request_read();
		String r;
		if (i < infoUsuarios.size())
			r= infoUsuarios.get(i).toString() + "\n";
		else
			r= "";
		monitorRW_LC.realese_read();
		return r;
	}
	
	public  ArrayList<String> listaUsuarios (String id, String ip, String nomArchivo){
		String key = id + "," + ip;
		return almacenArchivos.read(nomArchivo);
	}
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
	
	/*
	public Usuario buscarFichero(String id, String ip, String nomArchivo) {
		String key = id + "," + ip;
		
		ArrayList <String> lista = almacenArchivos.read(nomArchivo);
			
		if(lista == null) {
			for (String par : lista) {
				if (!key.equals(par)) {
					if (mapaUsuarios.containsKey(par))
						return mapaUsuarios.get(par);
				}
			}
		}
		else
			return null;
		return null;
	}
	*/
	public Usuario getUsuario(String id, String ip) {
		String key = id + "," + ip;
		if (mapaUsuarios.containsKey(key))
			return mapaUsuarios.get(key);
		return null;
	}
	
	public void desocuparUsuario(String id, String ip) {
		String key = id + "," + ip;
		if (mapaUsuarios.containsKey(key))
			mapaUsuarios.get(key).setDescargando(false);
	}
	
	public void descargaTerminada(String id, String ip, String nomArchivo) throws InterruptedException {
		String key = id + "," + ip;
		almacenArchivos.modify(nomArchivo, key, true);
		
		monitorRW_Synch.request_write();
		mapaInfoUsuarios.get(key).add(nomArchivo);
		monitorRW_Synch.request_read();
		
		
		monitorRW_LC.request_read();
		for (InfoUsuario user : infoUsuarios) {
			if (user.getId().equals(id) && user.getIp().equals(ip))
				user.getInfo().add(nomArchivo);
		}
		monitorRW_LC.realese_read();
	}
	
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
	
	public void actualizarUsuarios(boolean b, String id, String ip) throws InterruptedException {
		if (b) {
			monitorRW_LC.request_write();
			for (int i = 0; i < infoUsuarios.size(); i++) {
				InfoUsuario user = infoUsuarios.get(i);
				if (user.getId().equals(id) && user.getIp().equals(ip))
					infoUsuarios.remove(user);
			}
			monitorRW_LC.realese_write();
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