package com.hatiolab.things2d.dxhost;

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
	public void onDxEvent(Header header, Data data) {
		Log.i("HandlerHeartBeat", "HeartBeat");
	}
}
