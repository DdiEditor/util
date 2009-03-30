package org.ddialliance.ddiftp.util;

import org.ddialliance.ddiftp.util.log.Log;
import org.ddialliance.ddiftp.util.log.LogFactory;
import org.ddialliance.ddiftp.util.log.LogType;

public class DDIFtpException extends Exception {
	private static final long serialVersionUID = 1L;
	private Object value;
	private Throwable realThrowable;

	/** Default constructor */
	public DDIFtpException() {
	}

	/** Constructs an exception  */
	public DDIFtpException(Exception e) {
		super(e);
	}
	
	/** Constructs an exception with a specified message */
	public DDIFtpException(String msg) {
		super(Translator.trans(msg));
		realThrowable = null;
		log();
	}

	/**
	 * Constructs an exception with a specified message with an arg to the
	 * message string
	 */
	public DDIFtpException(String msg, Object obj) {
		super(Translator.trans(msg, obj));
		realThrowable = null;
		log();
	}

	/**
	 * Constructs an exception with a specified message with several args to the
	 * message string
	 */
	public DDIFtpException(String msg, Object[] objArray) {
		super(Translator.trans(msg, objArray));
		realThrowable = null;
		log();
	}

	/** Constructs an exception with a specified message and a throwable cause */
	public DDIFtpException(String msg, Throwable e) {
		super(Translator.trans(msg));
		realThrowable = e;
		log();
	}

	/**
	 * Constructs an exception with a specified message with an arg to the
	 * message string and a throwable cause
	 */
	public DDIFtpException(String msg, Object obj, Throwable e) {
		super(Translator.trans(msg, obj));
		realThrowable = e;
		log();
	}

	/**
	 * Constructs an exception with a specified message with several args to the
	 * message string and a throwable cause
	 */
	public DDIFtpException(String msg, Object[] objArray, Throwable e) {
		super(Translator.trans(msg, objArray));
		realThrowable = e;
		log();
	}
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Throwable getRealThrowable() {
		return realThrowable;
	}

	public void setRealThrowable(Throwable realThrowable) {
		this.realThrowable = realThrowable;
	}

	/** Log the exception */
	private void log() {
		Log log = LogFactory.getLog(LogType.EXCEPTION, this.getClass());
		log.info(this.getClass().getName() + ": " + this.getMessage(),
				realThrowable);
	}
}
