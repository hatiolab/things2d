package com.hatiolab.things2d.gl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Queue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

public class GlView extends GLSurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "GlView";
	
	private static Queue<byte[]> streamQueue;
	private BroadcastReceiver receiver;
	private QueueTask task;
//	private TextureRenderer txRenderer;

	private boolean stopFlag = false;
	
	private MediaCodec codec;
	private static final String MIME_TYPE = "video/avc";
	private static final int VIDEO_WIDTH_640 = 1440;
	private static final int VIDEO_HEIGHT_480 = 1080;
	
	public GlView(Context context) {
		super(context);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("frame");
		receiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				if (task == null) {
					task = new QueueTask();
				}
				if (task.getState() == Thread.State.NEW) {
					task.start();
				}
			}
		};
		
		context.registerReceiver(receiver, filter);
	}
	
	public GlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		super.surfaceCreated(holder);
		
		try {
			codec = MediaCodec.createDecoderByType(MIME_TYPE);
		} catch(IOException e) {
			e.printStackTrace();
		}
		MediaFormat mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE, VIDEO_WIDTH_640, VIDEO_HEIGHT_480);
		codec.configure(mediaFormat, holder.getSurface(), null, 0);
		codec.start();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		super.surfaceChanged(holder, format, w, h);
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		codec.stop();
		codec.release();
		codec = null;
	}
	
	class QueueTask extends Thread {
		@Override
		public void run() {
			if (streamQueue == null) {
				return;
			}
			
			while (!stopFlag) {
				byte[] frame = streamQueue.poll();
				if (frame != null) {
					display(frame, frame.length);
				} else {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void display(byte[] buf, int length) {
		BufferInfo info = new BufferInfo();
		ByteBuffer[] inputBuffers = codec.getInputBuffers();
//		ByteBuffer[] outputBuffers = codec.getOutputBuffers();
		try {
			int inputBufferIndex = codec.dequeueInputBuffer(1000);	// parameter is timeout value
			if (inputBufferIndex >= 0) {
				ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
				inputBuffer.clear();
				inputBuffer.put(buf, 0, length);
				codec.queueInputBuffer(inputBufferIndex, 0, length, info.presentationTimeUs, info.flags);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			int outputBufferIndex = codec.dequeueOutputBuffer(info, 1000);
			if (outputBufferIndex > 0) {
				codec.releaseOutputBuffer(outputBufferIndex, true);		// render UI
			} else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
	//				outputBuffers = codec.getOutputBuffers();
			} else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
	//				MediaFormat format = codec.getOutputFormat();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
			Log.d(TAG, "OutputBuffer BUFFER_FLAG_END_OF_STREAM");
		}
	}

	
	public static Queue<byte[]> getStreamQueue() {
		return streamQueue;
	}

	public static void setStreamQueue(Queue<byte[]> streamQueue) {
		GlView.streamQueue = streamQueue;
	}

//	public TextureRenderer getTxRenderer() {
//		return txRenderer;
//	}
//
//	public void setTxRenderer(TextureRenderer txRenderer) {
//		this.txRenderer = txRenderer;
//	}
}
