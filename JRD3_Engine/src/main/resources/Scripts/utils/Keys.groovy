package Scripts.utils

import org.jrd3.engine.core.sim.Window

import static org.lwjgl.glfw.GLFW.*

/**
 * Static functions for one-press keys.
 *
 * @author Ray1184
 * @version 1.0
 */

class Keys {

    def static invent = 2
    def static up = 2
    def static down = 2
    def static left = 2
    def static right = 2
    def static enter = 2
    def static m = 2

    static initKeys() {
        invent = 2
        up = 2
        down = 2
        left = 2
        right = 2
        enter = 2
        m = 2
    }

    static updateKeys(Window window) {
        if (invent == 1) {
            invent = 2
        }
        if (window.isKeyPressed(GLFW_KEY_I) && invent == 0) {
            invent = 1
        } else if (!window.isKeyPressed(GLFW_KEY_I)) {
            invent = 0
        }

        if (up == 1) {
            up = 2
        }
        if (window.isKeyPressed(GLFW_KEY_W) && up == 0) {
            up = 1
        } else if (!window.isKeyPressed(GLFW_KEY_W)) {
            up = 0
        }

        if (down == 1) {
            down = 2
        }
        if (window.isKeyPressed(GLFW_KEY_S) && down == 0) {
            down = 1
        } else if (!window.isKeyPressed(GLFW_KEY_S)) {
            down = 0
        }

        if (left == 1) {
            left = 2
        }
        if (window.isKeyPressed(GLFW_KEY_A) && left == 0) {
            left = 1
        } else if (!window.isKeyPressed(GLFW_KEY_A)) {
            left = 0
        }

        if (right == 1) {
            right = 2
        }
        if (window.isKeyPressed(GLFW_KEY_D) && right == 0) {
            right = 1
        } else if (!window.isKeyPressed(GLFW_KEY_D)) {
            right = 0
        }

        if (enter == 1) {
            enter = 2
        }
        if (window.isKeyPressed(GLFW_KEY_E) && enter == 0) {
            enter = 1
        } else if (!window.isKeyPressed(GLFW_KEY_E)) {
            enter = 0
        }

        if (m == 1) {
            m = 2
        }
        if (window.isKeyPressed(GLFW_KEY_M) && m == 0) {
            m = 1
        } else if (!window.isKeyPressed(GLFW_KEY_M)) {
            m = 0
        }
    }
}
