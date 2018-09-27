package org.jrd3.engine.core.sim;

import org.jrd3.engine.core.exceptions.JRD3Exception;

import java.util.Map;

/**
 * Minimal logic perimeter.
 *
 * @author Ray1184
 * @version 1.0
 */
public class BasicApp implements CustomLogic {

    private final Map<String, AbstractState> states;

    private AbstractState currentAbstractState;



    /**
     * Default constructor.
     *
     * @param states All app states.
     */
    public BasicApp(Map<String, AbstractState> states, String firstState) throws JRD3Exception {
        this.states = states;
        if (states.containsKey(firstState)) {
            currentAbstractState = states.get(firstState);
        } else {
            throw new JRD3Exception("AbstractState by name '" + firstState + "' not present in state list.");
        }
    }

    /**
     * Switches to a different state.
     *
     * @param stateName The state name.
     * @throws JRD3Exception
     */
    public final void switchState(String stateName) throws JRD3Exception {
        if (states.containsKey(stateName)) {
            currentAbstractState.cleanup();
            currentAbstractState.setExternalStatus(AbstractState.ExternalStatus.INACTIVE);
            currentAbstractState.setInnerStatus(AbstractState.InnerStatus.NEW);
            currentAbstractState = states.get(stateName);
            currentAbstractState.setExternalStatus(AbstractState.ExternalStatus.ACTIVE);

        } else {
            throw new JRD3Exception("AbstractState by name '" + stateName + "' not present in state list.");
        }
    }

    /**
     * Getter for property 'states'.
     *
     * @return Value for property 'states'.
     */
    public Map<String, AbstractState> getStates() {
        return states;
    }

    @Override
    public final void init(Window window) throws JRD3Exception {
        // TODO - Preload here all needed long-life resources.
        states.forEach((name, abstractState) -> {
            abstractState.setInnerStatus(AbstractState.InnerStatus.NEW);
            abstractState.setExternalStatus(AbstractState.ExternalStatus.INACTIVE);
            abstractState.setWindow(window);
            abstractState.setApp(this);
        });

    }

    @Override
    public final void input(Window window, MouseInput mouseInput) throws JRD3Exception {
        currentAbstractState.input(window, mouseInput);

    }

    @Override
    public final void update(float tpf) throws JRD3Exception {

        if (AbstractState.InnerStatus.NEW == currentAbstractState.getInnerStatus()) {
            // Common logic init.
            currentAbstractState.init();
            currentAbstractState.getScene().initControllers();
            currentAbstractState.setInnerStatus(AbstractState.InnerStatus.RUNNING);
        } else if (AbstractState.InnerStatus.RUNNING == currentAbstractState.getInnerStatus()) {
            currentAbstractState.update(tpf);
            currentAbstractState.getScene().updateControllers(tpf);


        }
    }

    @Override
    public final void render(Window window) {
        if (AbstractState.InnerStatus.RUNNING == currentAbstractState.getInnerStatus()) {
            currentAbstractState.getRenderer().render(currentAbstractState.getWindow(), currentAbstractState.getCamera(), currentAbstractState.getScene());
        }
    }

    @Override
    public final void cleanup() throws JRD3Exception {
        currentAbstractState.cleanup();
    }

}
