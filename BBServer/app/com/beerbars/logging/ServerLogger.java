package com.beerbars.logging;

import play.Logger;

/**
 * Classe responsável pelos Logs do Server
 * @author B35579
  */
public class ServerLogger{
	/**
	 * Log Trace
	 * @param message
	 */
	public static void trace(String message) {
		Logger.trace(message);
	}

	/**
     * Log Trace
     * @param message
	 * @param args 
     */
	public static void trace(String message, Object... args) {
		Logger.trace(message, args);
	}

	/**
     * Log Trace
     * @param message
	 * @param error 
     */
	public static void trace(String message, Throwable error) {
		Logger.trace(message, error);
	}

	/**
     * Log Debug
     * @param message
     */
	public static void debug(String message) {
		Logger.debug(message);
	}

	/**
     * Log Debug
     * @param message
	 * @param args 
     */
	public static void debug(String message, Object... args) {
		Logger.debug(message, args);
	}

	/**
     * Log Debug
     * @param message
	 * @param error 
     */
	public static void debug(String message, Throwable error) {
		Logger.debug(message, error);
	}

	/**
     * Log Info
     * @param message
     */
	public static void info(String message) {
		Logger.info(message);
	}

	/**
     * Log Info
     * @param message
	 * @param args 
     */
	public static void info(String message, Object... args) {
		Logger.info(message, args);
	}

	/**
     * Log Info
     * @param message
	 * @param error 
     */
	public static void info(String message, Throwable error) {
		Logger.info(message, error);
	}

	/**
     * Log Warn
     * @param message
     */
	public static void warn(String message) {
		Logger.warn(message);
	}

	/**
     * Log Warn
     * @param message
	 * @param args 
     */
	public static void warn(String message, Object... args) {
		Logger.warn(message, args);
	}

	/**
     * Log Warn
     * @param message
	 * @param error 
     */
	public static void warn(String message, Throwable error) {
		Logger.warn(message, error);
	}

	/**
     * Log Error
     * @param message
     */
	public static void error(String message) {
		Logger.error(message);
	}

	/**
     * Log Error
     * @param message
	 * @param args 
     */
	public static void error(String message, Object... args) {
		Logger.error(message, args);
	}

	/**
     * Log Error
     * @param message
	 * @param error 
     */
	public static void error(String message, Throwable error) {
		Logger.error(message, error);
	}

}
