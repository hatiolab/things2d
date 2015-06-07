package com.hatiolab.things2d.renderer;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.hatiolab.things2d.R;

public class ImageRenderer extends Things2DRenderer {

	// Geometric variables

	public ImageRenderer(Context context, int mode) {
		super(context, mode);
		mLastTime = System.currentTimeMillis() + 100;
	}

	protected IntentFilter getIntentFilter() {
		return new IntentFilter("IMAGE");
	}

	@Override
	public void setupTexture() {
		
		// Generate Textures, if more needed, alter these numbers.
		int[] texturenames = new int[1];
		GLES20.glGenTextures(1, texturenames, 0);
		
		// Retrieve our image from resources.
		int id = context.getResources().getIdentifier("drawable/ic_launcher",
				null, context.getPackageName());

		// Temporary create a bitmap
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);

		// Bind texture to texturename
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);

		// Load the bitmap into the bound texture.
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

		// We are done using the bitmap so we should recycle it.
		bmp.recycle();
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
		return R.raw.vs_image;
	}

	@Override
	protected int getFragmentShader() {
		return R.raw.fs_image;
	}
}
