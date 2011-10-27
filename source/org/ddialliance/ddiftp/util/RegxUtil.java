package org.ddialliance.ddiftp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegxUtil {
	private static Pattern pattern;
	private static Matcher matcher;

	/**
	 * Constructor
	 * @param regex
	 *            to search for
	 * @param text
	 *            to search in
	 */
	public RegxUtil(String regex, String text) {
		setPattern(regex);
		setMatcher(text);
	}

	public Pattern getPattern() {
		return pattern;
	}

	public static void setPattern(String regex) {
		pattern = Pattern.compile(regex);
	}

	public Matcher getMatcher() {
		return matcher;
	}

	public static void setMatcher(String text) {
		matcher = pattern.matcher(text);
	}

	public boolean find() {
		return matcher.find();
	}

	/**
	 * Is the regx in text
	 * 
	 * @param regex
	 *            to search for
	 * @param text
	 *            to search in
	 * @return result
	 */
	public static boolean find(String regex, String text) {
		setPattern(regex);
		setMatcher(text);
		return matcher.find();
	}
}
