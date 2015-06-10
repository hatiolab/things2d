package com.hatiolab.things2d.dxhost;

import android.content.Context;

import com.hatiolab.dx.packet.Packet;

public class Host {
	private static Host host;
	
	private Context context;

	public static Host getInstance(Context context) {
		if(host == null)
			host = new Host(context);
		return host;
	}
	
	private Host(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}
	
	/** TODO 임시 방편임 */
	
	public void sendPacket(Packet packet) {
		
	}
	
	public void sendHeartBeat() {
		
	}
	
	public void sendGetFile(String path, int begin, int end) {
		
	}
}
