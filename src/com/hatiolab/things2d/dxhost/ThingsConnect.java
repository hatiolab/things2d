package com.hatiolab.things2d.dxhost;

import com.hatiolab.dx.api.DxConnect;

public class ThingsConnect extends DxConnect implements Runnable {
	
	private Thread runner;

	public ThingsConnect(String hostname, int port) {
		super(hostname, port);
	}

	@Override
	public void start() {
		if(runner == null)
			runner = new Thread(this);
	}

	@Override
	public void run() {
		start();
	}
	
}
