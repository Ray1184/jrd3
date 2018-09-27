package org.jrd3.engine.core.items;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple scene node where to attach scene items and manipulate children transformations.
 *
 * @author Ray1184
 * @version 1.0
 */
public class SceneNode implements Actor {

    private final List<SceneNode> children;
    private final String name;
    private final Matrix4f worldTransform;
    private final Vector3f position;
    private final Quaternionf rotation;
    private SceneNode parent;
    private Matrix4f localTransform;
    private ModelItem geometry;
    private float scale;

    private final List<ActorController> controllers;

    /**
     * Default constructor.
     *
     * @param name The node name.
     */
    public SceneNode(String name) {
        this.name = name;
        children = new ArrayList<>();
        localTransform = new Matrix4f();
        worldTransform = new Matrix4f();
        position = new Vector3f();
        rotation = new Quaternionf();
        scale = 1;
        controllers = new ArrayList<>();
    }

    /**
     * Adds a child node.
     *
     * @param child The child to add.
     */
    public void addChild(SceneNode child) {
        children.add(child);
        child.parent = this;
    }

    /**
     * Unlink from parent.
     */
    public void detachFromParent() {
        if (parent != null) {
            parent.children.remove(this);
            parent = null;
        }
    }

    /**
     * Updates the whole transformation tree.
     */
    public void updateGraph() {
        updateGraph(true);
    }


    /**
     * Updates the whole transformation tree.
     *
     * @param updateFromPR Flag for updating local transform from position and rotation.
     */
    public void updateGraph(boolean updateFromPR) {
        if (updateFromPR) {
            localTransform.translationRotateScale(position, rotation, scale);
        }

        if (parent != null) {
            parent.worldTransform.mul(localTransform, worldTransform);
        } else {
            worldTransform.set(localTransform);
        }
        for (SceneNode child : children) {
            child.updateGraph();
        }
        if (geometry != null) {
            worldTransform.getTranslation(geometry.getPosition());
            worldTransform.getNormalizedRotation(geometry.getRotation());
            //worldTransform.mul(geometry.getTransformation());

        }

    }

    /**
     * Getter for property 'parent'.
     *
     * @return Value for property 'parent'.
     */
    public SceneNode getParent() {
        return parent;
    }

    /**
     * Setter for property 'parent'.
     *
     * @param parent Value to set for property 'parent'.
     */
    public void setParent(SceneNode parent) {
        this.parent = parent;
    }

    /**
     * Getter for property 'children'.
     *
     * @return Value for property 'children'.
     */
    public List<SceneNode> getChildren() {
        return children;
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
     * Getter for property 'localTransform'.
     *
     * @return Value for property 'localTransform'.
     */
    public Matrix4f getLocalTransform() {
        return localTransform;
    }

    /**
     * Setter for property 'localTransform'.
     *
     * @param localTransform Value to set for property 'localTransform'.
     */
    public void setLocalTransform(Matrix4f localTransform) {
        this.localTransform = localTransform;
    }

    /**
     * Getter for property 'worldTransform'.
     *
     * @return Value for property 'worldTransform'.
     */
    public Matrix4f getWorldTransform() {
        return worldTransform;
    }

    /**
     * Getter for property 'geometry'.
     *
     * @return Value for property 'geometry'.
     */
    public ModelItem getGeometry() {
        return geometry;
    }

    /**
     * Attaches a new geometry.
     *
     * @param geometry The geometry.
     */
    public void attachGeometry(ModelItem geometry) {
        this.geometry = geometry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SceneNode sceneNode = (SceneNode) o;

        return name != null ? name.equals(sceneNode.name) : sceneNode.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    @Override
    public Quaternionf getRotation() {
        return rotation;
    }

    @Override
    public final void setRotation(Quaternionf q) {
        rotation.set(q);
    }

    @Override
    public final void setPosition(Vector3f v) {
        position.set(v);
    }

    @Override
    public final void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    @Override
    public final void movePosition(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;

    }

    @Override
    public final void rotate(float angleX, float angleY, float angleZ) {
        // TODO - Check this [BUG-JRD3Core#14]
        rotation.rotateXYZ((float) Math.toRadians(angleX), (float) Math.toRadians(angleY), (float) Math.toRadians(angleZ));
        //rotation.rotate(angleX, angleY, angleZ);

    }

    @Override
    public float getScale() {
        return scale;
    }

    @Override
    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public List<ActorController> getControllers() {
        return controllers;
    }

    @Override
    public void addController(ActorController controller) {
        controllers.add(controller);
    }


}
