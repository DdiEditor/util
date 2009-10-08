package org.ddialliance.ddiftp.util.xml;

import static org.junit.Assert.*;

import java.io.File;

import junit.framework.Assert;

import org.apache.xmlbeans.XmlObject;
import org.ddialliance.ddi_3_0.xml.xmlbeans.group.AbstractDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.instance.DDIInstanceDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.instance.DDIInstanceType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.reusable.IdentifiedStructuredStringType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.reusable.LabelType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.reusable.StructuredStringType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.studyunit.StudyUnitType;
import org.junit.Ignore;
import org.junit.Test;

public class XmlBeansUtilTest {
	@Test
	public void openSaveDDIDoc() throws Exception {
		XmlObject xmlObject = XmlBeansUtil.openDDIDoc(new File(
				"resources/testfile_2.xml"));
		Assert.assertTrue(xmlObject instanceof DDIInstanceDocument);

		File testFile = new File("resources/xmlBeansUtilTest.xml");
		XmlBeansUtil.saveDDIDoc(xmlObject, testFile);
		Assert.assertTrue(testFile.exists());

		XmlObject testXmlObject = XmlBeansUtil.openDDIDoc(testFile);
		testFile.deleteOnExit();
		Assert.assertNotNull(testXmlObject);
		// xml formating issues
		// Assert.assertEquals("Not the same from open - save!",
		// xmlObject.xmlText().trim(), testXmlObject.xmlText().trim());
	}

	@Test
	public void nodeToXmlObject() throws Exception {
		DDIInstanceDocument doc = (DDIInstanceDocument) XmlBeansUtil
				.openDDIDoc(new File("resources/testfile_2.xml"));

		StudyUnitType studyUnitType = XmlBeansUtil.nodeToXmlObject(
				StudyUnitType.class, doc.getDDIInstance().getStudyUnitArray(0)
						.getDomNode());
		Assert.assertNotNull(studyUnitType);
	}

	@Test
	public void setTextOnMixedElement() throws Exception {
		DDIInstanceDocument doc = (DDIInstanceDocument) XmlBeansUtil
				.openDDIDoc(new File("resources/testfile_2.xml"));
		org.w3.x1999.xhtml.PType p = doc.getDDIInstance().getStudyUnitArray(0)
				.getAbstractArray(0).getContent().getPArray(0);
		String test = "Setting text on a mixed content element";
		p = (org.w3.x1999.xhtml.PType) XmlBeansUtil.setTextOnMixedElement(p,
				test);
		Assert.assertEquals(test, XmlBeansUtil.getTextOnMixedElement(p));
	}

	@Test
	public void setTextOnNewMixedElement() throws Exception {
		LabelType label = LabelType.Factory.newInstance();
		label.setLang("da");
		String test = "Setting text on a mixed content element";
		label = (LabelType) XmlBeansUtil.setTextOnMixedElement(label, test);
		Assert.assertEquals(test, XmlBeansUtil.getTextOnMixedElement(label));
	}

	@Test
	public void setTextOnMixedElementWithNamespace() throws Exception {
		String xml = "<Abstract xmlns=\"ddi:group:3_0\" id=\"ABSTRACT\"><Content xmlns=\"ddi:reusable:3_0\">Study description not available.</Content></Abstract>";
		String text = "new text";
		AbstractDocument doc = AbstractDocument.Factory.parse(xml);

		StructuredStringType result = (StructuredStringType) XmlBeansUtil
				.setTextOnMixedElement(doc.getAbstract().getContent(), text);
		Assert.assertEquals(text, XmlBeansUtil.getTextOnMixedElement(result));
	}

	@Test
	public void instanceOfXmlBeanDocument() throws Exception {
		DDIInstanceDocument doc = (DDIInstanceDocument) XmlBeansUtil
				.openDDIDoc(new File("resources/testfile_2.xml"));
		try {
			XmlBeansUtil.instanceOfXmlBeanDocument(doc, new Throwable());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void getXmlObjectTypeFromXmlDocument() throws Exception {
		DDIInstanceDocument doc = (DDIInstanceDocument) XmlBeansUtil
				.openDDIDoc(new File("resources/testfile_2.xml"));
		XmlObject docType = XmlBeansUtil.getXmlObjectTypeFromXmlDocument(doc,
				new Throwable());
		Assert.assertTrue("Not instanceof", docType instanceof DDIInstanceType);
	}
}
