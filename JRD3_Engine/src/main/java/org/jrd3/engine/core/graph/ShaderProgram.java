package org.jrd3.engine.core.graph;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.jrd3.engine.core.utils.Values.UNIFORM_DIFFUSE_SFX;
import static org.jrd3.engine.core.utils.Values.UNIFORM_HASTEXTURE_SFX;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private final int programId;
    private final Map<String, Integer> uniforms;
    private int vertexShaderId;
    private int fragmentShaderId;

    /**
     * Constructs a new ShaderProgram.
     */
    public ShaderProgram() throws JRD3Exception {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new JRD3Exception("Could not create Shader");
        }
        uniforms = new HashMap<>();
    }

    /**
     * @param uniformName
     * @throws JRD3Exception
     */
    public void createUniform(String uniformName) throws JRD3Exception {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            // TODO - Log this instead of std output.
            System.out.println("WARN: " + uniformName + " seems not used.");
            //throw new JRD3Exception("Could not find uniform: " + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }


    /**
     * @param uniformName
     * @throws JRD3Exception
     */
    public void createMaterialUniform(String uniformName) throws JRD3Exception {
        createUniform(uniformName + UNIFORM_DIFFUSE_SFX);
        createUniform(uniformName + UNIFORM_HASTEXTURE_SFX);
    }

    /**
     * @param uniformName
     * @param value
     */
    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Dump the matrix into a float buffer
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        }
    }

    /**
     * @param uniformName
     * @param matrices
     */
    public void setUniform(String uniformName, Matrix4f[] matrices) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            int length = matrices != null ? matrices.length : 0;
            FloatBuffer fb = stack.mallocFloat(16 * length);
            for (int i = 0; i < length; i++) {
                matrices[i].get(16 * i, fb);
            }
            glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        }
    }

    /**
     * @param uniformName
     * @param value
     */
    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    /**
     * @param uniformName
     * @param value
     */
    public void setUniform(String uniformName, float value) {
        glUniform1f(uniforms.get(uniformName), value);
    }


    /**
     * @param uniformName
     * @param value
     */
    public void setUniform(String uniformName, Vector3f value) {
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    /**
     * @param uniformName
     * @param value
     */
    public void setUniform(String uniformName, Vector4f value) {
        glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }


    /**
     * @param uniformName
     * @param material
     */
    public void setUniform(String uniformName, Material material) {
        setUniform(uniformName + UNIFORM_DIFFUSE_SFX, material.getDiffuseColour());
        setUniform(uniformName + UNIFORM_HASTEXTURE_SFX, material.isTextured() ? 1 : 0);
    }


    /**
     * @param shaderCode
     * @throws JRD3Exception
     */
    public void createVertexShader(String shaderCode) throws JRD3Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    /**
     * @param shaderCode
     * @throws JRD3Exception
     */
    public void createFragmentShader(String shaderCode) throws JRD3Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    /**
     * @param shaderCode
     * @param shaderType
     * @return
     * @throws JRD3Exception
     */
    protected int createShader(String shaderCode, int shaderType) throws JRD3Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new JRD3Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new JRD3Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    /**
     * @throws JRD3Exception
     */
    public void link() throws JRD3Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new JRD3Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    /**
     *
     */
    public void bind() {
        glUseProgram(programId);
    }

    /**
     *
     */
    public void unbind() {
        glUseProgram(0);
    }

    /**
     *
     */
    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
