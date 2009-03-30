package org.ddialliance.ddiftp.util.xml;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class UrnTest {

	@Test
	public void nullUrn() throws Exception {
		try {
			Urn urn = new Urn(null, null, null, null, null, null, null, null,
					null, null, null);
			urn.toUrnString();
			Assert.fail();
		} catch (Exception e) {
			// do nothing
		}
	}

	@Test
	public void maintained() throws Exception {
		Urn urn = new Urn();
		urn.setSchemaVersion("3.0");
		urn.setIdentifingAgency("dda.dk.ddi");
		urn.setMaintainableElement("StudyUnit");
		urn.setMaintainableId("id_1");
		urn.setMaintainableVersion("0.1");
		System.out.println(urn.toUrnString());
	}

	@Test
	public void versioned() throws Exception {
		Urn urn = new Urn();
		urn.setSchemaVersion("3.0");
		urn.setIdentifingAgency("dda.dk.ddi");
		urn.setMaintainableElement("StudyUnit");
		urn.setMaintainableId("su_1");
		urn.setMaintainableVersion("0.1");

		urn.setContainedElement("DataCollection");
		urn.setVersionableElementId("dd_1");
		urn.setVersionableElementVersion("0.1.1");
		System.out.println(urn.toUrnString());
	}

	@Test
	public void nestedVersioned() throws Exception {
		Urn urn = new Urn();
		urn.setSchemaVersion("3.0");
		urn.setIdentifingAgency("dda.dk.ddi");
		urn.setMaintainableElement("StudyUnit");
		urn.setMaintainableId("su_1");
		urn.setMaintainableVersion("0.1");

		urn.setContainedElement("QuestionItem");
		urn.setVersionableElementId("dd_1");
		urn.setVersionableElementVersion("0.1.1");

		urn.setNestedVersionableElementId("qi_1");
		urn.setNestedVersionableElementVersion("0.4.4");
		System.out.println(urn.toUrnString());
	}

	@Test
	public void contained() throws Exception {
		Urn urn = new Urn();
		urn.setSchemaVersion("3.0");
		urn.setIdentifingAgency("dda.dk.ddi");
		urn.setMaintainableElement("StudyUnit");
		urn.setMaintainableId("su_1");
		urn.setMaintainableVersion("0.1");

		urn.setContainedElement("QuestionItem");
		urn.setVersionableElementId("dd_1");
		urn.setVersionableElementVersion("0.1.1");
		urn.setContainedElementId("35");
		System.out.println(urn.toUrnString());

		urn.setNestedVersionableElementId("qi_1");
		urn.setNestedVersionableElementVersion("0.4.4");
		System.out.println(urn.toUrnString());
	}

	@Test
	public void parseUrn() throws Exception {
		String urn1 = "urn:ddi:3.0:StudyUnit=dda.dk.ddi:id_1[0.1]";
		String urn2 = "urn:ddi:3.0:StudyUnit.DataCollection=dda.dk.ddi:su_1[0.1].dd_1[0.1.1]";
		String urn3 = "urn:ddi:3.0:StudyUnit.QuestionItem=dda.dk.ddi:su_1[0.1].dd_1[0.1.1].qi_1[0.4.4]";
		String urn4 = "urn:ddi:3.0:StudyUnit.QuestionItem=dda.dk.ddi:su_1[0.1].dd_1[0.1.1].35";
		String urn5 = "urn:ddi:3.0:StudyUnit.QuestionItem=dda.dk.ddi:su_1[0.1].dd_1[0.1.1].qi_1[0.4.4].35";

		Urn urn = new Urn();
		urn.parseUrn(urn1);

		urn = new Urn();
		urn.parseUrn(urn2);

		urn = new Urn();
		urn.parseUrn(urn3);

		urn = new Urn();
		urn.parseUrn(urn4);

		urn = new Urn();
		urn.parseUrn(urn5);
	}
}
