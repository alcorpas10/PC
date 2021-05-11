
import java.net.*;
import java.io.*;

public class Server {
	
	public static final int MAX_CLIENTES = 5;
	
    public static void main(String[] args) throws IOException {
	    if (args.length != 1) {
	        System.err.println("Usage: java Server <port number>");
	        System.exit(1);
	    }
	    
        int portNumber = Integer.parseInt(args[0]), numClientes = 0;
        Socket s = null;
    	ServerSocket serverSocket = new ServerSocket(portNumber);
    	System.out.println("Servidor iniciado en puerto " + portNumber);
        while (numClientes < MAX_CLIENTES) {
        	s = serverSocket.accept();
        	numClientes++;
        	System.out.println("Nueva conexion creada");
        	System.out.println("Numero de clientes: " + numClientes);
            new ServerThread(s).start();
        	numClientes++;
        }
        serverSocket.close();
    }
}