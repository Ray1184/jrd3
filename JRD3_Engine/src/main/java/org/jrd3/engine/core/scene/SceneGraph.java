package org.jrd3.engine.core.scene;

import org.jrd3.engine.core.items.SceneNode;

import static org.jrd3.engine.core.scene.SceneGraph.Mode.BREADTH_FIRST;

/**
 * Scene graph methods for traversing.
 *
 * @author Ray1184
 * @version 1.0
 */
public class SceneGraph {

    /**
     * Traverse scene graph with default method.
     *
     * @param currentNode The current node (sectorA should be the root).
     * @param visitor     The node visitor callback.
     */
    public static void traverse(SceneNode currentNode, SceneGraphVisitor visitor) {
        traverse(currentNode, visitor, BREADTH_FIRST);
    }

    /**
     * Traverse scene graph with given method.
     *
     * @param currentNode The current node (sectorA should be the root).
     * @param visitor     The node visitor callback.
     * @param mode        The mode.
     */
    public static void traverse(SceneNode currentNode, SceneGraphVisitor visitor, Mode mode) {
        if (BREADTH_FIRST == mode) {
            traverseBreadthFirst(currentNode, visitor);
        } else {
            traverseDepthFirst(currentNode, visitor);
        }
    }

    /**
     * Traverse scene graph with depth sectorA method.
     *
     * @param currentNode The current node (sectorA should be the root).
     * @param visitor     The node visitor callback.
     */
    private static void traverseDepthFirst(SceneNode currentNode, SceneGraphVisitor visitor) {
        // TODO - Not supported yet.
    }

    /**
     * Traverse scene graph with breadth sectorA method.
     *
     * @param currentNode The current node (sectorA should be the root).
     * @param visitor     The node visitor callback.
     */
    private static void traverseBreadthFirst(SceneNode currentNode, SceneGraphVisitor visitor) {
        if (currentNode != null) {
            visitor.visit(currentNode);
            for (SceneNode child : currentNode.getChildren()) {
                traverseBreadthFirst(child, visitor);
            }
        }
    }

    public enum Mode {
        BREADTH_FIRST,
        DEPTH_FIRST
    }

    public static class NodeSearcherVisitor implements SceneGraphVisitor {

        private final String name;

        private SceneNode res;

        public NodeSearcherVisitor(String name) {
            this.name = name;
        }

        @Override
        public void visit(SceneNode node) {
            if (node.getName().equals(name)) {
                res = node;
            }
        }

        /**
         * Getter for property 'res'.
         *
         * @return Value for property 'res'.
         */
        public SceneNode getRes() {
            return res;
        }

        /**
         * Setter for property 'res'.
         *
         * @param res Value to set for property 'res'.
         */
        public void setRes(SceneNode res) {
            this.res = res;
        }
    }
}
