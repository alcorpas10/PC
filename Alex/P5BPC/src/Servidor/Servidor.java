package Servidor;

import java.net.*;
import java.util.ArrayList;

import Utils.InfoUsuario;
import Utils.Usuario;

import java.io.*;

public class Servidor {
	ServerSocket ss;
	ArrayList<InfoUsuario> infoUsuarios; //Pasar a OyenteCliente o hacer getter
	ArrayList<Usuario> usuarios;
	
	public Servidor(int puerto) {
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
	        	System.out.println("Nueva conexion creada");
	            new OyenteCliente(this, s).start();
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
			infoUsuarios = new ArrayList<>();
			for (String s : users) {
				String[] atributos = s.split(",");
				ArrayList<String> archivos = new ArrayList<String>();
				for (String a : atributos[2].split(" "))
					archivos.add(a);
				InfoUsuario user = new InfoUsuario(atributos[0], atributos[1], archivos);
				infoUsuarios.add(user);
			}
			br.close();
        	System.out.println("Base de datos cargada correctamente");
		} catch(Exception e) {
			System.err.println("Archivo users.txt no existente.");
			e.printStackTrace();
		}
	}
	
	public void guardarInfo() {
		
	}
	
	public ArrayList<InfoUsuario> listaUsuarios() {
		return infoUsuarios;
	}
	
	public void buscarFichero() {
		
	}
	
	public void buscarUsuario() {
		
	}
	
	public void finSesion() {
		//Actualizar base de datos
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