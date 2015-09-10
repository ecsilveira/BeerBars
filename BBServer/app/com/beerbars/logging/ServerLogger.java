package com.beerbars.logging;

import play.Logger;

public class ServerLogger{
	
	public static void trace(String message) {
		Logger.trace(message);
	}

	public static void trace(String message, Object... args) {
		Logger.trace(message, args);
	}

	public static void trace(String message, Throwable error) {
		Logger.trace(message, error);
	}

	public static void debug(String message) {
		Logger.debug(message);
	}

	public static void debug(String message, Object... args) {
		Logger.debug(message, args);
	}

	public static void debug(String message, Throwable error) {
		Logger.debug(message, error);
	}

	public static void info(String message) {
		Logger.info(message);
	}

	public static void info(String message, Object... args) {
		Logger.info(message, args);
	}

	public static void info(String message, Throwable error) {
		Logger.info(message, error);
	}

	public static void warn(String message) {
		Logger.warn(message);
	}

	public static void warn(String message, Object... args) {
		Logger.warn(message, args);
	}

	public static void warn(String message, Throwable error) {
		Logger.warn(message, error);
	}

	public static void error(String message) {
		Logger.error(message);
	}

	public static void error(String message, Object... args) {
		Logger.error(message, args);
	}

	public static void error(String message, Throwable error) {
		Logger.error(message, error);
	}

}
