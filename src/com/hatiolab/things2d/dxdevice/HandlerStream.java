package com.hatiolab.things2d.dxdevice;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.hatiolab.dx.data.Stream;
import com.hatiolab.dx.net.PacketEventListener;
import com.hatiolab.dx.packet.Code;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;

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
//		try {
//			
////			int type = stream.getType();
//			
//			System.out.println(String.format("Streaming [%d]", stream.getLen()));
//
//			Bundle extras = new Bundle();
//			Intent intent = new Intent("getFrame");
//			extras.putSerializable("frame", stream);
//			intent.putExtras(extras);
//			Host.getContext().sendBroadcast(intent);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
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
