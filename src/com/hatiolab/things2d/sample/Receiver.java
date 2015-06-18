package com.hatiolab.things2d.sample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

public class Receiver extends Thread {
	final String TAG = "ReceiverThread";
	final int port = 4445;
	protected boolean moreData = true;
	protected DatagramSocket socket;
	Decoder decorder;
	
	public Receiver() throws IOException {

	}
	
	public Receiver(String name, Surface surface) throws IOException {
		super(name);
		this.decorder = new Decoder(surface);
	}
	
	@Override
	public void run() {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (moreData) {
			try {
				byte[] buf = new byte[640 * 480 * 3 * 10];
				// receive request
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				
				if (this.decorder != null) {
					this.decorder.display(packet.getData(), packet.getLength());
				}
            
//				InetAddress address = packet.getAddress();	// get client address
//				int port = packet.getPort();
//				String feedback = "Received";  
//	            byte[] backbuf = feedback.getBytes();
//				DatagramPacket sendPacket = new DatagramPacket(backbuf, backbuf.length, address, port);
//				socket.send(sendPacket);
			} catch (UnknownHostException e) {
	            e.printStackTrace();  
	        } catch(IOException e) {
				e.printStackTrace();
				moreData = false;
			}
		}
		
		socket.close();
	}
	
	public class Decoder {
		private final String TAG = "Decoder";
		private static final String MIME_TYPE = "video/avc";
		private static final int VIDEO_WIDTH_640 = 320;
		private static final int VIDEO_HEIGHT_480 = 240;
		static final int framerate = 20;
		static final int bitrate = 2500000;
		
		private MediaCodec codec;
		public byte[] buf;
		
		public Decoder(Surface surface) {
			try {
				codec = MediaCodec.createDecoderByType(MIME_TYPE);
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			MediaFormat mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE, VIDEO_WIDTH_640, VIDEO_HEIGHT_480);
			codec.configure(mediaFormat, surface, null, 0);
			codec.start();
		}
		
		public void display(byte[] buf, int length) {
			BufferInfo info = new BufferInfo();
			ByteBuffer[] inputBuffers = codec.getInputBuffers();
//			ByteBuffer[] outputBuffers = codec.getOutputBuffers();
			try {
				int inputBufferIndex = codec.dequeueInputBuffer(10000);	// parameter is timeout value
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
				int outputBufferIndex = codec.dequeueOutputBuffer(info, 10000);
				if (outputBufferIndex > 0) {
//					ByteBuffer outputBuffer = codec.getOutputBuffer(outputBufferIndex);
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
			
			codec.stop();
			codec.release();
			codec = null;
		}
	}
}
