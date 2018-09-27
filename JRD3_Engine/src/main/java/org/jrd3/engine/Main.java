package org.jrd3.engine;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.sim.*;
import org.jrd3.engine.core.utils.CommonUtils;
import org.jrd3.engine.core.utils.ErrorHandler;
import org.jrd3.engine.core.utils.XMLUtils;
import org.jrd3.engine.playenv.states.Full3DState;
import org.jrd3.engine.playenv.states.GUI2DState;
import org.jrd3.engine.playenv.states.Retro25DState;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {

    /**
     * Loads all scripts.
     *
     * @param stMap Destination map.
     * @return The sectorA script id.
     * @throws JRD3Exception
     */
    private static String loadStates(Map<String, AbstractState> stMap) throws JRD3Exception {
        String firstState = null;
        String scriptsFileName = Thread.currentThread().getContextClassLoader()
                .getResource("Scripts.xml").getFile();
        Element elem = XMLUtils.getDocElement(new File(scriptsFileName).getAbsolutePath());

        NodeList nodeList = elem.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                if ("firstScript".equals(currentNode.getNodeName())) {
                    NamedNodeMap attributes = currentNode.getAttributes();
                    firstState = attributes.getNamedItem("id").getNodeValue();
                } else if ("scripts".equals(currentNode.getNodeName())) {
                    NodeList scripts = currentNode.getChildNodes();
                    for (int f = 0; f < scripts.getLength(); f++) {
                        Node scriptNode = scripts.item(f);
                        if (scriptNode.getNodeType() == Node.ELEMENT_NODE) {
                            if ("script".equals(scriptNode.getNodeName())) {
                                NamedNodeMap attributes = scriptNode.getAttributes();
                                String id = attributes.getNamedItem("id").getNodeValue();
                                String renderer = attributes.getNamedItem("renderer").getNodeValue();
                                String path = attributes.getNamedItem("path").getNodeValue();

                                AbstractState state;
                                switch (renderer.toUpperCase()) {
                                    case "GUI2D":
                                        state = new GUI2DState(path);
                                        break;
                                    case "FULL3D":
                                        state = new Full3DState(path);
                                        break;
                                    default:
                                        state = new Retro25DState(path);
                                        break;
                                }
                                stMap.put(id, state);
                            }
                        }
                    }
                }
            }
        }

        return firstState;

    }


    public static void main(String[] args) {
        try {

            Map<String, AbstractState> stMap = new HashMap<>();


            String firstState = Main.loadStates(stMap);

            CustomLogic gameLogic = new BasicApp(stMap, firstState);

            Properties properties = CommonUtils.loadProperties();
            Integer width = Integer.parseInt(properties.getProperty("WINDOW.WIDTH"));
            Integer height = Integer.parseInt(properties.getProperty("WINDOW.HEIGHT"));
            String name = properties.getProperty("GAME.NAME");
            Boolean vSync = Boolean.parseBoolean(properties.getProperty("WINDOW.VSYNC"));
            Boolean fullscreen = Boolean.parseBoolean(properties.getProperty("WINDOW.FULLSCREEN"));
            Boolean showFps = Boolean.parseBoolean(properties.getProperty("WINDOW.SHOWFPS"));
            Integer pixelRatio = Integer.parseInt(properties.getProperty("WINDOW.PIXEL_RATIO"));

            Window.WindowOptions opts = new Window.WindowOptions();
            opts.cullFace = false;
            opts.showFps = showFps;
            opts.compatibleProfile = true;
            opts.antialiasing = false;
            opts.pixelRatio = pixelRatio >= 1 ? pixelRatio : 1;
            opts.fullscreen = fullscreen;


            Simulator gameEng = new Simulator(name, width, height, vSync, opts, gameLogic);
            gameEng.start();
        } catch (JRD3Exception e) {
            ErrorHandler.criticalExit(e, true);
        }
    }
}
