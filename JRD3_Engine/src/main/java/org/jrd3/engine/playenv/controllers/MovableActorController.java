package org.jrd3.engine.playenv.controllers;

import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.jrd3.engine.core.items.Actor;
import org.jrd3.engine.core.items.ActorController;
import org.jrd3.engine.playenv.interaction.Collisor;


/**
 * Controller for movable actors.
 *
 * @author Ray1184
 * @version 1.0
 */
public class MovableActorController implements ActorController {

    protected static final Vector3f LOOK_FORWARD = new Vector3f(0f, 0f, 1f);
    protected final Vector3f direction;
    protected final Vector2f direction2D;
    protected final Vector3f nextPos;
    protected final Vector2f nextPos2D;
    protected final Matrix3f mat;
    protected Collisor collisor;
    protected float movStep;

    private float rotStep;


    /**
     * Default constructor.
     *
     * @param collisor The collisor.
     */
    public MovableActorController(Collisor collisor) {
        this.collisor = collisor;
        direction = new Vector3f();
        direction2D = new Vector2f();
        nextPos = new Vector3f();
        nextPos2D = new Vector2f();
        mat = new Matrix3f();
    }

    @Override
    public void init(Actor actor) {
        collisor.getPosition().set(actor.getPosition());
        collisor.setDelta(0.0f);
        direction.set(0f, 0f, 1f);
        nextPos.set(collisor.getPosition());
        direction2D.set(direction.x, direction.z);
        nextPos2D.set(nextPos.x, nextPos.z);


    }

    @Override
    public void update(Actor actor, float tpf) {

        mat.zero();
        direction.set(MovableActorController.LOOK_FORWARD);
        actor.rotate(0.0f, rotStep * tpf, 0.0f);
        actor.getRotation().get(mat);
        direction.set(direction.mul(mat).normalize());
        direction2D.set(direction.x, direction.z);
        nextPos.add(movStep * direction2D.x * tpf, 0, movStep * direction2D.y * tpf);
        nextPos2D.set(nextPos.x, nextPos.z);
        collisor.calculateCollisionResponse(nextPos2D, direction2D);
        actor.setPosition(collisor.getPosition().x, collisor.getPosition().y, collisor.getPosition().z);
        nextPos.set(collisor.getPosition());
    }

    /**
     * Getter for property 'collisor'.
     *
     * @return Value for property 'collisor'.
     */
    public Collisor getCollisor() {
        return collisor;
    }

    /**
     * Setter for property 'collisor'.
     *
     * @param collisor Value to set for property 'collisor'.
     */
    public void setCollisor(Collisor collisor) {
        this.collisor = collisor;
    }

    /**
     * Getter for property 'rotStep'.
     *
     * @return Value for property 'rotStep'.
     */
    public float getRotStep() {
        return rotStep;
    }

    /**
     * Setter for property 'rotStep'.
     *
     * @param rotStep Value to set for property 'rotStep'.
     */
    public void setRotStep(float rotStep) {
        this.rotStep = rotStep;
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
}
