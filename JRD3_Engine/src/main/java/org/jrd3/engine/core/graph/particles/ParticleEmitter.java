package org.jrd3.engine.core.graph.particles;

import org.jrd3.engine.core.items.ModelItem;
import org.jrd3.engine.core.items.Particle;

import java.util.List;

public interface ParticleEmitter {

    void cleanup();

    /**
     * Getter for property 'baseParticle'.
     *
     * @return Value for property 'baseParticle'.
     */
    Particle getBaseParticle();

    /**
     * Getter for property 'particles'.
     *
     * @return Value for property 'particles'.
     */
    List<ModelItem> getParticles();
}
