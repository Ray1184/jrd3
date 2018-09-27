/*
 * File created on 04/05/2015 by Ray1184
 * Last modification on 04/05/2015 by Ray1184
 */

package org.jrd3.engine.core.sim.scripting;

import org.jrd3.engine.core.sim.AbstractState;
import org.jrd3.engine.core.sim.MouseInput;
import org.jrd3.engine.core.sim.Window;

/**
 * This interface defines the operations done inside the scripts.
 *
 * @author Ray1184
 * @version 1.0
 */
public interface CustomScript {

    /**
     * Callback executed only one time after the scene loading.
     *
     * @param state The current state.
     */
    void onInit(AbstractState state);

    /**
     * Callback executed for each frame after initialization.
     *
     * @param state The current state.
     * @param tpf   the time per frame.
     */
    void onUpdate(AbstractState state, Float tpf);

    /**
     * Input manager.
     *
     * @param state      The current state.
     * @param window     Window for key input.
     * @param mouseInput Mouse input.
     */
    void onInput(AbstractState state, Window window, MouseInput mouseInput);

    /**
     * Callback executed only one time when the state is removed.
     *
     * @param state The current state.
     */
    void onClose(AbstractState state);

}
