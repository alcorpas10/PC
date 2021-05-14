package Servidor;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

//import Utils.InfoUsuario;
import Utils.Pair;
import Utils.Usuario;

import java.io.*;

public class Servidor {
	ServerSocket ss;
	//ArrayList<InfoUsuario> infoUsuarios;
	HashMap<Pair<String, String>, ArrayList<String>> mapaInfoUsuarios;
	HashMap<String, ArrayList<Pair<String, String>>> mapaArchivos;
	HashMap<Pair<String, String>, Usuario> mapaUsuarios;	
	
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
	        	System.out.println("Nueva conexion creada\n");
	        	
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
			//infoUsuarios = new ArrayList<>();
			for (String s : users) {
				String[] atributos = s.split(",");
				ArrayList<String> archivos = new ArrayList<String>();
				for (String a : atributos[2].split(" "))
					archivos.add(a);
				//InfoUsuario user = new InfoUsuario(atributos[0], atributos[1], archivos);
				//infoUsuarios.add(user);
				mapaInfoUsuarios.put(new Pair<>(atributos[0], atributos[1]), archivos);
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
			
			for (Entry<Pair<String, String>, ArrayList<String>> u : mapaInfoUsuarios.entrySet()) {
				br.append(u.getKey().getFirst());
				br.append(',');
				br.append(u.getKey().getSecond());
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
	
	public void guardarInfo() {
		
	}
	
	public void listaUsuarios() {
	}
	
	public void buscarFichero() {
		
	}
	
	public void buscarUsuario() {
		
	}
	
	public void finSesion(String id, String ip) {
    	System.out.println("Cliente desconectandose del servidor...");
		guardarArchivo();
		Pair<String, String> par = new Pair<>(id, ip);
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