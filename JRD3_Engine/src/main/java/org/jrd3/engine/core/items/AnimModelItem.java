package org.jrd3.engine.core.items;

import org.jrd3.engine.core.graph.Mesh;
import org.jrd3.engine.core.graph.anim.AnimController;
import org.jrd3.engine.core.graph.anim.AnimNode;
import org.jrd3.engine.core.graph.anim.Animation;

import java.util.Map;
import java.util.Optional;

public class AnimModelItem extends ModelItem {

    private final Map<String, Animation> animations;

    private final AnimNode rootAnimNode;

    private Animation currentAnimation;

    private AnimController animController;

    public AnimModelItem(Mesh[] meshes, Map<String, Animation> animations, AnimNode rootAnimNode) {
        super(meshes);
        this.animations = animations;
        this.rootAnimNode = rootAnimNode;
        Optional<Map.Entry<String, Animation>> entry = animations.entrySet().stream().findFirst();
        currentAnimation = entry.isPresent() ? entry.get().getValue() : null;
    }

    public Animation getAnimation(String name) {
        return animations.get(name);
    }

    /**
     * Getter for property 'currentAnimation'.
     *
     * @return Value for property 'currentAnimation'.
     */
    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    /**
     * Setter for property 'currentAnimation'.
     *
     * @param currentAnimation Value to set for property 'currentAnimation'.
     */
    public void setCurrentAnimation(Animation currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    /**
     * Getter for property 'animations'.
     *
     * @return Value for property 'animations'.
     */
    public Map<String, Animation> getAnimations() {
        return animations;
    }

    /**
     * Getter for property 'animController'.
     *
     * @return Value for property 'animController'.
     */
    public AnimController getAnimController() {
        return animController;
    }

    /**
     * Setter for property 'animController'.
     *
     * @param animController Value to set for property 'animController'.
     */
    public void setAnimController(AnimController animController) {
        this.animController = animController;
    }

    /**
     * Getter for property 'rootAnimNode'.
     *
     * @return Value for property 'rootAnimNode'.
     */
    public AnimNode getRootAnimNode() {
        return rootAnimNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AnimModelItem that = (AnimModelItem) o;

        if (animations != null ? !animations.equals(that.animations) : that.animations != null) return false;
        if (currentAnimation != null ? !currentAnimation.equals(that.currentAnimation) : that.currentAnimation != null)
            return false;
        return animController != null ? animController.equals(that.animController) : that.animController == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (animations != null ? animations.hashCode() : 0);
        result = 31 * result + (currentAnimation != null ? currentAnimation.hashCode() : 0);
        result = 31 * result + (animController != null ? animController.hashCode() : 0);
        return result;
    }
}
