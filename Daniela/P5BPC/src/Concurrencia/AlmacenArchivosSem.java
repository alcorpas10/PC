package Concurrencia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import Utils.Usuario;


public class AlmacenArchivosSem  {

	private Semaphore r;
	private Semaphore w;
	private Semaphore e;
	int nr, nw, dr, dw;

	private HashMap<String, ArrayList<String>> mapaArchivos;
	
	
	public AlmacenArchivosSem() {
		
		mapaArchivos = new HashMap<>();
		
		r = new Semaphore(0);
		w = new Semaphore(0);
		e = new Semaphore(1);
	
	}
	
	

	public void modify(String k, String nuevoUsuario, Boolean add) {
		
		try {
			e.acquire();
		
		
		if(nr>0  || nw>0 ) {
			dw = dw +1;
			e.release();
			w.acquire();  // PT
			
		}
		nw = nw +1;
		e.release();
		
		if(add) {
			if (mapaArchivos.containsKey(k))
				mapaArchivos.get(k).add(nuevoUsuario);
			else {
				ArrayList<String> usuarios = new ArrayList<>();
				usuarios.add(nuevoUsuario);
				mapaArchivos.put(k, usuarios);
			}
		}
		else //remove del almacen
		{
			if (mapaArchivos.containsKey(k))
				mapaArchivos.get(k).remove(nuevoUsuario);
		}
		
		e.acquire();
		nw = nw -1;
		if(dw>0) {
			dw = dw -1;
			w.release();
		}
		else if(dr >0) { //Pasa testigo al reader
			dr = dr -1;
			r.release();
		}
		else
			e.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	
	public ArrayList<String> read(String nomArchivo) { //Reader forma 1
		ArrayList<String> lista = null;

		try {
		
			e.acquire();
			
			if(nw>0) {
				dr = dr +1;
				e.release();
				r.acquire();
			}
		
		nr = nr +1;
		if(dr>0) {
			dr = dr -1;
			r.release();
		}
		else
			e.release();
		
		//Leeamos de BD
		if (mapaArchivos.containsKey(nomArchivo)) {
			lista = mapaArchivos.get(nomArchivo);
		}
		else
			lista = null;
		
		e.acquire();
		nr = nr -1;
		if(nr == 0 && dw>0) {
			dw = dw -1;
			w.release(); //PT
		}
		else
			e.release();
		
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lista;
	}
	

}
