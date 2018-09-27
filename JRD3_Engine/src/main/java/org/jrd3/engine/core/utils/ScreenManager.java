package org.jrd3.engine.core.utils;

import org.jrd3.engine.core.exceptions.JRD3Exception;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Helper for text managing.
 * Internal use only.
 *
 * @author Ray1184
 * @version 1.0
 */
public enum ScreenManager {

    INSTANCE;

    private final int resolutionX;

    private final int resolutionY;

    private final Map<String, Font> cachedFonts;

    /**
     * Default constructor.
     */
    ScreenManager() {
        cachedFonts = new HashMap<>();
        Properties properties = CommonUtils.loadProperties();
        resolutionX = Integer.parseInt(properties.getProperty("WINDOW.RESX"));
        resolutionY = Integer.parseInt(properties.getProperty("WINDOW.RESY"));
    }

    /**
     * Gets a cached font (or create it).
     *
     * @param name The font name and path.
     * @param size The font size.
     * @return The font.
     * @throws JRD3Exception
     */
    public Font getFont(String name, float size) throws JRD3Exception {
        if (!cachedFonts.containsKey(name + size)) {
            try {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(name)).deriveFont(size);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(name)));
                cachedFonts.put(name + size, customFont);
            } catch (IOException | FontFormatException e) {
                throw new JRD3Exception(e);
            }
        }
        return cachedFonts.get(name + size);
    }

    /**
     * Getter for property 'resolutionX'.
     *
     * @return Value for property 'resolutionX'.
     */
    public int getResolutionX() {
        return resolutionX;
    }

    /**
     * Getter for property 'resolutionY'.
     *
     * @return Value for property 'resolutionY'.
     */
    public int getResolutionY() {
        return resolutionY;
    }

}
