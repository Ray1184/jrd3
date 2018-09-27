package org.jrd3.engine.core.items;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.graph.Texture;
import org.jrd3.engine.core.utils.ResourceManager;

/**
 * Grayscale image for render depth only. This is the core object for simulate
 * depth in 2.5D scenarios.
 *
 * @author Ray1184
 * @version 1.0
 */
public class DepthMask extends Picture {

    public DepthMask(Texture texture) {
        super(texture, Mode.BACKGROUND);
    }

    public static DepthMask get(String filename) throws JRD3Exception {
        return new DepthMask(ResourceManager.INSTANCE.getOrCreateTexture(filename));
    }
}
