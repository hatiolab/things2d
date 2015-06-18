package com.hatiolab.things2d;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.hatiolab.things2d.dxconnect.ThingsConnect;
import com.hatiolab.things2d.dxdevice.Device;
import com.hatiolab.things2d.dxhost.Host;

public class SettingActivity extends Activity {
	private final int SENDER_MODE = 1;
	private final int RECEIVER_MODE = 2;
	
	private ThingsConnect connect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		((Switch)findViewById(R.id.sw_sender_mode)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					getConnection(SENDER_MODE);
				}
			}
		});
		
		((Switch)findViewById(R.id.sw_receiver_mode)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					getConnection(RECEIVER_MODE);
				}
			}
		});
		super.onWindowFocusChanged(hasFocus);
	}
	
	private void getConnection(int mode) {
		Device device = Device.getInstance(this);
		Host host = Host.getInstance(this);
		try {
			if (connect == null) {
				connect = new ThingsConnect(device, host, ThingsConnect.DISCOVERY_SERVICE_PORT);
				connect.start();
			}

			if (mode == RECEIVER_MODE) {
				connect.openDevice();
				Device.setConnect(connect);
			} else {
				connect.openHost();
				Host.setConnect(connect);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
