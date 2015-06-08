package com.hatiolab.things2d.renderer;

import java.io.IOException;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.util.Log;

import com.hatiolab.things2d.R;

public class CameraRenderer extends Things2DRenderer implements SurfaceTexture.OnFrameAvailableListener {
	
	Camera camera;
		
	// Geometric variables

	public CameraRenderer(Context context, int mode) {
		super(context, mode);
	}
	
	private int[] hTex;
	private SurfaceTexture mSTexture;

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);
		
		hTex = new int[1];
		
		mSTexture = new SurfaceTexture ( hTex[0] );
	    mSTexture.setOnFrameAvailableListener(this);
	    
		camera = Camera.open();
		try {
		    camera.setPreviewTexture(mSTexture);
		} catch ( Exception ioe ) {
			Log.e("XXXXXXXXXX", "YYYYYYYYYYYY", ioe);
		}
	}

	int camera_width = 0;
	int camera_height = 0;
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		super.onSurfaceChanged(gl, width, height);
		
	    camera.stopPreview();

		Camera.Parameters param = camera.getParameters();
	    List psize = param.getSupportedPreviewSizes();
	    if ( psize.size() > 0 ) {
	    	int i;
    		Camera.Size sz = null;
	    	for ( i = 0; i < psize.size(); i++ ) {
	    		sz = (Camera.Size)psize.get(i);
	    		if ( sz.width < width || sz.height < height )
	    			break;
	    	}
	    	if ( i > 0 )
	    		i--;

	    	sz = (Camera.Size)psize.get(i);

	    	param.setPreviewSize(sz.width, sz.height);
	    	
	    	camera_width = sz.width;
	    	camera_height = sz.height;
	    	
	    	Log.i("mr","ssize: "+sz.width+", "+sz.height);
	    }
	    
	    param.set("orientation", "landscape");
	    camera.setParameters ( param );
	    camera.startPreview();		
	}
	
	public void destroy() {
		super.destroy();
		
		mUpdateST = false;
	    mSTexture.release();
	    camera.stopPreview();
	    camera.release();
	    camera = null;
	    
	    GLES20.glDeleteTextures ( 1, hTex, 0 );
	}

	protected IntentFilter getIntentFilter() {
		return new IntentFilter("YUV");
	}

	@Override
	public void setupTexture() {
	}
		
	byte vy = 0;
	
	@Override
	protected void onBeforeDrawElement() {
		synchronized(this) {
			if ( mUpdateST ) {
				mSTexture.updateTexImage();
				mUpdateST = false;
			}
		}
	    int th = GLES20.glGetUniformLocation (program, "texture");
	  
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	    GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, hTex[0]);
	    GLES20.glUniform1i(th, 0);
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
		return R.raw.vs_camera;
	}

	@Override
	protected int getFragmentShader() {
		return R.raw.fs_camera;
	}

	boolean mUpdateST = false;
	
	@Override
	public void onFrameAvailable(SurfaceTexture surfaceTexture) {
		mUpdateST = true;
	}
}
