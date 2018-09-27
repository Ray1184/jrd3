package org.jrd3.engine.core.graph.anim;

import java.util.Map;

/**
 * Generic callback for actor animations.
 *
 * @author Ray1184
 * @version 1.0
 */
public interface AnimController {


    /**
     * Inits the anim controller.
     *
     * @param anims The animations.
     */
    void init(Map<String, Animation> anims);

    /**
     * Updates the anim controller.
     *
     * @param anim The current animation.
     * @param tpf  Time per frame.
     */
    void update(Animation anim, float tpf);
}
