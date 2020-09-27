package view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

import processing.core.PApplet;
import processing.core.PVector;

public class Main extends PApplet {
	Socket socket; 
	BufferedReader reader;
	BufferedWriter writer; 
	User[] usuariosByDefault; 
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("view.Main");

	}
	public void settings() {

		size(500,500); 
	}

	public void setup() {
		serverInit(); 
		 usuariosByDefault =new User[2]; 
		 usuariosByDefault[0]= new User("daniel","123");
		 usuariosByDefault[1]= new User("sofia","123");
		 for (int i = 0; i < usuariosByDefault.length; i++) {
			System.out.println(usuariosByDefault[i].getName());
		}
		 

	}

	public void draw() {
		background(0,255,255);

	}
	public void sendMessage(String ms) {
		new Thread(
				()->{
					try {
						writer.write(ms+"\n");
						writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				).start();
	}
	
	public void serverInit() {
		new Thread(
				()->{
					try {
						ServerSocket server = new ServerSocket(5000);
						//esperando
						System.out.println("server esperando");
						  socket = server.accept();
						  System.out.println("cliente conectado");
						  InputStream is = socket.getInputStream(); 
						  InputStreamReader isr = new InputStreamReader(is); 
						  reader = new BufferedReader(isr);
						  OutputStream os = socket.getOutputStream(); 
						  OutputStreamWriter osw = new OutputStreamWriter(os); 
						  writer = new BufferedWriter(osw); 
						  
						  while(true) {
							  String line = reader.readLine(); 
							  System.out.println("se ha leido desde el server "+line);
					
							  Gson gson = new Gson(); 
							  User obj =  gson.fromJson(line, User.class);
							  
							  User userGson =new User (obj.getName(),obj.getId()); 
							  System.out.println(userGson.getName());
							
							  if(useSet(usuariosByDefault,userGson.getName(),userGson.getId())) {
								  System.out.println("siiii");
								  sendMessage("ok"); 
								  
							  }else   System.out.println("noooo");
							  
							 
						  }
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
				
				).start();; 
		
	}
	
	   public static boolean useSet(User[] arr, String targetValue, String idpro) {
		   String[] usernameC = new String[arr.length]; 
		   for (int i = 0; i < usernameC.length; i++) {
			   usernameC[i]= arr[i].getName(); 
		}
		   String[] idC = new String[arr.length]; 
		   for (int i = 0; i < idC.length; i++) {
			   idC[i]= arr[i].getId(); 
		}
	        Set<String> set = new HashSet<String>(Arrays.asList(usernameC));
	        Set<String> set2 = new HashSet<String>(Arrays.asList(idC));
	        return set.contains(targetValue)&&set2.contains(idpro);
	    }
}
