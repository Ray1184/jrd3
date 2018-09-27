/*
 * File created on 20/11/2015 by Ray1184
 * Last modification on 20/11/2015 by Ray1184
 */
package org.jrd3.engine.core.exceptions;

/**
 * Exception thrown when there's a critical application error and needs to stop
 * the program with a window error.
 *
 * @author Ray1184
 * @version 1.0
 */
public class JRD3Exception extends RuntimeException {

    /**
     * Serial Id.
     */
    private static final long serialVersionUID = 2745331472161374356L;

    /**
     * Exception constructor.
     *
     * @param message The message to display.
     */
    public JRD3Exception(String message) {
        super(message);
    }

    /**
     * Exception constructor.
     *
     * @param t The throwable.
     */
    public JRD3Exception(Throwable t) {
        super(t);
    }
}
