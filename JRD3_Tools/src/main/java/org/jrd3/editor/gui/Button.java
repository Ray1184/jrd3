package org.jrd3.editor.gui;

import org.jrd3.editor.Window;
import processing.core.PConstants;
import processing.core.PVector;

import static org.jrd3.editor.Window.PIXEL_RATIO;

/**
 * Simple GUI button.
 *
 * @author Ray1184
 * @version 1.0
 */
public class Button extends GUIElement {

    public static final PVector BTN_MAIN_COLOR = new PVector(0, 0, 255);
    public static final PVector BTN_BACK_COLOR = new PVector(0, 0, 64);
    public static final PVector BTN_LABEL_COLOR = new PVector(200, 200, 200);

    private Window.MouseStatus mouseStatus;
    private ActionListener actionListener;

    public Button(String id, Window window, int x, int y, int width, int height, String label, boolean centerLabel) {
        super(id, window, x, y, width, height, label, centerLabel, BTN_MAIN_COLOR, BTN_BACK_COLOR, BTN_LABEL_COLOR);
    }

    public Button(String id, Window window, int x, int y, int width, int height, String label, boolean centerLabel, PVector rectColor, PVector backColor, PVector labelColor) {
        super(id, window, x, y, width, height, label, centerLabel, rectColor, backColor, labelColor);
    }

    @Override
    public void draw() {
        if (!visible) {
            return;
        }

        if (window.mouseX > position.x && window.mouseX < (position.x + size.x) &&
                window.mouseY > position.y && window.mouseY < (position.y + size.y) &&
                window.mousePressed) {
            mouseStatus = Window.MouseStatus.CLICKED;

        } else {
            if (mouseStatus == Window.MouseStatus.CLICKED) {
                mouseStatus = Window.MouseStatus.RELEASED;
            } else {
                mouseStatus = Window.MouseStatus.INACTIVE;

            }
        }


        window.pushMatrix();
        window.rectMode(PConstants.CORNER);
        window.noStroke();
        window.fill(backColor.x, backColor.y, backColor.z);
        window.rect(position.x + PIXEL_RATIO, position.y + PIXEL_RATIO, size.x, size.y);
        if (mouseStatus == Window.MouseStatus.CLICKED) {
            window.fill(backColor.x, backColor.y, backColor.z);
        } else {
            window.fill(rectColor.x, rectColor.y, rectColor.z);
        }
        window.rect(position.x, position.y, size.x, size.y);
        window.noFill();
        window.stroke(labelColor.x, labelColor.y, labelColor.z);
        window.rect(position.x + PIXEL_RATIO, position.y + PIXEL_RATIO, size.x - (PIXEL_RATIO * 3), size.y - (PIXEL_RATIO * 3));
        window.fill(labelColor.x, labelColor.y, labelColor.z);
        if (label != null && label.trim().length() > 0) {
            window.textAlign(PConstants.CENTER);
            window.textFont(font, 12);
            if (centerLabel) {
                window.text(label, (position.x + size.x / 2), (position.y + size.y / 2) + (3 * PIXEL_RATIO));
            } else {
                window.text(label, (position.x + size.x / 2), position.y + font.getSize() * PIXEL_RATIO + PIXEL_RATIO);
            }
        }
        window.popMatrix();

        // Fire action if mouse released.
        if (mouseStatus == Window.MouseStatus.RELEASED && actionListener != null) {
            actionListener.performAction(label);
        }
    }

    /**
     * Getter for property 'mouseStatus'.
     *
     * @return Value for property 'mouseStatus'.
     */
    public Window.MouseStatus getMouseStatus() {
        return mouseStatus;
    }

    /**
     * Getter for property 'actionListener'.
     *
     * @return Value for property 'actionListener'.
     */
    public ActionListener getActionListener() {
        return actionListener;
    }

    /**
     * Setter for property 'actionListener'.
     *
     * @param actionListener Value to set for property 'actionListener'.
     */
    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }


}
