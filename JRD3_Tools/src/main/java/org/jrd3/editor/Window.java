package org.jrd3.editor;

import org.jrd3.editor.gui.GUIElement;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Main window for graphics issues.
 *
 * @author Ray1184
 * @version 1.0
 */
public abstract class Window extends PApplet {

    public static final int PIXEL_RATIO = 5;

    private final Map<String, GUIElement> elements;


    public Window() {
        elements = new LinkedHashMap<>();
    }

    @Override
    public final void settings() {
        size(1280, 800);
        noSmooth();

    }


    @Override
    public final void draw() {
        background(0, 0, 0);
        update();
        for (Map.Entry<String, GUIElement> element : elements.entrySet()) {
            if (element.getValue().isVisible()) {
                element.getValue().draw();
            }
        }

        postProcessing(PIXEL_RATIO);
    }

    @Override
    public void line(float x1, float y1, float x2, float y2) {
        super.line(x1 / PIXEL_RATIO, y1 / PIXEL_RATIO, x2 / PIXEL_RATIO, y2 / PIXEL_RATIO);
    }

    @Override
    public void rect(float a, float b, float c, float d) {
        super.rect(a / PIXEL_RATIO, b / PIXEL_RATIO, c / PIXEL_RATIO, d / PIXEL_RATIO);
    }

    @Override
    public void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        super.triangle(x1 / PIXEL_RATIO, y1 / PIXEL_RATIO, x2 / PIXEL_RATIO, y2 / PIXEL_RATIO, x3 / PIXEL_RATIO, y3 / PIXEL_RATIO);
    }

    @Override
    public void translate(float x, float y) {
        super.translate(x / PIXEL_RATIO, y / PIXEL_RATIO);
    }

    @Override
    public void text(String str, float x, float y) {
        super.text(str, x / PIXEL_RATIO, y / PIXEL_RATIO);
    }

    /**
     * User-defined update.
     */
    protected abstract void update();

    /**
     * Pixelate the current frame.
     *
     * @param ratio The pixel ratio.
     */
    protected void postProcessing(int ratio) {
        PImage img = getFrame(ratio);
        background(0);
        imageMode(CORNER);
        image(img, 0, 0, width * ratio, height * ratio);
    }

    /**
     * Capture current frame.
     *
     * @param ratio The pixel ratio.
     * @return Current frame.
     */
    private PImage getFrame(int ratio) {
        PImage img = new PImage(width, height);

        g.loadPixels();
        img.loadPixels();

        img.pixels = g.pixels;

        img.updatePixels();
        g.updatePixels();

        return img;
    }

    /**
     * Getter for property 'elements'.
     *
     * @return Value for property 'elements'.
     */
    public Map<String, GUIElement> getElements() {
        return elements;
    }

    public enum MouseStatus {
        CLICKED,
        RELEASED,
        INACTIVE;
    }
}
