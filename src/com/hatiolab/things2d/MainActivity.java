package com.hatiolab.things2d;

import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.hatiolab.dx.net.PacketIO;
import com.hatiolab.dx.packet.Code;
import com.hatiolab.dx.packet.Packet;
import com.hatiolab.dx.packet.Type;
import com.hatiolab.things2d.dxconnect.ThingsConnect;
import com.hatiolab.things2d.dxdevice.Device;
import com.hatiolab.things2d.gl.GlView;
import com.hatiolab.things2d.gl.TextureRenderer;

public class MainActivity extends Activity {

	private ThingsConnect connect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		GlView vvLive = new GlView(getApplicationContext());
		vvLive.setEGLContextClientVersion(2);
		TextureRenderer renderer = new TextureRenderer();
		renderer.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
		vvLive.setRenderer(renderer);
		vvLive.setTxRenderer(renderer);
		((RelativeLayout)findViewById(R.id.lo_receiver)).addView(vvLive, params);
		
		StreamSenderSurfaceView tsv = new StreamSenderSurfaceView(getApplicationContext());
		((LinearLayout)findViewById(R.id.lo_sender)).addView(tsv, params);
		
//		ThingsSurfaceView tsv = new ThingsSurfaceView(getApplicationContext(), 4);
//		((LinearLayout)findViewById(R.id.lo_sender)).addView(tsv, params);
		
		((LinearLayout)findViewById(R.id.lo_buttons)).bringToFront();
		
		((Button)findViewById(R.id.btn_start_sending)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new startCommand().start();
			}
		});
		
		((Button)findViewById(R.id.btn_stop_sending)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new stopCommand().start();
			}
		});
	}
	
	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		restoreActionBar();
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(getApplicationContext(), SettingActivity.class);
			startActivity(i);
		} else if (id == R.id.action_example) {
			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		if (connect != null) {			
			connect.stop();
		}
		
		super.onDestroy();
	}
	
	class startCommand extends Thread {
		@Override
		public void run() {
			try {
				if (Device.getToSenderChannel() == null) {
					Log.d("Start command", "connection is null");
					return;
				}
				
				Packet p = new Packet(Type.DX_PACKET_TYPE_COMMAND, Code.DX_CMD_START_SENDING, null);
				PacketIO.sendPacket(Device.getToSenderChannel(), p);	// Receiver -> Sender
//				PacketIO2Sender pio = new PacketIO2Sender();
//				pio.sendPacket(ThingsConnect.getToSenderChannel(), p);	// Receiver -> Sender
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			super.run();
		}
	}
	
	class stopCommand extends Thread {
		@Override
		public void run() {
			try {
				if (Device.getToSenderChannel() == null) {
					Log.d("Stop command", "connection is null");
					return;
				}
				
				Packet p = new Packet(Type.DX_PACKET_TYPE_COMMAND, Code.DX_CMD_STOP_SENDING, null);
				PacketIO.sendPacket(Device.getToSenderChannel(), p);	// Receiver -> Sender
//				PacketIO2Sender pio = new PacketIO2Sender();
//				pio.sendPacket(ThingsConnect.getToSenderChannel(), p);	// Receiver -> Sender
			} catch (IOException e) {
				e.printStackTrace();
			}

			super.run();
		}
	}
}
