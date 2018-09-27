package org.jrd3.editor.gui;

import org.jrd3.editor.Window;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.*;

import static org.jrd3.editor.Window.PIXEL_RATIO;

/**
 * Simple list item menu.
 *
 * @author Ray1184
 * @version 1.0
 */
public class ListItem<T> extends GUIElement {

    private Map<String, List<T>> elements;

    private int currentSelection;

    private List<String> allSelections;

    private Window.MouseStatus mouseStatus;

    private boolean left, right;


    public static final PVector ITEMS_MAIN_COLOR = new PVector(20, 70, 20);
    public static final PVector ITEMS_BACK_COLOR = new PVector(10, 10, 10);
    public static final PVector ITEMS_LABEL_COLOR = new PVector(200, 200, 200);

    public ListItem(String id, Window window, int x, int y, int width, int height, String label, boolean centerLabel) {
        super(id, window, x, y, width, height, label, centerLabel, ITEMS_MAIN_COLOR, ITEMS_BACK_COLOR, ITEMS_LABEL_COLOR);
        elements = new TreeMap<>();
        allSelections = new ArrayList<>();
    }

    /**
     * Reset all.
     */
    public void reset() {
        elements.clear();
        allSelections.clear();
    }


    /**
     * Insert a list of value referenced by key (label).
     * If a a value inside the referenced list it's already referenced by another
     * key, this reference will be broken and reference with new key created (each value
     * for each least cannot be referenced by multiples keys).
     *
     * @param key  The key.
     * @param list A list of value.
     */
    public void addElement(String key, List<T> list) {
        // Avoid concurrent modification.
        Map<String, List<T>> cpElements = new HashMap<>(elements);
        for (Map.Entry<String, List<T>> entry : cpElements.entrySet()) {
            List<T> other = entry.getValue();
            for (T t : list) {
                if (list.contains(t)) {
                    other.remove(t);
                }
            }
            if (other.isEmpty()) {
                elements.remove(entry.getKey());
            }
        }
        elements.put(key, list);

        if (!allSelections.contains(key)) {
            allSelections.add(key);
            currentSelection = allSelections.size() - 1;

        }
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
            if (window.mouseX <= position.x + size.x / 4) {
                left = true;
                right = false;

            } else if (window.mouseX >= position.x + size.x - size.x / 4) {
                left = false;
                right = true;

            }
        } else {
            if (mouseStatus == Window.MouseStatus.CLICKED) {
                mouseStatus = Window.MouseStatus.RELEASED;
            } else {
                mouseStatus = Window.MouseStatus.INACTIVE;
                left = false;
                right = false;

            }
        }


        window.pushMatrix();
        window.rectMode(PConstants.CORNER);
        window.noStroke();
        window.fill(backColor.x, backColor.y, backColor.z);
        window.rect(position.x + PIXEL_RATIO, position.y + PIXEL_RATIO, size.x, size.y);
        window.fill(rectColor.x, rectColor.y, rectColor.z);
        window.rect(position.x, position.y, size.x, size.y);


        window.fill(labelColor.x, labelColor.y, labelColor.z);


        if (mouseStatus == Window.MouseStatus.CLICKED && left) {
            window.fill(backColor.x, backColor.y, backColor.z);
            window.stroke(backColor.x, backColor.y, backColor.z);
        } else {
            window.fill(labelColor.x, labelColor.y, labelColor.z);
            window.stroke(labelColor.x, labelColor.y, labelColor.z);
        }

        window.triangle(position.x + 5, position.y + size.y / 2, position.x + 20, position.y + size.y / 2 - 15, position.x + 20, position.y + size.y / 2 + 15);


        if (mouseStatus == Window.MouseStatus.CLICKED && right) {
            window.fill(backColor.x, backColor.y, backColor.z);
            window.stroke(backColor.x, backColor.y, backColor.z);
        } else {
            window.fill(labelColor.x, labelColor.y, labelColor.z);
            window.stroke(labelColor.x, labelColor.y, labelColor.z);
        }


        window.triangle(position.x + size.x - 10, position.y + size.y / 2, position.x + size.x - 25, position.y + size.y / 2 - 15, position.x + size.x - 25, position.y + size.y / 2 + 15);

        window.fill(labelColor.x, labelColor.y, labelColor.z);

        change();

        if (label != null && label.trim().length() > 0) {
            window.textAlign(PConstants.CENTER);
            window.text(label, (position.x + size.x / 2), position.y + font.getSize() * PIXEL_RATIO + 8 * PIXEL_RATIO);
        }

        if (!allSelections.isEmpty()) {
            window.text(allSelections.get(currentSelection), (position.x + size.x / 2), position.y + font.getSize() + PIXEL_RATIO + 6 * PIXEL_RATIO);
        } else {
            window.text("---", (position.x + size.x / 2), position.y + font.getSize() + PIXEL_RATIO + 6 * PIXEL_RATIO);

        }
        window.popMatrix();
    }

    private void change() {
        if (mouseStatus == Window.MouseStatus.RELEASED && allSelections.size() > 1) {
            if (right) {
                if (currentSelection >= allSelections.size() - 1) {
                    currentSelection = 0;
                } else {
                    currentSelection++;
                }
            } else if (left) {
                if (currentSelection == 0) {
                    currentSelection = allSelections.size() - 1;
                } else {
                    currentSelection--;
                }
            }
        }
    }


    /**
     * Returns selected element list.
     *
     * @return Element list.
     */
    public List<T> getSelectedElement() {
        if (elements == null || allSelections == null || allSelections.isEmpty()) {
            return null;
        }
        return elements.get(allSelections.get(currentSelection));
    }

    public String getSelectedLabel() {
        if (allSelections == null || allSelections.isEmpty()) {
            return null;
        }
        return allSelections.get(currentSelection);
    }

    /**
     * Getter for property 'elements'.
     *
     * @return Value for property 'elements'.
     */
    public Map<String, List<T>> getElements() {
        return elements;
    }

    /**
     * Setter for property 'elements'.
     *
     * @param elements Value to set for property 'elements'.
     */
    public void setElements(Map<String, List<T>> elements) {
        this.elements = elements;
        elements.forEach((key, ts) -> allSelections.add(key));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ListItem{");
        sb.append("elements=").append(elements);
        sb.append('}');
        return sb.toString();
    }


    public boolean changed() {
        return left || right;
    }
}
