package org.jrd3.engine.core.items;

import org.joml.Vector3f;
import org.jrd3.engine.core.graph.Mesh;
import org.jrd3.engine.core.graph.Texture;

public class Particle extends ModelItem {

    private final int animFrames;
    private long updateTextureMillis;
    private long currentAnimTimeMillis;
    private Vector3f speed;
    /**
     * Time to live for particle in milliseconds.
     */
    private long ttl;

    public Particle(Mesh mesh, Vector3f speed, long ttl, long updateTextureMillis) {
        super(mesh);
        this.speed = new Vector3f(speed);
        this.ttl = ttl;
        this.updateTextureMillis = updateTextureMillis;
        currentAnimTimeMillis = 0;
        Texture texture = getMesh().getMaterial().getTexture();
        animFrames = texture.getNumCols() * texture.getNumRows();
    }

    public Particle(Particle baseParticle) {
        super(baseParticle.getMesh());
        Vector3f aux = baseParticle.getPosition();
        setPosition(aux.x, aux.y, aux.z);
        setRotation(baseParticle.getRotation());
        setScale(baseParticle.getScale());
        speed = new Vector3f(baseParticle.speed);
        ttl = baseParticle.geTtl();
        updateTextureMillis = baseParticle.getUpdateTextureMillis();
        currentAnimTimeMillis = 0;
        animFrames = baseParticle.getAnimFrames();
    }

    /**
     * Getter for property 'animFrames'.
     *
     * @return Value for property 'animFrames'.
     */
    public int getAnimFrames() {
        return animFrames;
    }

    /**
     * Getter for property 'speed'.
     *
     * @return Value for property 'speed'.
     */
    public Vector3f getSpeed() {
        return speed;
    }

    /**
     * Setter for property 'speed'.
     *
     * @param speed Value to set for property 'speed'.
     */
    public void setSpeed(Vector3f speed) {
        this.speed = speed;
    }

    /**
     * Getter for property 'updateTextureMillis'.
     *
     * @return Value for property 'updateTextureMillis'.
     */
    public long getUpdateTextureMillis() {
        return updateTextureMillis;
    }

    public long geTtl() {
        return ttl;
    }

    /**
     * Setter for property 'ttl'.
     *
     * @param ttl Value to set for property 'ttl'.
     */
    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    /**
     * Setter for property 'updateTextureMills'.
     *
     * @param updateTextureMillis Value to set for property 'updateTextureMills'.
     */
    public void setUpdateTextureMills(long updateTextureMillis) {
        this.updateTextureMillis = updateTextureMillis;
    }

    /**
     * Updates the Particle's TTL
     *
     * @param elapsedTime Elapsed Time in milliseconds
     * @return The Particle's TTL
     */
    public long updateTtl(long elapsedTime) {
        ttl -= elapsedTime;
        currentAnimTimeMillis += elapsedTime;
        if (currentAnimTimeMillis >= getUpdateTextureMillis() && animFrames > 0) {
            currentAnimTimeMillis = 0;
            int pos = getTexturePos();
            pos++;
            if (pos < animFrames) {
                setTexturePos(pos);
            } else {
                setTexturePos(0);
            }
        }
        return ttl;
    }

}