package com.hatiolab.things2d.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.hatiolab.things2d.gl.GraphicTools;

public abstract class Things2DRenderer extends BroadcastReceiver implements GLSurfaceView.Renderer {
	protected int mode = 0;
	protected Context context;

	// OpenGL program
	protected int program;

    // screen resolution
    protected float	mScreenWidth = 1280;
    protected float	mScreenHeight = 768;

    // matrix projection and view
    protected float[] mtrxProjectionAndView;

    // Geometric variables
    protected FloatBuffer vertexBuffer;
    protected ShortBuffer drawListBuffer;
    protected FloatBuffer uvBuffer;
  
    // misc
    protected long mLastTime;

    public Things2DRenderer(Context context, int mode) {
		super();
		this.context = context;
		this.mode = mode;
        mLastTime = System.currentTimeMillis() + 100;
	}

	public void onReceive(Context context, Intent intent) {
		Log.i("XXXX", "YYYYY");
	}
	
	protected IntentFilter getIntentFilter() {
		return new IntentFilter("ABC");
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		LocalBroadcastManager.getInstance(context).registerReceiver(this, getIntentFilter());
		
		program = createProgram(getVertexShader(), getFragmentShader());
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

        // We need to know the current width and height.
        mScreenWidth = width;
        mScreenHeight = height;
 
        // Create the model & texture
        setupModel();
        setupTextureCoordinate();
        setupTexture();
		setTexureParams();
		
        // Redo the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, width, height);
        
        // Build Projectiong And View
        mtrxProjectionAndView = getProjectionAndView(width, height);
 	}

	@Override
	public void onDrawFrame(GL10 gl) {
		 
        // Get the current time
        long now = System.currentTimeMillis();
 
        // We should make sure we are valid and sane
        if (mLastTime > now) return;
 
        // Get the amount of time the last frame took.
        long elapsed = now - mLastTime;
 
        // clear Screen and Depth Buffer, we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
 
        // get handle to vertex shader's vPosition member
        int vertices_handle = GLES20.glGetAttribLocation(program, "vertices");
 
        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(vertices_handle);
 
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(vertices_handle, 3,
                                     GLES20.GL_FLOAT, false,
                                     0, vertexBuffer);
 
        // Get handle to shape's transformation matrix
        int mtrx_handle = GLES20.glGetUniformLocation(program, "transformer");
 
        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrx_handle, 1, false, mtrxProjectionAndView, 0);
 
		// Get handle to texture coordinates location
		int texture_coord_handle = GLES20.glGetAttribLocation(program, "texture_coord");

		// Enable generic vertex attribute array
		GLES20.glEnableVertexAttribArray(texture_coord_handle);

		// Prepare the texture coordinates
		GLES20.glVertexAttribPointer(texture_coord_handle, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);
	
		// Get handle to textures locations
		int sampler_handle = GLES20.glGetUniformLocation(program, "texture");

		// Set the sampler texture unit to 0, where we have saved the texture.
		GLES20.glUniform1i(sampler_handle, 0);

		
		// Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawListBuffer.capacity(),
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
 
        
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(vertices_handle);
		if(uvBuffer != null) {
			GLES20.glDisableVertexAttribArray(texture_coord_handle);
		}

        // Save the current time to see how long it took.
        mLastTime = now;
 
	}

	public void destroy() {
		LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
	}
	
	public void onResume() {
        mLastTime = System.currentTimeMillis();
	}
	
	public void onPause() {
		
	}
	
	protected int createProgram(int rcVertexShader, int rcFragmentShader) {
		
		// Set the clear color to black
	    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

	    // Create the shaders
	    int vertexShader = GraphicTools.loadShader(context, GLES20.GL_VERTEX_SHADER, rcVertexShader);
	    int fragmentShader = GraphicTools.loadShader(context, GLES20.GL_FRAGMENT_SHADER, rcFragmentShader);

	    int program = GLES20.glCreateProgram();             // create empty OpenGL ES Program
	    GLES20.glAttachShader(program, vertexShader);   // add the vertex shader to program
	    GLES20.glAttachShader(program, fragmentShader); // add the fragment shader to program
	    GLES20.glLinkProgram(program);                  // creates OpenGL ES program executables

	    // Set our shader programm
	    GLES20.glUseProgram(program);
	    
	    return program;
	}
	
	protected float[] getProjectionAndView(int width, int height) {
		float[] projection = new float[16];
		float[] view = new float[16];
		float[] projectionAndView = new float[16];
		
        // Clear our matrices
        for(int i=0;i<16;i++)
        {
        	projection[i] = 0.0f;
            view[i] = 0.0f;
            projectionAndView[i] = 0.0f;
        }
 
        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(projection, 0, 0f, width, 0.0f, height, 0, 50);
 
        // Set the camera position (View matrix)
        Matrix.setLookAtM(view, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
 
        // Calculate the projection and view transformation
        Matrix.multiplyMM(projectionAndView, 0, projection, 0, view, 0);
        
        return projectionAndView;
	}
	
    public void setupModel()
    {
        // We have create the vertices of our view.
        float[] vertices = getVertices();
 
        short[] indices = getIndices();
 
        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
 
        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);
 
    }

	protected void setupTextureCoordinate() {
		// Create our UV coordinates.
		float[] uvs = getTextureCoordinate();

		// The texture buffer
		ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
		bb.order(ByteOrder.nativeOrder());
		uvBuffer = bb.asFloatBuffer();
		uvBuffer.put(uvs);
		uvBuffer.position(0);
	}

	protected void setupTexture() {}

	protected abstract int getVertexShader();
	
	protected abstract int getFragmentShader();

	protected abstract float[] getVertices();
	
	protected abstract short[] getIndices();
	
	protected float[] getTextureCoordinate() {
		return new float[] { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f };
	};
	
	protected void setTexureParams() {
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);
   }
}
