/*
    Fragment shader for depth mask.

    Written by: Ray1184
    Last update: 2017-10-17
 */

#version 330 core

in vec2 uvTex;

uniform mat4 projectionMatrix;
uniform sampler2D textureSampler;
uniform float zNear;
uniform float zFar;

void main() 
{
   float imageDepth = texture2D(textureSampler, uvTex).r;
   imageDepth = -imageDepth;
   vec4 temp = vec4(0.0, 0.0, (imageDepth * (zFar - zNear) - zNear), 1.0);
   vec4 clipSpace = projectionMatrix * temp;
   clipSpace.z /= clipSpace.w;
   clipSpace.z = 0.5 * (clipSpace.z + 1.0);
   float depth = clipSpace.z;
   gl_FragDepth = depth;
}