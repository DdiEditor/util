package org.ddialliance.ddiftp.util.xml.ddi_3_0;

import java.io.File;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.ddialliance.ddi_3_0.xml.xmlbeans.conceptualcomponent.GeographicStructureSchemeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.datacollection.QuestionItemType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.datacollection.QuestionSchemeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.instance.DDIInstanceDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.BaseLogicalProductType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.reusable.AbstractIdentifiableType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.reusable.ActionCodeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.reusable.GeographicStructureType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.studyunit.StudyUnitType;
import org.ddialliance.ddiftp.util.xml.Urn;
import org.ddialliance.ddiftp.util.xml.XmlBeansUtil;
import org.junit.Assert;
import org.junit.Test;

public class GroupParserTest {
	String file = "resources/testfile_group_2.xml";

	private GroupParserDDI3 getpaser(String file) throws Exception {
		return new GroupParserDDI3(new File(file));
	}

	private Urn getGroupUrn(GroupParserDDI3 parser) throws Exception {
		return UrnUtilDDI3.getUrn(parser.getGroups().get(0));
	}

	private Urn getStudyUrn(GroupParserDDI3 parser) throws Exception {
		return UrnUtilDDI3.getUrn(parser.getStudyUnits().get(0));
	}

	@Test
	public void changeElementInList() throws Exception {
		GroupParserDDI3 parser = getpaser(file);

		StudyUnitType topElement = parser.getStudyUnits().get(0);
		BaseLogicalProductType element = parser.getLogicalProducts().get(0);

		String listMethodName = "getBaseLogicalProductList";
		int count = topElement.getBaseLogicalProductList().size();

		// add
		parser.changeElementInList(element, topElement, listMethodName,
				ActionCodeType.ADD);
		Assert.assertEquals("Not added!", count + 1, topElement
				.getBaseLogicalProductList().size());

		// update
		count = topElement.getBaseLogicalProductList().size();
		element = parser.getLogicalProducts().get(0);
		parser.changeElementInList(element, topElement, listMethodName,
				ActionCodeType.UPDATE);
		Assert.assertEquals("Not replaced!", count, topElement
				.getBaseLogicalProductList().size());

		// delete
		count = topElement.getBaseLogicalProductList().size();
		element = parser.getLogicalProducts().get(0);
		parser.changeElementInList(element, topElement, listMethodName,
				ActionCodeType.DELETE);
		Assert.assertEquals("Not deleted!", count - 1, topElement
				.getBaseLogicalProductList().size());
	}

	@Test
	public void changeElement() throws Exception {
		// to when ddi-3.0 is final
	}

	@Test
	public void parseGroupedStudyUnit() throws Exception {
		GroupParserDDI3 parser = getpaser(file);
		Urn groupUrn = getGroupUrn(parser);
		Urn studyUrn = getStudyUrn(parser);

		DDIInstanceDocument document = parser.parseGroupedStudyUnit(groupUrn
				.toUrnString(), studyUrn.toUrnString());

		StudyUnitType studyUnit = document.getDDIInstance()
				.getStudyUnitArray(0);
		
		// clean action check
		XmlCursor xmlCursor = studyUnit.newCursor();
		XmlObject current;
		while (xmlCursor != null && xmlCursor.hasNextToken()) {
			xmlCursor.toNextToken();
			if (xmlCursor.currentTokenType().isStart()) {
				current = xmlCursor.getObject();

				// clean up add, delete, replace tags
				if (current instanceof AbstractIdentifiableType) {
					if (((AbstractIdentifiableType) current).getAction() != null) {
						Assert.fail("Has action set!, " + current.xmlText());
					}
				}
			}
		}
		xmlCursor.dispose();

		// check inherited changed elements
		// add
		Assert.assertEquals("Instrument_4245", studyUnit
				.getDataCollectionArray(0).getInstrumentArray(0).getId());

		// delete
		for (QuestionSchemeType questionScheme : studyUnit
				.getDataCollectionArray(0).getQuestionSchemeList()) {
			if (questionScheme.getId().equals("QuestionScheme_4245")) {
				for (QuestionItemType questionItem : questionScheme
						.getQuestionItemList()) {
					if (questionItem.getId().equals("A7")) {
						Assert.fail("Question item id A7 found!");
					}
				}
			}
		}

		// update
		for (GeographicStructureSchemeType geographicStructureScheme : studyUnit
				.getConceptualComponentArray(0)
				.getGeographicStructureSchemeList()) {
			if (geographicStructureScheme.getId().equals(
					"GeographicStructureScheme_4245")) {
				for (GeographicStructureType geographicStructure : geographicStructureScheme
						.getGeographicStructureList()) {
					if (geographicStructure.getId().equals(
							"GeographicStructure_4245")) {
						Assert.assertEquals(1, geographicStructure
								.getGeographyList().size());
					}
				}
			}
		}
		System.out.println(document);
	}
}
