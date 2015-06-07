package com.hatiolab.things2d.renderer;

import android.content.Context;
import android.content.IntentFilter;

import com.hatiolab.things2d.R;

public class TriangleRenderer extends Things2DRenderer {

    public TriangleRenderer(Context context, int mode) {
		super(context, mode);
	}

	protected IntentFilter getIntentFilter() {
		return new IntentFilter("TRIANGLE");
	}
	
	@Override
	protected float[] getVertices() {
		return new float[] {
			10.0f, 800f, 0.0f,
			10.0f, 400f, 0.0f,
			400f, 400f, 0.0f,
		};
	}

	@Override
	protected short[] getIndices() {
		return new short[] {0, 1, 2};
	}

	@Override
	protected int getVertexShader() {
		return R.raw.vs_solid_color;
	}

	@Override
	protected int getFragmentShader() {
		return R.raw.fs_solid_color;
	}

}
