package org.jrd3.engine.core.graph.anim;

import java.util.List;

public class Animation {

    private final List<AnimatedFrame> frames;
    private final String name;
    private final double duration;
    private int currentFrame;
    private boolean playing;
    private boolean loop;
    private int invSpeed;
    private int jmpCount;
    private int first;
    private int last;
    private boolean completeAfterHalf;
    private boolean cycleCompleted;


    public Animation(String name, List<AnimatedFrame> frames, double duration) {
        this.name = name;
        this.frames = frames;
        currentFrame = 0;
        this.duration = duration;
        playing = false;
        loop = false;
        invSpeed = 1;
        completeAfterHalf = false;
        first = 0;
        last = frames.size() - 1;
        cycleCompleted = false;
        jmpCount = 1;
    }

    /**
     * Getter for property 'currentFrame'.
     *
     * @return Value for property 'currentFrame'.
     */
    public AnimatedFrame getCurrentFrame() {
        return frames.get(currentFrame);
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    /**
     * Getter for property 'duration'.
     *
     * @return Value for property 'duration'.
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Getter for property 'frames'.
     *
     * @return Value for property 'frames'.
     */
    public List<AnimatedFrame> getFrames() {
        return frames;
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
     * Getter for property 'nextFrame'.
     *
     * @return Value for property 'nextFrame'.
     */
    public AnimatedFrame getNextFrame() {
        nextFrame();
        return frames.get(currentFrame);
    }

    /**
     * Goes to the next frame.
     */
    public void nextFrame() {
        if (jmpCount < invSpeed) {
            jmpCount++;
            return;
        }
        jmpCount = 1;
        int nextFrame = currentFrame + 1;
        if (nextFrame >= last) {
            currentFrame = first;
        } else {
            currentFrame = nextFrame;
        }
        if (currentFrame + nextFrame >= last) {
            cycleCompleted = true;
        } else {
            cycleCompleted = false;
        }
    }

    /**
     * Getter for property 'playing'.
     *
     * @return Value for property 'playing'.
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Play animation.
     */
    public void play() {
        playing = true;
    }

    /**
     * Stop animation.
     */
    public void stop() {
        playing = false;
    }

    /**
     * Getter for property 'loop'.
     *
     * @return Value for property 'loop'.
     */
    public boolean isLoop() {
        return loop;
    }

    /**
     * Setter for property 'loop'.
     *
     * @param loop Value to set for property 'loop'.
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    /**
     * Gets the current frame index.
     *
     * @return The current frame index.
     */
    public int getCurrentFrameIndex() {
        return currentFrame;
    }

    /**
     * Sets the current frame index.
     *
     * @param currentFrame The frame index.
     */
    public void setCurrentFrameIndex(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    /**
     * Getter for property 'invSpeed'.
     *
     * @return Value for property 'invSpeed'.
     */
    public int getInvSpeed() {
        return invSpeed;
    }

    /**
     * Setter for property 'invSpeed'.
     *
     * @param invSpeed Value to set for property 'invSpeed'.
     */
    public void setInvSpeed(int invSpeed) {
        this.invSpeed = invSpeed;
    }

    /**
     * Getter for property 'first'.
     *
     * @return Value for property 'first'.
     */
    public int getFirst() {
        return first;
    }

    /**
     * Setter for property 'first'.
     *
     * @param first Value to set for property 'first'.
     */
    public void setFirst(int first) {
        this.first = first;
    }

    /**
     * Getter for property 'last'.
     *
     * @return Value for property 'last'.
     */
    public int getLast() {
        return last;
    }

    /**
     * Setter for property 'last'.
     *
     * @param last Value to set for property 'last'.
     */
    public void setLast(int last) {
        this.last = last;
    }

    /**
     * Getter for property 'completeAfterHalf'.
     *
     * @return Value for property 'completeAfterHalf'.
     */
    public boolean isCompleteAfterHalf() {
        return completeAfterHalf;
    }

    /**
     * Setter for property 'completeAfterHalf'.
     *
     * @param completeAfterHalf Value to set for property 'completeAfterHalf'.
     */
    public void setCompleteAfterHalf(boolean completeAfterHalf) {
        this.completeAfterHalf = completeAfterHalf;
    }

    /**
     * Getter for property 'cycleCompleted'.
     *
     * @return Value for property 'cycleCompleted'.
     */
    public boolean isCycleCompleted() {
        return cycleCompleted;
    }

    /**
     * Setter for property 'cycleCompleted'.
     *
     * @param cycleCompleted Value to set for property 'cycleCompleted'.
     */
    public void setCycleCompleted(boolean cycleCompleted) {
        this.cycleCompleted = cycleCompleted;
    }
}
