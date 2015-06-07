package com.hatiolab.things2d.gl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;

import com.hatiolab.things2d.DevtimeError;

public class GraphicTools {
    // Program variables
    public static int sp_SolidColor;
    public static int sp_Image;
 
    private static String readShaderInAssets(Context context, String path) {
		StringBuffer shader = new StringBuffer();
    	AssetManager assets = context.getAssets();

		try {
		
			InputStream inputStream = assets.open(path);
		
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		
			String read = in.readLine();
			while (read != null) {
			  shader.append(read + "\n");
				read = in.readLine();
			}
		
			shader.deleteCharAt(shader.length() - 1);
		} catch (Exception e) {
			throw new DevtimeError("Could not read shader: " + e.getLocalizedMessage(), e);
		}
		
		return shader.toString();
    }
    
    private static String readShaderInResource(Context context, int resId) {
		StringBuffer shader = new StringBuffer();
		
		try {
		
			InputStream inputStream = context.getResources().openRawResource(resId);
		
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		
			String read = in.readLine();
			while (read != null) {
			  shader.append(read + "\n");
				read = in.readLine();
			}
		
			shader.deleteCharAt(shader.length() - 1);
		} catch (Exception e) {
			throw new DevtimeError("Could not read shader: " + e.getLocalizedMessage(), e);
		}
		
		return shader.toString();
    }
    
    public static int loadShader(int type, String shaderCode){
 
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
 
        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
 
        // return the shader
        return shader;
    }

    public static int loadShader(Context context, int type, String path) {
    	return loadShader(type, readShaderInAssets(context, path));
    }
    
    public static int loadShader(Context context, int type, int resId) {
    	return loadShader(type, readShaderInResource(context, resId));
    }

}
