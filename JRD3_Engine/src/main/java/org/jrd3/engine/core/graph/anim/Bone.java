package org.jrd3.engine.core.graph.anim;

import org.joml.Matrix4f;

public class Bone {

    private final int boneId;

    private final String boneName;

    private final Matrix4f offsetMatrix;

    public Bone(int boneId, String boneName, Matrix4f offsetMatrix) {
        this.boneId = boneId;
        this.boneName = boneName;
        this.offsetMatrix = offsetMatrix;
    }

    /**
     * Getter for property 'boneId'.
     *
     * @return Value for property 'boneId'.
     */
    public int getBoneId() {
        return boneId;
    }

    /**
     * Getter for property 'boneName'.
     *
     * @return Value for property 'boneName'.
     */
    public String getBoneName() {
        return boneName;
    }

    /**
     * Getter for property 'offsetMatrix'.
     *
     * @return Value for property 'offsetMatrix'.
     */
    public Matrix4f getOffsetMatrix() {
        return offsetMatrix;
    }

}
