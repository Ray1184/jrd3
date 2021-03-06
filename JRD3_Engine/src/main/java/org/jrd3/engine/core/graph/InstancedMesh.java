package org.jrd3.engine.core.graph;

import org.joml.Matrix4f;
import org.jrd3.engine.core.items.ModelItem;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class InstancedMesh extends Mesh {

    private static final int FLOAT_SIZE_BYTES = 4;

    private static final int VECTOR4F_SIZE_BYTES = 4 * InstancedMesh.FLOAT_SIZE_BYTES;

    private static final int MATRIX_SIZE_FLOATS = 4 * 4;

    private static final int MATRIX_SIZE_BYTES = InstancedMesh.MATRIX_SIZE_FLOATS * InstancedMesh.FLOAT_SIZE_BYTES;

    private static final int INSTANCE_SIZE_BYTES = InstancedMesh.MATRIX_SIZE_BYTES + InstancedMesh.FLOAT_SIZE_BYTES * 2 + InstancedMesh.FLOAT_SIZE_BYTES;

    private static final int INSTANCE_SIZE_FLOATS = InstancedMesh.MATRIX_SIZE_FLOATS + 3;

    private final int numInstances;

    private final int instanceDataVBO;

    private FloatBuffer instanceDataBuffer;

    public InstancedMesh(float[] positions, float[] textCoords, float[] normals, int[] indices, int numInstances) {
        super(positions, textCoords, normals, indices, Mesh.createEmptyIntArray(Mesh.MAX_WEIGHTS * positions.length / 3, 0), Mesh.createEmptyFloatArray(Mesh.MAX_WEIGHTS * positions.length / 3, 0));

        this.numInstances = numInstances;

        glBindVertexArray(vaoId);

        instanceDataVBO = glGenBuffers();
        vboIdList.add(instanceDataVBO);
        instanceDataBuffer = MemoryUtil.memAllocFloat(numInstances * InstancedMesh.INSTANCE_SIZE_FLOATS);
        glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);
        int start = 5;
        int strideStart = 0;
        // Model matrix
        for (int i = 0; i < 4; i++) {
            glVertexAttribPointer(start, 4, GL_FLOAT, false, InstancedMesh.INSTANCE_SIZE_BYTES, strideStart);
            glVertexAttribDivisor(start, 1);
            start++;
            strideStart += InstancedMesh.VECTOR4F_SIZE_BYTES;
        }

        // Texture offsets
        glVertexAttribPointer(start, 2, GL_FLOAT, false, InstancedMesh.INSTANCE_SIZE_BYTES, strideStart);
        glVertexAttribDivisor(start, 1);
        strideStart += InstancedMesh.FLOAT_SIZE_BYTES * 2;
        start++;

        // Selected or Scaling (for particles)
        glVertexAttribPointer(start, 1, GL_FLOAT, false, InstancedMesh.INSTANCE_SIZE_BYTES, strideStart);
        glVertexAttribDivisor(start, 1);
        start++;

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanup() {
        super.cleanup();
        if (instanceDataBuffer != null) {
            MemoryUtil.memFree(instanceDataBuffer);
            instanceDataBuffer = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initRender() {
        super.initRender();

        int start = 5;
        int numElements = 4 * 2 + 2;
        for (int i = 0; i < numElements; i++) {
            glEnableVertexAttribArray(start + i);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void endRender() {
        int start = 5;
        int numElements = 4 * 2 + 2;
        for (int i = 0; i < numElements; i++) {
            glDisableVertexAttribArray(start + i);
        }

        super.endRender();
    }

    public void renderListInstanced(List<ModelItem> modelItems, Transformation transformation, Matrix4f viewMatrix) {
        renderListInstanced(modelItems, false, transformation, viewMatrix);
    }

    public void renderListInstanced(List<ModelItem> modelItems, boolean billBoard, Transformation transformation, Matrix4f viewMatrix) {
        initRender();

        int chunkSize = numInstances;
        int length = modelItems.size();
        for (int i = 0; i < length; i += chunkSize) {
            int end = Math.min(length, i + chunkSize);
            List<ModelItem> subList = modelItems.subList(i, end);
            renderChunkInstanced(subList, billBoard, transformation, viewMatrix);
        }

        endRender();
    }

    private void renderChunkInstanced(List<ModelItem> modelItems, boolean billBoard, Transformation transformation, Matrix4f viewMatrix) {
        instanceDataBuffer.clear();

        int i = 0;

        Texture text = getMaterial().getTexture();
        for (ModelItem modelItem : modelItems) {
            Matrix4f modelMatrix = transformation.buildModelMatrix(modelItem);
            if (viewMatrix != null && billBoard) {
                viewMatrix.transpose3x3(modelMatrix);
            }
            modelMatrix.get(InstancedMesh.INSTANCE_SIZE_FLOATS * i, instanceDataBuffer);
            if (text != null) {
                int col = modelItem.getTexturePos() % text.getNumCols();
                int row = modelItem.getTexturePos() / text.getNumCols();
                float textXOffset = (float) col / text.getNumCols();
                float textYOffset = (float) row / text.getNumRows();
                int buffPos = InstancedMesh.INSTANCE_SIZE_FLOATS * i + InstancedMesh.MATRIX_SIZE_FLOATS;
                instanceDataBuffer.put(buffPos, textXOffset);
                instanceDataBuffer.put(buffPos + 1, textYOffset);
            }

            // Selected data or scaling for billboard
            int buffPos = InstancedMesh.INSTANCE_SIZE_FLOATS * i + InstancedMesh.MATRIX_SIZE_FLOATS + 2;
            instanceDataBuffer.put(buffPos, billBoard ? modelItem.getScale() : 0);

            i++;
        }

        glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);
        glBufferData(GL_ARRAY_BUFFER, instanceDataBuffer, GL_DYNAMIC_READ);

        glDrawElementsInstanced(
                GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0, modelItems.size());

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
