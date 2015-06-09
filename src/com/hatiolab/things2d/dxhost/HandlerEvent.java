package com.hatiolab.things2d.dxhost;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Timer;
import java.util.TimerTask;

import com.hatiolab.dx.api.DxConnect;
import com.hatiolab.dx.api.EventListener;
import com.hatiolab.dx.packet.Code;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;
import com.hatiolab.dx.packet.Packet;

public class HandlerEvent implements EventListener {
	
	Host	host;
	DxConnect connect;
	
	HeartBeatJob job = null;
	Timer scheduler = null;
	
	HandlerEvent(DxConnect connect, Host host) {
		this.connect = connect;
		this.host = host;
	}
	
	@Override
	public void onEvent(Header header, Data data) throws IOException {
		switch (header.getCode()) {
		case Code.DX_EVT_CONNECT:
//			Intent intent = new Intent("Connect");
//			intent.putExtra("Connect", "Connect Complete");
//			Host.getContext().sendBroadcast(intent);
			
			
			scheduler = new Timer();
			job = new HeartBeatJob();
			scheduler.scheduleAtFixedRate(job, 10000, 20000);
			
			/* 아래는 테스트를 위한 코드임. */
//			try {
//				drive.sendGetFileList("/usr/include");
//				drive.sendGetFile("/home/in/Develop/canux/out/dragonboard/common/boot.img", 0, Data.FILE_PARTIAL_MAX_SIZE);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
			break;
		case Code.DX_EVT_DISCONNECT:
			if(scheduler != null) {
				scheduler.cancel();
				scheduler = null;
				job = null;
			}
			break;
//		case Code.OD_EVT_ERROR:
//			break;
//		case Code.OD_EVT_ALARM:
//			break;

		default:

			/* For test - echo back */
			try {
				Packet packet = new Packet(header, data);
				connect.sendPacket(packet);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			break;
		}
	}
	
	class HeartBeatJob extends TimerTask {
		public void run() {
			try {
				connect.sendHeartBeat();
			} catch (Exception e) {
			}
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
