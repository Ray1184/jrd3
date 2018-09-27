package org.jrd3.engine.core.items;

import org.joml.Vector2f;
import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.graph.Shapes;
import org.jrd3.engine.core.graph.Texture;
import org.jrd3.engine.core.utils.ResourceManager;
import org.jrd3.engine.core.utils.ScreenManager;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Picture image for backgrounds and foregrounds.
 *
 * @author Ray1184
 * @version 1.0
 */
public class Picture {

    private final Texture texture;

    private final Mode mode;

    private final int vboHandle;
    private final Vector2f position;
    private float alpha;

    /**
     * Default constructor.
     *
     * @param texture The source texture.
     * @param mode    The mode.
     */
    public Picture(Texture texture, Mode mode) {
        this.texture = texture;
        this.mode = mode;
        vboHandle = glGenBuffers();
        position = new Vector2f(0.0f, 0.0f);
        alpha = 1;
        glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
        glBufferData(GL_ARRAY_BUFFER, Shapes.QUAD.getData(), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

    }

    /**
     * Takes picture using texture from resource manager.
     *
     * @param filename The path.
     * @param mode     The mode.
     * @throws JRD3Exception
     */
    public static Picture get(String filename, Mode mode) throws JRD3Exception {
        return new Picture(ResourceManager.INSTANCE.getOrCreateTexture(filename), mode);
    }

    /**
     * Perform picture rendering.
     */
    public void render() {
        if (texture != null) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.getId());
            glEnableVertexAttribArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            glDrawArrays(GL_TRIANGLES, 0, 6);
            glDisableVertexAttribArray(0);
            glBindTexture(GL_TEXTURE_2D, 0);
        }
    }

    public void setPosition(float x, float y) {
        setPositionFromAbsoluteToRelative(x, y);
    }

    /**
     * Getter for property 'position'.
     *
     * @return Value for property 'position'.
     */
    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f v) {
        setPositionFromAbsoluteToRelative(v.x, v.y);
    }

    private void setPositionFromAbsoluteToRelative(float x, float y) {
        float resX = ScreenManager.INSTANCE.getResolutionX();
        float resY = ScreenManager.INSTANCE.getResolutionY();
        position.set((x / resX) * 2f, (y / resY) * 2f);
    }

    /**
     * Cleanup picture from memory.
     *
     * @param cleanTexture Flag for texture cleanup.
     */
    public void cleanup(boolean cleanTexture) {
        if (texture != null && cleanTexture) {
            texture.cleanup();
        }

        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboHandle);
    }

    /**
     * Getter for property 'texture'.
     *
     * @return Value for property 'texture'.
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Getter for property 'mode'.
     *
     * @return Value for property 'mode'.
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Getter for property 'alpha'.
     *
     * @return Value for property 'alpha'.
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * Setter for property 'alpha'.
     *
     * @param alpha Value to set for property 'alpha' (with 0 - 1 range values).
     */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    /**
     * The position mode.
     */
    public enum Mode {
        BACKGROUND,
        FOREGROUND
    }
}
