package org.ddialliance.ddiftp.util.log;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

/**
 * Log factory facade to obtain loggers and cache produced loggers.
 * <br>The log factory is independent of backing logging framework used
 * <br>Log factory  currently implements log4j.  
 */
public class LogFactory {
	private static Map<String, Log> instances = new HashMap<String, Log>();
	private static boolean initialized = false;

	private static void initialize() {
		initialized = true;
		PropertyConfigurator.configureAndWatch("resources/log.properties");
	}

	private LogFactory() {
	}

	public static Log getLog(Class logName) {
		if (!initialized)
			initialize();

		Log instance = (Log) instances.get(logName.getName());

		if (instance == null) {
			instance =  new Log4jLog(LogManager.getLogger(logName));
			instances.put(logName.getName(), instance);
		}
		return instance;
	}

	public static Log getLog(LogType logType, Class className) {
		if (!initialized)
			initialize();

		String logName = logType.getLogName() + "." + className.getName();
		Log instance = (Log)instances.get(logName);

		if (instance == null) {
			instance =  new Log4jLog(LogManager.getLogger(logName));
			instances.put(logName, instance);
		}
		return instance;
	}

	public static Log getLog(LogType logType, String subType) {
		if (!initialized)
			initialize();

		String logName = logType.getLogName() + "." + subType;
		Log instance = (Log) instances.get(logName);

		if (instance == null) {
			instance =  new Log4jLog(LogManager.getLogger(logName));
			instances.put(logName, instance);
		}

		return instance;
	}
}
