package org.jrd3.engine.core.graph.postfx;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.graph.ShaderProgram;

import java.util.Map;

/**
 * Post processing interface for custom effect post rendering.
 *
 * @author Ray1184
 * @version 1.0
 */
public interface PostProcessingFx {

    /**
     * Vertex shader acquirement.
     *
     * @return The vertex shader filename.
     */
    String getVertexShaderFilename();

    /**
     * Fragment shader acquirement.
     *
     * @return The fragment shader filename.
     */
    String getFragmentShaderFilename();

    /**
     * Defines shader uniforms.
     *
     * @param shaderProgram The shader.
     */
    void defineShader(ShaderProgram shaderProgram) throws JRD3Exception;

    /**
     * Post FX shader update.
     *
     * @param shaderProgram The shader.
     * @param params Additional params.
     */
    void postProcess(ShaderProgram shaderProgram, Map<String, Object> params);

    /**
     * Retrives the offscreen buffer width.
     *
     * @return The width.
     */
    float getOffscreenWidth();

    /**
     * Retrives the offscreen buffer height.
     *
     * @return The height.
     */
    float getOffscreeHeight();

    /**
     * Retrives the pixel ratio.
     *
     * @return The pixel ratio.
     */
    float getRatio();


}
