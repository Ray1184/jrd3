package org.jrd3.engine.core.scene;

import org.jrd3.engine.core.items.SceneNode;

/**
 * Visitor callback for scene graph.
 *
 * @author Ray1184
 * @version 1.0
 */
public interface SceneGraphVisitor {

    /**
     * Visitor callback
     * .
     *
     * @param node The current node
     */
    void visit(SceneNode node);
}
