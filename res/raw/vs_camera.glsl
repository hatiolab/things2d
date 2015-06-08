uniform mat4 transformer;

attribute vec4 vertices;
attribute vec2 texture_coord;

varying vec2 v_texture_coord;

void main() {
  gl_Position = transformer * vertices;
  v_texture_coord = texture_coord;
}