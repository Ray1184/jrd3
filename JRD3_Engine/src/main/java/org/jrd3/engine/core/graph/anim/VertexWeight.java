package org.jrd3.engine.core.graph.anim;

public class VertexWeight {

    private final int boneId;

    private int vertexId;

    private float weight;

    public VertexWeight(int boneId, int vertexId, float weight) {
        this.boneId = boneId;
        this.vertexId = vertexId;
        this.weight = weight;
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
     * Getter for property 'vertexId'.
     *
     * @return Value for property 'vertexId'.
     */
    public int getVertexId() {
        return vertexId;
    }

    /**
     * Setter for property 'vertexId'.
     *
     * @param vertexId Value to set for property 'vertexId'.
     */
    public void setVertexId(int vertexId) {
        this.vertexId = vertexId;
    }

    /**
     * Getter for property 'weight'.
     *
     * @return Value for property 'weight'.
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Setter for property 'weight'.
     *
     * @param weight Value to set for property 'weight'.
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }
}
