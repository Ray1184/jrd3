package org.jrd3.engine.core.sim;

import org.jrd3.engine.core.exceptions.JRD3Exception;

public interface CustomLogic {

    void init(Window window) throws JRD3Exception;

    void input(Window window, MouseInput mouseInput) throws JRD3Exception;

    void update(float tpf) throws JRD3Exception;

    void render(Window window) throws JRD3Exception;

    void cleanup() throws JRD3Exception;


}