package org.jrd3.engine.core.utils;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.graph.Texture;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Some utility functions for capture screens and debug GPU buffer states.
 *
 * @author Ray1184
 * @version 1.0
 */
public class Capturer {

    /**
     * Store image of current bounded texture.
     *
     * @param filename Where to store the image.
     * @param width    Image width.
     * @param height   Image height.
     */
    public static void printBoundedTexture(String filename, int width, int height) throws JRD3Exception {
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        printBoundedGraphicsData(filename, width, height, buffer);

    }

    /**
     * Store image of current bounded buffer on file.
     *
     * @param filename Where to store the image.
     * @param width    Image width.
     * @param height   Image height.
     */
    public static void printBoundedBuffer(String filename, int width, int height) throws JRD3Exception {
        Capturer.printBoundedBuffer(filename, width, height, GL_BACK);
    }

    /**
     * Store image of current bounded buffer on file.
     *
     * @param filename   Where to store the image.
     * @param width      Image width.
     * @param height     Image height.
     * @param bufferType The buffer type (GL_FRONT or GL_BACK).
     */
    public static void printBoundedBuffer(String filename, int width, int height, int bufferType) throws JRD3Exception {
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        glReadBuffer(bufferType);
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        printBoundedGraphicsData(filename, width, height, buffer);

    }

    /**
     * Store image of current bounded buffer in a texture.
     *
     * @param width  Image width.
     * @param height Image height.
     * @param mono   Flag for monochromatic capture.
     * @return The texture.
     */
    public static Texture printBoundedBuffer(int width, int height, boolean mono) throws JRD3Exception {
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        glReadBuffer(GL_BACK);
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        return printBoundedGraphicsData(width, height, buffer, mono);

    }

    /**
     * Store image of current bounded data (texture or buffer).
     *
     * @param filename Where to store the image.
     * @param width    Image width.
     * @param height   Image height.
     * @param buffer   Buffer where store the image.
     */
    public static void printBoundedGraphicsData(String filename, int width, int height, ByteBuffer buffer) throws JRD3Exception {
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        File file = new File(filename); // The file to save to.
        String format = "PNG"; // Example: "PNG" or "JPG"
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }

        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) {
            throw new JRD3Exception(e.getMessage());
        }
    }

    /**
     * Store image of current bounded data (texture or buffer).
     *
     * @param width  Image width.
     * @param height Image height.
     * @param buffer Buffer where store the image.
     * @param mono   Flag for monochromatic capture.
     * @return The texture.
     */
    public static Texture printBoundedGraphicsData(int width, int height, ByteBuffer buffer, boolean mono) throws JRD3Exception {
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                if (mono) {
                    int m = (r + g + b) / 3;
                    image.setRGB(x, height - (y + 1), (0xFF << 24) | (m << 16) | (m << 8) | m);
                } else {
                    image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
                }
            }
        }

        CommonUtils.flipImage(image);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            throw new JRD3Exception(e);
        }
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        return new Texture(CommonUtils.inputStreamToByteBuffer(is, 1024));
    }
}

