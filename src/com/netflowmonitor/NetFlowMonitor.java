package com.netflowmonitor;

import java.io.IOException;

public class NetFlowMonitor {
	public static void main(String[] args) throws IOException, InterruptedException{
		String iface = "eth0";
		if (args.length > 0){
			iface = args[0];	
		}
		WebSocketFlowServer socket = new WebSocketFlowServer(8888, iface);
		socket.start();
	}
}