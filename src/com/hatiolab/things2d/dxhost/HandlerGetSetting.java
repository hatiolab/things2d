package com.hatiolab.things2d.dxhost;

import com.hatiolab.dx.api.DxConnect;
import com.hatiolab.dx.api.EventListener;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;

public class HandlerGetSetting implements EventListener {

	Host	host;
	DxConnect connect;
	
	HandlerGetSetting(DxConnect connect, Host host) {
		this.connect = connect;
		this.host = host;
	}
	
	@Override
	public void onDxEvent(Header header, Data data) {

		switch (header.getCode()) {

	    default :
	        break;
		}
	}
}