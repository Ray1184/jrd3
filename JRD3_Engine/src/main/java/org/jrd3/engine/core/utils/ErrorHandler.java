/*
 * File created on 01/12/2015 by Ray1184
 * Last modification on 01/12/2015 by Ray1184
 */

package org.jrd3.engine.core.utils;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.utils.Logger.Level;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Central point for error management.
 *
 * @author Ray1184
 * @version 1.0
 */
public class ErrorHandler {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.create(ErrorHandler.class);

    /**
     * Handles a critical error.
     *
     * @param message The error message.
     */
    public static void handle(String message) {
        JRD3Exception exception = new JRD3Exception(message);
        ErrorHandler.LOGGER.log(Level.FATAL, message, exception);
        throw exception;
    }

    /**
     * Handles stack trace.
     *
     * @param throwable The cause.
     */
    public static void handleStackTrace(Throwable throwable) {
        JRD3Exception exception = new JRD3Exception(throwable);
        StringWriter errors = new StringWriter();
        throwable.printStackTrace(new PrintWriter(errors));
        ErrorHandler.LOGGER.log(Level.FATAL, errors.toString());
        throw exception;
    }

    /**
     * Exits from the application, showing the cause if dialog is used.
     *
     * @param throwable The cause.
     * @param useDialog Dialog usage.
     */
    public static void criticalExit(Throwable throwable, boolean useDialog) {
        if (useDialog) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException e) {
                // Nothing to do.
            }
            JFrame frame = new JFrame();
            String message;
            String title = "Error";
            if (throwable instanceof NullPointerException) {
                message = "Null pointer exception! Something inside the code has not been managed properly.";
            } else {
                message = throwable.getMessage();
            }
            JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
        }
        System.exit(0);
    }

    /**
     * Exits from the application, showing the cause if dialog is used.
     *
     * @param message   The exception description.
     * @param log       The information log to send.
     * @param useDialog Dialog usage.
     */
    public static void criticalExit(String message, String log, boolean useDialog) {
        if (useDialog) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException e) {
                // Nothing to do.
            }
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, message + log, "Error", JOptionPane.ERROR_MESSAGE);
        }
        System.exit(0);
    }
}
