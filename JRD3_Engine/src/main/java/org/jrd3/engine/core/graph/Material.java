package org.jrd3.engine.core.graph;

import org.joml.Vector4f;

public class Material {

    public static final Vector4f DEFAULT_COLOUR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    private Vector4f ambientColour;

    private Vector4f diffuseColour;

    private Vector4f specularColour;

    private float shininess;

    private float reflectance;

    private Texture texture;

    private Texture normalMap;

    /**
     * Constructs a new Material.
     */
    public Material() {
        ambientColour = DEFAULT_COLOUR;
        diffuseColour = DEFAULT_COLOUR;
        specularColour = DEFAULT_COLOUR;
        texture = null;
        reflectance = 0;
    }

    public Material(Vector4f colour, float reflectance) {
        this(colour, colour, colour, null, reflectance);
    }

    public Material(Texture texture) {
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, 0);
    }

    public Material(Texture texture, float reflectance) {
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, reflectance);
    }

    public Material(Vector4f ambientColour, Vector4f diffuseColour, Vector4f specularColour, float reflectance) {
        this(ambientColour, diffuseColour, specularColour, null, reflectance);
    }

    public Material(Vector4f ambientColour, Vector4f diffuseColour, Vector4f specularColour, Texture texture, float reflectance) {
        this.ambientColour = ambientColour;
        this.diffuseColour = diffuseColour;
        this.specularColour = specularColour;
        this.texture = texture;
        this.reflectance = reflectance;
    }

    /**
     * Getter for property 'ambientColour'.
     *
     * @return Value for property 'ambientColour'.
     */
    public Vector4f getAmbientColour() {
        return ambientColour;
    }

    /**
     * Setter for property 'ambientColour'.
     *
     * @param ambientColour Value to set for property 'ambientColour'.
     */
    public void setAmbientColour(Vector4f ambientColour) {
        this.ambientColour = ambientColour;
    }

    /**
     * Getter for property 'diffuseColour'.
     *
     * @return Value for property 'diffuseColour'.
     */
    public Vector4f getDiffuseColour() {
        return diffuseColour;
    }

    /**
     * Setter for property 'diffuseColour'.
     *
     * @param diffuseColour Value to set for property 'diffuseColour'.
     */
    public void setDiffuseColour(Vector4f diffuseColour) {
        this.diffuseColour = diffuseColour;
    }

    /**
     * Getter for property 'specularColour'.
     *
     * @return Value for property 'specularColour'.
     */
    public Vector4f getSpecularColour() {
        return specularColour;
    }

    /**
     * Setter for property 'specularColour'.
     *
     * @param specularColour Value to set for property 'specularColour'.
     */
    public void setSpecularColour(Vector4f specularColour) {
        this.specularColour = specularColour;
    }

    /**
     * Getter for property 'reflectance'.
     *
     * @return Value for property 'reflectance'.
     */
    public float getReflectance() {
        return reflectance;
    }

    /**
     * Setter for property 'reflectance'.
     *
     * @param reflectance Value to set for property 'reflectance'.
     */
    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    /**
     * Getter for property 'textured'.
     *
     * @return Value for property 'textured'.
     */
    public boolean isTextured() {
        return texture != null;
    }

    /**
     * Getter for property 'texture'.
     *
     * @return Value for property 'texture'.
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Setter for property 'texture'.
     *
     * @param texture Value to set for property 'texture'.
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean hasNormalMap() {
        return normalMap != null;
    }

    /**
     * Getter for property 'normalMap'.
     *
     * @return Value for property 'normalMap'.
     */
    public Texture getNormalMap() {
        return normalMap;
    }

    /**
     * Setter for property 'normalMap'.
     *
     * @param normalMap Value to set for property 'normalMap'.
     */
    public void setNormalMap(Texture normalMap) {
        this.normalMap = normalMap;
    }
}