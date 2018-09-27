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

import java.nio.DoubleBuffer;

public class GUI2DRenderer implements Renderer {

    private final Transformation transformation;
    private final PostProcessingFx postProcessingFx;
    private ShaderProgram sceneShaderProgram;
    private ShaderProgram picturesShaderProgram;
    private long vgHandle;
    private DoubleBuffer mouseX;
    private DoubleBuffer mouseY;
    private Window window;
    private FrameBuffer frameBuffer;

    public GUI2DRenderer() {
        this(null);
    }


    public GUI2DRenderer(PostProcessingFx postProcessingFx) {
        transformation = new Transformation();
        this.postProcessingFx = postProcessingFx;
    }

    @Override
    public void init(Window window, Scene scene) throws JRD3Exception {
        this.window = window;

        setupImagesShader();
        setupSceneShader();

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

    private void setupSceneShader() throws JRD3Exception {
        sceneShaderProgram = RenderUtils.createSceneShader();
    }

    private void setupFrameBuffer(PostProcessingFx postProcessingFx) throws JRD3Exception {
        frameBuffer = new FrameBuffer(window.getWidth(), window.getHeight(), postProcessingFx);
        frameBuffer.prepareOffscreenRendering();
    }


    private void renderScene(Window window, Camera camera, Scene scene) {
        RenderUtils.renderScene(window, camera, scene,
                sceneShaderProgram, transformation, true);
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
