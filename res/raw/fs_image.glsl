precision mediump float;

uniform sampler2D texture;

varying vec2 v_texture_coord;

void main() {
  gl_FragColor = texture2D( texture, v_texture_coord );
}
