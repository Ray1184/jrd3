package org.jrd3.engine.core.utils;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.graph.Texture;
import org.jrd3.engine.core.items.ModelItem;
import org.jrd3.engine.core.loaders.assimp.AnimMeshesLoader;
import org.jrd3.engine.core.utils.Logger.Level;

/**
 * Access point for all cached resources.
 *
 * @author Ray1184
 * @version 1.0
 */
public enum ResourceManager {

    INSTANCE;

    private static final long TEXTURE_MAX_CACHE = 128;

    private static final long MESH_MAX_CACHE = 128;

    private final LruCache<String, Texture> texturesMap;

    private final LruCache<String, ModelItem> modelsMap;

    private static final Logger LOGGER = Logger.create(ResourceManager.class);

    /**
     * Default constructor.
     */
    ResourceManager() throws JRD3Exception {
        texturesMap = new LruCache<>(TEXTURE_MAX_CACHE);
        modelsMap = new LruCache<>(MESH_MAX_CACHE);

    }

    /**
     * Loads dynamic texture from cache or filesystem.
     *
     * @param name     The texture name.
     * @param callback Texture creation procedure callback.
     * @return The texture.
     * @throws JRD3Exception
     */
    public Texture getOrCreateDynamicTexture(String name, TextureCreator callback) throws JRD3Exception {
        ResourceManager.LOGGER.log(Level.DEBUG, "Dyn Textures Cache size: " + texturesMap.getCurrentSize());
        if (texturesMap.hasKey(name)) {
            Texture texture = texturesMap.retrieve(name);
            texture.setCurrentlyUsed(true);
            return texture;
        }
        Texture texture = callback.createTexture();
        texturesMap.attach(name, texture);
        return texture;
    }

    /**
     * Loads texture from cache or filesystem.
     *
     * @param fileName The file name.
     * @return The texture.
     * @throws JRD3Exception
     */
    public Texture getOrCreateTexture(String fileName) throws JRD3Exception {

        ResourceManager.LOGGER.log(Level.DEBUG, "Textures Cache size: " + texturesMap.getCurrentSize());
        if (texturesMap.hasKey(fileName)) {
            Texture texture = texturesMap.retrieve(fileName);
            texture.setCurrentlyUsed(true);
            return texture;
        }
        Texture texture = new Texture(fileName);
        texturesMap.attach(fileName, texture);
        return texture;
    }

    /**
     * Loads model from cache or filesystem.
     *
     * @param fileName    The file name.
     * @param texFileName Texture file name.
     * @return The model item.
     * @throws JRD3Exception
     */

    public ModelItem getOrCreateModelItem(String fileName, String texFileName) throws JRD3Exception {
        return getOrCreateModelItem(fileName, texFileName, "");
    }

    /**
     * Loads model from cache or filesystem.
     *
     * @param fileName    The file name.
     * @param texFileName Texture file name.
     * @param id          Id for models duplicates.
     * @return The model item.
     * @throws JRD3Exception
     */
    // TODO - For different instances with same mesh, avoid to load a new mesh and create new model using same mesh.
    public ModelItem getOrCreateModelItem(String fileName, String texFileName, String id) throws JRD3Exception {
        ResourceManager.LOGGER.log(Level.DEBUG, "Models Cache size: " + modelsMap.getCurrentSize());
        if (modelsMap.hasKey(fileName + texFileName + id)) {
            ModelItem item = modelsMap.retrieve(fileName + texFileName + id);
            item.setCurrentlyUsed(true);
            return item;
        }
        ModelItem item = AnimMeshesLoader.loadAnimModelItem(fileName, texFileName);
        modelsMap.attach(fileName + texFileName, item);
        return item;
    }


    @FunctionalInterface
    public interface TextureCreator {
        Texture createTexture();
    }


}
