package com.hatiolab.things2d.sample;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hatiolab.things2d.R;

public class ReceiverActivity extends Activity implements SurfaceHolder.Callback {

	SurfaceHolder holder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receiver);
		
		SurfaceView svDisplay = (SurfaceView)findViewById(R.id.sv_display);
		this.holder = svDisplay.getHolder();
		this.holder.addCallback(this);

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		try {
			new Receiver("TEST", this.holder.getSurface()).start();
//			new ReceiverThread2("TEST", DisplayManager.holder.getSurface()).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
}

