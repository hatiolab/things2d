package com.hatiolab.things2d.dxdevice;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import android.util.Log;

import com.hatiolab.dx.net.PacketEventListener;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;

public class HandlerHeartBeat implements PacketEventListener {

	Device	device;
	
	HandlerHeartBeat(Device device) {
		this.device = device;
	}	
	@Override
	public void onEvent(SocketChannel channel, Header header, Data data) throws IOException {
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
