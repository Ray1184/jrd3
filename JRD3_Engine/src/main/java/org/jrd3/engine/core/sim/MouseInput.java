package org.jrd3.engine.core.sim;

import org.joml.Vector2d;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {

    private final Vector2d previousPos;

    private final Vector2d currentPos;

    private final Vector2f displVec;

    private boolean inWindow;

    private boolean leftButtonPressed;

    private boolean rightButtonPressed;

    /**
     * Constructs a new MouseInput.
     */
    public MouseInput() {
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f();
    }

    public void init(Window window) {
        glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
        });
        glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered) -> {
            inWindow = entered;
        });
        glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
    }

    /**
     * Getter for property 'displVec'.
     *
     * @return Value for property 'displVec'.
     */
    public Vector2f getDisplVec() {
        return displVec;
    }

    /**
     * Getter for property 'currentPos'.
     *
     * @return Value for property 'currentPos'.
     */
    public Vector2d getCurrentPos() {
        return currentPos;
    }

    public void input(Window window) {
        displVec.x = 0;
        displVec.y = 0;
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displVec.y = (float) deltax;
            }
            if (rotateY) {
                displVec.x = (float) deltay;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    /**
     * Getter for property 'leftButtonPressed'.
     *
     * @return Value for property 'leftButtonPressed'.
     */
    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    /**
     * Getter for property 'rightButtonPressed'.
     *
     * @return Value for property 'rightButtonPressed'.
     */
    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
