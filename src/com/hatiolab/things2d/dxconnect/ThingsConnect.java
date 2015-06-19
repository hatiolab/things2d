package com.hatiolab.things2d.dxconnect;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import android.util.Log;

import com.hatiolab.dx.api.DxClient;
import com.hatiolab.dx.api.DxServer;
import com.hatiolab.dx.mplexer.EventMultiplexer;
import com.hatiolab.dx.net.DiscoveryListener;
import com.hatiolab.dx.net.PacketEventListener;
import com.hatiolab.dx.net.PacketIO;
import com.hatiolab.things2d.MainActivity;
import com.hatiolab.things2d.dxdevice.Device;
import com.hatiolab.things2d.dxdevice.DeviceEventHandler;
import com.hatiolab.things2d.dxhost.Host;
import com.hatiolab.things2d.dxhost.HostEventHandler;

public class ThingsConnect implements Runnable {
	private static final String TAG = "ThingsConnect";
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
					InetAddress broadcastAddr = getBroadcastAddr(getIpAddress());
					PacketIO.setBroadcastAddr(broadcastAddr);
					
					try {
						client.discovery();
					} catch (Exception e) {
						e.printStackTrace();
					}
					MainActivity.setConnected(false);
				} else {
					MainActivity.setConnected(true);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public InetAddress getIpAddress() {
		InetAddress inetAddress = null;
		InetAddress myAddr = null;

		try {
			for (Enumeration<NetworkInterface> networkInterface = NetworkInterface
					.getNetworkInterfaces(); networkInterface.hasMoreElements();) {

				NetworkInterface singleInterface = networkInterface
						.nextElement();

				for (Enumeration<InetAddress> IpAddresses = singleInterface
						.getInetAddresses(); IpAddresses.hasMoreElements();) {
					inetAddress = IpAddresses.nextElement();

					if (!inetAddress.isLoopbackAddress()
							&& (singleInterface.getDisplayName().contains(
									"wlan0")
									|| singleInterface.getDisplayName()
											.contains("eth0") || singleInterface
									.getDisplayName().contains("ap0"))) {

						myAddr = inetAddress;
					}
				}
			}

		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return myAddr;
	}
	
	public InetAddress getBroadcastAddr(InetAddress inetAddr) {
	    NetworkInterface temp;
	    InetAddress iAddr = null;
	    try {
	        temp = NetworkInterface.getByInetAddress(inetAddr);
	        List < InterfaceAddress > addresses = temp.getInterfaceAddresses();

	        for (InterfaceAddress inetAddress: addresses)

	            iAddr = inetAddress.getBroadcast();
	        Log.d(TAG, "iAddr=" + iAddr);
	        return iAddr;
	    } catch (SocketException e) {
	        e.printStackTrace();
	        Log.d(TAG, "getBroadcast" + e.getMessage());
	    }
	    return null;
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
					e.printStackTrace();
				}
			}
		};

		client = new DxClient(this.mplexer, discoveryPort, discoveryListener);
		client.start();
	}
	
//	public void makeChannel(String ip, int port) throws Exception {
//		try {
//	        String[] ipStr = ip.split("\\.");
//	        byte[] ipBuf = new byte[4];
//	        for(int i = 0; i < 4; i++) {
//	            ipBuf[i] = (byte)(Integer.parseInt(ipStr[i])&0xff);
//	        }
//	        InetAddress address = InetAddress.getByAddress(ipBuf);
//			client.startPacketClient(address, port, hostPacketListener);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
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
