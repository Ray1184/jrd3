package org.jrd3.engine.playenv.controllers;

import org.jrd3.engine.core.graph.anim.AnimController;
import org.jrd3.engine.core.graph.anim.Animation;

import java.util.Map;

/**
 * Default controller for player entity.
 *
 * @author Ray1184
 * @version 1.0
 */
public class MovableAnimController implements AnimController {


    @Override
    public void init(Map<String, Animation> anims) {

    }

    @Override
    public void update(Animation anim, float tpf) {
        int lastFrame = anim.isCompleteAfterHalf() ? anim.getLast() / 2 : anim.getLast();
        // Always finish loop animation.
        if (anim.isPlaying()) {
            anim.nextFrame();
            if (anim.getCurrentFrameIndex() == anim.getFirst() ||
                    anim.getCurrentFrameIndex() == lastFrame - 1 ||
                    anim.getCurrentFrameIndex() == lastFrame || anim.getCurrentFrameIndex() == lastFrame + 1) {
                if (!anim.isLoop()) {
                    anim.stop();
                }
            }
        }
    }
}
