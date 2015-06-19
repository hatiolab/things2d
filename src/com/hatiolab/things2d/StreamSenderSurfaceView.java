package com.hatiolab.things2d;

import java.io.IOException;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.hatiolab.dx.data.Stream;
import com.hatiolab.dx.net.PacketIO;
import com.hatiolab.dx.packet.Code;
import com.hatiolab.dx.packet.Packet;
import com.hatiolab.dx.packet.Type;
import com.hatiolab.things2d.dxhost.Host;
import com.hatiolab.things2d.sample.AvcEncoder;

public class StreamSenderSurfaceView extends SurfaceView implements
		PreviewCallback, SurfaceHolder.Callback {

	int width = 1920;
	int height = 1080;
	int framerate = 30;
	int bitrate = 2500000;
		
	byte[] h264 = new byte[width * height * 3 / 2];
	AvcEncoder avcCodec;
	@SuppressWarnings("deprecation")
	public Camera m_camera;
	SurfaceView m_prevewview;
	SurfaceHolder m_surfaceHolder;
	private static boolean startFlag = false;
	
	public StreamSenderSurfaceView(Context context) {
		super(context);
		
//		avcCodec = new AvcEncoder(width, height, framerate, bitrate);

		m_surfaceHolder = this.getHolder(); // 绑定SurfaceView，取得SurfaceHolder对象
		m_surfaceHolder.addCallback((Callback) this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			m_camera = Camera.open();
			m_camera.setPreviewDisplay(m_surfaceHolder);
			Camera.Parameters parameters = m_camera.getParameters();
			parameters.setPreviewSize(width, height);
			parameters.setPictureSize(width, height);
			parameters.setPreviewFormat(ImageFormat.YV12);
			m_camera.setParameters(parameters);
			m_camera.setPreviewCallback(this);
			m_camera.startPreview();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		m_camera.setPreviewCallback(null);
		m_camera.stopPreview();
		m_camera.release();
		m_camera = null;
		if (avcCodec != null) {
			avcCodec.close();
		}
	}
	
	@Override
	public void onPreviewFrame(byte[] data, @SuppressWarnings("deprecation") Camera camera) {
		if (!isStartFlag()) {
			return;
		}
		
		if (avcCodec == null) {
			avcCodec = new AvcEncoder(width, height, framerate, bitrate);
		}
		
		int ret = avcCodec.offerEncoder(data, h264);
		try {
			if (ret > 0) {				
				byte[] tmpByte = new byte[ret];
				System.arraycopy(h264, 0, tmpByte, 0, ret);
				Stream stream = new Stream(Type.DX_PACKET_TYPE_STREAM, tmpByte);
				Packet p = new Packet(Type.DX_PACKET_TYPE_STREAM, Code.DX_STREAM, stream);
				PacketIO.sendPacket(Host.getToReceiverChannel(), p, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isStartFlag() {
		return startFlag;
	}

	public static void setStartFlag(boolean startFlag) {
		StreamSenderSurfaceView.startFlag = startFlag;
	}
}
