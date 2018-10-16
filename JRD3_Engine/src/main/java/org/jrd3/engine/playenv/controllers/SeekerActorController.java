package org.jrd3.engine.playenv.controllers;


import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.jrd3.engine.core.items.Actor;
import org.jrd3.engine.core.items.ActorController;
import org.jrd3.engine.playenv.interaction.map.PathsMap;
import org.jrd3.engine.playenv.interaction.map.Sector;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for seeker actors.
 *
 * @author Ray1184
 * @version 1.0
 */
public class SeekerActorController implements ActorController {

    protected static final Vector3f LOOK_FORWARD_ROT = new Vector3f(0f, 1f, 0f);
    protected final Vector2f actorPos2D;
    protected final Vector2f seekPos2D;
    protected final Quaternionf rotation;
    protected final Vector3f direction;
    protected final Vector2f direction2D;
    protected final Vector3f nextPos;
    protected Actor seek;
    protected PathsMap pathMap;
    protected List<Sector> path;
    protected float movStep;

    private float distance;

    private boolean seeking;


    /**
     * Default constructor.
     *
     * @param seek    The actor to seek.
     * @param pathMap The paths map.
     */
    public SeekerActorController(Actor seek, PathsMap pathMap) {

        this.seek = seek;
        this.pathMap = pathMap;
        path = new ArrayList<>();
        actorPos2D = new Vector2f();
        seekPos2D = new Vector2f();
        rotation = new Quaternionf();
        direction = new Vector3f();
        direction2D = new Vector2f();
        nextPos = new Vector3f();
        seeking = true;
    }


    @Override
    public void init(Actor actor) {
        direction.set(0f, 0f, 1f);
        direction2D.set(direction.x, direction.z);
        nextPos.set(actor.getPosition());

    }

    @Override
    public void update(Actor actor, float tpf) {

        if (pathMap != null) {
            actorPos2D.set(actor.getPosition().x, actor.getPosition().z);
            seekPos2D.set(seek.getPosition().x, seek.getPosition().z);
            seekPos2D.sub(actorPos2D, direction2D);
            direction2D.normalize();
            direction.set(direction2D.x, 0.0f, direction2D.y);
            rotation.lookAlong(direction.negate(), SeekerActorController.LOOK_FORWARD_ROT);
            rotation.set(rotation.x, -rotation.y, rotation.z, rotation.w);
            actor.getRotation().set(rotation);
            rotation.set(0, 0, 0, 1);


            if (seeking) {
                pathMap.calculateShortestPath(pathMap.sampleSector(actorPos2D, 0.0f),
                        pathMap.sampleSector(seekPos2D, 0.0f), path);
                if (path.size() > 0) {

                    Sector t = path.get(0);
                    t.getTriangle().center2f.sub(actorPos2D, direction2D);
                    direction2D.normalize();
                }

                nextPos.add(movStep * direction2D.x * tpf, 0, movStep * direction2D.y * tpf);

                actor.setPosition(nextPos.x, nextPos.y, nextPos.z);
            }


        }
        distance = actor.getPosition().distance(seek.getPosition());

    }

    /**
     * Getter for property 'seek'.
     *
     * @return Value for property 'seek'.
     */
    public Actor getSeek() {
        return seek;
    }

    /**
     * Setter for property 'seek'.
     *
     * @param seek Value to set for property 'seek'.
     */
    public void setSeek(Actor seek) {
        this.seek = seek;
    }

    /**
     * Getter for property 'movStep'.
     *
     * @return Value for property 'movStep'.
     */
    public float getMovStep() {
        return movStep;
    }

    /**
     * Setter for property 'movStep'.
     *
     * @param movStep Value to set for property 'movStep'.
     */
    public void setMovStep(float movStep) {
        this.movStep = movStep;
    }

    /**
     * Getter for property 'distance'.
     *
     * @return Value for property 'distance'.
     */
    public float getDistance() {
        return distance;
    }


    /**
     * Getter for property 'seeking'.
     *
     * @return Value for property 'seeking'.
     */
    public boolean isSeeking() {
        return seeking;
    }

    /**
     * Setter for property 'seeking'.
     *
     * @param seeking Value to set for property 'seeking'.
     */
    public void setSeeking(boolean seeking) {
        this.seeking = seeking;
    }
}
