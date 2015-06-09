package com.hatiolab.things2d.dxhost;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import android.util.Log;

import com.hatiolab.dx.api.DxConnect;
import com.hatiolab.dx.api.EventListener;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;

public class HandlerHeartBeat implements EventListener {

	Host	host;
	DxConnect connect;
	
	HandlerHeartBeat(DxConnect connect, Host host) {
		this.connect = connect;
		this.host = host;
	}
	
	@Override
	public void onEvent(Header header, Data data) throws IOException {
		Log.i("HandlerHeartBeat", "HeartBeat");
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
