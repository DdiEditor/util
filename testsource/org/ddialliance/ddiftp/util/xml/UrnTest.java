package org.ddialliance.ddiftp.util.xml;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class UrnTest {

	@Test
	public void nullUrn() throws Exception {
		try {
			Urn urn = new Urn(null, null, null, null, null, null, null, null);				
			urn.toUrnString();
			//Assert.fail();
		} catch (Exception e) {
			// do nothing
			e.printStackTrace();
		}
	}

	@Test
	public void maintained() throws Exception {
		Urn urn = new Urn();

		urn.setIdentifingAgency("dda.dk.ddi");
		urn.setMaintainableElement("StudyUnit");
		urn.setMaintainableId("id_1");
		urn.setMaintainableVersion("1.0.0");
		System.out.println(urn.toUrnString());
	}

	@Test
	public void contained() throws Exception {
		Urn urn = new Urn();

		urn.setIdentifingAgency("dda.dk.ddi");
		urn.setMaintainableElement("StudyUnit");
		urn.setMaintainableId("su_1");
		urn.setMaintainableVersion("2.0.0");

		urn.setContainedElement("DataCollection");
		urn.setContainedElementId("dd_1");
		urn.setContainedElementVersion("0.1.1");
		System.out.println(urn.toUrnString());
	}

	@Test
	public void parseUrn() throws Exception {
		String urn1 = "urn:ddi:dda.dk.ddi:StudyUnit.su_1.1.0.1";
		String urn2 = "urn:ddi:dda.dk.ddi:StudyUnit.su_1.1.0.1:QuestionItem.qi_1.0.1.1";

		Urn urn = new Urn();
		urn.parseUrn(urn1);
		Assert.assertEquals("Not same!", urn1, urn.toUrnString());

		urn = new Urn();
		urn.parseUrn(urn2);
		Assert.assertEquals("Not same!", urn2, urn.toUrnString());
	}
}
