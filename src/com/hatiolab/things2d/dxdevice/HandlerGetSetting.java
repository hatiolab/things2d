package com.hatiolab.things2d.dxdevice;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.hatiolab.dx.net.PacketEventListener;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;

public class HandlerGetSetting implements PacketEventListener {

	Device	device;
	
	HandlerGetSetting(Device device) {
		this.device = device;
	}
	
	@Override
	public void onEvent(SocketChannel channel, Header header, Data data) throws IOException {

		switch (header.getCode()) {

	    default :
	        break;
		}
	}

	@Override
	public void onConnected(SocketChannel channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected(SocketChannel channel) {
		// TODO Auto-generated method stub
		
	}
}
