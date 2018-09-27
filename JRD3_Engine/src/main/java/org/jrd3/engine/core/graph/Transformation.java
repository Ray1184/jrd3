package org.jrd3.engine.core.graph;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jrd3.engine.core.items.ModelItem;

public class Transformation {

    private final Matrix4f modelMatrix;

    private final Matrix4f modelViewMatrix;

    private final Matrix4f modelLightViewMatrix;

    private final Matrix4f lightViewMatrix;

    private final Matrix4f ortho2DMatrix;

    private final Matrix4f orthoModelMatrix;

    private final Matrix4f orthoMatrix;

    private final Vector3f eulerAngles;

    /**
     * Constructs a new Transformation.
     */
    public Transformation() {
        modelMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();
        modelLightViewMatrix = new Matrix4f();
        ortho2DMatrix = new Matrix4f();
        orthoModelMatrix = new Matrix4f();
        lightViewMatrix = new Matrix4f();
        orthoMatrix = new Matrix4f();
        eulerAngles = new Vector3f();
    }

    public static Matrix4f updateGenericViewMatrix(Vector3f position, Vector3f rotation, Matrix4f matrix) {
        // First do the rotation so camera rotates over its position
        return matrix.rotationX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .translate(-position.x, -position.y, -position.z);
    }

    /**
     * Getter for property 'lightViewMatrix'.
     *
     * @return Value for property 'lightViewMatrix'.
     */
    public Matrix4f getLightViewMatrix() {
        return lightViewMatrix;
    }

    /**
     * Setter for property 'lightViewMatrix'.
     *
     * @param lightViewMatrix Value to set for property 'lightViewMatrix'.
     */
    public void setLightViewMatrix(Matrix4f lightViewMatrix) {
        this.lightViewMatrix.set(lightViewMatrix);
    }

    public Matrix4f updateLightViewMatrix(Vector3f position, Vector3f rotation) {
        return Transformation.updateGenericViewMatrix(position, rotation, lightViewMatrix);
    }

    public final Matrix4f getOrtho2DProjectionMatrix(float left, float right, float bottom, float top) {
        return ortho2DMatrix.setOrtho2D(left, right, bottom, top);
    }

    public Matrix4f buildModelMatrix(ModelItem modelItem) {
        Quaternionf rotation = modelItem.getRotation();
        return modelMatrix.translationRotateScale(
                modelItem.getPosition().x, modelItem.getPosition().y, modelItem.getPosition().z,
                rotation.x, rotation.y, rotation.z, rotation.w,
                modelItem.getScale(), modelItem.getScale(), modelItem.getScale());
    }

    public Matrix4f buildModelViewMatrix(ModelItem modelItem, Matrix4f viewMatrix) {
        return buildModelViewMatrix(buildModelMatrix(modelItem), viewMatrix);
    }

    public Matrix4f buildModelViewMatrix(Matrix4f modelMatrix, Matrix4f viewMatrix) {
        return viewMatrix.mulAffine(modelMatrix, modelViewMatrix);
    }

    public Matrix4f buildModelLightViewMatrix(ModelItem modelItem, Matrix4f lightViewMatrix) {
        return buildModelViewMatrix(buildModelMatrix(modelItem), lightViewMatrix);
    }

    public Matrix4f buildModelLightViewMatrix(Matrix4f modelMatrix, Matrix4f lightViewMatrix) {
        return lightViewMatrix.mulAffine(modelMatrix, modelLightViewMatrix);
    }

    public Matrix4f buildOrthoProjModelMatrix(ModelItem modelItem, Matrix4f orthoMatrix) {
        return orthoMatrix.mulOrthoAffine(buildModelMatrix(modelItem), orthoModelMatrix);
    }

    public Matrix4f getOrtoProjModelMatrix(ModelItem gameItem, Matrix4f orthoMatrix) {
        Vector3f rotation = gameItem.getRotation().getEulerAnglesXYZ(eulerAngles);
        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.identity().translate(gameItem.getPosition()).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(gameItem.getScale());
        Matrix4f orthoMatrixCurr = new Matrix4f(orthoMatrix);
        orthoMatrixCurr.mul(modelMatrix);
        return orthoMatrixCurr;
    }

    public final Matrix4f getOrthoProjectionMatrix(float left, float right, float bottom, float top) {
        orthoMatrix.identity();
        orthoMatrix.setOrtho2D(left, right, bottom, top);
        return orthoMatrix;
    }
}
