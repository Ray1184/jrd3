package org.jrd3.engine.core.items;

/**
 * Controller callback for actors.
 *
 * @author Ray1184
 * @version 1.0
 */
public interface ActorController {


    /**
     * Inits the actor controller.
     *
     * @param actor The actor.
     */
    void init(Actor actor);

    /**
     * Updates the actor controller.
     *
     * @param actor The actor.
     * @param tpf   Time per frame.
     */
    void update(Actor actor, float tpf);
}
