package org.jrd3.engine.core.items;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

/**
 * Generic spatial on screen.
 *
 * @author Ray1184
 * @version 1.0
 */
public interface Actor {

    /**
     * Set actor position.
     *
     * @param x X coord.
     * @param y Y coord.
     * @param z Z coord.
     */
    void setPosition(float x, float y, float z);

    /**
     * Set actor position.
     *
     * @param v Vector position.
     */
    void setPosition(Vector3f v);

    /**
     * Move actor by offset.
     *
     * @param x X coord.
     * @param y Y coord.
     * @param z Z coord.
     */
    void movePosition(float x, float y, float z);

    /**
     * Rotate the actor.
     *
     * @param angleX X angle.
     * @param angleY Y angle.
     * @param angleZ Z angle.
     */
    void rotate(float angleX, float angleY, float angleZ);

    /**
     * Gets the actor position.
     *
     * @return The position.
     */
    Vector3f getPosition();

    /**
     * Gets the actor rotation.
     *
     * @return The rotation.
     */
    Quaternionf getRotation();

    /**
     * Set actor rotation.
     *
     * @param q The quaternion.
     */
    void setRotation(Quaternionf q);

    /**
     * Gets the actor scale.
     *
     * @return The scale.
     */
    float getScale();

    /**
     * Sets the actor scale.
     *
     * @param scale The scale.
     */
    void setScale(float scale);

    /**
     * Gets the actor controllers.
     *
     * @return The controllers.
     */
    List<ActorController> getControllers();

    /**
     * Adds an actor controller.
     *
     * @param controller The controller.
     */
    void addController(ActorController controller);
}
