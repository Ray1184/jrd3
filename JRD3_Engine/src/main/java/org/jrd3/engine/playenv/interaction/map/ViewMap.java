package org.jrd3.engine.playenv.interaction.map;

import org.joml.Vector2f;
import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.graph.Camera;
import org.jrd3.engine.core.items.DepthMask;
import org.jrd3.engine.core.items.Picture;
import org.jrd3.engine.core.items.Picture.Mode;
import org.jrd3.engine.core.utils.Values;
import org.jrd3.engine.playenv.interaction.CamView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Walkmap extension for different fixed views management.
 *
 * @author Ray1184
 * @version 1.0
 */
public class ViewMap extends WalkMap {


    private static final long serialVersionUID = -4661082882509557771L;

    private final Map<Sector, CamView> views;

    private final Vector2f viewActivatorPosition;

    private final Map<String, Picture> backgrounds;

    private final Map<String, DepthMask> depthMasks;

    private Picture currentBackground;

    private DepthMask currentDepthMask;

    // Used only for JRD3 Tools
    private Map<String, List<Sector>> elements;

    /**
     * Default constructor.
     *
     * @param resourcePath The walkmap path.
     * @throws JRD3Exception
     */
    public ViewMap(String resourcePath) throws JRD3Exception {
        super(resourcePath);
        views = new HashMap<>();
        viewActivatorPosition = new Vector2f();
        backgrounds = new HashMap<>();
        depthMasks = new HashMap<>();
        elements = new HashMap<>();
    }

    /**
     * Loads the backgrounds and foreground images related for the current map views.
     *
     * @throws JRD3Exception
     */
    public void bindImages() throws JRD3Exception {
        for (CamView view : views.values()) {
            backgrounds.computeIfAbsent(view.getBackgroundImage(), f -> Picture.get(Values.SCREENS_DIR + view.getBackgroundImage() + ".png", Mode.BACKGROUND));
            depthMasks.computeIfAbsent(view.getDepthImage(), f -> DepthMask.get(Values.SCREENS_DIR + view.getDepthImage() + ".png"));
        }
    }

    /**
     * Updates the view.
     *
     * @param camera The camera to update.
     * @param x      The x position of view activator.
     * @param y      The y position of view activator.
     */
    public void updateView(Camera camera, float x, float y) {
        viewActivatorPosition.set(x, y);
        updateView(camera, viewActivatorPosition);
    }

    /**
     * Updates the view (camera, background and depth).
     *
     * @param camera The camera to update.
     * @param point  The position of view activator.
     */
    public void updateView(Camera camera, Vector2f point) {
        Sector currentSample = sampleSector(point, 0.0f);
        if (current == currentSample) {
            return;
        } else {
            current = currentSample;
        }
        if (current != null) {
            if (views.containsKey(current)) {
                CamView view = views.get(current);
                camera.set(view.getViewMatrix());
                currentBackground = backgrounds.get(view.getBackgroundImage());
                currentDepthMask = depthMasks.get(view.getDepthImage());
            }
        }
    }

    /**
     * Adds new view for this walkmap.
     *
     * @param sector The activation sector.
     * @param view   The view.
     */
    public void addView(Sector sector, CamView view) {
        views.put(sector, view);
    }

    /**
     * Getter for property 'currentBackground'.
     *
     * @return Value for property 'currentBackground'.
     */
    public Picture getCurrentBackground() {
        return currentBackground;
    }

    /**
     * Getter for property 'currentDepthMask'.
     *
     * @return Value for property 'currentDepthMask'.
     */
    public DepthMask getCurrentDepthMask() {
        return currentDepthMask;
    }

    /**
     * Getter for property 'elements'.
     *
     * @return Value for property 'elements'.
     */
    public Map<String, List<Sector>> getElements() {
        return elements;
    }

    /**
     * Setter for property 'elements'.
     *
     * @param elements Value to set for property 'elements'.
     */
    public void setElements(Map<String, List<Sector>> elements) {
        this.elements = elements;
    }
}
