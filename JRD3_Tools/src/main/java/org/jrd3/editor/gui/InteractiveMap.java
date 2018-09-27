package org.jrd3.editor.gui;

import org.joml.Vector2f;
import org.jrd3.editor.Window;
import org.jrd3.engine.playenv.interaction.map.Sector;
import org.jrd3.engine.playenv.interaction.map.WalkMap;
import org.jrd3.engine.playenv.serialization.RGBA;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.jrd3.editor.Window.PIXEL_RATIO;

/**
 * View for walkmaps.
 *
 * @author Ray1184
 * @version 1.0
 */
public class InteractiveMap extends GUIElement {

    public static final String DK_SECTOR_COLOR = "MD-COLOR";


    private WalkMap walkMap;

    private final List<Sector> selectedSectors;

    private String currentSectorGroup;

    private int factor;

    private final PVector offset;

    private final Vector2f mousePosition;

    private Window.MouseStatus mouseStatus;

    public InteractiveMap(String id, Window window, int x, int y, int width, int height, String label, boolean centerLabel) {
        super(id, window, x, y, width, height, label, centerLabel);
        selectedSectors = new ArrayList<>();
        offset = new PVector(310, 280);
        mousePosition = new Vector2f(0, 0);
    }

    @Override
    public void draw() {
        if (walkMap == null || !visible) {
            return;
        }

        if (window.mouseX > position.x && window.mouseY > position.y && window.mousePressed) {
            mouseStatus = Window.MouseStatus.CLICKED;
        } else {
            if (mouseStatus == Window.MouseStatus.CLICKED) {
                mouseStatus = Window.MouseStatus.RELEASED;
            } else {
                mouseStatus = Window.MouseStatus.INACTIVE;
            }
        }

        checkKeys();

        // Reset sector colors
        for (Sector sector : walkMap.getSectors()) {
            RGBA selColor = new RGBA(0, 0, 255, 128);
            if (sector != null && selColor.equals(sector.getSectorData().getGeneric(DK_SECTOR_COLOR))) {
                sector.getSectorData().removeGeneric(DK_SECTOR_COLOR);
            }
        }


        // Paint (blue) sectors for current group of list item
        ListItem<Sector> sectors = (ListItem<Sector>) window.getElements().get("SG_SEL");

        if (sectors != null && sectors.getSelectedElement() != null) {
            for (Sector selected : sectors.getSelectedElement()) {
                if (selected != null) {
                    RGBA selColor = new RGBA(0, 0, 255, 128);
                    selected.getSectorData().setGeneric(DK_SECTOR_COLOR, selColor);
                }
            }
        }

        // If click inside sector then reset current group and paint new sectors (red)
        List<Sector> toRemove = null;
        if (mouseStatus == Window.MouseStatus.RELEASED) {
            Sector sampled = sample();
            if (sampled != null) {
                if (sectors != null) {
                    if (sectors != null && sectors.getSelectedElement() != null) {
                        for (Sector selected : sectors.getSelectedElement()) {
                            if (selected != null) {
                                selected.getSectorData().removeGeneric(DK_SECTOR_COLOR);
                                toRemove = sectors.getSelectedElement();
                            }
                        }
                    }
                }
                if (sampled.getSectorData().getGeneric(DK_SECTOR_COLOR) == null) {
                    RGBA selColor = new RGBA(255, 0, 0, 128);
                    sampled.getSectorData().setGeneric(DK_SECTOR_COLOR, selColor);
                    selectedSectors.add(sampled);
                } else {
                    sampled.getSectorData().removeGeneric(DK_SECTOR_COLOR);
                    selectedSectors.remove(sampled);
                }
            }
        }

        if (toRemove != null) {
            toRemove.clear();
        }
        for (Sector sector : walkMap.getSectors()) {

            window.pushMatrix();
            window.translate(position.x + offset.x, position.y + offset.y);
            window.stroke(255, 255, 255);
            if (sector.getSectorData().getGeneric(DK_SECTOR_COLOR) == null) {
                window.noFill();
            } else {
                RGBA color = (RGBA) sector.getSectorData().getGeneric(DK_SECTOR_COLOR);
                window.fill(color.r, color.g, color.b, color.a);
            }
            Sector.Triangle t = sector.getTriangle();
            window.triangle(t.x1 * factor, t.z1 * factor, t.x2 * factor, t.z2 * factor, t.x3 * factor, t.z3 * factor);

            if (sector.getName() != null) {
                window.textFont(font, 12);
                float xCent = (t.x1 * factor + t.x2 * factor + t.x3 * factor) / 3;
                float yCent = (t.z1 * factor + t.z2 * factor + t.z3 * factor) / 3;
                if (sector.getSectorData().getGeneric(DK_SECTOR_COLOR) == null) {
                    window.fill(180, 180, 180, 255);
                } else {
                    window.fill(0, 255, 0, 255);
                }
                window.text(sector.getName(), xCent, yCent);
            }
            window.popMatrix();
        }
    }

    private Sector sample() {
        if (walkMap == null) {
            return null;
        }
        window.pushMatrix();
        mousePosition.set((window.mouseX - position.x - offset.x) / factor, (window.mouseY - position.y - offset.y) / factor);
        Sector ret = walkMap.sampleSector(mousePosition, 0.0f);
        window.popMatrix();
        return ret;
    }

    private boolean somethingSelected() {
        for (Map.Entry<String, GUIElement> elem : window.getElements().entrySet()) {
            if (elem.getValue() instanceof InputText) {
                if (((InputText) elem.getValue()).isSelected()) {
                    return true;
                }
            }
        }

        return false;
    }

    private void checkKeys() {
        if (window.keyPressed && !somethingSelected()) {
            if (window.key == PConstants.LEFT || window.key == 'a' || window.key == 'A') {
                offset.add(PIXEL_RATIO, 0);
            }

            if (window.key == PConstants.RIGHT || window.key == 'd' || window.key == 'D') {
                offset.add(-PIXEL_RATIO, 0);
            }

            if (window.key == PConstants.UP || window.key == 'w' || window.key == 'W') {
                offset.add(0, PIXEL_RATIO);
            }

            if (window.key == PConstants.DOWN || window.key == 's' || window.key == 'S') {
                offset.add(0, -PIXEL_RATIO);
            }

            if (window.key == 'z' || window.key == 'Z') {
                if (factor > 0) {
                    factor -= (factor / 100) + 1;
                }
            }

            if (window.key == 'x' || window.key == 'X') {
                if (factor < 1000) {
                    factor += (factor / 100) + 1;
                }
            }
        }
    }

    /**
     * Getter for property 'walkMap'.
     *
     * @return Value for property 'walkMap'.
     */
    public WalkMap getWalkMap() {
        return walkMap;
    }

    /**
     * Setter for property 'walkMap'.
     *
     * @param walkMap Value to set for property 'walkMap'.
     */
    public void setWalkMap(WalkMap walkMap) {
        this.walkMap = walkMap;
    }

    /**
     * Getter for property 'selectedSectors'.
     *
     * @return Value for property 'selectedSectors'.
     */
    public List<Sector> getSelectedSectors() {
        return selectedSectors;
    }


    /**
     * Getter for property 'factor'.
     *
     * @return Value for property 'factor'.
     */
    public int getFactor() {
        return factor;
    }

    /**
     * Setter for property 'factor'.
     *
     * @param factor Value to set for property 'factor'.
     */
    public void setFactor(int factor) {
        this.factor = factor;
    }


    /**
     * Getter for property 'currentSectorGroup'.
     *
     * @return Value for property 'currentSectorGroup'.
     */
    public String getCurrentSectorGroup() {
        return currentSectorGroup;
    }

    /**
     * Setter for property 'currentSectorGroup'.
     *
     * @param currentSectorGroup Value to set for property 'currentSectorGroup'.
     */
    public void setCurrentSectorGroup(String currentSectorGroup) {
        this.currentSectorGroup = currentSectorGroup;
    }
}
