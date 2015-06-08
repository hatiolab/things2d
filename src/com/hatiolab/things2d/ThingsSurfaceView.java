package com.hatiolab.things2d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.hatiolab.things2d.renderer.CameraRenderer;
import com.hatiolab.things2d.renderer.ImageRenderer;
import com.hatiolab.things2d.renderer.Things2DRenderer;
import com.hatiolab.things2d.renderer.TriangleRenderer;
import com.hatiolab.things2d.renderer.YUV420Renderer;

public class ThingsSurfaceView extends GLSurfaceView {
	
	private Things2DRenderer renderer;
	
	public ThingsSurfaceView(Context context, int mode) {
		super(context);
		
		ready(mode);
	}
	
	public ThingsSurfaceView(Context context, AttributeSet attrs, int mode) {
		super(context, attrs);

		ready(mode);
	}
	
	private void ready(int mode) {
		setEGLContextClientVersion(2);
		
		switch(mode) {
		case 1:
			renderer = new TriangleRenderer(getContext(), mode);
			break;
		case 2:
			renderer = new ImageRenderer(getContext(), mode);
			break;
		case 3:
			renderer = new YUV420Renderer(getContext(), mode);
			break;
		case 4:
			renderer = new CameraRenderer(getContext(), mode);
			break;
		default:
			renderer = new TriangleRenderer(getContext(), mode);
		}
		
		setRenderer(renderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		super.surfaceCreated(holder);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		super.surfaceDestroyed(holder);
		this.renderer.destroy();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// TODO Auto-generated method stub
		super.surfaceChanged(holder, format, w, h);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		renderer.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		renderer.onResume();
	}
	
}
