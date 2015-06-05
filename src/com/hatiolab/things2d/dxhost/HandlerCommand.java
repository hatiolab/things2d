package com.hatiolab.things2d.dxhost;

import com.hatiolab.dx.api.DxConnect;
import com.hatiolab.dx.api.EventListener;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;

public class HandlerCommand implements EventListener {
	
	Host	host;
	DxConnect connect;
	
	HandlerCommand(DxConnect connect, Host host) {
		this.connect = connect;
		this.host = host;
	}
	
	@Override
	public void onDxEvent(Header header, Data data) {
		switch (header.getCode()) {
		default:
			break;
		}
	}
}
