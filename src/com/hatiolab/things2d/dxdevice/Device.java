package com.hatiolab.things2d.dxdevice;

import android.content.Context;

public class Device {
	private static Device host;

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

}
