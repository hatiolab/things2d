package com.hatiolab.things2d.dxdevice;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Intent;

import com.hatiolab.dx.data.Stream;
import com.hatiolab.dx.net.PacketEventListener;
import com.hatiolab.dx.packet.Code;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;
import com.hatiolab.things2d.gl.GlView;

public class HandlerStream implements PacketEventListener {
	
	Device	device;
	
	HandlerStream(Device device) {
		this.device = device;
	}
	
	public void onEvent(SocketChannel channel, Header header, Data data) throws IOException {
		switch(header.getCode()) {
		case Code.DX_STREAM:
			onStream(data);
			break;
		default:
			break;
				
		}
	}

	public void onStream(Data data) {
		Stream stream = (Stream)data;
		
		if (GlView.getStreamQueue() == null) {
			GlView.setStreamQueue(new ConcurrentLinkedQueue<byte[]>());
		}
		
		GlView.getStreamQueue().add(stream.getContent());
		try {
			Intent intent = new Intent("frame");
			device.getContext().sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
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
