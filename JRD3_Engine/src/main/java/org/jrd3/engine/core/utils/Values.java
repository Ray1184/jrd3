package org.jrd3.engine.core.utils;

/**
 * All core inner constants.
 *
 * @author Ray1184
 * @version 1.0
 */
public interface Values {

    // Fonts directories.
    String SHADERS_DIR = "/Shaders/";
    String TEXTURES_DIR = "/Textures/";
    String SCREENS_DIR = "/Screens/";
    String MODELS_DIR = "/Models/";
    String FONTS_DIR = "/Models/";
    String AUDIO_DIR = "/Audio/";


    // Default shaders.
    String SHADER_FILE_PARTICLES_VERT = Values.SHADERS_DIR + "Particles.vert";
    String SHADER_FILE_PARTICLES_FRAG = Values.SHADERS_DIR + "Particles.frag";
    String SHADER_FILE_PIXELFX_VERT = Values.SHADERS_DIR + "PostFX.vert";
    String SHADER_FILE_PIXELFX_FRAG = Values.SHADERS_DIR + "PostFX.frag";
    String SHADER_FILE_SCENE_VERT = Values.SHADERS_DIR + "Scene.vert";
    String SHADER_FILE_SCENE_FRAG = Values.SHADERS_DIR + "Scene.frag";
    String SHADER_FILE_PICTURE_VERT = Values.SHADERS_DIR + "Picture.vert";
    String SHADER_FILE_PICTURE_FRAG = Values.SHADERS_DIR + "Picture.frag";
    String SHADER_FILE_DEPTHMASK_VERT = Values.SHADERS_DIR + "DepthMask.vert";
    String SHADER_FILE_DEPTHMASK_FRAG = Values.SHADERS_DIR + "DepthMask.frag";

    // Composite material shader uniform.
    String UNIFORM_DIFFUSE_SFX = ".diffuse";
    String UNIFORM_HASTEXTURE_SFX = ".hasTexture";

    // Simple shader uniform list.
    String UNIFORM_VIEWMATRIX = "viewMatrix";
    String UNIFORM_PROJMATRIX = "projectionMatrix";
    String UNIFORM_PROJMODELMATRIX = "projModelMatrix";
    String UNIFORM_MODELVIEWMATRIX = "modelViewMatrix";
    String UNIFORM_TEXSAMPLER = "textureSampler";
    String UNIFORM_NORMALSAMPLER = "normalSampler";
    String UNIFORM_NUMCOLS = "numCols";
    String UNIFORM_NUMROWS = "numRows";
    String UNIFORM_AMBIENTLIGHT = "ambientLight";
    String UNIFORM_COLOUR = "colour";
    String UNIFORM_HASTEXTURE = "hasTexture";
    String UNIFORM_MATERIAL = "material";
    String UNIFORM_NONINSTMATRIX = "modelNonInstancedMatrix";
    String UNIFORM_INSTANCED = "instanced";
    String UNIFORM_JOINTSMATRIX = "jointsMatrix";
    String UNIFORM_ZNEAR = "zNear";
    String UNIFORM_ZFAR = "zFar";
    String UNIFORM_ALPHA = "alpha";
    String UNIFORM_DIFFUSEINTENSITY = "diffuseIntensity";
    String UNIFORM_X = "x";
    String UNIFORM_Y = "y";
    String UNIFORM_GLOBALALPHA = "globalAlpha";

    // Consts
    String K_GLOBAL_ALPHA = "KGALPHA";
}
