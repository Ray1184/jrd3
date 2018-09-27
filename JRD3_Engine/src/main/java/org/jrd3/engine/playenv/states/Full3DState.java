package org.jrd3.engine.playenv.states;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.graph.Camera;
import org.jrd3.engine.core.graph.postfx.PixelationPostProcessingFx;
import org.jrd3.engine.core.graph.renderer.Full3DRenderer;
import org.jrd3.engine.core.graph.renderer.Renderer;
import org.jrd3.engine.core.scene.Scene;
import org.jrd3.engine.core.sim.AbstractState;
import org.jrd3.engine.core.sim.MouseInput;
import org.jrd3.engine.core.sim.Window;

/**
 * Generic state for Retro 2.5D Rendering scenarios.
 *
 * @author Ray1184
 * @version 1.0
 */
public class Full3DState extends AbstractState {

    /**
     * Default constructor.
     *
     * @param scriptPath The script path.
     */
    public Full3DState(String scriptPath) {
        super(scriptPath);
    }

    @Override
    public void init() throws JRD3Exception {
        int pixelRatio = getWindow().getWindowOptions().pixelRatio;
        Renderer renderer = null;
        renderer = new Full3DRenderer(new PixelationPostProcessingFx(getWindow().getWidth(), getWindow().getHeight(), pixelRatio));
        setScene(new Scene("RootNode"));
        setCamera(new Camera());
        setRenderer(renderer);


        try {
            customScript.onInit(this);
        } catch (Throwable t) {
            throw new JRD3Exception(t);
        }
        getRenderer().init(getWindow(), getScene());
    }

    @Override
    public void update(float tpf) throws JRD3Exception {

        try {
            customScript.onUpdate(this, tpf);
        } catch (Throwable t) {
            throw new JRD3Exception(t);
        }

        camera.updateViewMatrix();
    }

    @Override
    public void input(Window window, MouseInput mouseInput) throws JRD3Exception {
        try {
            customScript.onInput(this, window, mouseInput);
        } catch (Throwable t) {
            throw new JRD3Exception(t);
        }
    }
}
