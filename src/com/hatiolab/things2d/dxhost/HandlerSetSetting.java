package com.hatiolab.things2d.dxhost;

import android.content.Intent;

import com.hatiolab.dx.api.DxConnect;
import com.hatiolab.dx.api.EventListener;
import com.hatiolab.dx.data.Primitive;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;

public class HandlerSetSetting implements EventListener {

	Host host;
	DxConnect connect;

	HandlerSetSetting(DxConnect connect, Host host) {
		this.connect = connect;
		this.host = host;
	}

	@Override
	public void onDxEvent(Header header, Data data) {

		Primitive primitive = (Primitive) data;
		long value = primitive.getU32();
		int setValue = (int) value;

//		Intent intent = new Intent("setSetting");
		switch (header.getCode()) {

		default:
			break;
		}
	}

}
