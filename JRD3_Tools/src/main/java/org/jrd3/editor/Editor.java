package org.jrd3.editor;


import org.joml.Vector3f;
import org.jrd3.editor.gui.*;
import org.jrd3.engine.playenv.interaction.CamView;
import org.jrd3.engine.playenv.interaction.map.PathsMap;
import org.jrd3.engine.playenv.interaction.map.Sector;
import org.jrd3.engine.playenv.interaction.map.ViewMap;
import org.jrd3.engine.playenv.interaction.map.WalkMap;
import processing.core.PVector;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Main editor.
 *
 * @author Ray1184
 * @version 1.0
 */
public class Editor extends Window {

    private Button load, save, newSector, exit, confirm;
    private InputText x, y, z, rx, ry, rz, newSectorGroupName;
    private ListItem<Sector> sectorGroupSelection;
    private GUIElement camSettings;
    private InteractiveMap interactiveMap;

    public static final String DK_VIEW_X = "V_X";
    public static final String DK_VIEW_Y = "V_Y";
    public static final String DK_VIEW_Z = "V_Z";
    public static final String DK_VIEW_RX = "V_RX";
    public static final String DK_VIEW_RY = "V_RY";
    public static final String DK_VIEW_RZ = "V_RZ";


    public static final PVector OK_MAIN_COLOR = new PVector(200, 52, 0);
    public static final PVector OK_BACK_COLOR = new PVector(64, 8, 0);
    public static final PVector OK_LABEL_COLOR = new PVector(200, 200, 200);

    private boolean refreshInputText;


    @Override
    public void setup() {

        super.setup();

        interactiveMap = new InteractiveMap("WM_MAP", this, 440, 175, 115, 45, null, true);


        camSettings = new GUIElement("SETTINGS_G", this, 8, 8, 420, 790, null, false);


        newSector = new Button("NEW_BT", this, 740, 93, 170, 75, "New", true, Editor.OK_MAIN_COLOR, Editor.OK_BACK_COLOR, Editor.OK_LABEL_COLOR);
        newSector.setActionListener(args -> {
            String sgName = newSectorGroupName.getText();
            if (sgName != null && !sgName.isEmpty()) {
                sectorGroupSelection.addElement(sgName, new ArrayList<>());
            }
            newSectorGroupName.setText("");
            refreshInputText = true;

        });

        newSectorGroupName = new InputText("NEW_TX", this, 435, 102, 295, 52, null, false, 6);
        save = new Button("SAVE_BT", this, 740, 8, 170, 75, "Save", true);
        save.setActionListener(args -> {
            if (interactiveMap.getWalkMap() != null) {
                selectOutput("Save map", "onMapSave");
            }
        });

        load = new Button("LOAD_BT", this, 920, 8, 170, 75, "Load", true);
        load.setActionListener(args -> selectInput("Select map (*.jrd3m, *.objw, *objv, *.objp)", "onMapSelect"));

        exit = new Button("EXIT_BT", this, 1100, 8, 170, 75, "Exit", true);
        exit.setActionListener(args -> exit());

        confirm = new Button("CONFIRM_BT", this, 435, 8, 295, 75, "Confirm", true, Editor.OK_MAIN_COLOR, Editor.OK_BACK_COLOR, Editor.OK_LABEL_COLOR);
        confirm.setActionListener(args -> {
            String sgName = sectorGroupSelection.getSelectedLabel();
            if (sgName != null && !sgName.isEmpty()) {
                if (interactiveMap.getSelectedSectors() != null && interactiveMap.getSelectedSectors().size() > 0) {
                    sectorGroupSelection.addElement(sgName, new ArrayList<>(interactiveMap.getSelectedSectors()));
                    interactiveMap.getSelectedSectors().clear();
                }

                for (Sector s : sectorGroupSelection.getSelectedElement()) {
                    s.getSectorData().setString(Editor.DK_VIEW_X, x.getText());
                    s.getSectorData().setString(Editor.DK_VIEW_Y, y.getText());
                    s.getSectorData().setString(Editor.DK_VIEW_Z, z.getText());
                    s.getSectorData().setString(Editor.DK_VIEW_RX, rx.getText());
                    s.getSectorData().setString(Editor.DK_VIEW_RY, ry.getText());
                    s.getSectorData().setString(Editor.DK_VIEW_RZ, rz.getText());
                }

                refreshInputText = true;
            }
        });

        sectorGroupSelection = new ListItem<>("SG_SEL", this, 40, 30, 360, 52, "SECTORS MAP", false);

        x = new InputText("X_TX", this, 40, 140, 360, 52, "X AXIS", false, 8, true);
        y = new InputText("Y_TX", this, 40, 250, 360, 52, "Y AXIS", false, 8, true);

        z = new InputText("Z_TX", this, 40, 360, 360, 52, "Z AXIS", false, 8, true);
        rx = new InputText("RX_TX", this, 40, 465, 360, 52, "X ROTATION", false, 8, true);

        ry = new InputText("RY_TX", this, 40, 570, 360, 52, "Y ROTATION", false, 8, true);
        rz = new InputText("RZ_TX", this, 40, 675, 360, 52, "Z ROTATION", false, 8, true);
    }

    @Override
    protected void update() {
        background(0);
        pushMatrix();

        fill(15, 15, 15);
        rect(440, 180, 835, 615);
        fill(30, 30, 30);
        rect(435, 175, 835, 615);
        popMatrix();


        if (refreshInputText || sectorGroupSelection.changed()) {
            if (sectorGroupSelection.getSelectedElement() != null && sectorGroupSelection.getSelectedElement().size() > 0) {
                Sector s = sectorGroupSelection.getSelectedElement().get(0);
                if (s != null) {
                    x.setText(s.getSectorData().getString(Editor.DK_VIEW_X));
                    y.setText(s.getSectorData().getString(Editor.DK_VIEW_Y));
                    z.setText(s.getSectorData().getString(Editor.DK_VIEW_Z));
                    rx.setText(s.getSectorData().getString(Editor.DK_VIEW_RX));
                    ry.setText(s.getSectorData().getString(Editor.DK_VIEW_RY));
                    rz.setText(s.getSectorData().getString(Editor.DK_VIEW_RZ));
                }
            } else {
                x.setText("");
                y.setText("");
                z.setText("");
                rx.setText("");
                ry.setText("");
                rz.setText("");
            }
            refreshInputText = false;
        }

    }


    // CALLBACKS

    public void onMapSelect(File selection) {
        try {
            if (selection != null) {
                WalkMap walkMap;
                sectorGroupSelection.reset();
                refreshInputText = true;
                if (selection.getAbsolutePath().endsWith(".objw")) {
                    walkMap = new WalkMap(selection.getAbsolutePath());
                    int i = 0;
                    for (Sector s : walkMap.getSectors()) {
                        String name = "S" + i++;
                        s.setName(name);
                        walkMap.getSectorByName().put(name, s);
                    }

                } else if (selection.getAbsolutePath().endsWith(".objv")) {
                    walkMap = new ViewMap(selection.getAbsolutePath());

                } else if (selection.getAbsolutePath().endsWith(".objp")) {
                    walkMap = new PathsMap(selection.getAbsolutePath());

                } else if (selection.getAbsolutePath().endsWith(".jrd3m")) {
                    InputStream file = new FileInputStream(selection.getAbsolutePath());
                    InputStream buffer = new BufferedInputStream(file);
                    ObjectInput input = new ObjectInputStream(buffer);
                    Object res = input.readObject();
                    if (res instanceof ViewMap) {
                        walkMap = (ViewMap) res;
                        sectorGroupSelection.setElements(((ViewMap) walkMap).getElements());
                        refreshInputText = true;
                    } else if (res instanceof PathsMap) {
                        walkMap = (PathsMap) res;
                    } else {
                        walkMap = (WalkMap) res;
                    }


                } else {
                    JOptionPane.showMessageDialog(null, "Only .obj-w/v/p and .jrd3m files are allowed.");
                    return;
                }

                if (walkMap != null) {
                    interactiveMap.setWalkMap(walkMap);
                    interactiveMap.setFactor(100);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading walkmap:\n" + e.getMessage());
        }
    }

    public void onMapSave(File selection) {
        try {
            if (selection != null) {
                if (interactiveMap.getWalkMap() instanceof ViewMap) {

                    sectorGroupSelection.getElements().forEach((sName, sGroup) -> {

                        for (Sector s : sGroup) {
                            Vector3f pos = new Vector3f(Float.parseFloat(s.getSectorData().getString(Editor.DK_VIEW_X)), Float.parseFloat(s.getSectorData().getString(Editor.DK_VIEW_Y)), Float.parseFloat(s.getSectorData().getString(Editor.DK_VIEW_Z)));
                            Vector3f rot = new Vector3f(Float.parseFloat(s.getSectorData().getString(Editor.DK_VIEW_RX)), Float.parseFloat(s.getSectorData().getString(Editor.DK_VIEW_RY)), Float.parseFloat(s.getSectorData().getString(Editor.DK_VIEW_RZ)));
                            String back = sName + "_B";
                            String depth = sName + "_D";
                            CamView camView = new CamView(pos, rot, back, depth);
                            ((ViewMap) interactiveMap.getWalkMap()).addView(s, camView);
                        }
                    });

                    ((ViewMap) interactiveMap.getWalkMap()).setElements(sectorGroupSelection.getElements());
                }
                FileOutputStream fos;
                ObjectOutputStream out;
                try {
                    fos = new FileOutputStream(selection);
                    out = new ObjectOutputStream(fos);
                    out.writeObject(interactiveMap.getWalkMap());

                    out.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Cannot save " + selection.getAbsolutePath() + " file.");
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving walkmap:\n" + e.getMessage());
        }
    }
}
