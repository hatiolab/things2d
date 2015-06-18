package com.hatiolab.things2d.dxdevice;

import java.nio.channels.SocketChannel;

import com.hatiolab.things2d.dxconnect.ThingsConnect;

import android.content.Context;

public class Device {
	private static Device host;
	private static SocketChannel toSenderChannel;
	private static ThingsConnect connect;
	
	private Context context;
	
	public static Device getInstance(Context context) {
		if (host == null)
			host = new Device(context);
		return host;
	}

	private Device(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}
	
	public static SocketChannel getToSenderChannel() {
		return toSenderChannel;
	}

	public static void setToSenderChannel(SocketChannel toSenderChannel) {
		Device.toSenderChannel = toSenderChannel;
	}

	public static ThingsConnect getConnect() {
		return connect;
	}

	public static void setConnect(ThingsConnect connect) {
		Device.connect = connect;
	}
}
