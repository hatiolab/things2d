package com.hatiolab.things2d;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.hatiolab.things2d.dxconnect.ThingsConnect;
import com.hatiolab.things2d.dxdevice.Device;
import com.hatiolab.things2d.dxhost.Host;
import com.hatiolab.things2d.hotspot.WifiApManager;

public class SettingActivity extends Activity {
	public final static int SENDER_MODE = 1;
	public final static int RECEIVER_MODE = 2;
	
	private boolean connectFlag = true;
	
	WifiApManager wifiApManager;
	private static Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		wifiApManager = new WifiApManager(this);
		mContext = this;
		
		createDialog().show();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		((Switch)findViewById(R.id.sw_receiver_mode)).setChecked(MainActivity.isReceiver());
		((Switch)findViewById(R.id.sw_sender_mode)).setChecked(MainActivity.isSender());
		super.onResume();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		((Switch)findViewById(R.id.sw_sender_mode)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					MainActivity.setSender(true);
					getConnection(SENDER_MODE);
				} else {
					MainActivity.setSender(false);
				}
			}
		});
		
		((Switch)findViewById(R.id.sw_receiver_mode)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					MainActivity.setReceiver(true);
					getConnection(RECEIVER_MODE);
				} else {
					MainActivity.setReceiver(false);
				}
			}
		});
		super.onWindowFocusChanged(hasFocus);
	}
	
	private void getConnection(int mode) {
		Device device = Device.getInstance(this);
		Host host = Host.getInstance(this);
		try {
			if (MainActivity.getConnect() == null) {
				ThingsConnect connect = new ThingsConnect(device, host, ThingsConnect.DISCOVERY_SERVICE_PORT);
				MainActivity.setConnect(connect);
				MainActivity.getConnect().start();
			}

			if (mode == RECEIVER_MODE) {
				MainActivity.getConnect().openDevice();
			} else {
				MainActivity.getConnect().openHost();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressLint("InflateParams")
	private AlertDialog createDialog() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View settingView = factory.inflate(R.layout.hotspot_setting, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
		builder.setIconAttribute(android.R.attr.alertDialogIcon);
		builder.setTitle(R.string.setting_wifi_hotspot);
		builder.setView(settingView);

		final EditText edtSsid = (EditText) settingView
				.findViewById(R.id.edt_ssid);
		final EditText edtSecurity = (EditText) settingView
				.findViewById(R.id.edt_security);
		final EditText edtPassword = (EditText) settingView
				.findViewById(R.id.edt_password);
		CheckBox cbShowPassword = (CheckBox) settingView
				.findViewById(R.id.cb_show_password);

		cbShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							edtPassword.setInputType(InputType.TYPE_CLASS_TEXT
									| InputType.TYPE_TEXT_VARIATION_NORMAL);
						} else {
							edtPassword.setInputType(InputType.TYPE_CLASS_TEXT
									| InputType.TYPE_TEXT_VARIATION_PASSWORD);
							edtPassword
									.setTransformationMethod(PasswordTransformationMethod
											.getInstance());
						}
					}
				});

		wifiApManager = new WifiApManager(mContext);
		WifiConfiguration getConfig = wifiApManager.getWifiApConfiguration();
		edtSsid.setText(getConfig.SSID);
		edtPassword.setText(getConfig.preSharedKey);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				if (TextUtils.isEmpty(edtSsid.getText())
						|| TextUtils.isEmpty(edtSecurity.getText())
						|| TextUtils.isEmpty(edtPassword.getText())) {
					Toast.makeText(getApplicationContext(), "edtSsid is empty",
							Toast.LENGTH_SHORT).show();
				} else {

					WifiConfiguration config = new WifiConfiguration();
					config.SSID = edtSsid.getText().toString();
					config.status = WifiConfiguration.Status.ENABLED;
					config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
					config.preSharedKey = edtPassword.getText().toString();

					wifiApManager.setWifiApEnabled(config, true);

					dialog.dismiss();
				}
			}
		});
		builder.setNegativeButton("Cancel",
			new DialogInterface.OnClickListener() {
			@Override
				public void onClick(DialogInterface dialog, int whichButton) {

			}
			});

		AlertDialog dialog = builder.create();

		return dialog;
	}

	public boolean isConnectFlag() {
		return connectFlag;
	}

	public void setConnectFlag(boolean connectFlag) {
		this.connectFlag = connectFlag;
	}
}
