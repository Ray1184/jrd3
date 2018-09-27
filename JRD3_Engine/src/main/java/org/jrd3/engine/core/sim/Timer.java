package org.jrd3.engine.core.sim;

public class Timer {

    private double lastLoopTime;

    public void init() {
        lastLoopTime = getTime();
    }

    /**
     * @return Current time in seconds
     */
    public double getTime() {
        return System.nanoTime() / 1000_000_000.0;
    }

    /**
     * Getter for property 'elapsedTime'.
     *
     * @return Value for property 'elapsedTime'.
     */
    public float getElapsedTime() {
        double time = getTime();
        float elapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return elapsedTime;
    }

    /**
     * Getter for property 'lastLoopTime'.
     *
     * @return Value for property 'lastLoopTime'.
     */
    public double getLastLoopTime() {
        return lastLoopTime;
    }
}