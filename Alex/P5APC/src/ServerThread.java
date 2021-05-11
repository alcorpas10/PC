
import java.net.*;
import java.io.*;

public class ServerThread extends Thread {
    private Socket socket;
    private final static String FILE = "Archivo no existente";
	public static final int MAX_ARCHIVOS = 5;

    public ServerThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;
    }
    
    public void run() {
    	int numArchivos = 0;
    	try {
			OutputStream out = socket.getOutputStream();
	    	InputStream in = socket.getInputStream();
	    	while (numArchivos < MAX_ARCHIVOS) {
		    	byte[] nomFile = in.readAllBytes();
		    	numArchivos++;
		    	System.out.println("Archivos pedidos: " + numArchivos);
		    	
		    	File f = new File(nomFile.toString());
		    	if (f.exists()) {
		    		FileReader fr = new FileReader(f);
		    		BufferedReader br = new BufferedReader(fr);
		    		ObjectInputStream input = new ObjectInputStream(in);
		    	}
		    	else out.write(FILE.getBytes());
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        /*try (PrintWriter out1 = new PrintWriter(socket1.getOutputStream(), true);
        		BufferedReader in1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        		PrintWriter out2 = new PrintWriter(socket2.getOutputStream(), true);
        		BufferedReader in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));) {
            String inputLine;
            out1.println("Comenzando partida: Eres el jugador numero " + 1);
            out2.println("Comenzando partida: Eres el jugador numero " + 2);
        while (true) {
        	//outputLine = protocol.processInput(inputLine, i);
        	//out1.println(outputLine);
            if ((jugada % 2) + 1 == 1) {
            	inputLine = in1.readLine();
            	if (inputLine.equalsIgnoreCase("salir")) {
            		out1.println("Juego finalizado");
            		out2.println("Juego finalizado");
            		break;
            	}
                out2.println(inputLine);
        	}
            else {
            	inputLine = in2.readLine();
            	if (inputLine.equalsIgnoreCase("salir")) {
            		out1.println("Juego finalizado");
            		out2.println("Juego finalizado");
            		break;
            	}
                out1.println(inputLine);
            }
            jugada++;
        }
        socket.close();
        System.out.println("Conexion finalizada");
        /*} catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}