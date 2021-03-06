package com.hatiolab.things2d.dxhost;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import android.util.Log;

import com.hatiolab.dx.net.PacketEventListener;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;

public class HandlerHeartBeat implements PacketEventListener {

	Host	host;
	
	HandlerHeartBeat(Host host) {
		this.host = host;
	}	
	@Override
	public void onEvent(SocketChannel channel, Header header, Data data) throws IOException {
		Log.i("HandlerHeartBeat", "HOST - HeartBeat");
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
