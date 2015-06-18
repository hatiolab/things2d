package com.hatiolab.things2d.dxconnect;

import java.io.IOException;
import java.net.InetAddress;

import com.hatiolab.dx.api.DxClient;
import com.hatiolab.dx.api.DxServer;
import com.hatiolab.dx.mplexer.EventMultiplexer;
import com.hatiolab.dx.net.DiscoveryListener;
import com.hatiolab.dx.net.PacketEventListener;
import com.hatiolab.things2d.dxdevice.Device;
import com.hatiolab.things2d.dxdevice.DeviceEventHandler;
import com.hatiolab.things2d.dxhost.Host;
import com.hatiolab.things2d.dxhost.HostEventHandler;

public class ThingsConnect implements Runnable {
	
	public static final int DISCOVERY_SERVICE_PORT = 5550;
	
	private EventMultiplexer mplexer;

	private Thread runner;

	private DxClient client;
	private DxServer server;

	private int discoveryPort;

	private Host host;
	private Device device;
	
	private PacketEventListener hostPacketListener;
	private PacketEventListener devicePacketListener;
	
	
	public ThingsConnect(Device device, Host host, int discoveryPort) throws IOException {
		this.mplexer = EventMultiplexer.getInstance();

		this.device = device;
		this.host = host;
		this.discoveryPort = discoveryPort;
	}

	public void start() {
		if(runner == null) {
			runner = new Thread(this);
			
			runner.start();
		}
	}

	@Override
	public void run() {
		
		try {
			while(true) {
				mplexer.poll(1000);
				
				if(client != null && !client.isConnected()) {
					try {
						client.discovery();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/*
	 * Device, Host start/stop
	 */
	
	public void stop() {
		// TODO ..
	}
	
	public void openDevice() throws IOException {
		
		devicePacketListener = new DeviceEventHandler(this.device);
		
		server = new DxServer(this.mplexer, 0, discoveryPort, devicePacketListener);
		server.start();
	}
	
	public void closeDevice() throws Exception {
		server.close();
	}
	
	public void openHost() throws Exception {

		hostPacketListener = new HostEventHandler(this.host);

		DiscoveryListener discoveryListener = new DiscoveryListener() {

			@Override
			public void onFoundServer(InetAddress address, int port) {
				try {
					client.startPacketClient(address, port, hostPacketListener);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		client = new DxClient(this.mplexer, discoveryPort, discoveryListener);
		client.start();
	}
	
	public void closeHost() throws Exception {
		client.close();
	}

	public DxClient getClient() {
		return client;
	}

	public void setClient(DxClient client) {
		this.client = client;
	}

	public DxServer getServer() {
		return server;
	}

	public void setServer(DxServer server) {
		this.server = server;
	}

}
