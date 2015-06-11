package com.hatiolab.things2d.dxhost;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.hatiolab.dx.data.Primitive;
import com.hatiolab.dx.net.PacketEventListener;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;

public class HandlerSetSetting implements PacketEventListener {

	Host	host;
	
	HandlerSetSetting(Host host) {
		this.host = host;
	}

	@Override
	public void onEvent(SocketChannel channel, Header header, Data data) throws IOException {

		Primitive primitive = (Primitive) data;
		long value = primitive.getU32();
		int setValue = (int) value;

//		Intent intent = new Intent("setSetting");
		switch (header.getCode()) {

		default:
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
