package org.ddialliance.ddiftp.util.log;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Adapter to wrap an output stream on top of a Log
 */
public abstract class LogOutputStream extends OutputStream {	
	/**
	 * Constructor defining logger and load
	 * @param log logger to log
	 * @param logLevel log level to log to
	 * @throws IllegalArgumentException
	 */
	public LogOutputStream(Log log, LogLevel logLevel)
			throws IllegalArgumentException {
	}

	/**
	 * Closes this output stream and releases any system resources associated
	 * with this stream. The general contract of <code>close</code> is that it
	 * closes the output stream. A closed stream cannot perform output
	 * operations and cannot be reopened.
	 */
	public void close() {
	}

	/**
	 * Writes the specified byte to this output stream. The general contract for
	 * <code>write</code> is that one byte is written to the output stream.
	 * The byte to be written is the eight low-order bits of the argument
	 * <code>b</code>. The 24 high-order bits of <code>b</code> are
	 * ignored.
	 * 
	 * @param b
	 *            the <code>byte</code> to write
	 * @throws IOException
	 *             if an I/O error occurs. In particular, an
	 *             <code>IOException</code> may be thrown if the output stream
	 *             has been closed.
	 */
	public void write(final int b) throws IOException {
	}

	/**
	 * Flushes this output stream and forces any buffered output bytes to be
	 * written out. The general contract of <code>flush</code> is that calling
	 * it is an indication that, if any bytes previously written have been
	 * buffered by the implementation of the output stream, such bytes should
	 * immediately be written to their intended destination.
	 */
	public void flush() {
	}
}