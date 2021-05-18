package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.sun.security.ntlm.Client;

import Cliente.Cliente;
import Servidor.Servidor;

class test1 {

	/*
	@Test
	void test() {
		fail("Not yet implemented");
	}
*/
	@Test
	 void test()  {
	    System.out.println("main");
	    String[] args1 = new String[1]; args1[0]= "1200";
	    String[] args2 = new String[2]; args2[0]= "192.168.56.1";args2[1]="1200";
	    System.setIn(System.in);
	    
	    new Thread(new Runnable() {
	       
	        public void run() {

	        	 try {
					Servidor.main(args1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	      }).start();
	    
	    try {
			Thread.sleep(1200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    new Thread(new Runnable() {
		       
	        public void run() {

	        	 try {
	        		 Cliente.main(args2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	      }).start();

	}

	    //final InputStream original = System.in;
	    //final FileInputStream fips = new FileInputStream(new File("[path_to_file]"));
	   // System.setIn(fips);
	    //Main.main(args);
	    //System.setIn(original);
	    
	    
	
}
	/*
	 * 
	 try {
	    	
	    	
		   Servidor.main(args1);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	     
	   	    
	    try {
			
			
			Cliente.main(args2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	 */

