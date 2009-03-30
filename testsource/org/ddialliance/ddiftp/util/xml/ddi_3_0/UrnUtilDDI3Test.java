package org.ddialliance.ddiftp.util.xml.ddi_3_0;

import java.io.File;

import junit.framework.Assert;

import org.apache.xmlbeans.XmlObject;
import org.ddialliance.ddi_3_0.xml.xmlbeans.conceptualcomponent.ConceptType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.datacollection.DataCollectionType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.instance.DDIInstanceDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.CategoryType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.VariableType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.reusable.MaintainableType;
import org.ddialliance.ddiftp.util.DDIFtpException;
import org.ddialliance.ddiftp.util.xml.Urn;
import org.ddialliance.ddiftp.util.xml.XmlBeansUtil;
import org.junit.Test;

public class UrnUtilDDI3Test {

	@Test
	public void getUrn() throws Exception {
		DDIInstanceDocument doc = (DDIInstanceDocument) XmlBeansUtil
				.openDDIDoc(new File("resources/testfile_2.xml"));

		// abstract
		Urn urn = UrnUtilDDI3.getUrn(doc.getDDIInstance().getStudyUnitArray(0)
				.getAbstractArray(0));
		System.out.println(urn.toUrnString());
		new Urn(urn.toUrnString());

		urn = UrnUtilDDI3.getUrn(doc.getDDIInstance().getStudyUnitArray(0)
				.getPurposeArray(0));
		System.out.println(urn.toUrnString());
		new Urn(urn.toUrnString());

		// identifiable
		VariableType xmlObject = doc.getDDIInstance().getStudyUnitArray(0)
				.getBaseLogicalProductArray(0).getVariableSchemeArray(0)
				.getVariableArray(0);
		urn = UrnUtilDDI3.getUrn(xmlObject);
		Assert.assertNotNull("Not found!", urn);
		System.out.println(urn.toUrnString());
		new Urn(urn.toUrnString());

		// maintainable
		DataCollectionType dataCollection = doc.getDDIInstance()
				.getStudyUnitArray(0).getDataCollectionArray(0);
		urn = UrnUtilDDI3.getUrn(dataCollection);
		Assert.assertNotNull("Not found!", urn);
		new Urn(urn.toUrnString());
		System.out.println(urn.toUrnString());

		// versionable
		CategoryType category = doc.getDDIInstance().getStudyUnitArray(0)
				.getBaseLogicalProductArray(0).getCategorySchemeArray(0)
				.getCategoryArray(0);
		urn = UrnUtilDDI3.getUrn(category);
		Assert.assertNotNull("Not found!", urn);
		new Urn(urn.toUrnString());
		System.out.println(urn.toUrnString());
		new Urn(urn.toUrnString());
	}

	@Test
	public void getXmlObject() throws Exception {
		DDIInstanceDocument doc = (DDIInstanceDocument) XmlBeansUtil
				.openDDIDoc(new File("resources/testfile_2.xml"));
		System.out.println(UrnUtilDDI3.getUrn(
				doc.getDDIInstance().getStudyUnitArray(0)
						.getBaseLogicalProductArray(0)
						.getVariableSchemeArray(0).getVariableArray(0))
				.toUrnString());

		String urnString = "urn:ddi:3.0:ConceptualComponent=ICPSR:ConceptualComponent_4245(1.0)";
		XmlObject xmlObjectLookUp = UrnUtilDDI3.getXmlObject(new File(
				"resources/testfile_2.xml"), urnString);
		Assert.assertNotNull(urnString + "Not found!", xmlObjectLookUp);

		urnString = "urn:ddi:3.0:Variable=dda.dk:ID_55499c11-4288-4cb5-89f0-0c57a7eeeb30_VarSch(2.7).V1(2.7)";
		xmlObjectLookUp = UrnUtilDDI3.getXmlObject(new File(
				"resources/testfile_2.xml"), urnString);
		Assert.assertNotNull(urnString + "Not found!", xmlObjectLookUp);
	}

	@Test
	public void parseUrn() throws Exception {
		String urnString = null;
		try {
			new Urn(urnString);
			Assert.fail();
		} catch (DDIFtpException e) {
			// do nothing
		}

		urnString = "";
		try {
			new Urn(urnString);
			Assert.fail();
		} catch (DDIFtpException e) {
			// do nothing
		}

		urnString = "urn:ddi:3.0:ConceptualComponent=ICPSR:ConceptualComponent_4245(1.0)";
		XmlObject xmlObjectLookUp = UrnUtilDDI3.getXmlObject(new File(
				"resources/testfile_2.xml"), urnString);
		Assert.assertNotNull(urnString + "Not found!", xmlObjectLookUp);

		// object class
		urnString = "urn:ddi:3.0:=ICPSR:ConceptualComponent_4245(1.0)";
		try {
			UrnUtilDDI3.getXmlObject(new File("resources/testfile_2.xml"),
					urnString);
			Assert.fail("Error on: " + urnString);
		} catch (DDIFtpException e) {
			// do nothing
		}

		// agency id
		urnString = "urn:ddi:3.0:class=############:ConceptualComponent_4245(1.0)";
		try {
			UrnUtilDDI3.getXmlObject(new File("resources/testfile_2.xml"),
					urnString);
			Assert.fail("Error on: " + urnString);
		} catch (DDIFtpException e) {
			// do nothing
		}

		urnString = "urn:ddi:3.0:class=:ConceptualComponent_4245(1.0)";
		try {
			UrnUtilDDI3.getXmlObject(new File("resources/testfile_2.xml"),
					urnString);
			Assert.fail("Error on: " + urnString);
		} catch (DDIFtpException e) {
			// do nothing
		}

		// maintainable id
		urnString = "urn:ddi:3.0:class=:ConceptualComponent_4245(1.0)";
		try {
			UrnUtilDDI3.getXmlObject(new File("resources/testfile_2.xml"),
					urnString);
			Assert.fail("Error on: " + urnString);
		} catch (DDIFtpException e) {
			// do nothing
		}
		urnString = "urn:ddi:3.0:class=:(1.0).ConceptualComponent_4245(1.0)";
		try {
			UrnUtilDDI3.getXmlObject(new File("resources/testfile_2.xml"),
					urnString);
			Assert.fail("Error on: " + urnString);
		} catch (DDIFtpException e) {
			// do nothing
		}

		// maintainable version
		urnString = "urn:ddi:3.0:ConceptualComponent=ICPSR:ConceptualComponent_4245(h1.0)";
		try {
			UrnUtilDDI3.getXmlObject(new File("resources/testfile_2.xml"),
					urnString);
			Assert.fail("Error on: " + urnString);
		} catch (DDIFtpException e) {
			// do nothing
		}

		urnString = "urn:ddi:3.0:ConceptualComponent=ICPSR:ConceptualComponent_4245()";
		try {
			UrnUtilDDI3.getXmlObject(new File("resources/testfile_2.xml"),
					urnString);
			Assert.fail("Error on: " + urnString);
		} catch (DDIFtpException e) {
			// do nothing
		}

		// contained item id
		urnString = "urn:ddi:3.0:Variable=ICPSR:Schema_89(569.4568).V12##";
		try {
			UrnUtilDDI3.getXmlObject(new File("resources/testfile_2.xml"),
					urnString);
			Assert.fail("Error on: " + urnString);
		} catch (DDIFtpException e) {
			// do nothing
		}

		urnString = "urn:ddi:3.0:ConceptualComponent=ICPSR:Instance_4245(569.4568).some#Id(1.0)";
		try {
			UrnUtilDDI3.getXmlObject(new File("resources/testfile_2.xml"),
					urnString);
			Assert.fail("Error on: " + urnString);
		} catch (DDIFtpException e) {
			// do nothing
		}

		// contained item version
		urnString = "urn:ddi:3.0:ConceptualComponent=ICPSR:Instance_4245(569.4568).someId()";
		try {
			UrnUtilDDI3.getXmlObject(new File("resources/testfile_2.xml"),
					urnString);
			Assert.fail("Error on: " + urnString);
		} catch (DDIFtpException e) {
			// do nothing
		}

		// no contained item version
		urnString = null;
		urnString = "urn:ddi:3.0:ConceptualComponent=ICPSR:Instance_4245(569.4568).someId";
		UrnUtilDDI3.getXmlObject(new File("resources/testfile_2.xml"),
				urnString);
		Assert.assertNotNull("URN not created", urnString);

		// no contained item
		urnString = null;
		urnString = "urn:ddi:3.0:ConceptualComponent=ICPSR:Instance_4245(569.4568)";
		UrnUtilDDI3.getXmlObject(new File("resources/testfile_2.xml"),
				urnString);
		Assert.assertNotNull("URN not created", urnString);
	}

	@Test
	public void parseUrnContainingAnUrn() throws Exception {
		DDIInstanceDocument doc = (DDIInstanceDocument) XmlBeansUtil
				.openDDIDoc(new File("resources/testfile_2.xml"));
		MaintainableType xmlObject = doc.getDDIInstance().getStudyUnitArray(0)
				.getConceptualComponentArray(0);
		Urn urn = UrnUtilDDI3.getUrn(xmlObject);
		Assert.assertNotNull("XmlObject not found!", urn);

		Assert.assertEquals(urn.toUrnString(), UrnUtilDDI3.getUrn(xmlObject)
				.toUrnString());
	}

	@Test
	public void validateAgencyString() throws Exception {
		Boolean result = Urn.validateAgencyString("ABC##_@_##abc1");
		Assert.assertFalse(result);
		result = Urn.validateAgencyString("ABC.abc.aa.aa2");
		Assert.assertTrue(result);
		result = Urn.validateAgencyString(null);
		Assert.assertFalse(result);
	}

	@Test
	public void getUrnByXPath() throws Exception {
		String xpath = "/ns1:DDIInstance[1]/s:StudyUnit[1]/c:ConceptualComponent[1]/c:UniverseScheme[1]/c:Universe[1]";
		XmlObject xmlObject = XmlBeansUtil.openDDIDoc(new File(
				"resources/testfile_2.xml"));

		Urn urn = UrnUtilDDI3.getUrnByXpath(xmlObject, xpath);
		Assert.assertNotNull("Urn not found!", urn);
		System.out.println(urn.toUrnString());
	}

	@Test
	public void getUrnInGroup() throws Exception {
		DDIInstanceDocument xmlObject = (DDIInstanceDocument) XmlBeansUtil
				.openDDIDoc(new File("resources/testfile_group_2.xml"));

		ConceptType concept = xmlObject.getDDIInstance().getGroupArray(0)
				.getConceptsArray(0).getConceptualComponent()
				.getConceptSchemeArray(0).getConceptArray(0);
		Urn urn = UrnUtilDDI3.getUrn(concept);
		System.out.println(urn.toUrnString());

		Assert.assertNotNull(urn);
	}
}
