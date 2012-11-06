package org.ddialliance.ddiftp.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.ddialliance.ddiftp.util.log.Log;
import org.ddialliance.ddiftp.util.log.LogFactory;
import org.ddialliance.ddiftp.util.log.LogType;
import org.python.util.PythonInterpreter;

/**
 * Utility to execute Python scripts
 * 
 * @author ddajvj
 */
public class CallPythonScript {
	/**
	 * Execute a Python script
	 * 
	 * @param script
	 *            to execute
	 * @param args
	 *            arguments to script
	 * @param caller
	 *            throwable
	 * @throws DDIFtpException
	 */
	public static void exec(File script, String[] args, Throwable caller)
			throws DDIFtpException {
		// for arguments see:
		// http://stackoverflow.com/questions/6467407/jython-2-5-1-calling-from-java-into-main-how-to-pass-in-command-line-args
		// http://www.jython.org/docs/using/cmdline.html
		String[] param = null;
		if (args != null) {
			param = new String[args.length + 1];
			param[0] = script.getAbsolutePath();
			for (int i = 0; i < args.length; i++) {
				param[i + 1] = args[i];
			}
		}

		// python interpreter
		PythonInterpreter.initialize(System.getProperties(),
				System.getProperties(), param);
		PythonInterpreter interp = new PythonInterpreter();

		// streams
		OutputStream errorStream = getOutputStream();
		OutputStream infoStream = getOutputStream();
		interp.setErr(errorStream);
		interp.setOut(infoStream);

		// execute script
		interp.execfile(script.getAbsolutePath());

		// log info
		if (infoStream.toString().length() != 0) {
			Log log = LogFactory.getLog(LogType.SYSTEM, CallPythonScript.class);
			if (log.isInfoEnabled()) {
				log.info(caller.getStackTrace()[0] + ": " + infoStream);
			}
		}

		// check exception
		if (errorStream.toString().length() != 0) {
			throw new DDIFtpException(
					"Python error: " + errorStream.toString(), caller);
		}
	}

	/**
	 * Execute a Python script
	 * 
	 * @param script
	 *            to execute
	 * @param caller
	 *            throwable
	 * @throws DDIFtpException
	 */
	public static void exec(File script, Throwable caller)
			throws DDIFtpException {
		exec(script, null, caller);
	}

	private static OutputStream getOutputStream() {
		OutputStream outputStream = new OutputStream() {
			private StringBuilder stringBuilder = new StringBuilder();

			@Override
			public void write(int b) throws IOException {
				this.stringBuilder.append((char) b);
			}

			public String toString() {
				return this.stringBuilder.toString();
			}
		};
		return outputStream;
	}
}
