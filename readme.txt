
===============================================================================
                              Java Socket Bridge
===============================================================================

By Stephen Ware
   sgware@gmail.com
April 25, 2009

Built on Java Version 6, Update 11
This is freeware, but I always appreciate a shoutout.


This project provides a small Java applet that interfaces with a web browser
to give JavaScript true socket capabilities.


This zip file should contain:
  JavaSocketBridge.jar           The jar containing the applet
  java_socket_bridge.js          The JavaScript interface
  index.htm                      An example implementation

Also a folder called "Source," which should contain the source code for:
  JavaSocketBridge.java
  Listener.java


To use this, you must insert the applet into your page using the <applet> tag:

  <applet id="JavaSocketBridge"
   archive="JavaSocketBridge.jar"
   code="JavaSocketBridge.class"
   width="0"
   height="0">
  </applet>

You must also import the JavaScript file:

  <script type="text/javascript" src="java_socket_bridge.js"></script>


The following JavaScript functions will now be available to you:

  // Connect to a server on the given port:
  // url is a string
  // port is an integer
  // returns true or false depending on the success of the connection
  function socket_connect(url, port)

  // Stop the current connection:
  // returns true or false depending on the success of the disconnect
  function socket_disconnect()
  
  // Send something over the socket:
  // message is a string
  // returns true or false depending on the success of the sending
  function socket_send(message)
  
  // Receive something from the socket:
  // This method will be called every time something comes over the socket.
  // By default, this method is empty.  You will need to overwrite it.
  // message is a string containing the data
  function on_socket_get(message)
  
  // Notification of an error:
  // This method will be called every time an error occurs.
  // By default, this method is empty.  You will need to overwrite it.
  // message is a string containing a description of the error
  function on_socket_error(message)


You may only have one connection open at a time.

The applet has stop() and destroy() methods that will gracefully disconnect
from the current socket (if any) when you navigate away from the page.

All the activity of the Java Socket Bridge is logged to System.out in the Java
Plugin.

To connect to URL's on domains other than the one hosting this applet, users
will need to accept the security certificate.  I have self-signed it, which is
not ideal, but it should work if they are not too scrupulous about security.