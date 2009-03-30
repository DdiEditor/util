package org.ddialliance.ddiftp.util.xml;

import junit.framework.Assert;

import org.ddialliance.ddiftp.util.ReflectionUtil;
import org.junit.Test;

public class ReflectionUtilTest {

	public interface TestInterface {
		public String getTest();
		public String setTest(String test);
	}
	
	public class TestImpl implements TestInterface {
		String test;

		public TestImpl() {
		}

		public TestImpl(String test) {
			this.test = test;
		}

		public String getTest() {
			return test;
		}

		public String setTest(String test) {
			this.test = test;
			return test;
		}
	}
		
	public class Example  {
		private TestInterface test;

		public TestInterface getTest() {
			return test;
		}

		public void setTest(TestInterface test) {
			this.test = test;
		}
	}

	@Test
	public void invokeMethod() throws Exception {
		Example example = new Example();
		TestImpl testImpl = new TestImpl("test");
		try {
			ReflectionUtil.invokeMethod(example, "setTest", false, testImpl);
			Assert.fail();
		} catch (NoSuchMethodException e) {
			// TODO: handle exception
		}
		
		ReflectionUtil.invokeMethod(example, "setTest", true, testImpl);
		Assert.assertEquals(testImpl, ReflectionUtil.invokeMethod(example, "getTest", false));
	}
}
