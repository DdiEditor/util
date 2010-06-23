package org.ddialliance.ddiftp.util;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class LanguageUtilTest {
	@Test
	public void getLanguagesExcludingLanguageSingle() throws Exception {
		String[][] availableLanguages = LanguageUtil.getAvailableLanguages();
		String[][] result = LanguageUtil.getLanguagesExcludingLanguage("da",
				availableLanguages);

		assertEquals(availableLanguages.length - 1, result.length);
	}

	@Test
	public void getLanguagesExcludingLanguageAslist() throws Exception {
		String[][] availableLanguages = LanguageUtil.getAvailableLanguages();
		String[] languagesToRemove = { "da", "en" };

		String[][] result = LanguageUtil.getLanguagesExcludingLanguage(Arrays
				.asList(languagesToRemove), availableLanguages);

		assertEquals(availableLanguages.length - 2, result.length);
	}
	
	@Test
	public void indexOfLangCode() throws Exception {
		int da = LanguageUtil.indexOfLangCode("da");
		assertEquals(8, da);
	}
}
