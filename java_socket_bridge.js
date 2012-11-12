/*global document: false */
// Global variables
var java_socket_bridge_ready_flag = false,
	is_connected_flag;

// Get the applet object
function get_java_socket_bridge() {
	"use strict";
	return document.getElementById('JavaSocketBridge');
}

function is_connected (b) {
	"use strict";
	return is_connected_flag;
}

// Report an error
function on_socket_error(message) {
	"use strict";
	console.log(message);
}

// Applet reports it is ready to use
function java_socket_bridge_ready() {
	"use strict";
	java_socket_bridge_ready_flag = true;
}

function is_ready() {
	"use strict";
	return java_socket_bridge_ready_flag;
}

// Connect to a given url and port
function socket_connect(url, port) {
	"use strict";
	if (java_socket_bridge_ready_flag) {
		if (get_java_socket_bridge().connect(url, port) !== true) {
			is_connected_flag = false;
			throw new Error("Could not connect");
		} else {
			is_connected_flag = true;
		}
	} else {
		on_socket_error("Java Socket Bridge cannot connect until the applet has loaded");
	}
}

// Disconnect
function socket_disconnect() {
	"use strict";
	if (java_socket_bridge_ready_flag) {
		if (get_java_socket_bridge().disconnect() !== true) {
			throw new Error("Could not disconnect");
		}
	} else {
		on_socket_error("Java Socket Bridge cannot disconnect until the applet has loaded");
	}
}

// Write something to the socket
function socket_send(message) {
	"use strict";
	if (java_socket_bridge_ready_flag) {
		return get_java_socket_bridge().send(message);
	} else {
		on_socket_error("Java Socket Bridge cannot send a message until the applet has loaded");
	}
}

// MUST BE IMPLEMENTED
// Get something from the socket
//function on_socket_get(message) {"use strict"; }