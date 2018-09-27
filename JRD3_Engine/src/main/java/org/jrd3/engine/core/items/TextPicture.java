package org.jrd3.engine.core.items;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.graph.Texture;
import org.jrd3.engine.core.utils.CommonUtils;
import org.jrd3.engine.core.utils.ResourceManager;
import org.jrd3.engine.core.utils.ScreenManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Picture with a text rendered on.
 *
 * @author Ray1184
 * @version 1.0
 */
public class TextPicture {


    public static final Font DEFAULT_FONT;

    static {
        String fileName = Thread.currentThread().getContextClassLoader()
                .getResource("Fonts/Alagard.ttf").getFile();
        File file = new File(fileName);
        DEFAULT_FONT = ScreenManager.INSTANCE.getFont(file.getAbsolutePath(), 16);
    }

    private final Picture picture;

    /**
     * Default constructor.
     *
     * @param content The text content.
     * @param x       X position.
     * @param y       Y position.
     * @throws JRD3Exception
     */
    private TextPicture(String content, int x, int y) throws JRD3Exception {
        this(content, x, y, TextPicture.DEFAULT_FONT, new Color(250, 200, 40, 255));
    }

    /**
     * Default constructor.
     *
     * @param content The text content.
     * @param x       X position.
     * @param y       Y position.
     * @param font    Text font.
     * @param color   The color.
     * @throws JRD3Exception
     */
    private TextPicture(String content, int x, int y, Font font, Color color) throws JRD3Exception {
        this(content, x, y, font, color, true);
    }

    /**
     * Default constructor.
     *
     * @param content The text content.
     * @param x       X position.
     * @param y       Y position.
     * @param font    Text font.
     * @param color   The color.
     * @param center  Flag for label in center of screen.
     * @throws JRD3Exception
     */
    private TextPicture(String content, int x, int y, Font font, Color color, boolean center) throws JRD3Exception {

        Texture texture = ResourceManager.INSTANCE.getOrCreateDynamicTexture(x + String.valueOf(content.hashCode()) + y + String.valueOf(font.hashCode()) + color, () -> createTexture(content, x, y, font, color, center));
        picture = new Picture(texture, Picture.Mode.FOREGROUND);
    }

    /**
     * Takes text from cached resources.
     *
     * @param content The text content.
     * @param x       X position.
     * @param y       Y position.
     * @throws JRD3Exception
     */
    public static TextPicture get(String content, int x, int y) throws JRD3Exception {
        return new TextPicture(content, x, y, TextPicture.DEFAULT_FONT, new Color(250, 200, 40, 255));
    }

    /**
     * Takes text from cached resources.
     *
     * @param content The text content.
     * @param x       X position.
     * @param y       Y position.
     * @param font    Text font.
     * @param color   The color.
     * @throws JRD3Exception
     */
    public static TextPicture get(String content, int x, int y, Font font, Color color) throws JRD3Exception {
        return new TextPicture(content, x, y, font, color, true);
    }

    /**
     * Takes text from cached resources.
     *
     * @param content The text content.
     * @param x       X position.
     * @param y       Y position.
     * @param font    Text font.
     * @param color   The color.
     * @param center  Flag for label in center of screen.
     * @throws JRD3Exception
     */
    public static TextPicture get(String content, int x, int y, Font font, Color color, boolean center) throws JRD3Exception {
        return new TextPicture(content, x, y, font, color, center);
    }

    private Texture createTexture(String content, int x, int y, Font font, Color color, boolean center) throws JRD3Exception {
        int wx = ScreenManager.INSTANCE.getResolutionX();
        int wy = ScreenManager.INSTANCE.getResolutionY();

        BufferedImage bufferedImage = new BufferedImage(wx, wy,
                BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(new Color(0, true));

        graphics.setColor(color);
        graphics.setFont(font);
        for (String line : content.split("\n")) {
            int sum = 0;
            int[] numbers = graphics.getFontMetrics().getWidths();
            for (int i = 0; i < numbers.length; i++) {
                sum += numbers[i];
            }
            float average = sum / numbers.length + 1;
            float xLength = line.length() * average;
            graphics.drawString(line, center ? x + (wx / 2) - ((int) (xLength / 2)) : x, y += graphics.getFontMetrics().getHeight());
        }

        CommonUtils.flipImage(bufferedImage);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", os);
        } catch (IOException e) {
            throw new JRD3Exception(e);
        }
        InputStream is = new ByteArrayInputStream(os.toByteArray());

        return new Texture(CommonUtils.inputStreamToByteBuffer(is, 1024));
    }

    /**
     * Perform picture rendering.
     */
    public void render() {
        picture.render();
    }

    /**
     * Cleanup picture from memory.
     */
    public void cleanup() {
        picture.cleanup(true);
    }

    /**
     * Getter for property 'picture'.
     *
     * @return Value for property 'picture'.
     */
    public Picture getPicture() {
        return picture;
    }
}
