package com.hatiolab.things2d.dxhost;

import java.nio.channels.SocketChannel;

import com.hatiolab.dx.api.DxConnect;
import com.hatiolab.dx.api.EventListener;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;

public class HandlerSetState implements EventListener {

	Host	host;
	DxConnect connect;
	
	HandlerSetState(DxConnect connect, Host host) {
		this.connect = connect;
		this.host = host;
	}
	
	@Override
	public void onEvent(Header header, Data data) {

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
