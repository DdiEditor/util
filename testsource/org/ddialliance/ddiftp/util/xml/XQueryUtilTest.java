package org.ddialliance.ddiftp.util.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.apache.xmlbeans.XmlCursor;
import org.ddialliance.ddi_3_0.xml.xmlbeans.instance.DDIInstanceDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.BaseLogicalProductType;
import org.junit.Before;
import org.junit.Test;

public class XQueryUtilTest {
	DDIInstanceDocument doc = null;
	XQueryUtil xqueryUtil = null;

	@Before
	public void test() {
		try {
			doc = DDIInstanceDocument.Factory.parse(new File(
					"resources/testfile_2.xml"));
			xqueryUtil = new XQueryUtil(doc);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void generateXNamespaceDeclerations() throws Exception {
		assertTrue(!xqueryUtil.getNamespaces().isEmpty());
	}

	@Test
	public void getXPath() {
		String xpath = xqueryUtil.getXPath(doc.getDDIInstance().getDomNode());
		assertEquals("/ns1:DDIInstance[1]", xpath);
	}

	@Test
	public void selectXPath() throws Exception {
		String query = "/ns1:DDIInstance[1]/s:StudyUnit[1]/l:LogicalProduct[1]";
		XmlCursor xmlCursor = xqueryUtil.selectXPath(query, doc, true);
		while (xmlCursor.hasNextSelection()) {
			xmlCursor.toNextSelection();
			assertEquals("ID_55499c11-4288-4cb5-89f0-0c57a7eeeb30_LogPrd",
					((BaseLogicalProductType) xmlCursor.getObject()).getId());
			String computeXPath = xqueryUtil.getXPath(xmlCursor.getObject()
					.getDomNode());
			assertEquals(query, computeXPath);
		}
	}
}
