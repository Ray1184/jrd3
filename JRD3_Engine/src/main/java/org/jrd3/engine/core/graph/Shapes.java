package org.jrd3.engine.core.graph;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

/**
 * Pre-calculated buffers with useful shapes.
 *
 * @author Ray1184
 * @version 1.0
 */
public enum Shapes {

    QUAD(new float[]{-1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f,
            1.0f, 0.0f});


    private final FloatBuffer buffer;

    /**
     * Default constructor.
     *
     * @param data The raw data.
     */
    Shapes(float[] data) {
        buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data).flip();
    }

    /**
     * Gets the buffered data.
     *
     * @return The buffered data.
     */
    public FloatBuffer getData() {
        return buffer;
    }
}
