/*
    Fragment shader for pictures.

    Written by: Ray1184
    Last update: 2017-10-17
 */

#version 330 core

in vec2 uvTex;

out vec4 color;

uniform sampler2D textureSampler;
uniform float alpha;

void main()
{
	vec4 tempColor = texture(textureSampler, uvTex);
	color = vec4(tempColor.x, tempColor.y, tempColor.z, tempColor.w * alpha);
}