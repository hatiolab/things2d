package com.hatiolab.things2d.dxhost;

import java.net.InetAddress;

import com.hatiolab.dx.api.DxClient;
import com.hatiolab.dx.net.DiscoveryListener;
import com.hatiolab.dx.net.PacketEventListener;

public class ThingsConnect implements Runnable {
	
	public static final int DISCOVERY_SERVICE_PORT = 3456;
	
	private Thread runner;
	private DxClient client;

	private int discoveryPort;

	private PacketEventListener packetEventListener;
	private Host host;
	
	public ThingsConnect(Host host, int discoveryPort) {
		this.host = host;
		this.discoveryPort = discoveryPort;
	}

	public void start() {
		if(runner == null) {
			runner = new Thread(this);
			
			runner.start();
		}
	}

	public void stop() {
		
	}
	
	@Override
	public void run() {
		client = new DxClient();
		
		packetEventListener = new DxEventHandler(this.host, client);
		
		DiscoveryListener discoveryListener = new DiscoveryListener() {

			@Override
			public void onFoundServer(InetAddress address, int port) {
				client.startPacketClient(address, port, packetEventListener);
			}
		};

		try {
			
			client.start(discoveryPort, discoveryListener);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
