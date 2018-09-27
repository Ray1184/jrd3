package org.jrd3.engine.core.graph.anim;

import org.joml.Matrix4f;

import java.util.Arrays;

public class AnimatedFrame {

    public static final int MAX_JOINTS = 150;
    private static final Matrix4f IDENTITY_MATRIX = new Matrix4f();
    private final Matrix4f[] jointMatrices;

    /**
     * Constructs a new AnimatedFrame.
     */
    public AnimatedFrame() {
        jointMatrices = new Matrix4f[MAX_JOINTS];
        Arrays.fill(jointMatrices, IDENTITY_MATRIX);
    }

    /**
     * Getter for property 'jointMatrices'.
     *
     * @return Value for property 'jointMatrices'.
     */
    public Matrix4f[] getJointMatrices() {
        return jointMatrices;
    }

    public void setMatrix(int pos, Matrix4f jointMatrix) {
        jointMatrices[pos] = jointMatrix;
    }
}

