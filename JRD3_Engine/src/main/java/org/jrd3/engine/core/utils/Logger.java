/*
 * File created on 20/11/2015 by Ray1184
 * Last modification on 20/11/2015 by Ray1184
 */
package org.jrd3.engine.core.utils;

import com.google.code.ultralog.BasicLogger;
import com.google.code.ultralog.LoggerManager;
import com.google.code.ultralog.configuration.PropertiesConfigurator;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Central application logger.
 *
 * @author Ray1184
 * @version 1.0
 */
public class Logger {

    /**
     * The log file name.
     */
    private static final String JRD3_LOG_FILE = "JRD3.log";

    /**
     * The basic logger.
     */
    private final BasicLogger logger;

    /**
     * The log buffer (at maxim one log for each class).
     */
    private static final Map<Class<?>, Logger> LOG_BUFFER = new HashMap<>();

    /**
     * Private constructor.
     *
     * @param clazz The class.
     */
    private Logger(Class<?> clazz) {
        Properties properties = new Properties();
        properties.put("writer.type", "file");
        properties.put("writer.fileName", Logger.JRD3_LOG_FILE);
        properties.put("writer.level", Level.TRACE.toString());
        PropertiesConfigurator.configure(properties);
        logger = LoggerManager.createLogger(BasicLogger.class, clazz.getCanonicalName());
    }

    /**
     * Gets a new logger.
     *
     * @param clazz The class.
     * @return The logger instance.
     */
    public static Logger create(Class<?> clazz) {
        Logger.LOG_BUFFER.putIfAbsent(clazz, new Logger(clazz));
        return Logger.LOG_BUFFER.get(clazz);
    }

    /**
     * Makes the log.
     *
     * @param level   The log level.
     * @param message The log message.
     */
    public void log(Level level, String message) {
        log(level, message, null);
    }

    /**
     * Makes the log.
     *
     * @param level     The log level.
     * @param message   The log message.
     * @param throwable The throwable.
     */
    public void log(Level level, String message, Throwable throwable) {
        switch (level) {
            case TRACE:
                if (logger.isTraceEnabled()) {
                    logger.trace(message);
                }
                break;
            case DEBUG:
                if (logger.isDebugEnabled()) {
                    logger.debug(message);
                }
                break;
            case INFO:
                if (logger.isInfoEnabled()) {
                    logger.info(message);
                }
                break;
            case WARNING:
                if (logger.isWarningEnabled()) {
                    if (throwable == null) {
                        logger.warning(message);
                    } else {
                        logger.warning(message, throwable);
                    }
                }
                break;
            case FATAL:
                if (logger.isErrorEnabled()) {
                    logger.error(message, throwable);
                }
                break;
        }
    }

    /**
     * All possible levels for log.
     */
    public enum Level {
        /**
         * Trace level.
         */
        TRACE("TRACE"),

        /**
         * Debug level.
         */
        DEBUG("DEBUG"),

        /**
         * Info level.
         */
        INFO("INFO"),

        /**
         * Warning level.
         */
        WARNING("WARNING"),

        /**
         * Critical level.
         */
        FATAL("FATAL");

        /**
         * The level string.
         */
        private final String level;

        /**
         * Enum constructor.
         *
         * @param level The level.
         */
        Level(String level) {
            this.level = level;
        }

        @Override
        public String toString() {
            return level;
        }
    }
}
