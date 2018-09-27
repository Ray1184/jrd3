package org.jrd3.engine.core.graph.particles;

import org.joml.Vector3f;
import org.jrd3.engine.core.items.ModelItem;
import org.jrd3.engine.core.items.Particle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FlowParticleEmitter implements ParticleEmitter {

    private final List<ModelItem> particles;
    private final Particle baseParticle;
    private int maxParticles;
    private boolean active;
    private long creationPeriodMillis;

    private long lastCreationTime;

    private float speedRndRange;

    private float positionRndRange;

    private float scaleRndRange;

    private long animRange;

    public FlowParticleEmitter(Particle baseParticle, int maxParticles, long creationPeriodMillis) {
        particles = new ArrayList<>();
        this.baseParticle = baseParticle;
        this.maxParticles = maxParticles;
        active = false;
        lastCreationTime = 0;
        this.creationPeriodMillis = creationPeriodMillis;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Particle getBaseParticle() {
        return baseParticle;
    }

    /**
     * Getter for property 'creationPeriodMillis'.
     *
     * @return Value for property 'creationPeriodMillis'.
     */
    public long getCreationPeriodMillis() {
        return creationPeriodMillis;
    }

    /**
     * Setter for property 'creationPeriodMillis'.
     *
     * @param creationPeriodMillis Value to set for property 'creationPeriodMillis'.
     */
    public void setCreationPeriodMillis(long creationPeriodMillis) {
        this.creationPeriodMillis = creationPeriodMillis;
    }

    /**
     * Getter for property 'maxParticles'.
     *
     * @return Value for property 'maxParticles'.
     */
    public int getMaxParticles() {
        return maxParticles;
    }

    /**
     * Setter for property 'maxParticles'.
     *
     * @param maxParticles Value to set for property 'maxParticles'.
     */
    public void setMaxParticles(int maxParticles) {
        this.maxParticles = maxParticles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ModelItem> getParticles() {
        return particles;
    }

    /**
     * Getter for property 'positionRndRange'.
     *
     * @return Value for property 'positionRndRange'.
     */
    public float getPositionRndRange() {
        return positionRndRange;
    }

    /**
     * Setter for property 'positionRndRange'.
     *
     * @param positionRndRange Value to set for property 'positionRndRange'.
     */
    public void setPositionRndRange(float positionRndRange) {
        this.positionRndRange = positionRndRange;
    }

    /**
     * Getter for property 'scaleRndRange'.
     *
     * @return Value for property 'scaleRndRange'.
     */
    public float getScaleRndRange() {
        return scaleRndRange;
    }

    /**
     * Setter for property 'scaleRndRange'.
     *
     * @param scaleRndRange Value to set for property 'scaleRndRange'.
     */
    public void setScaleRndRange(float scaleRndRange) {
        this.scaleRndRange = scaleRndRange;
    }

    /**
     * Getter for property 'speedRndRange'.
     *
     * @return Value for property 'speedRndRange'.
     */
    public float getSpeedRndRange() {
        return speedRndRange;
    }

    /**
     * Setter for property 'speedRndRange'.
     *
     * @param speedRndRange Value to set for property 'speedRndRange'.
     */
    public void setSpeedRndRange(float speedRndRange) {
        this.speedRndRange = speedRndRange;
    }

    /**
     * Setter for property 'animRange'.
     *
     * @param animRange Value to set for property 'animRange'.
     */
    public void setAnimRange(long animRange) {
        this.animRange = animRange;
    }

    /**
     * Getter for property 'active'.
     *
     * @return Value for property 'active'.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Setter for property 'active'.
     *
     * @param active Value to set for property 'active'.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    public void update(long elapsedTime) {
        long now = System.currentTimeMillis();
        if (lastCreationTime == 0) {
            lastCreationTime = now;
        }
        Iterator<? extends ModelItem> it = particles.iterator();
        while (it.hasNext()) {
            Particle particle = (Particle) it.next();
            if (particle.updateTtl(elapsedTime) < 0) {
                it.remove();
            } else {
                updatePosition(particle, elapsedTime);
            }
        }

        int length = getParticles().size();
        if (now - lastCreationTime >= creationPeriodMillis && length < maxParticles) {
            createParticle();
            lastCreationTime = now;
        }
    }

    private void createParticle() {
        Particle particle = new Particle(getBaseParticle());
        // Add a little bit of randomness of the parrticle
        float sign = Math.random() > 0.5d ? -1.0f : 1.0f;
        float speedInc = sign * (float) Math.random() * speedRndRange;
        float posInc = sign * (float) Math.random() * positionRndRange;
        float scaleInc = sign * (float) Math.random() * scaleRndRange;
        long updateAnimInc = (long) sign * (long) (Math.random() * (float) animRange);
        particle.getPosition().add(posInc, posInc, posInc);
        particle.getSpeed().add(speedInc, speedInc, speedInc);
        particle.setScale(particle.getScale() + scaleInc);
        particle.setUpdateTextureMills(particle.getUpdateTextureMillis() + updateAnimInc);
        particles.add(particle);
    }

    /**
     * Updates a particle position
     *
     * @param particle    The particle to update
     * @param elapsedTime Elapsed time in milliseconds
     */
    public void updatePosition(Particle particle, long elapsedTime) {
        Vector3f speed = particle.getSpeed();
        float delta = elapsedTime / 1000.0f;
        float dx = speed.x * delta;
        float dy = speed.y * delta;
        float dz = speed.z * delta;
        Vector3f pos = particle.getPosition();
        particle.setPosition(pos.x + dx, pos.y + dy, pos.z + dz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanup() {
        for (ModelItem particle : getParticles()) {
            particle.cleanup();
        }
    }
}
