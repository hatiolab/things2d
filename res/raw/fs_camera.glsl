#extension GL_OES_EGL_image_external : require

precision mediump float;

uniform samplerExternalOES texture;
varying vec2 v_texture_coord;

void main() {
	gl_FragColor = texture2D(texture, v_texture_coord);
}