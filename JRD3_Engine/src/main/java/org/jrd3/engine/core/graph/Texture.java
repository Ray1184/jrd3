package org.jrd3.engine.core.graph;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.loaders.Cacheable;
import org.jrd3.engine.core.utils.CommonUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Texture implements Cacheable {

    private static int debMeshCount;

    private final int id;

    private final int width;

    private final int height;

    private int numRows = 1;

    private int numCols = 1;

    private boolean used;

    /**
     * Creates an empty texture.
     *
     * @param width       Width of the texture
     * @param height      Height of the texture
     * @param pixelFormat Specifies the format of the pixel data (GL_RGBA, etc.)
     * @throws JRD3Exception
     */
    public Texture(int width, int height, int pixelFormat) throws JRD3Exception {
        id = glGenTextures();
        this.width = width;
        this.height = height;
        glBindTexture(GL_TEXTURE_2D, id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, this.width, this.height, 0, pixelFormat, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        used = true;
        Texture.debMeshCount++;
    }

    public Texture(String fileName, int numCols, int numRows) throws JRD3Exception {
        this(fileName);
        this.numCols = numCols;
        this.numRows = numRows;
    }

    public Texture(String fileName) throws JRD3Exception {
        this(CommonUtils.ioResourceToByteBuffer(fileName, 1024));
    }


    public Texture(ByteBuffer imageData) {
        try (MemoryStack stack = stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer avChannels = stack.mallocInt(1);

            // Decode texture image into a byte buffer
            ByteBuffer decodedImage = stbi_load_from_memory(imageData, w, h, avChannels, 4);

            width = w.get();
            height = h.get();

            // Create a new OpenGL texture 
            id = glGenTextures();
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, id);

            // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            // Upload the texture data
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, decodedImage);
            used = true;
        }
        Texture.debMeshCount++;
    }

    /**
     * Getter for property 'numCols'.
     *
     * @return Value for property 'numCols'.
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Getter for property 'numRows'.
     *
     * @return Value for property 'numRows'.
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Getter for property 'width'.
     *
     * @return Value for property 'width'.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Getter for property 'height'.
     *
     * @return Value for property 'height'.
     */
    public int getHeight() {
        return height;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    /**
     * Getter for property 'id'.
     *
     * @return Value for property 'id'.
     */
    public int getId() {
        return id;
    }

    /**
     * Debug utils.
     *
     * @return All meshes.
     */
    public static int getDebMeshCount() {
        return Texture.debMeshCount;
    }

    @Override
    public void cleanup() {
        glDeleteTextures(id);
        Texture.debMeshCount--;
    }

    @Override
    public boolean isCurrentlyUsed() {
        return used;
    }

    @Override
    public long getSize() {
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Texture texture = (Texture) o;

        if (id != texture.id) return false;
        if (width != texture.width) return false;
        if (height != texture.height) return false;
        if (numRows != texture.numRows) return false;
        return numCols == texture.numCols;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + numRows;
        result = 31 * result + numCols;
        return result;
    }

    public void setCurrentlyUsed(boolean used) {
        this.used = used;
    }
}
