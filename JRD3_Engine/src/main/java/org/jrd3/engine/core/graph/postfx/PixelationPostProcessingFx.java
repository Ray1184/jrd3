package org.jrd3.engine.core.graph.postfx;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.graph.ShaderProgram;

import java.util.Map;

import static org.jrd3.engine.core.utils.Values.*;

public class PixelationPostProcessingFx implements PostProcessingFx {

    private final float width;
    private final float height;
    private final float ratio;

    public PixelationPostProcessingFx(float width, float height, float ratio) {
        this.width = width;
        this.height = height;
        this.ratio = ratio;
    }

    @Override
    public String getVertexShaderFilename() {
        return SHADER_FILE_PIXELFX_VERT;
    }

    @Override
    public String getFragmentShaderFilename() {
        return SHADER_FILE_PIXELFX_FRAG;
    }

    @Override
    public void defineShader(ShaderProgram shaderProgram) throws JRD3Exception {
        shaderProgram.createUniform(UNIFORM_TEXSAMPLER);
        shaderProgram.createUniform(UNIFORM_GLOBALALPHA);
    }

    @Override
    public void postProcess(ShaderProgram shaderProgram, Map<String, Object> params) {
        shaderProgram.setUniform(UNIFORM_TEXSAMPLER, 0);

        float alpha = 0.0f;
        if (params != null) {
            alpha = (float) params.getOrDefault(K_GLOBAL_ALPHA, 0.0f);
            shaderProgram.setUniform(UNIFORM_GLOBALALPHA, alpha);
        }
    }

    @Override
    public float getOffscreenWidth() {
        return width;
    }

    @Override
    public float getOffscreeHeight() {
        return height;
    }

    @Override
    public float getRatio() {
        return ratio;
    }
}
