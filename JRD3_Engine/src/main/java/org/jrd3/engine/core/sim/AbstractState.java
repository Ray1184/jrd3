package org.jrd3.engine.core.sim;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.graph.Camera;
import org.jrd3.engine.core.graph.renderer.Renderer;
import org.jrd3.engine.core.scene.Scene;
import org.jrd3.engine.core.sim.scripting.CustomScript;
import org.jrd3.engine.core.sim.scripting.ScriptUtils;

/**
 * App-state logic holder.
 *
 * @author Ray1184
 * @version 1.0
 */
public abstract class AbstractState {

    protected Scene scene;
    protected Camera camera;
    protected BasicApp app;
    protected CustomScript customScript;
    private InnerStatus innerStatus;
    private ExternalStatus externalStatus;
    private Renderer renderer;
    private Window window;

    /**
     * Default constructor.
     *
     * @param scriptPath The script path.
     */
    public AbstractState(String scriptPath) {
        customScript = ScriptUtils.loadScriptCallback(scriptPath);
    }


    /**
     * AbstractState cleanup.
     */
    public final void cleanup() throws JRD3Exception {
        if (scene != null) {
            scene.cleanup();
        }
        if (renderer != null) {
            renderer.cleanup();
        }
        // TODO - Check whether additional user clean are needed.
    }

    /**
     * AbstractState init.
     */
    public abstract void init() throws JRD3Exception;

    /**
     * AbstractState update.
     *
     * @param tpf Time per frame.
     */
    public abstract void update(float tpf) throws JRD3Exception;

    /**
     * The input manager.
     *
     * @param window     The window.
     * @param mouseInput The mouse input handle.
     */
    public abstract void input(Window window, MouseInput mouseInput) throws JRD3Exception;


    /**
     * Getter for property 'innerStatus'.
     *
     * @return Value for property 'innerStatus'.
     */
    public InnerStatus getInnerStatus() {
        return innerStatus;
    }

    /**
     * Setter for property 'innerStatus'.
     *
     * @param innerStatus Value to set for property 'innerStatus'.
     */
    public void setInnerStatus(InnerStatus innerStatus) {
        this.innerStatus = innerStatus;
    }

    /**
     * Getter for property 'externalStatus'.
     *
     * @return Value for property 'externalStatus'.
     */
    public ExternalStatus getExternalStatus() {
        return externalStatus;
    }

    /**
     * Setter for property 'externalStatus'.
     *
     * @param externalStatus Value to set for property 'externalStatus'.
     */
    public void setExternalStatus(ExternalStatus externalStatus) {
        this.externalStatus = externalStatus;
    }

    /**
     * Getter for property 'scene'.
     *
     * @return Value for property 'scene'.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Setter for property 'scene'.
     *
     * @param scene Value to set for property 'scene'.
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Getter for property 'renderer'.
     *
     * @return Value for property 'renderer'.
     */
    public Renderer getRenderer() {
        return renderer;
    }

    /**
     * Setter for property 'renderer'.
     *
     * @param renderer Value to set for property 'renderer'.
     */
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Getter for property 'camera'.
     *
     * @return Value for property 'camera'.
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Setter for property 'camera'.
     *
     * @param camera Value to set for property 'camera'.
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * Getter for property 'window'.
     *
     * @return Value for property 'window'.
     */
    public Window getWindow() {
        return window;
    }

    /**
     * Setter for property 'window'.
     *
     * @param window Value to set for property 'window'.
     */
    public void setWindow(Window window) {
        this.window = window;
    }

    /**
     * Getter for property 'app'.
     *
     * @return Value for property 'app'.
     */
    public BasicApp getApp() {
        return app;
    }

    /**
     * Setter for property 'app'.
     *
     * @param app Value to set for property 'app'.
     */
    public void setApp(BasicApp app) {
        this.app = app;
    }

    /**
     * Getter for property 'customScript'.
     *
     * @return Value for property 'customScript'.
     */
    public CustomScript getCustomScript() {
        return customScript;
    }

    /**
     * Setter for property 'customScript'.
     *
     * @param customScript Value to set for property 'customScript'.
     */
    public void setCustomScript(CustomScript customScript) {
        this.customScript = customScript;
    }

    /**
     * Lifecycle of state.
     */
    public enum InnerStatus {
        NEW,
        RUNNING
    }

    /**
     * Usage of state.
     */
    public enum ExternalStatus {
        ACTIVE,
        INACTIVE
    }
}
