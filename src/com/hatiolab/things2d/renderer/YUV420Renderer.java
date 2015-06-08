package com.hatiolab.things2d.renderer;

import java.nio.ByteBuffer;
import java.util.Random;

import android.content.Context;
import android.content.IntentFilter;
import android.opengl.GLES20;

import com.hatiolab.things2d.R;

public class YUV420Renderer extends Things2DRenderer {

	int y_texture;
	int u_texture;
	int v_texture;
	int[] y_texture_names;
	int[] u_texture_names;
	int[] v_texture_names;
	int y_texture_name;
	int u_texture_name;
	int v_texture_name;
	
	private ByteBuffer y_buffer;
	private ByteBuffer u_buffer;
	private ByteBuffer v_buffer;
	
	byte[] ys = new byte[800*480];
	byte[] us = new byte[800*480/4];
	byte[] vs = new byte[800*480/4];
	
	// Geometric variables

	public YUV420Renderer(Context context, int mode) {
		super(context, mode);
	}

	protected IntentFilter getIntentFilter() {
		return new IntentFilter("YUV");
	}

	@Override
	public void setupTexture() {

		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		y_texture = GLES20.glGetUniformLocation(program, "y_texture");
		y_texture_names = new int[1];
		GLES20.glGenTextures(1, y_texture_names, 0);
		y_texture_name = y_texture_names[0];

		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		u_texture = GLES20.glGetUniformLocation(program, "u_texture");
		u_texture_names = new int[1];
		GLES20.glGenTextures(1, u_texture_names, 0);
		u_texture_name = u_texture_names[0];

		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		v_texture = GLES20.glGetUniformLocation(program, "v_texture");
		v_texture_names = new int[1];
		GLES20.glGenTextures(1, v_texture_names, 0);
		v_texture_name = v_texture_names[0];
		
		y_buffer = ByteBuffer.allocateDirect(800 * 480);
		u_buffer = ByteBuffer.allocateDirect(800 * 480 / 4);
		v_buffer = ByteBuffer.allocateDirect(800 * 480 / 4);
	}
		
	byte vy = 0;
	
	@Override
	protected void onBeforeDrawElement() {
		for(int i = 0;i < 800*480;i++, vy++)
			ys[i] = vy;
		
		for(int i = 0;i < 800*480/4;i++) {
			us[i] = 33;
			vs[i] = 33;
		}

//		(new Random()).nextBytes(ys);
//		(new Random()).nextBytes(us);
//		(new Random()).nextBytes(vs);
		
		y_buffer.put(ys);
		y_buffer.position(0);
		u_buffer.put(us);
		u_buffer.position(0);
		v_buffer.put(vs);
		v_buffer.position(0);
		
//		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, y_texture_names[0]);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, 800,
				480, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, y_buffer);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, y_texture_names[0]);
		GLES20.glUniform1i(y_texture, 0);

//		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, u_texture_names[0]);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, 400,
				240, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, u_buffer);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1 + 2);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, u_texture_names[0]);
		GLES20.glUniform1i(u_texture, 2);

//		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, v_texture_names[0]);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, 400,
				240, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, v_buffer);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1 + 1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, v_texture_names[0]);
		GLES20.glUniform1i(v_texture, 1);
	}
	
	@Override
	protected void onAfterDrawElement() {
	}

	@Override
	protected float[] getVertices() {
		return new float[] {
			0f, mScreenHeight, 0.0f,
			0f, 0f, 0.0f,
			mScreenWidth, 0f, 0.0f,
			mScreenWidth, mScreenHeight, 0.0f,
		};
	}

	@Override
	protected short[] getIndices() {
		return new short[] {0, 1, 2, 0, 2, 3};
	}

	@Override
	protected int getVertexShader() {
		return R.raw.vs_yuv420_to_rgb;
	}

	@Override
	protected int getFragmentShader() {
		return R.raw.fs_yuv420_to_rgb;
	}
}
