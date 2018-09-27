package org.jrd3.engine.core.graph.anim;

import org.joml.Matrix4f;
import org.jrd3.engine.core.items.SceneNode;

import java.util.ArrayList;
import java.util.List;

public class AnimNode {

    private final List<AnimNode> children;

    private final List<Matrix4f> transformations;

    private final String name;

    private final AnimNode parent;

    private final SceneNode sceneNode;

    private final Matrix4f rootTransform;
    // DEBUG ONLY
    public int currentFrame;
    private Matrix4f holder;
    public AnimNode(String name, AnimNode parent) {
        this.name = name;
        this.parent = parent;
        sceneNode = new SceneNode(name);
        transformations = new ArrayList<>();
        children = new ArrayList<>();
        rootTransform = new Matrix4f();
    }

    public static Matrix4f getParentTransforms(AnimNode animNode, int framePos) {
        return AnimNode.getParentTransforms(animNode, framePos, null);
    }

    public static Matrix4f getParentTransforms(AnimNode animNode, int framePos, Matrix4f rootTransform) {
        if (animNode == null) {
            if (rootTransform != null) {
                return rootTransform;
            } else {
                return new Matrix4f();
            }
        } else {

            Matrix4f parentTransform = new Matrix4f(AnimNode.getParentTransforms(animNode.getParent(), framePos, rootTransform));
            List<Matrix4f> transformations = animNode.getTransformations();
            Matrix4f nodeTransform;
            if (framePos < transformations.size()) {
                nodeTransform = transformations.get(framePos);
            } else {
                nodeTransform = new Matrix4f();
            }
            //animNode.sceneNode.getWorldTransform().set(nodeTransform);
            return parentTransform.mul(nodeTransform);
        }
    }

    public Matrix4f getCurrentTransf() {
        return transformations.get(currentFrame);
    }

    public void addChild(AnimNode animNode) {
        children.add(animNode);
    }

    public void addTransformation(Matrix4f transformation) {
        transformations.add(transformation);
    }

    public AnimNode findByNameInternal(String targetName) {
        AnimNode result = null;
        if (name.equals(targetName)) {
            result = this;
        } else {
            for (AnimNode child : children) {
                result = child.findByNameInternal(targetName);
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Updates the whole transformation tree for nodes attached to animated bones.
     *
     * @param frameIndex Transform index for current animation.
     */
    public void updateAnimationGraph(int frameIndex) {
        if (holder != null) {
            currentFrame = frameIndex;
            //rootTransform.scale(0.2f);
            //rootTransform.translationRotate(holder.getPosition().x, holder.getPosition().y, holder.getPosition().z, holder.getRotation());
            rootTransform.set(holder);
            sceneNode.getWorldTransform().set(AnimNode.getParentTransforms(this, frameIndex, rootTransform));

        }

    }

    /**
     * Getter for property 'holder'.
     *
     * @return Value for property 'holder'.
     */
    public Matrix4f getHolder() {
        return holder;
    }



    /**
     * Getter for property 'animationFrames'.
     *
     * @return Value for property 'animationFrames'.
     */
    public int getAnimationFrames() {
        int numFrames = transformations.size();
        for (AnimNode child : children) {
            int childFrame = child.getAnimationFrames();
            numFrames = Math.max(numFrames, childFrame);
        }
        return numFrames;
    }

    /**
     * Getter for property 'children'.
     *
     * @return Value for property 'children'.
     */
    public List<AnimNode> getChildren() {
        return children;
    }

    /**
     * Getter for property 'transformations'.
     *
     * @return Value for property 'transformations'.
     */
    public List<Matrix4f> getTransformations() {
        return transformations;
    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for property 'parent'.
     *
     * @return Value for property 'parent'.
     */
    public AnimNode getParent() {
        return parent;
    }

    /**
     * Getter for property 'sceneNode'.
     *
     * @return Value for property 'sceneNode'.
     */
    public SceneNode getSceneNode() {
        return sceneNode;
    }

    /**
     * Set holder.
     *
     * @param holder The holder.
     */
    public void setHolder(Matrix4f holder) {
        this.holder = holder;

    }


}
