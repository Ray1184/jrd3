package org.jrd3.engine.core.graph;

import org.jrd3.engine.core.items.ModelItem;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private static int debMeshCount;

    public static final int MAX_WEIGHTS = 4;

    protected final int vaoId;

    protected final List<Integer> vboIdList;

    private final int vertexCount;

    private Material material;

    private float boundingRadius;

    private String name;

    public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
        this(positions, textCoords, normals, indices, createEmptyIntArray(MAX_WEIGHTS * positions.length / 3, 0), createEmptyFloatArray(MAX_WEIGHTS * positions.length / 3, 0));
    }

    public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices, int[] jointIndices, float[] weights) {
        FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        FloatBuffer vecNormalsBuffer = null;
        FloatBuffer weightsBuffer = null;
        IntBuffer jointIndicesBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            calculateBoundingRadius(positions);

            vertexCount = indices.length;
            vboIdList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Texture coordinates VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Vertex normals VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            vecNormalsBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            // Weights
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            weightsBuffer = MemoryUtil.memAllocFloat(weights.length);
            weightsBuffer.put(weights).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, weightsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(3, 4, GL_FLOAT, false, 0, 0);

            // Joint indices
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            jointIndicesBuffer = MemoryUtil.memAllocInt(jointIndices.length);
            jointIndicesBuffer.put(jointIndices).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, jointIndicesBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(4, 4, GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
            }
            if (vecNormalsBuffer != null) {
                MemoryUtil.memFree(vecNormalsBuffer);
            }
            if (weightsBuffer != null) {
                MemoryUtil.memFree(weightsBuffer);
            }
            if (jointIndicesBuffer != null) {
                MemoryUtil.memFree(jointIndicesBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
        Mesh.debMeshCount++;
    }

    protected static float[] createEmptyFloatArray(int length, float defaultValue) {
        float[] result = new float[length];
        Arrays.fill(result, defaultValue);
        return result;
    }

    protected static int[] createEmptyIntArray(int length, int defaultValue) {
        int[] result = new int[length];
        Arrays.fill(result, defaultValue);
        return result;
    }

    private void calculateBoundingRadius(float positions[]) {
        int length = positions.length;
        boundingRadius = 0;
        for (int i = 0; i < length; i++) {
            float pos = positions[i];
            boundingRadius = Math.max(Math.abs(pos), boundingRadius);
        }
    }

    /**
     * Getter for property 'material'.
     *
     * @return Value for property 'material'.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Setter for property 'material'.
     *
     * @param material Value to set for property 'material'.
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Getter for property 'vaoId'.
     *
     * @return Value for property 'vaoId'.
     */
    public final int getVaoId() {
        return vaoId;
    }

    /**
     * Getter for property 'vertexCount'.
     *
     * @return Value for property 'vertexCount'.
     */
    public int getVertexCount() {
        return vertexCount;
    }

    /**
     * Getter for property 'boundingRadius'.
     *
     * @return Value for property 'boundingRadius'.
     */
    public float getBoundingRadius() {
        return boundingRadius;
    }

    /**
     * Setter for property 'boundingRadius'.
     *
     * @param boundingRadius Value to set for property 'boundingRadius'.
     */
    public void setBoundingRadius(float boundingRadius) {
        this.boundingRadius = boundingRadius;
    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for property 'name'.
     *
     * @param name Value to set for property 'name'.
     */
    public void setName(String name) {
        this.name = name;
    }

    protected void initRender() {
        Texture texture = material != null ? material.getTexture() : null;
        if (texture != null) {
            // Activate sectorA texture bank
            glActiveTexture(GL_TEXTURE0);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }
        Texture normalMap = material != null ? material.getNormalMap() : null;
        if (normalMap != null) {
            // Activate sectorB texture bank
            glActiveTexture(GL_TEXTURE1);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, normalMap.getId());
        }

        // Draw the mesh
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
        glEnableVertexAttribArray(4);
    }

    protected void endRender() {
        // Restore state
        glDisableVertexAttribArray(4);
        glDisableVertexAttribArray(3);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void render() {


        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

    }

    public void renderList(List<ModelItem> modelItems, Consumer<ModelItem> consumer) {
        initRender();

        for (ModelItem modelItem : modelItems) {
            if (modelItem.isInsideFrustum()) {
                consumer.accept(modelItem);
                glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
            }
        }
        endRender();
    }

    /**
     * Debug utils.
     *
     * @return All meshes.
     */
    public static int getDebMeshCount() {
        return Mesh.debMeshCount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mesh mesh = (Mesh) o;

        if (vaoId != mesh.vaoId) return false;
        if (vertexCount != mesh.vertexCount) return false;
        if (Float.compare(mesh.boundingRadius, boundingRadius) != 0) return false;
        if (vboIdList != null ? !vboIdList.equals(mesh.vboIdList) : mesh.vboIdList != null) return false;
        return material != null ? material.equals(mesh.material) : mesh.material == null;
    }

    @Override
    public int hashCode() {
        int result = vaoId;
        result = 31 * result + (vboIdList != null ? vboIdList.hashCode() : 0);
        result = 31 * result + vertexCount;
        result = 31 * result + (material != null ? material.hashCode() : 0);
        result = 31 * result + (boundingRadius != +0.0f ? Float.floatToIntBits(boundingRadius) : 0);
        return result;
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // IMPORTANT!!!
        // Delete texture not performed here, because delete
        // process is managed from central texture cache.
        // Just mark as no longer used.
        Texture texture = material.getTexture();
        if (texture != null) {
            texture.setCurrentlyUsed(false);
            // texture.cleanup();
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
        Mesh.debMeshCount--;
    }


}
