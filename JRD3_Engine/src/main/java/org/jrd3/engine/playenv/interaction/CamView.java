package org.jrd3.engine.playenv.interaction;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.jrd3.engine.core.graph.Transformation;

import java.io.Serializable;

/**
 * Current view information.
 *
 * @author Ray1184
 * @version 1.0
 */
public class CamView implements Serializable {

    private final Vector3f position;

    private final Vector3f rotation;

    private final Matrix4f viewMatrix;

    private final String backgroundImage;

    private final String depthImage;

    /**
     * Default constructor.
     *
     * @param position        The view position.
     * @param rotation        The view rotation.
     * @param backgroundImage The view background.
     * @param depthImage      The view depth mask.
     */
    public CamView(Vector3f position, Vector3f rotation, String backgroundImage, String depthImage) {
        this.position = position;
        this.rotation = rotation;
        this.backgroundImage = backgroundImage;
        this.depthImage = depthImage;
        viewMatrix = new Matrix4f();
        Transformation.updateGenericViewMatrix(position, rotation, viewMatrix);
    }

    /**
     * Getter for property 'viewMatrix'.
     *
     * @return Value for property 'viewMatrix'.
     */
    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    /**
     * Getter for property 'backgroundImage'.
     *
     * @return Value for property 'backgroundImage'.
     */
    public String getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * Getter for property 'depthImage'.
     *
     * @return Value for property 'depthImage'.
     */
    public String getDepthImage() {
        return depthImage;
    }
}
