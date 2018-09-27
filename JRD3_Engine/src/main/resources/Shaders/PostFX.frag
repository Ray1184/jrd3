/*
    Fragment shader for post FX.

    Written by: Ray1184
    Last update: 2016-04-17
 */

#version 330 core

in vec2 uvTex;

out vec4 color;

uniform sampler2D textureSampler;
uniform float globalAlpha;

void main()
{
	vec4 tempColor = texture(textureSampler, uvTex);
	color = vec4(tempColor.x, tempColor.y, tempColor.z, globalAlpha);
}