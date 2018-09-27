package org.jrd3.editor.gui;

import org.jrd3.editor.Window;
import processing.core.PConstants;
import processing.core.PVector;

import static org.jrd3.editor.Window.PIXEL_RATIO;
import static processing.core.PConstants.BACKSPACE;
import static processing.core.PConstants.ENTER;

/**
 * Simple input text box.
 *
 * @author Ray1184
 * @version 1.0
 */
public class InputText extends GUIElement {

    public static final PVector INPUT_MAIN_COLOR = new PVector(100, 100, 100);
    public static final PVector INPUT_BACK_COLOR = new PVector(10, 10, 10);
    public static final PVector INPUT_LABEL_COLOR = new PVector(200, 200, 200);

    private String text;
    private final int limit;
    private boolean canDigit;

    private final boolean onlyDigits;
    private boolean selected;

    public InputText(String id, Window window, int x, int y, int width, int height, String label, boolean centerLabel, int limit) {
        this(id, window, x, y, width, height, label, centerLabel, limit, false);
    }


    public InputText(String id, Window window, int x, int y, int width, int height, String label, boolean centerLabel, int limit, boolean onlyDigits) {
        super(id, window, x, y, width, height, label, centerLabel, INPUT_MAIN_COLOR, INPUT_BACK_COLOR, INPUT_LABEL_COLOR);
        text = "";
        selected = false;

        canDigit = true;
        this.limit = limit;
        this.onlyDigits = onlyDigits;
    }

    @Override
    public void draw() {
        if (!visible) {
            return;
        }
        if (window.mouseX > position.x && window.mouseX < (position.x + size.x) &&
                window.mouseY > position.y && window.mouseY < (position.y + size.y)) {
            if (window.mousePressed) {
                selected = true;

            }
        } else {
            if (window.mousePressed) {
                selected = false;

            }
        }

        window.pushMatrix();
        window.rectMode(PConstants.CORNER);
        window.noStroke();
        window.fill(backColor.x, backColor.y, backColor.z);
        window.rect(position.x + PIXEL_RATIO, position.y + PIXEL_RATIO, size.x, size.y);
        if (selected) {
            window.fill(rectColor.x / 2, rectColor.y / 2, rectColor.z / 2);
        } else {
            window.fill(rectColor.x, rectColor.y, rectColor.z);
        }
        window.rect(position.x, position.y, size.x, size.y);
        window.fill(labelColor.x, labelColor.y, labelColor.z);
        if (label != null && label.trim().length() > 0) {
            window.textAlign(PConstants.CENTER);
            window.text(label, (position.x + size.x / 2), position.y + font.getSize() * PIXEL_RATIO + 8 * PIXEL_RATIO);
        }

        if (selected) {
            recordText();
        }
        window.text(text, (position.x + size.x / 2), position.y + font.getSize() + PIXEL_RATIO + 6 * PIXEL_RATIO);
        window.popMatrix();
    }

    private void recordText() {
        if (window.keyPressed && canDigit) {
            canDigit = false;
            if (window.key == BACKSPACE && text.length() > 0) {
                text = text.substring(0, text.length() - 1);
            } else if (window.key == ENTER) {
                selected = false;
            } else if (text.length() <= limit) {
                if (!onlyDigits) {
                    text += Character.toUpperCase(window.key);
                } else {
                    if (Character.isDigit(window.key) || (window.key == '+' && text.length() == 0) || (window.key == '-' && text.length() == 0) || (window.key == '.' && !text.contains("."))) {
                        text += window.key;
                    }
                }
            }

        } else if (!window.keyPressed) {
            canDigit = true;
        }
    }

    /**
     * Getter for property 'text'.
     *
     * @return Value for property 'text'.
     */
    public String getText() {
        return text;
    }

    /**
     * Setter for property 'text'.
     *
     * @param text Value to set for property 'text'.
     */
    public void setText(String text) {
        this.text = text;
    }


    /**
     * Getter for property 'selected'.
     *
     * @return Value for property 'selected'.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Setter for property 'selected'.
     *
     * @param selected Value to set for property 'selected'.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
