package com.hatiolab.things2d.dxhost;

import java.nio.channels.SocketChannel;

import com.hatiolab.things2d.dxconnect.ThingsConnect;

import android.content.Context;

public class Host {
	private static Host host;
	private static SocketChannel toReceiverChannel;
	private static ThingsConnect connect;
	
	private Context context;

	public static Host getInstance(Context context) {
		if (host == null)
			host = new Host(context);
		return host;
	}

	private Host(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public static SocketChannel getToReceiverChannel() {
		return toReceiverChannel;
	}

	public static void setToReceiverChannel(SocketChannel toReceiverChannel) {
		Host.toReceiverChannel = toReceiverChannel;
	}

	public static ThingsConnect getConnect() {
		return connect;
	}

	public static void setConnect(ThingsConnect connect) {
		Host.connect = connect;
	}
}
