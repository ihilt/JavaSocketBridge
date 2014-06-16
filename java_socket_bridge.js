/*global document: false */
// Global variables
var java_socket_bridge_ready_flag = false,
	websocket_connected = false,
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
}

// Applet reports it is ready to use
function java_socket_bridge_ready() {
	"use strict";
	java_socket_bridge_ready_flag = true;
}

function is_ready() {
	"use strict";
	return java_socket_bridge_ready_flag || websocket_connected;
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

// Get something from the socket
function on_socket_get(message) {"use strict"; }

function websocket_create() {
	var ws = new WebSocket("ws://10.56.97.27:8080");
	ws.onopen = function () {
		console.log("connected");
		websocket_connected = true;
		ws.send("Action: QueueStatus");
		ws.send("");
	};
	ws.onmessage = function (evt) {
		on_socket_get(evt.data);
	};
	ws.onerror = function (err) {
		console.log(err);
	};
	ws.onclose = function () {
		document.getElementById("loading").innerHTML = "Not&nbsp;Ready&hellip;";
		document.getElementById("loading").setAttribute('style', 'color:red;');
		console.log("closed");
	};
}