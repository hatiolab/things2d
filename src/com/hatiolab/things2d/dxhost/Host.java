package com.hatiolab.things2d.dxhost;

import android.content.Context;

public class Host {
	private static Host host;

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

}
