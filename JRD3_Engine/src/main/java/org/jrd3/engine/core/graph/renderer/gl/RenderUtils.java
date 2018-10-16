package org.jrd3.engine.core.graph.renderer.gl;

import org.joml.Matrix4f;
import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.graph.*;
import org.jrd3.engine.core.graph.anim.AnimNode;
import org.jrd3.engine.core.graph.anim.AnimatedFrame;
import org.jrd3.engine.core.graph.particles.ParticleEmitter;
import org.jrd3.engine.core.items.AnimModelItem;
import org.jrd3.engine.core.items.ModelItem;
import org.jrd3.engine.core.items.Picture;
import org.jrd3.engine.core.scene.Scene;
import org.jrd3.engine.core.sim.Window;
import org.jrd3.engine.core.utils.CommonUtils;

import java.util.List;
import java.util.Map;

import static org.jrd3.engine.core.utils.Values.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * All common rendering functions.
 *
 * @author Ray1184
 * @version 1.0
 */
public class RenderUtils {

    /**
     * @return
     * @throws JRD3Exception
     */
    public static ShaderProgram createPicturesShader() throws JRD3Exception {
        ShaderProgram picturesShaderProgram = new ShaderProgram();
        picturesShaderProgram.createVertexShader(CommonUtils.loadResource(SHADER_FILE_PICTURE_VERT));
        picturesShaderProgram.createFragmentShader(CommonUtils.loadResource(SHADER_FILE_PICTURE_FRAG));
        picturesShaderProgram.link();

        picturesShaderProgram.createUniform(UNIFORM_TEXSAMPLER);
        picturesShaderProgram.createUniform(UNIFORM_ALPHA);
        picturesShaderProgram.createUniform(UNIFORM_X);
        picturesShaderProgram.createUniform(UNIFORM_Y);
        return picturesShaderProgram;
    }


    /**
     * @return
     * @throws JRD3Exception
     */
    public static ShaderProgram createDepthMaskShader() throws JRD3Exception {
        ShaderProgram depthShaderProgram = new ShaderProgram();
        depthShaderProgram.createVertexShader(CommonUtils.loadResource(SHADER_FILE_DEPTHMASK_VERT));
        depthShaderProgram.createFragmentShader(CommonUtils.loadResource(SHADER_FILE_DEPTHMASK_FRAG));
        depthShaderProgram.link();

        depthShaderProgram.createUniform(UNIFORM_TEXSAMPLER);
        depthShaderProgram.createUniform(UNIFORM_PROJMATRIX);
        depthShaderProgram.createUniform(UNIFORM_ZNEAR);
        depthShaderProgram.createUniform(UNIFORM_ZFAR);
        return depthShaderProgram;
    }

    /**
     * @return
     * @throws JRD3Exception
     */
    public static ShaderProgram createParticlesShader() throws JRD3Exception {
        ShaderProgram particlesShaderProgram = new ShaderProgram();
        particlesShaderProgram.createVertexShader(CommonUtils.loadResource(SHADER_FILE_PARTICLES_VERT));
        particlesShaderProgram.createFragmentShader(CommonUtils.loadResource(SHADER_FILE_PARTICLES_FRAG));
        particlesShaderProgram.link();

        particlesShaderProgram.createUniform(UNIFORM_VIEWMATRIX);
        particlesShaderProgram.createUniform(UNIFORM_PROJMATRIX);
        particlesShaderProgram.createUniform(UNIFORM_TEXSAMPLER);
        particlesShaderProgram.createUniform(UNIFORM_NUMCOLS);
        particlesShaderProgram.createUniform(UNIFORM_NUMROWS);
        return particlesShaderProgram;
    }

    /**
     * @return
     * @throws JRD3Exception
     */
    public static ShaderProgram createSceneShader() throws JRD3Exception {
        ShaderProgram sceneShaderProgram = new ShaderProgram();
        sceneShaderProgram.createVertexShader(CommonUtils.loadResource(SHADER_FILE_SCENE_VERT));
        sceneShaderProgram.createFragmentShader(CommonUtils.loadResource(SHADER_FILE_SCENE_FRAG));
        sceneShaderProgram.link();

        sceneShaderProgram.createUniform(UNIFORM_VIEWMATRIX);
        sceneShaderProgram.createUniform(UNIFORM_PROJMATRIX);
        sceneShaderProgram.createUniform(UNIFORM_TEXSAMPLER);
        sceneShaderProgram.createUniform(UNIFORM_NONINSTMATRIX);
        sceneShaderProgram.createMaterialUniform(UNIFORM_MATERIAL);
        sceneShaderProgram.createUniform(UNIFORM_AMBIENTLIGHT);
        sceneShaderProgram.createUniform(UNIFORM_JOINTSMATRIX);
        sceneShaderProgram.createUniform(UNIFORM_DIFFUSEINTENSITY);
        return sceneShaderProgram;
    }


    /**
     *
     */
    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    /**
     * @param window
     * @param camera
     * @param scene
     * @param shaderProgram
     * @param transformation
     */
    public static void renderParticles(Window window, Camera camera, Scene scene,
                                       ShaderProgram shaderProgram, Transformation transformation) {

        shaderProgram.bind();

        Matrix4f viewMatrix = camera.getViewMatrix();
        shaderProgram.setUniform(UNIFORM_VIEWMATRIX, viewMatrix);
        shaderProgram.setUniform(UNIFORM_TEXSAMPLER, 0);
        Matrix4f projectionMatrix = window.getProjectionMatrix();
        shaderProgram.setUniform(UNIFORM_PROJMATRIX, projectionMatrix);

        ParticleEmitter[] emitters = scene.getParticleEmitters();
        int numEmitters = emitters != null ? emitters.length : 0;

        glDepthMask(false);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);

        for (int i = 0; i < numEmitters; i++) {
            ParticleEmitter emitter = emitters[i];
            InstancedMesh mesh = (InstancedMesh) emitter.getBaseParticle().getMesh();

            Texture text = mesh.getMaterial().getTexture();
            shaderProgram.setUniform(UNIFORM_NUMCOLS, text.getNumCols());
            shaderProgram.setUniform(UNIFORM_NUMROWS, text.getNumRows());

            mesh.renderListInstanced(emitter.getParticles(), true, transformation, viewMatrix);
        }

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(true);

        shaderProgram.unbind();

    }

    /**
     * @param window
     * @param camera
     * @param scene
     * @param shaderProgram
     * @param transformation
     */
    public static void renderScene(Window window, Camera camera, Scene scene,
                                   ShaderProgram shaderProgram, Transformation transformation) {
        renderScene(window, camera, scene, shaderProgram, transformation, false);

    }

    /**
     * @param window
     * @param camera
     * @param scene
     * @param shaderProgram
     * @param transformation
     * @param gui
     */
    public static void renderScene(Window window, Camera camera, Scene scene,
                                   ShaderProgram shaderProgram, Transformation transformation, boolean gui) {
        shaderProgram.bind();

        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = window.getProjectionMatrix();


        shaderProgram.setUniform(UNIFORM_VIEWMATRIX, viewMatrix);
        shaderProgram.setUniform(UNIFORM_PROJMATRIX, projectionMatrix);
        shaderProgram.setUniform(UNIFORM_DIFFUSEINTENSITY, gui ? 1.0f : 0.1f);

        SceneLight sceneLight = scene.getSceneLight();
        renderLights(shaderProgram, sceneLight);

        shaderProgram.setUniform(UNIFORM_TEXSAMPLER, 0);

        scene.getRootNode().updateGraph();
        renderNonInstancedMeshes(scene, shaderProgram, transformation);

        // TODO - FIX instanced
        // renderInstancedMeshes(scene, viewMatrix, shaderProgram, transformation, filteredItems);
        shaderProgram.unbind();
    }

    public static void renderDepthLayers(Window window, Scene scene, ShaderProgram shaderProgram) {
        if (scene.getDepthMask() != null) {
            shaderProgram.bind();
            shaderProgram.setUniform(UNIFORM_TEXSAMPLER, 0);
            shaderProgram.setUniform(UNIFORM_PROJMATRIX, window.getProjectionMatrix());
            shaderProgram.setUniform(UNIFORM_ZNEAR, window.getPerspective().zNear);
            shaderProgram.setUniform(UNIFORM_ZFAR, window.getPerspective().zFar);
            scene.getDepthMask().render();
            shaderProgram.unbind();
        }
    }

    /**
     * @param scene
     * @param foreground
     * @param shaderProgram
     */
    public static void renderPictures(Scene scene, boolean foreground, ShaderProgram shaderProgram) {
        //glDepthMask(false);
        glDisable(GL_DEPTH_TEST);

        shaderProgram.bind();
        shaderProgram.setUniform(UNIFORM_TEXSAMPLER, 0);

        List<Picture> pics = foreground ? scene.getForePictureList() : scene.getBackPictureList();

        for (Picture pic : pics) {
            if (pic != null) {
                shaderProgram.setUniform(UNIFORM_ALPHA, pic.getAlpha());
                shaderProgram.setUniform(UNIFORM_X, pic.getPosition().x);
                shaderProgram.setUniform(UNIFORM_Y, -pic.getPosition().y);
                pic.render();
            }
        }

        shaderProgram.unbind();
        //glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
    }


    /**
     * @param shaderProgram
     * @param sceneLight
     */
    private static void renderLights(ShaderProgram shaderProgram, SceneLight sceneLight) {
        if (sceneLight != null) {
            shaderProgram.setUniform(UNIFORM_AMBIENTLIGHT, sceneLight.getAmbientLight());
        }
    }

    /**
     * @param scene
     * @param viewMatrix
     * @param shaderProgram
     * @param transformation
     * @param filteredItems
     */
    private static void renderInstancedMeshes(Scene scene, Matrix4f viewMatrix,
                                              ShaderProgram shaderProgram, Transformation transformation,
                                              List<ModelItem> filteredItems) {
        shaderProgram.setUniform(UNIFORM_INSTANCED, 1);


        Map<InstancedMesh, List<ModelItem>> mapMeshes = scene.getGameInstancedMeshes();
        for (InstancedMesh mesh : mapMeshes.keySet()) {
            Texture text = mesh.getMaterial().getTexture();
            if (text != null) {
                shaderProgram.setUniform(UNIFORM_NUMCOLS, text.getNumCols());
                shaderProgram.setUniform(UNIFORM_NUMROWS, text.getNumRows());
            }

            shaderProgram.setUniform(UNIFORM_MATERIAL, mesh.getMaterial());

            filteredItems.clear();
            for (ModelItem modelItem : mapMeshes.get(mesh)) {
                if (modelItem.isInsideFrustum()) {
                    filteredItems.add(modelItem);
                }
            }


            mesh.renderListInstanced(filteredItems, transformation, viewMatrix);
        }
    }

    /**
     * @param scene
     * @param shaderProgram
     * @param transformation
     */
    private static void renderNonInstancedMeshes(Scene scene, ShaderProgram shaderProgram,
                                                 Transformation transformation) {

        Map<Mesh, List<ModelItem>> mapMeshes = scene.getGameMeshes();
        for (Mesh mesh : mapMeshes.keySet()) {
            shaderProgram.setUniform(UNIFORM_MATERIAL, mesh.getMaterial());

            mesh.renderList(mapMeshes.get(mesh), (ModelItem modelItem) -> {

                Matrix4f modelMatrix = transformation.buildModelMatrix(modelItem);
                shaderProgram.setUniform(UNIFORM_NONINSTMATRIX, modelMatrix);
                if (modelItem instanceof AnimModelItem) {
                    AnimModelItem animGameItem = (AnimModelItem) modelItem;
                    if (animGameItem.getCurrentAnimation() != null) {
                        // TEST ONLY
                        AnimNode n = animGameItem.getRootAnimNode().findByNameInternal("Bone_004");
                        if (n != null) {
                            n.updateAnimationGraph(animGameItem.getCurrentAnimation().getCurrentFrameIndex());
                        }
                        // END TEST
                        AnimatedFrame frame = animGameItem.getCurrentAnimation().getCurrentFrame();
                        shaderProgram.setUniform(UNIFORM_JOINTSMATRIX, frame.getJointMatrices());
                    }
                }
            });
        }
    }
}
