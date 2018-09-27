package org.jrd3.engine.core.graph.renderer;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.graph.Camera;
import org.jrd3.engine.core.scene.Scene;
import org.jrd3.engine.core.sim.Window;

/**
 * Generic rendering engine.
 *
 * @author Ray1184
 * @version 1.0
 */
public interface Renderer {

    /**
     * Init procedure.
     *
     * @param window The window.
     * @param scene The scene.
     * @throws JRD3Exception
     */
    void init(Window window, Scene scene) throws JRD3Exception;

    /**
     * Loop rendering.
     *
     * @param window The window.
     * @param camera The camera.
     * @param scene The scene.
     */
    void render(Window window, Camera camera, Scene scene);

    /**
     * Cleanup procedure.
     */
    void cleanup();


}
