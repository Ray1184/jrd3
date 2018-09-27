package org.jrd3.editor.gui;


import org.jrd3.editor.Window;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PVector;

import static org.jrd3.editor.Window.PIXEL_RATIO;

/**
 * Simple GUI element.
 *
 * @author Ray1184
 * @version 1.0
 */
public class GUIElement {

    protected PFont font;
    protected final Window window;
    protected final PVector position;
    protected final PVector size;
    protected final PVector rectColor;
    protected final PVector backColor;
    protected final PVector labelColor;
    protected final String label;
    protected boolean visible;
    protected final boolean centerLabel;
    protected String id;


    public static final PVector AREA_MAIN_COLOR = new PVector(64, 50, 0);
    public static final PVector AREA_BACK_COLOR = new PVector(32, 16, 0);
    public static final PVector AREA_LABEL_COLOR = new PVector(200, 200, 200);


    public GUIElement(String id, Window window, int x, int y, int width, int height, String label, boolean centerLabel) {
        this(id, window, x, y, width, height, label, centerLabel, AREA_MAIN_COLOR, AREA_BACK_COLOR, AREA_LABEL_COLOR);

    }

    public GUIElement(String id, Window window, int x, int y, int width, int height, String label, boolean centerLabel, PVector rectColor, PVector backColor, PVector labelColor) {
        this.id = id;
        this.window = window;
        position = new PVector(x, y);
        size = new PVector(width, height);
        this.rectColor = rectColor;
        this.backColor = backColor;
        this.labelColor = labelColor;
        font = window.loadFont("Fonts/CourierNewPSMT-12.vlw");
        this.label = label;
        visible = true;
        window.getElements().put(id, this);
        this.centerLabel = centerLabel;
    }

    public void draw() {
        if (!visible) {
            return;
        }
        window.pushMatrix();
        window.rectMode(PConstants.CORNER);
        window.noStroke();
        window.fill(backColor.x, backColor.y, backColor.z);
        window.rect(position.x + PIXEL_RATIO, position.y + PIXEL_RATIO, size.x, size.y);
        window.fill(rectColor.x, rectColor.y, rectColor.z);
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
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Getter for property 'id'.
     *
     * @return Value for property 'id'.
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for property 'id'.
     *
     * @param id Value to set for property 'id'.
     */
    public void setId(String id) {
        this.id = id;
    }

}
