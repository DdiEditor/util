package org.ddialliance.ddiftp.util;

import java.io.File;
import java.util.Properties;
import java.util.Scanner;

import junit.framework.Assert;

import org.junit.Test;

public class FileUtilTest {
	@Test
	public void copyFile() throws Exception {
		File destination = new File("test");
		destination.deleteOnExit();
		FileUtil.copyFile(new File("resources" + File.separator
				+ "util-message.properties"), destination);
		Assert.assertTrue(destination.exists());
	}

	@Test
	public void getParentDir() throws Exception {
		File result = FileUtil.getParentDir(new File("."));
		Assert.assertEquals("build", result.getName());
	}

	class TestLineScanner implements LineScanner {
		public void processLine(String aLine) throws Exception {
			// use a second Scanner to parse the content of each line
			Scanner scanner = new Scanner(aLine);
			// scanner.useDelimiter(" ");
			int count = 0;
			System.out.println("\n");
			while (scanner.hasNext()) {
				String string = (String) scanner.next();
				System.out.println("String " + ++count + ": " + string);
			}
			// if (scanner.hasNext()) {
			// String name = scanner.next();
			// String value = scanner.next();
			// if (log.isDebugEnabled()) {
			// log.debug("name: "+ name+", value: "+value);
			// }
			// } else {
			// //Empty or invalid line. Unable to process
			// if (log.isDebugEnabled()) {
			// log.debug("Nothing to process");
			// }
			// }
			// (no need for finally here, since String is source)
			scanner.close();
		}
	}

	@Test
	public void processLineByLine() throws Exception {
		TestLineScanner testFileScanner = new TestLineScanner();
		FileUtil.processFileLineByLine(new File("resources" + File.separator
				+ "element-list"), testFileScanner);
	}

	@Test
	public void properties() throws Exception {
		File test = new File("storePropertiesTest.properties");
		test.deleteOnExit();
		Properties properties = new Properties();
		properties.put("test", "test");
		FileUtil.storeProperties(test.getAbsolutePath(), properties);
		
		Properties loadTest = FileUtil.loadProperties(test);
		Assert.assertEquals("test", loadTest.get("test"));
	}
}
