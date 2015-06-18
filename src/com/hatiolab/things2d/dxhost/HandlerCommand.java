package com.hatiolab.things2d.dxhost;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.hatiolab.dx.net.PacketEventListener;
import com.hatiolab.dx.packet.Code;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;
import com.hatiolab.things2d.StreamSenderSurfaceView;

public class HandlerCommand implements PacketEventListener {
	
	Host	host;
	
	HandlerCommand(Host host) {
		this.host = host;
	}
	
	@Override
	public void onEvent(SocketChannel channel, Header header, Data data) throws IOException {
		switch (header.getCode()) {
		case Code.DX_CMD_BASE:
			break;
		case Code.DX_CMD_START_SENDING:
			StreamSenderSurfaceView.setStartFlag(true);
			break;
		case Code.DX_CMD_STOP_SENDING:
			StreamSenderSurfaceView.setStartFlag(false);
			break;
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
