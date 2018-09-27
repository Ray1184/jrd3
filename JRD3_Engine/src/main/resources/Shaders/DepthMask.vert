/*
    Vertex shader for depth mask.

    Written by: Ray1184
    Last update: 2017-10-17
 */

#version 330 core

layout(location = 0) in vec3 position;

out vec2 uvTex;

void main()
{
    gl_Position = vec4(position, 1);
	uvTex = (position.xy + vec2(1, 1)) / 2.0;
}

