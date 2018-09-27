package org.jrd3.engine.playenv.interaction;

import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.jrd3.engine.playenv.interaction.map.Sector;
import org.jrd3.engine.playenv.interaction.map.WalkMap;

/**
 * Collision entity, can be moved on a walkmap and can calculate a interaction response base on position.
 *
 * @author Ray1184
 * @version 1.0
 */
public class Collisor {

    private WalkMap walkMap;

    private final Vector3f position;

    private final Vector2f position2D;

    private final Vector2f n;

    private final Vector2f v;

    private final Vector2f vn;

    private final Vector2f vt;

    private final Vector2f result;

    private Sector currentSector;

    private float delta;


    /**
     * Default constructor.
     *
     * @param walkMap The walkmap.
     */
    public Collisor(WalkMap walkMap) {
        this(walkMap, 0.0f);
    }

    /**
     * Constructor with threshold.
     *
     * @param walkMap The walkmap.
     * @param delta   The threshold.
     */
    public Collisor(WalkMap walkMap, float delta) {
        this.walkMap = walkMap;
        position = new Vector3f();
        position2D = new Vector2f();
        n = new Vector2f();
        v = new Vector2f();
        vn = new Vector2f();
        vt = new Vector2f();
        result = new Vector2f();
        this.delta = delta;
    }


    /**
     * Perform the interaction calculation against the walkmap.
     *
     * @param nextPosition The desired position.
     * @param direction    The current direction of the interaction holder.
     */
    public void calculateCollisionResponse(Vector2f nextPosition, Vector2f direction) {
        Sector nextSector = walkMap.sampleSector(nextPosition, delta);
        if (nextSector != null) {
            position.set(nextPosition.x, position.y, nextPosition.y);
            position2D.set(position.x, position.z);
            currentSector = nextSector;
        } else {
            // If not current triangle found just exit.
            if (currentSector != null) {
                // Find the intersected
                for (Sector.Side side : currentSector.getExternalSides()) {
                    float t = Intersectionf.intersectRayLineSegment(position2D, direction, side.p1, side.p2);
                    // Correct side.
                    if (t > -1) {
                        side.p2.sub(side.p1, n);
                        n.perpendicular().normalize();
                        nextPosition.sub(position2D, v);
                        n.mul(v.dot(n), vn);
                        v.sub(vn, vt);
                        result.set(position.x + vt.x, position.z + vt.y);
                        // Just to restore the last position in case of some bug in calculation.
                        if (walkMap.sampleSector(result, delta) != null) {
                            position.set(result.x, position.y, result.y);
                            position2D.set(position.x, position.z);
                        }
                    }

                }
            }
        }
    }

    /**
     * Getter for property 'walkMap'.
     *
     * @return Value for property 'walkMap'.
     */
    public WalkMap getWalkMap() {
        return walkMap;
    }

    /**
     * Setter for property 'walkMap'.
     *
     * @param walkMap Value to set for property 'walkMap'.
     */
    public void setWalkMap(WalkMap walkMap) {
        this.walkMap = walkMap;
    }

    /**
     * Getter for property 'position'.
     *
     * @return Value for property 'position'.
     */
    public Vector3f getPosition() {
        return position;
    }


    /**
     * Getter for property 'delta'.
     *
     * @return Value for property 'delta'.
     */
    public float getDelta() {
        return delta;
    }

    /**
     * Setter for property 'delta'.
     *
     * @param delta Value to set for property 'delta'.
     */
    public void setDelta(float delta) {
        this.delta = delta;
    }
}
