package org.jrd3.engine.core.graph.renderer;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.graph.Camera;
import org.jrd3.engine.core.graph.FrameBuffer;
import org.jrd3.engine.core.graph.ShaderProgram;
import org.jrd3.engine.core.graph.Transformation;
import org.jrd3.engine.core.graph.postfx.PostProcessingFx;
import org.jrd3.engine.core.graph.renderer.gl.RenderUtils;
import org.jrd3.engine.core.scene.Scene;
import org.jrd3.engine.core.sim.Window;
import org.jrd3.engine.core.utils.Values;

public class Full3DRenderer implements Renderer {

    private final Transformation transformation;
    private final PostProcessingFx postProcessingFx;
    private ShaderProgram sceneShaderProgram;
    private ShaderProgram particlesShaderProgram;
    private ShaderProgram picturesShaderProgram;
    private Window window;
    private FrameBuffer frameBuffer;

    public Full3DRenderer() {
        this(null);
    }


    public Full3DRenderer(PostProcessingFx postProcessingFx) {
        transformation = new Transformation();
        this.postProcessingFx = postProcessingFx;
    }

    @Override
    public void init(Window window, Scene scene) throws JRD3Exception {
        this.window = window;
        setupImagesShader();
        setupSceneShader();
        setupParticlesShader();

        if (postProcessingFx != null) {
            setupFrameBuffer(postProcessingFx);
        }
    }

    @Override
    public void render(Window window, Camera camera, Scene scene) {
        if (postProcessingFx != null) {
            preRenderingProcess();
        }
        RenderUtils.clear();
        window.updateProjectionMatrix();


        renderPictures(scene, false);
        renderScene(window, camera, scene);
        renderParticles(window, camera, scene);
        renderPictures(scene, true);


        if (postProcessingFx != null) {
            frameBuffer.getParams().put(Values.K_GLOBAL_ALPHA, scene.getGlobalAlpha());
            postRenderingProcessing();
        }
    }

    @Override
    public void cleanup() {

        if (sceneShaderProgram != null) {
            sceneShaderProgram.cleanup();
        }
        if (particlesShaderProgram != null) {
            particlesShaderProgram.cleanup();
        }
        if (picturesShaderProgram != null) {
            picturesShaderProgram.cleanup();
        }
        if (frameBuffer != null) {
            frameBuffer.cleanup();
        }
    }

    private void setupImagesShader() throws JRD3Exception {
        picturesShaderProgram = RenderUtils.createPicturesShader();
    }

    private void setupParticlesShader() throws JRD3Exception {
        particlesShaderProgram = RenderUtils.createParticlesShader();
    }

    private void setupSceneShader() throws JRD3Exception {
        sceneShaderProgram = RenderUtils.createSceneShader();
    }

    private void setupFrameBuffer(PostProcessingFx postProcessingFx) throws JRD3Exception {
        frameBuffer = new FrameBuffer(window.getWidth(), window.getHeight(), postProcessingFx);
        frameBuffer.prepareOffscreenRendering();
    }



    private void renderParticles(Window window, Camera camera, Scene scene) {
        RenderUtils.renderParticles(window, camera, scene, particlesShaderProgram, transformation);
    }

    private void renderScene(Window window, Camera camera, Scene scene) {
        RenderUtils.renderScene(window, camera, scene,
                sceneShaderProgram, transformation);
    }


    private void renderPictures(Scene scene, boolean foreground) {
        RenderUtils.renderPictures(scene, foreground, picturesShaderProgram);
    }



    /**
     * Starts the FBO recording.
     */
    private void preRenderingProcess() {
        frameBuffer.preRendering();


    }

    /**
     * Shows the off-screen rendering.
     */
    private void postRenderingProcessing() {
        frameBuffer.postRendering();

    }
}
