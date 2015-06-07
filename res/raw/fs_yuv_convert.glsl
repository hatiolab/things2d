precision mediump float;

varying vec2 v_texture_coord;

uniform sampler2D y_texture;
uniform sampler2D u_texture;
uniform sampler2D v_texture;

void main()
{   
    float y = texture2D(y_texture, v_texture_coord).r;
    float u = texture2D(u_texture, v_texture_coord).r - 0.5;
    float v = texture2D(v_texture, v_texture_coord).r - 0.5;

    float r = y + 1.13983 * v;
    float g = y - 0.39465 * u - 0.58060 * v;
    float b = y + 2.03211 * u;

    gl_FragColor = vec4(r, g, b, 1.0);
}