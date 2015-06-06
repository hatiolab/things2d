package com.hatiolab.things2d.renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.opengl.GLSurfaceView;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class ThingsRenderer extends BroadcastReceiver implements GLSurfaceView.Renderer {
	protected int mode = 0;
	protected Context context;
	
	public ThingsRenderer(Context context, int mode) {
		super();
		this.context = context;
		this.mode = mode;
	}

	public void onReceive(Context context, Intent intent) {
		Log.i("XXXX", "YYYYY");
	}
	
	protected IntentFilter getIntentFilter() {
		return new IntentFilter("ABC");
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		LocalBroadcastManager.getInstance(context).registerReceiver(this, getIntentFilter());
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		
	}
	
	public void destroy() {
		LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
	}
	
	public void onResume() {
		
	}
	
	public void onPause() {
		
	}
	
}
