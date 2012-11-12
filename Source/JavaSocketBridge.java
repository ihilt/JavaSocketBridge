// JavaSocketBridge.java
// by Stephen Ware
// April 25, 2009
//
// Part of the JavaSocketBridge project.
// This applet provides an interface for using true sockets in JavaScript.
//
// Note: You will need to have the Java Plugin archive in your classpath to compile this.
//       For me, that's C:\Program Files\Java\jre6\lib\plugin.jar
// Note: You will need to jar this class and Listener.class into a signed jar file if
//       you want your sockets to access domains other than the one this is running on.
// Note: Apparently, when you grant permissions to Java applets in Java 6, you only grant
//       them to the main applet thread.  That's the reason for all the confusing stuff
//       in the connect methods... so that connections always happen on the main thread.

import java.applet.*;
import javax.swing.*;
import netscape.javascript.*;
import java.net.*;
import java.io.*;

public class JavaSocketBridge extends JApplet {

	// Instance variables
	JSObject browser = null;		// The browser
	Socket socket = null;			// The current socket
	PrintWriter out = null;			// Output
	Listener listener = null;		// Listens for input
	boolean running = false;		// Am I still running?
	String address = null;			// Where you will connect to
	int port = -1;					// Port
	boolean connectionDone = false;	// Thread synchronization

	// Initialize
	public void init(){
		browser = JSObject.getWindow(this);
	}

	// Stop and destroy
	public void stop(){
		running = false;
		disconnect();
	}
	public void destroy(){
		running = false;
		disconnect();
	}

	// Main
	// Note: This method loops over and over to handle requests becuase only
	//       this thread gets the elevated security policy.  Java == stupid.
	public void start(){
		try {
			browser.call("java_socket_bridge_ready", null);
		} catch (Exception e) {
			try {
				Thread.currentThread().sleep(5000);
				start();
			} catch (InterruptedException ex) {
			}
		}
		running = true;
		while(running){
			// Wait
			try{
				Thread.sleep(100);
			}
			catch(Exception ex){
				running = false;
				return;
			}
			// Connect
			if(address != null && port != -1 && socket == null){
				do_connect(address, port);
			}
		}
	}

	// Connect
	public boolean connect(String url, int p){
		address = url;
		port = p;
		// Wait for the connection to happen in the main thread
		connectionDone = false;
		while(!connectionDone){
			try{ Thread.sleep(100); }
			catch(Exception ex){ return false; }
		}
		connectionDone = false;
		// Did it work?
		if(socket != null) return true;
		return false;
	}
	private void do_connect(String url, int p){
		if(socket == null){
			try{
				socket = new Socket(url, p);
				out = new PrintWriter(socket.getOutputStream());
				listener = new Listener(socket, this);
				listener.start();
				log("Java Socket Bridge CONNECTED: "+getUrl());
			}
			catch(Exception ex){
				error("Could not connect to "+url+" on port "+p+"\n"+ex.getMessage());
				connectionDone = true;
			}
		}
		else{
			error("Already connected to "+getUrl());
		}
		connectionDone = true;
	}

	// Disconnect
	public boolean disconnect(){
		if(socket != null){
			try{
				log("Java Socket Bridge DISCONNECTED: "+getUrl());
				listener.close();
				out.close();
				socket = null;
				address = null;
				port = -1;
				return true;
			}
			catch(Exception ex){
				error("An error occured while closing the socket\n"+ex.getMessage());
				socket = null;
				return false;
			}
		}
		return false;
	}

	// Send a message
	public boolean send(String message){
		if(out != null){
			try{
				out.println(message);
				out.flush();
				log("Java Socket Bridge SENT: "+message);
			}
			catch(Exception ex){
				error("Could not write to socket\n"+ex.getMessage());
			}
			return true;
		}
		else{
			error("Not connected");
			return false;
		}
	}

	// Get input from the socket
	public void hear(String message){
		Object[] arguments = new Object[1];
		arguments[0] = message;
		browser.call("on_socket_get", arguments);
		//log("Java Socket Bridge RECEIVED: "+message);
	}

	// Report an error
	public void error(String message){
		message = "Java Socket Bridge ERROR: " + message;
		log(message);
		Object[] arguments = new Object[1];
		arguments[0] = message;
		browser.call("on_socket_error", arguments);
	}

	// Log something
	public void log(String message){
		System.out.println(message);
	}

	// Get the connected URL
	private String getUrl(){
		if(socket == null) return null;
		return socket.getInetAddress().getHostName() +":"+socket.getPort();
	}
}