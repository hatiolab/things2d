uniform    mat4        transformer;
attribute  vec4        vertices;

void main() {
  gl_Position = transformer * vertices;
}
