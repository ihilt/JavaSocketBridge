// Listener.java
// by Stephen Ware
// April 25, 2009
//
// Part of the JavaSocketBridge project.
// This class listens for input from a given socket.

import java.io.*;
import java.net.*;

// Thread that listens for input
public class Listener extends Thread{

	// Instance variables
	JavaSocketBridge parent;	// Report to this object
	Socket socket;				// Listen to this socket
	BufferedReader in;			// Input
	boolean running = false;	// Am I still running?

	// Constructor
	public Listener(Socket s, JavaSocketBridge b) throws IOException{
		parent = b;
		socket = s;
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}

	// Close
	public void close() throws IOException{
		if(running == false) return;
		running = false;
		socket.close();
		in.close();
	}

	// Main loop
	public void run(){
		running = true;
		String str = null;
		while(running){
			try{
				str = in.readLine();
				if (str != null) {
					parent.hear(str);
				}
			}
			catch(Exception ex){
				if(running){
					parent.error("An error occured while reading from the socket\n"+ex.getMessage());
					parent.disconnect();
					try{ close(); } catch(Exception ex2){}
				}
			}
		}
		try{ close(); } catch(Exception ex){}
	}
}