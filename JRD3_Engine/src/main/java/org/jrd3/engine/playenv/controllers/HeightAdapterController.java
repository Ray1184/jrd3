package org.jrd3.engine.playenv.controllers;

import org.joml.Vector2f;
import org.jrd3.engine.core.items.Actor;
import org.jrd3.engine.core.items.ActorController;
import org.jrd3.engine.playenv.interaction.map.Sector;
import org.jrd3.engine.playenv.interaction.map.WalkMap;

/**
 * Controller for adapting actor to walkmap height.
 *
 * @author Ray1184
 * @version 1.0
 */
public class HeightAdapterController implements ActorController {

    private final WalkMap walkMap;

    private final float delta;

    private float oldHeight;

    private final Vector2f pos2D;

    /**
     * Default constructor.
     *
     * @param walkMap The walkmap.
     * @param delta   The height threshold.
     */
    public HeightAdapterController(WalkMap walkMap, float delta) {
        this.walkMap = walkMap;
        this.delta = delta;
        pos2D = new Vector2f();
    }

    @Override
    public void init(Actor actor) {

    }

    @Override
    public void update(Actor actor, float tpf) {
        pos2D.set(actor.getPosition().x, actor.getPosition().z);
        Sector s = walkMap.sampleSector(pos2D, 0.0f);
        if (s != null) {
            float calcY = s.getTriangle().calcHeightByCoords(pos2D);

            if (calcY != Float.NaN) {
                actor.setPosition(pos2D.x, calcY + delta, pos2D.y);
                oldHeight = calcY + delta;
            }
        } else {
            actor.setPosition(pos2D.x, oldHeight, pos2D.y);
        }
    }
}
