package org.ddialliance.ddiftp.util.xml.ddi_3_0;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class ParserDDI3Test {
	String file = "resources/testfile_2.xml";

	private ParserDDI3 getPaser(String file) throws Exception {
		return new ParserDDI3(new File(file), true);
	}

	@Test
	public void getStudyUnits() throws Exception {
		ParserDDI3 parser = getPaser(file);
		Assert.assertTrue(!parser.getStudyUnits().isEmpty());
		Assert.assertEquals(1, parser.getStudyUnits().size());		
		Assert.assertEquals("StudyUnit_4245", parser.getStudyUnits().get(0).getId());
	}

	@Test
	public void getGroups() throws Exception {
		ParserDDI3 parser = getPaser(file);
		Assert.assertTrue(parser.getGroups().isEmpty());
	}
	
	@Test
	public void getConceptualComponents() throws Exception {
		ParserDDI3 parser = getPaser(file);
		Assert.assertEquals(2, parser.getConceptualComponents().size());
	}
	
	@Test
	public void getPhysicalDataProduct() throws Exception {
		ParserDDI3 parser = getPaser(file);
		Assert.assertEquals(1, parser.getPhysicalDataProducts().size());
	}
}
