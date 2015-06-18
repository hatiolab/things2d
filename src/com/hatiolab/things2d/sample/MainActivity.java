package com.hatiolab.things2d.sample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class MainActivity extends Activity implements SurfaceHolder.Callback,
		PreviewCallback {

	DatagramSocket socket;
	InetAddress address;
	final int port = 4445;

	AvcEncoder avcCodec;
	public Camera m_camera;
	SurfaceView m_prevewview;
	SurfaceHolder m_surfaceHolder;
//	int width = 1280;
//	int height = 720;
//	int width = 640;
//	int height = 480;
	int width = 320;
	int height = 240;
	int framerate = 20;
	int bitrate = 2500000;

	byte[] h264 = new byte[width * height * 3 * 10];

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectAll() // or
																	// .detectAll()
																	// for all
																	// detectable
																	// problems
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());

		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main); TODO

		avcCodec = new AvcEncoder(width, height, framerate, bitrate);

//		m_prevewview = (SurfaceView) findViewById(R.id.sv_player); TODO
		m_surfaceHolder = m_prevewview.getHolder(); // 绑定SurfaceView，取得SurfaceHolder对象
		m_surfaceHolder.setFixedSize(width, height); // 预览大小設置
		m_surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		m_surfaceHolder.addCallback((Callback) this);

		try {
			socket = new DatagramSocket();
			address = InetAddress.getByName("192.168.0.21");
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

//	@Override
//	protected void onPause() {
//		m_camera.setPreviewCallback(null); // ！！这个必须在前，不然退出出错
//		m_camera.stopPreview();
//	};
//	
//	@Override
//	protected void onResume() {
//		m_camera.startPreview();
//	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		try {
			m_camera = Camera.open();
			m_camera.setPreviewDisplay(m_surfaceHolder);
			Camera.Parameters parameters = m_camera.getParameters();
			parameters.setPreviewSize(width, height);
			parameters.setPictureSize(width, height);
			parameters.setPreviewFormat(ImageFormat.YV12);
			m_camera.setParameters(parameters);
			m_camera.setPreviewCallback((PreviewCallback) this);
			m_camera.startPreview();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		m_camera.setPreviewCallback(null); // ！！这个必须在前，不然退出出错
		m_camera.stopPreview();
		m_camera.release();
		m_camera = null;
		avcCodec.close();
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {

		Log.v("h264", "h264 start");
		int ret = avcCodec.offerEncoder(data, h264);

		if (ret > 0) {
			try {
				DatagramPacket packet = new DatagramPacket(h264, ret, address, port);
				socket.send(packet);
				Log.v("h264", "h264 end");
			} catch (IOException e) {
				e.printStackTrace();
				
				Log.e("TEST", "" + ret);
			}
		}
	}
}
