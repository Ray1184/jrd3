package org.jrd3.engine.core.loaders;

/**
 * Interface for all cacheable items.
 *
 * @author Ray1184
 * @version 1.0
 */
public interface Cacheable {

    /**
     * Gets the element size.
     *
     * @return The element size.
     */
    long getSize();

    /**
     * Resource cleanup.
     */
    void cleanup();

    /**
     * Flag for current usage.
     *
     * @return <code>TRUE</code> if texture is currently in use, <code>FALSE</code> otherwise.
     */
    boolean isCurrentlyUsed();
}
