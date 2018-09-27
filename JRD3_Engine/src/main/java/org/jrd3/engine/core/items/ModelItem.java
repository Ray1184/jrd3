package org.jrd3.engine.core.items;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jrd3.engine.core.graph.Mesh;
import org.jrd3.engine.core.loaders.Cacheable;
import org.jrd3.engine.core.utils.ResourceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelItem implements Actor, Cacheable {

    private final Vector3f position;

    private final Quaternionf rotation;
    private final Matrix4f transformation;
    private Mesh[] meshes;
    private float scale;
    private int texturePos;
    private boolean disableFrustumCulling;
    private boolean insideFrustum;
    private boolean visible;
    private final List<ActorController> controllers;
    private boolean used;

    /**
     * Constructs a new ModelItem.
     */
    public ModelItem() {
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Quaternionf();
        texturePos = 0;
        insideFrustum = true;
        disableFrustumCulling = false;
        visible = true;
        transformation = new Matrix4f();
        controllers = new ArrayList<>();
        used = true;
    }

    /**
     * Takes model item from resource manager.
     *
     * @param fileName The file name.
     * @return The model item.
     */
    public static ModelItem get(String fileName) {
        return ResourceManager.INSTANCE.getOrCreateModelItem(fileName, "");
    }

    /**
     * Takes model item from resource manager.
     *
     * @param fileName    The file name.
     * @param texFileName Texture file name.
     * @return The model item.
     */
    public static ModelItem get(String fileName, String texFileName) {
        return ResourceManager.INSTANCE.getOrCreateModelItem(fileName, texFileName);
    }

    public ModelItem(Mesh mesh) {
        this();
        meshes = new Mesh[]{mesh};
    }

    public ModelItem(Mesh[] meshes) {
        this();
        this.meshes = meshes;
    }


    /**
     * Getter for property 'texturePos'.
     *
     * @return Value for property 'texturePos'.
     */
    public int getTexturePos() {
        return texturePos;
    }

    /**
     * Setter for property 'texturePos'.
     *
     * @param texturePos Value to set for property 'texturePos'.
     */
    public void setTexturePos(int texturePos) {
        this.texturePos = texturePos;
    }

    /**
     * Getter for property 'mesh'.
     *
     * @return Value for property 'mesh'.
     */
    public Mesh getMesh() {
        return meshes[0];
    }

    /**
     * Setter for property 'mesh'.
     *
     * @param mesh Value to set for property 'mesh'.
     */
    public void setMesh(Mesh mesh) {
        meshes = new Mesh[]{mesh};
    }

    /**
     * Getter for property 'meshes'.
     *
     * @return Value for property 'meshes'.
     */
    public Mesh[] getMeshes() {
        return meshes;
    }

    /**
     * Setter for property 'meshes'.
     *
     * @param meshes Value to set for property 'meshes'.
     */
    public void setMeshes(Mesh[] meshes) {
        this.meshes = meshes;
    }

    @Override
    public long getSize() {
        return 1;
    }

    public void cleanup() {
        int numMeshes = meshes != null ? meshes.length : 0;
        for (int i = 0; i < numMeshes; i++) {
            meshes[i].cleanup();
        }
    }

    @Override
    public boolean isCurrentlyUsed() {
        return used;
    }

    public void setCurrentlyUsed(boolean used) {
        this.used = used;
    }

    /**
     * Getter for property 'insideFrustum'.
     *
     * @return Value for property 'insideFrustum'.
     */
    public boolean isInsideFrustum() {
        return insideFrustum;
    }

    /**
     * Setter for property 'insideFrustum'.
     *
     * @param insideFrustum Value to set for property 'insideFrustum'.
     */
    public void setInsideFrustum(boolean insideFrustum) {
        this.insideFrustum = insideFrustum;
    }

    /**
     * Getter for property 'disableFrustumCulling'.
     *
     * @return Value for property 'disableFrustumCulling'.
     */
    public boolean isDisableFrustumCulling() {
        return disableFrustumCulling;
    }

    /**
     * Setter for property 'disableFrustumCulling'.
     *
     * @param disableFrustumCulling Value to set for property 'disableFrustumCulling'.
     */
    public void setDisableFrustumCulling(boolean disableFrustumCulling) {
        this.disableFrustumCulling = disableFrustumCulling;
    }

    /**
     * Getter for property 'visible'.
     *
     * @return Value for property 'visible'.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Setter for property 'visible'.
     *
     * @param visible Value to set for property 'visible'.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Getter for property 'transformation'.
     *
     * @return Value for property 'transformation'.
     */
    public Matrix4f getTransformation() {
        return transformation;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelItem modelItem = (ModelItem) o;

        if (Float.compare(modelItem.scale, scale) != 0) return false;
        if (texturePos != modelItem.texturePos) return false;
        if (disableFrustumCulling != modelItem.disableFrustumCulling) return false;
        if (insideFrustum != modelItem.insideFrustum) return false;
        if (visible != modelItem.visible) return false;
        if (position != null ? !position.equals(modelItem.position) : modelItem.position != null) return false;
        if (rotation != null ? !rotation.equals(modelItem.rotation) : modelItem.rotation != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(meshes, modelItem.meshes)) return false;
        return controllers != null ? controllers.equals(modelItem.controllers) : modelItem.controllers == null;
    }

    @Override
    public int hashCode() {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + (rotation != null ? rotation.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(meshes);
        result = 31 * result + (scale != +0.0f ? Float.floatToIntBits(scale) : 0);
        result = 31 * result + texturePos;
        result = 31 * result + (disableFrustumCulling ? 1 : 0);
        result = 31 * result + (insideFrustum ? 1 : 0);
        result = 31 * result + (visible ? 1 : 0);
        result = 31 * result + (controllers != null ? controllers.hashCode() : 0);
        return result;
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
        transformation.setTranslation(position);
    }

    @Override
    public final void movePosition(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
        transformation.setTranslation(position);
    }

    @Override
    public float getScale() {
        return scale;
    }

    @Override
    public final void setScale(float scale) {
        this.scale = scale;
        transformation.scale(scale);
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
    public final void rotate(float angleX, float angleY, float angleZ) {
        // TODO - Check this [BUG-JRD3Core#14]
        rotation.rotateXYZ((float) Math.toRadians(angleX), (float) Math.toRadians(angleY), (float) Math.toRadians(angleZ));
        transformation.rotate(rotation);
        transformation.setRotationXYZ(rotation.x, rotation.y, rotation.z);

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
