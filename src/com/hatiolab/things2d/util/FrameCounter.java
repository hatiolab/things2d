package com.hatiolab.things2d.util;

import android.util.Log;

public class FrameCounter {
	protected long startTime;
	protected long lastTime;
	protected long counter;
	
	public FrameCounter() {
		reset();
	}
	
	public void reset() {
		startTime = System.currentTimeMillis();
		lastTime = startTime;
		counter = 0;
	}
	
	public void up() {
		long now = System.currentTimeMillis();
		
		counter++;
		
		if(now > lastTime + 1000) {
			lastTime = now;
			int seconds = Math.round((lastTime - startTime) / 1000);
			Log.i("FRAME COUNT", "" + Math.round(counter / seconds) + "(" + counter + "/" + seconds + ")");
		}
	}

}
