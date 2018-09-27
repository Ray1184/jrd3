package org.jrd3.engine.core.graph;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.graph.postfx.PostProcessingFx;
import org.jrd3.engine.core.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


/**
 * Frame buffer object wrapper for managing post-processing effects.
 *
 * @author Ray1184
 * @version 1.0
 */
public class FrameBuffer {

    /**
     * The buffer width.
     */
    private final int width;
    /**
     * The buffer height.
     */
    private final int height;
    /**
     * The post processing effect.
     */
    private final PostProcessingFx fx;

    /**
     * Additional FX params.
     */
    private final Map<String, Object> params;

    /**
     * The frame buffer handle.
     */
    private int fboHandle;
    /**
     * The off screen texture handle.
     */
    private int texHandle;
    /**
     * The quad VBO handle.
     */
    private int vboHandle;
    /**
     * Flag for deleted mesh.
     */
    private boolean deleted;
    /**
     * The framebuffer post effect shader.
     */
    private ShaderProgram fboShaderProgram;


    /**
     * Default constructor.
     *
     * @param width  The buffer width.
     * @param height The buffer height.
     */
    public FrameBuffer(int width, int height, PostProcessingFx fx) {
        this.width = width;
        this.height = height;
        this.fx = fx;
        params = new HashMap<>();
    }


    /**
     * Gets the off-screen rendering.
     *
     * @throws JRD3Exception
     */
    public void prepareOffscreenRendering() throws JRD3Exception {
        fboHandle = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboHandle);

        texHandle = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texHandle);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                (java.nio.ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        int rboHandle = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboHandle);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);

        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboHandle);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texHandle, 0);

        glDrawBuffers(GL_COLOR_ATTACHMENT0);

        int stat = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if (GL_FRAMEBUFFER_COMPLETE != stat) {
            throw new JRD3Exception("Frame buffer error!");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        vboHandle = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
        glBufferData(GL_ARRAY_BUFFER, Shapes.QUAD.getData(), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        fboShaderProgram = new ShaderProgram();
        if (fx == null || fx.getFragmentShaderFilename() == null || fx.getVertexShaderFilename() == null) {
            throw new JRD3Exception("Framebuffer shader files are not defined.");
        }
        fboShaderProgram.createVertexShader(CommonUtils.loadResource(fx.getVertexShaderFilename()));
        fboShaderProgram.createFragmentShader(CommonUtils.loadResource(fx.getFragmentShaderFilename()));
        fboShaderProgram.link();
        fx.defineShader(fboShaderProgram);


    }

    /**
     * Starts to record the screen frame.
     */
    public void preRendering() {
        glViewport(0, 0, (int) (fx.getOffscreenWidth() / fx.getRatio()), (int) (fx.getOffscreeHeight() / fx.getRatio()));
        glBindFramebuffer(GL_FRAMEBUFFER, fboHandle);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    }

    /**
     * Stops to record the screen frame.
     */
    public void postRendering() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glViewport(0, 0, (int) (fx.getOffscreenWidth() * fx.getRatio()), (int) (fx.getOffscreeHeight() * fx.getRatio()));
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        fboShaderProgram.bind();
        fx.postProcess(fboShaderProgram, params);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texHandle);


        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glDisableVertexAttribArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }


    /**
     * Deletes the frame buffer.
     */
    public void cleanup() {
        if (!deleted) {
            deleted = true;
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
            glDeleteFramebuffers(fboHandle); // TODO - Check if ok!
        }
        fboShaderProgram.cleanup();
    }

    /**
     * Gets the FBO handle.
     *
     * @return The FBO Handle.
     */
    public int getFboHandle() {
        return fboHandle;
    }

    /**
     * Gets the off screen texture handle.
     *
     * @return The off screen texture handle.
     */
    public int getOffscreenTexHandle() {
        return texHandle;
    }

    /**
     * Gets the quad vertex buffer.
     *
     * @return The VBO handle.
     */
    public int getVboHandle() {
        return vboHandle;
    }

    /**
     * Gets the FBO width.
     *
     * @return The width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the FBO height.
     *
     * @return The height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Getter for property 'params'.
     *
     * @return Value for property 'params'.
     */
    public Map<String, Object> getParams() {
        return params;
    }


}
