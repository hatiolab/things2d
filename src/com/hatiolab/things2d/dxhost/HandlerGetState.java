package com.hatiolab.things2d.dxhost;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.hatiolab.dx.net.PacketEventListener;
import com.hatiolab.dx.net.PacketSender;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;

public class HandlerGetState implements PacketEventListener {

	Host	host;
	PacketSender sender;
	
	HandlerGetState(Host host, PacketSender sender) {
		this.host = host;
		this.sender = sender;
	}
	
	@Override
	public void onEvent(Header header, Data data) throws IOException {

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
