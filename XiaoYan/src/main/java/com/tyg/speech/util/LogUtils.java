package com.tyg.speech.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {
    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    public static void info(String info) {
        logger.info(info);
    }

    public static void logAuthSuccess(String clientIp) {
        logger.info("Authentication successful for client: {}", clientIp);
    }

    public static void logAuthFailure(String clientIp) {
        logger.warn("Authentication failed for client: {}", clientIp);
    }

    public static void logRecognitionResult(String sessionId, String text) {
        logger.info("Recognition result - Session: {}, Text: {}", sessionId, text);
    }

    public static void logRecognitionTime(long millis) {
        logger.debug("Recognition took {} ms", millis);
    }

    public static void logError(String message) {
        logger.error(message);
    }
    public static void logError(String message,Throwable throwable) {
        logger.error(message,throwable);
    }
}