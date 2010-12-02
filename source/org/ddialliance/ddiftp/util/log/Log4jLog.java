package org.ddialliance.ddiftp.util.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Wrapper logger for log4j logger
 */
public final class Log4jLog implements Log {
	final Logger logger;

	final static String FQCN = Log4jLog.class.getName();

	final boolean traceCapable;

	protected Log4jLog(org.apache.log4j.Logger logger) {
		this.logger = logger;
		traceCapable = isTraceCapable();
	}

	private boolean isTraceCapable() {
		try {
			logger.isTraceEnabled();
			return true;
		} catch (NoSuchMethodError e) {
			return false;
		}
	}

	public Logger getLogger() {
		return logger;
	}
	/**
	 * Get logger name
	 * 
	 * @return name
	 */
	public String getName() {
		return logger.getName();
	}

	/**
	 * Is this logger instance enabled for the TRACE level?
	 * 
	 * @return True if this Logger is enabled for level TRACE, false otherwise.
	 */
	public boolean isTraceEnabled() {
		if (traceCapable) {
			return logger.isTraceEnabled();
		} else {
			return logger.isDebugEnabled();
		}
	}

	public void trace(Object obj) {
		logger.log(FQCN, traceCapable ? Level.TRACE : Level.DEBUG, obj, null);
	}

	public void trace(Object obj, Throwable t) {
		logger.log(FQCN, traceCapable ? Level.TRACE : Level.DEBUG, obj, t);
	}

	/**
	 * Is this logger instance enabled for the DEBUG level?
	 * 
	 * @return True if this Logger is enabled for level DEBUG, false otherwise.
	 */
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public void debug(Object obj) {
		logger.log(FQCN, Level.DEBUG, obj, null);
	}

	public void debug(Object obj, Throwable t) {
		logger.log(FQCN, Level.DEBUG, obj, t);
	}

	/**
	 * Is this logger instance enabled for the INFO level?
	 * 
	 * @return True if this Logger is enabled for the INFO level, false
	 *         otherwise.
	 */
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	public void info(Object obj) {
		logger.log(FQCN, Level.INFO, obj, null);
	}

	public void info(Object obj, Throwable t) {
		logger.log(FQCN, Level.INFO, obj, t);
	}

	/**
	 * Is this logger instance enabled for the WARN level?
	 * 
	 * @return True if this Logger is enabled for the WARN level, false
	 *         otherwise.
	 */
	public boolean isWarnEnabled() {
		return logger.isEnabledFor(Level.WARN);
	}

	public void warn(Object obj) {
		logger.log(FQCN, Level.WARN, obj, null);
	}

	public void warn(Object obj, Throwable t) {
		logger.log(FQCN, Level.WARN, obj, t);
	}

	/**
	 * Is this logger instance enabled for level ERROR?
	 * 
	 * @return True if this Logger is enabled for level ERROR, false otherwise.
	 */
	public boolean isErrorEnabled() {
		return logger.isEnabledFor(Level.ERROR);
	}

	public void error(Object obj) {
		logger.log(FQCN, Level.ERROR, obj, null);
	}

	public void error(Object obj, Throwable t) {
		logger.log(FQCN, Level.ERROR, obj, t);
	}

	/**
	 * Fatal is not part of log4j
	 * 
	 * @return defaults to true
	 */
	public boolean isFatalEnabled() {
		return true;
	}

	public void fatal(Object obj) {
		logger.log(FQCN, Level.ERROR, obj, null);
	}

	public void fatal(Object obj, Throwable t) {
		logger.log(FQCN, Level.ERROR, obj, t);
	}

	public void log(LogLevel logLevel, Object obj) {
		logImpl(logLevel, obj, null);
	}

	public void log(LogLevel logLevel, Object obj, Throwable throwable) {
		logImpl(logLevel, obj, throwable);
	}

	private void logImpl(LogLevel logLevel, Object obj, Throwable throwable) {
		if (logLevel.equals(LogLevel.DEBUG)) {
			debug(obj, throwable);
		} else if (logLevel.equals(LogLevel.ERROR)) {
			error(obj, throwable);
		} else if (logLevel.equals(LogLevel.FATAL)) {
			fatal(obj, throwable);
		} else if (logLevel.equals(LogLevel.INFO)) {
			info(obj, throwable);
		} else if (logLevel.equals(LogLevel.TRACE)) {
			trace(obj, throwable);
		} else if (logLevel.equals(LogLevel.WARN)) {
			warn(obj, throwable);
		}
	}
}
