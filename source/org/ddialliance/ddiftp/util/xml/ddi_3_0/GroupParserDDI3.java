package org.ddialliance.ddiftp.util.xml.ddi_3_0;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.ddialliance.ddi_3_0.xml.xmlbeans.conceptualcomponent.ConceptSchemeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.conceptualcomponent.ConceptType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.conceptualcomponent.ConceptualComponentType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.conceptualcomponent.GeographicLocationSchemeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.conceptualcomponent.GeographicStructureSchemeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.conceptualcomponent.UniverseSchemeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.conceptualcomponent.UniverseType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.datacollection.CodingType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.datacollection.CollectionEventType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.datacollection.ControlConstructSchemeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.datacollection.ControlConstructType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.datacollection.DataCollectionType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.datacollection.InstructionType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.datacollection.InstrumentType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.datacollection.InterviewerInstructionSchemeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.datacollection.MethodologyType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.datacollection.QuestionItemType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.datacollection.QuestionSchemeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.group.GroupType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.group.ResourcePackageType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.instance.DDIInstanceDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.instance.DDIInstanceType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.BaseLogicalProductType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.CategoryGroupType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.CategorySchemeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.CategoryType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.CodeSchemeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.CodeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.DataRelationshipType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.LogicalProductDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.LogicalProductType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.LogicalRecordType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.RecordRelationshipType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.VariableSchemeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.VariableType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.physicaldataproduct.BaseRecordLayoutType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.physicaldataproduct.DataItemType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.physicaldataproduct.GrossRecordStructureType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.physicaldataproduct.PhysicalDataProductType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.physicaldataproduct.PhysicalRecordSegmentType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.physicaldataproduct.PhysicalStructureSchemeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.physicaldataproduct.RecordLayoutSchemeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.physicalinstance.PhysicalInstanceType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.reusable.AbstractIdentifiableType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.reusable.ActionCodeType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.reusable.GeographicLocationType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.reusable.GeographicStructureType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.reusable.GeographyType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.reusable.IdentifiedStructuredStringType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.reusable.impl.MaintainableTypeImpl;
import org.ddialliance.ddi_3_0.xml.xmlbeans.studyunit.StudyUnitType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.studyunit.impl.StudyUnitTypeImpl;
import org.ddialliance.ddiftp.util.DDIFtpException;
import org.ddialliance.ddiftp.util.ReflectionUtil;
import org.ddialliance.ddiftp.util.log.Log;
import org.ddialliance.ddiftp.util.log.LogFactory;
import org.ddialliance.ddiftp.util.log.LogType;
import org.ddialliance.ddiftp.util.xml.Urn;

public class GroupParserDDI3 extends ParserDDI3 {
	private Log log = LogFactory.getLog(LogType.SYSTEM, this.getClass());
	private DDIInstanceType ddiInstance = null;
	private StudyUnitType study = null;
	private StudyUnitType newStudy = null;
	private DDIInstanceDocument newDDDIInstanceDocument;

	public GroupParserDDI3(File xmlFile) throws Exception {
		super(xmlFile, true);
	}

	public GroupParserDDI3(XmlObject xmlObject) {
		super(xmlObject, true);
	}

	/**
	 * Parse a study unit from a group with resolved elements inherited from the
	 * group
	 * 
	 * @param groupUrn
	 *            urn defining the group
	 * @param studyUrn
	 *            urn defining the study unit to retrieve
	 * @return DDI instance document wrapping the resolved study unit
	 * @throws Exception
	 */
	public DDIInstanceDocument parseGroupedStudyUnit(String groupUrn,
			String studyUrn) throws Exception {
		// instance
		ddiInstance = getDDIInstance();
		newDDDIInstanceDocument = DDIInstanceDocument.Factory.newInstance();
		if (ddiInstance != null) {
			newDDDIInstanceDocument.setDDIInstance(ddiInstance);

			for (Iterator<GroupType> iterator = newDDDIInstanceDocument
					.getDDIInstance().getGroupList().iterator(); iterator
					.hasNext();) {
				iterator.next();
				iterator.remove();
				iterator = newDDDIInstanceDocument.getDDIInstance()
						.getGroupList().iterator();
			}

			// add resource packages
			for (ResourcePackageType resourcePackage : ddiInstance
					.getResourcePackageList()) {
				log.debug(resourcePackage.getId());
			}

			for (ResourcePackageType resourcePackage : newDDDIInstanceDocument
					.getDDIInstance().getResourcePackageList()) {
				log.debug(resourcePackage.getId());
			}
		}

		// group
		GroupType group = (GroupType) UrnUtilDDI3.getXmlObject(xmlObject,
				groupUrn);
		if (group == null) {
			throw new DDIFtpException("group.notfound.error",
					new Object[] { groupUrn });
		}
		Urn newDDDIInstanceDocumentUrn = new Urn(groupUrn);
		newDDDIInstanceDocument.getDDIInstance().setId(
				newDDDIInstanceDocumentUrn.getMaintainableId() + "_"
						+ System.currentTimeMillis());
		newDDDIInstanceDocument.getDDIInstance().setAgency(
				newDDDIInstanceDocumentUrn.getIdentifingAgency());
		newDDDIInstanceDocument.getDDIInstance().setVersion(
				newDDDIInstanceDocumentUrn.getMaintainableVersion());

		// study
		for (org.ddialliance.ddi_3_0.xml.xmlbeans.group.StudyUnitType studyUnitGroup : group
				.getStudyUnitList()) {
			if (UrnUtilDDI3.getUrn(studyUnitGroup.getStudyUnit()).toUrnString()
					.equals(studyUrn)) {
				study = (StudyUnitType) studyUnitGroup.getStudyUnit();
				break;
			}
		}
		if (study == null) {
			throw new DDIFtpException("group.studyurn.error",
					new Object[] { studyUrn });
		}

		// new study to create
		newStudy = newDDDIInstanceDocument.getDDIInstance().addNewStudyUnit();
		Urn oldStudyUrn = new Urn(studyUrn);
		newStudy.setId(oldStudyUrn.getMaintainableId());
		newStudy.setVersion(oldStudyUrn.getMaintainableVersion());
		newStudy.setAgency(oldStudyUrn.getIdentifingAgency());

		// Citation
		if (group.getCitation() != null) {
			newStudy.setCitation(group.getCitation());
		}
		if (study.getCitation() != null) {
			newStudy.setCitation(study.getCitation());
		}

		// Abstract
		if (!group.getAbstractList().isEmpty()) {
			for (IdentifiedStructuredStringType identifiedStructuredString : group
					.getAbstractList()) {
				newStudy.getAbstractList().add(identifiedStructuredString);
			}
		}

		// Purpose
		if (!group.getPurposeList().isEmpty()) {
			for (IdentifiedStructuredStringType identifiedStructuredString : group
					.getPurposeList()) {
				newStudy.getPurposeList().add(identifiedStructuredString);
			}
		}

		// FundingInformation
		if (!group.getFundingInformationList().isEmpty()) {
			newStudy.getFundingInformationList().addAll(
					group.getFundingInformationList());
		}
		if (!study.getFundingInformationList().isEmpty()) {
			newStudy.getFundingInformationList().addAll(
					study.getFundingInformationList());
		}

		// Coverage
		if (group.getCoverage() != null) {
			newStudy.setCoverage(group.getCoverage());
		}
		if (study.getCoverage() != null) {
			newStudy.setCoverage(study.getCoverage());
		}

		// UniverseReference
		if (group.getUniverseReference() != null) {
			newStudy.getUniverseReferenceList().add(
					group.getUniverseReference());
		}
		newStudy.getUniverseReferenceList().addAll(
				study.getUniverseReferenceList());

		// OtherMaterial
		newStudy.getOtherMaterialList().addAll(group.getOtherMaterialList());
		newStudy.getOtherMaterialList().addAll(study.getOtherMaterialList());

		// Archive
		if (group.getArchive() != null) {
			newStudy.setArchive(group.getArchive());
		}

		// Note
		newStudy.getNoteList().addAll(group.getNoteList());
		newStudy.getNoteList().addAll(study.getNoteList());

		// Concepts
		for (org.ddialliance.ddi_3_0.xml.xmlbeans.group.ConceptType groupConcept : group
				.getConceptsList()) {
			newStudy.getConceptualComponentList().add(
					groupConcept.getConceptualComponent());
		}

		// add data collection
		for (org.ddialliance.ddi_3_0.xml.xmlbeans.group.DataCollectionType groupDataCollection : group
				.getDataCollectionList()) {
			newStudy.getDataCollectionList().add(
					groupDataCollection.getDataCollection());
		}

		// add logical product
		for (org.ddialliance.ddi_3_0.xml.xmlbeans.group.LogicalProductType groupLocicalProduct : group
				.getLogicalProductList()) {
			BaseLogicalProductType baseLogicalProduct = groupLocicalProduct
					.getBaseLogicalProduct();
			String id = baseLogicalProduct.getId();
			newStudy.getBaseLogicalProductList().add(baseLogicalProduct);

			BaseLogicalProductType newBaseLogicalProduct = null;
			for (BaseLogicalProductType listBaseLogicalProduct : newStudy
					.getBaseLogicalProductList()) {
				if (listBaseLogicalProduct.getId().equals(id)) {
					newBaseLogicalProduct = listBaseLogicalProduct;
				}
			}
			newBaseLogicalProduct.substitute(LogicalProductDocument.type
					.getDocumentElementName(), LogicalProductType.type);
		}

		// add physical data product
		for (org.ddialliance.ddi_3_0.xml.xmlbeans.group.PhysicalDataProductType groupPhysicalDataProduct : group
				.getPhysicalDataProductList()) {
			newStudy.getPhysicalDataProductList().add(
					groupPhysicalDataProduct.getPhysicalDataProduct());
		}

		// sort add, delete, update from study to inherit to
		processDocument((StudyUnitTypeImpl) study);
		// some elements may not be identifiable and therefore not be detected
		// work this out when final spec emerges

		// PhysicalInstance
		for (PhysicalInstanceType physicalInstance : study
				.getPhysicalInstanceList()) {
			newStudy.getPhysicalInstanceList().add(physicalInstance);
		}

		// clean out action codes
		cleanAction();

		return newDDDIInstanceDocument;
	}

	private void processDocument(MaintainableTypeImpl module) throws Exception {
		// start scan
		XmlCursor xmlCursor = module.newCursor();
		XmlObject current;
		while (xmlCursor.hasNextToken()) {
			xmlCursor.toNextToken();
			if (xmlCursor.currentTokenType().isStart()) {
				current = xmlCursor.getObject();

				// stop scan
				// xml cursor hack, the cursor does not stop at end of element
				// but at end of document!
				if (current instanceof PhysicalInstanceType) {
					break;
				}

				// delegate current
				if (current instanceof AbstractIdentifiableType) {
					ActionCodeType.Enum actionCode = ((AbstractIdentifiableType) current)
							.getAction();

					// when adding test if element exist in new study
					if (actionCode == null
							|| actionCode.equals(ActionCodeType.ADD)) {
						Urn urn = UrnUtilDDI3
								.getUrn(((AbstractIdentifiableType) current));
						if (UrnUtilDDI3.getXmlObject(newStudy, urn
								.toUrnString()) == null) {
							delegateDocumentChange(
									((AbstractIdentifiableType) current),
									(actionCode == null ? ActionCodeType.ADD
											: actionCode));
						} else if (log.isDebugEnabled()) {
							log.debug("Exists in study:  " + urn.toUrnString());
						}
					} else {
						delegateDocumentChange(
								((AbstractIdentifiableType) current),
								actionCode);
					}
				}
			}
		}
		xmlCursor.dispose();
	}

	private void delegateDocumentChange(AbstractIdentifiableType element,
			ActionCodeType.Enum actionCode) throws XmlException,
			DDIFtpException, Exception {
		// element
		String nodeName = element.getDomNode().getLocalName();

		// element to add to
		String parentNodeName = element.getDomNode().getParentNode()
				.getLocalName();

		AbstractIdentifiableType oldTopElement = UrnUtilDDI3
				.getParentElement(element.getDomNode());
		Urn topElementUrn = UrnUtilDDI3.getUrn(oldTopElement);
		AbstractIdentifiableType topElement = (AbstractIdentifiableType) UrnUtilDDI3
				.getXmlObject(newStudy, topElementUrn.toUrnString());
		// // study unit
		// abstract
		if (nodeName.equals("Abstract")
				&& element instanceof IdentifiedStructuredStringType) {
			changeElementInList(element, newStudy, "getAbstractList",
					actionCode);
		}
		// purpose
		else if (nodeName.equals("Purpose")
				&& element instanceof IdentifiedStructuredStringType) {
			changeElementInList(element, newStudy, "getPurposeList", actionCode);
		}
		// // conceptual component
		else if (element instanceof ConceptualComponentType) {
			changeElementInList(element, newStudy,
					"getConceptualComponentList", actionCode);
		}
		// concept scheme
		else if (element instanceof ConceptSchemeType) {
			changeElementInList(element, topElement, "getConceptSchemeList",
					actionCode);
		}
		// concept
		else if (element instanceof ConceptType) {
			changeElementInList(element, topElement, "getConceptList",
					actionCode);
		}
		// universe scheme
		else if (element instanceof UniverseSchemeType) {
			changeElementInList(element, topElement, "getUniverseSchemeList",
					actionCode);
		}
		// universe
		else if (element instanceof UniverseType) {
			changeElementInList(element, topElement, "getUniverseList",
					actionCode);
		}
		// geographic structure scheme
		else if (element instanceof GeographicStructureSchemeType) {
			changeElementInList(element, topElement,
					"getGeographicStructureSchemeList", actionCode);
		}
		// geographic structure
		else if (element instanceof GeographicStructureType) {
			changeElementInList(element, topElement,
					"getGeographicStructureList", actionCode);
		}
		// geographic location scheme
		else if (element instanceof GeographicLocationSchemeType) {
			changeElementInList(element, topElement,
					"getGeographicLocationSchemeList", actionCode);
		}
		// geographic location
		else if (element instanceof GeographicLocationType) {
			changeElementInList(element, topElement,
					"getGeographicLocationList", actionCode);
		}
		// geography
		else if (element instanceof GeographyType) {
			changeElementInList(element, topElement, "getGeographyList",
					actionCode);
		}
		// // data collection
		else if (element instanceof DataCollectionType) {
			changeElementInList(element, newStudy, "getDataCollectionList",
					actionCode);
		}
		// methodology
		else if (element instanceof MethodologyType) {
			changeElement(element, topElement, "setMethodology",
					"unsetMethodology", actionCode);
		}
		// methodology::data collection methodology
		else if (nodeName.equals("DataCollectionMethodology")
				&& parentNodeName.equals("Methodology")
				&& element instanceof IdentifiedStructuredStringType) {
			changeElementInList(element, topElement,
					"getDataCollectionMethodologyList", actionCode);
		}
		// methodology::time method
		else if (nodeName.equals("TimeMethod")
				&& element instanceof IdentifiedStructuredStringType) {
			changeElementInList(element, topElement, "getTimeMethodList",
					actionCode);
		}
		// methodology::sampling procedure
		else if (nodeName.equals("SamplingProcedure")
				&& element instanceof IdentifiedStructuredStringType) {
			changeElementInList(element, topElement,
					"getSamplingProcedureList", actionCode);
		}
		// methodology::deviation from sample design
		else if (nodeName.equals("DeviationFromSampleDesign")
				&& element instanceof IdentifiedStructuredStringType) {
			changeElementInList(element, topElement,
					"getDeviationFromSampleDesignList", actionCode);
		}
		// collection event
		else if (element instanceof CollectionEventType) {
			changeElementInList(element, topElement, "getCollectionEventList",
					actionCode);
		}
		// collection event::mode of collection
		else if (nodeName.equals("ModeOfCollection")
				&& parentNodeName.equals("CollectionEvent")
				&& element instanceof IdentifiedStructuredStringType) {
			changeElementInList(element, topElement, "getModeOfCollectionList",
					actionCode);
		}
		// collection event::collection situation
		else if (nodeName.equals("CollectionSituation")
				&& parentNodeName.equals("CollectionEvent")
				&& element instanceof IdentifiedStructuredStringType) {
			changeElementInList(element, topElement,
					"getCollectionSituationList", actionCode);
		}
		// collection event::action to minimize losses
		else if (nodeName.equals("ActionToMinimizeLosses")
				&& parentNodeName.equals("CollectionEvent")
				&& element instanceof IdentifiedStructuredStringType) {
			changeElementInList(element, topElement,
					"getActionToMinimizeLossesList", actionCode);
		}
		// question scheme
		else if (element instanceof QuestionSchemeType) {
			changeElementInList(element, topElement, "getQuestionSchemeList",
					actionCode);
		}
		// question item
		else if (element instanceof QuestionItemType) {
			changeElementInList(element, topElement, "getQuestionItemList",
					actionCode);
		}
		// control construct scheme
		else if (element instanceof ControlConstructSchemeType) {
			changeElementInList(element, topElement,
					"getControlConstructSchemeList", actionCode);
		}
		// control construct
		else if (element instanceof ControlConstructType) {
			changeElementInList(element, topElement, "getControlConstructList",
					actionCode);
		}
		// interviewer instruction scheme
		else if (element instanceof InterviewerInstructionSchemeType) {
			changeElementInList(element, topElement,
					"getInterviewerInstructionSchemeList", actionCode);
		}
		// interviewer instruction
		else if (element instanceof InstructionType) {
			changeElementInList(element, topElement, "getInstructionList",
					actionCode);
		}
		// instrument
		else if (element instanceof InstrumentType) {
			changeElementInList(element, topElement, "getInstrumentList",
					actionCode);
		}
		// processing event::weighting
		else if (nodeName.equals("Weighting")
				&& element instanceof IdentifiedStructuredStringType) {
			changeElementInList(element, topElement, "getWeightingList",
					actionCode);
		}
		// processing event::coding
		else if (element instanceof CodingType) {
			changeElementInList(element, topElement, "getCodingList",
					actionCode);
		}

		// // logical product
		else if (element instanceof LogicalProductType) {
			changeElementInList(element, newStudy, "getBaseLogicalProductList",
					actionCode);
		}
		// data relationship
		else if (element instanceof DataRelationshipType) {
			changeElementInList(element, topElement, "getDataRelationshipList",
					actionCode);
		}
		// data relationship::logical record
		else if (element instanceof LogicalRecordType) {
			if (actionCode.equals(ActionCodeType.DELETE)) {
				throw new DDIFtpException("ddi.unsupport.specmethod",
						new Object[] { "action='Delete'", nodeName, element });
			} else {
				changeElement((LogicalRecordType) element, topElement,
						"setLogicalRecord", null, actionCode);
			}
		}
		// data relationship::record relationship
		else if (element instanceof RecordRelationshipType) {
			changeElementInList(element, topElement,
					"getRecordRelationshipList", actionCode);
		}
		// category scheme
		else if (element instanceof CategorySchemeType) {
			changeElementInList(element, topElement, "getCategorySchemeList",
					actionCode);
		}
		// category group
		else if (element instanceof CategoryGroupType) {
			changeElementInList(element, topElement, "getCategoryGroupList",
					actionCode);
		}
		// category in a category group
		else if (parentNodeName.equals("CategoryGroup")
				&& element instanceof CategoryType) {
			changeElementInList(element, topElement, "getCategoryList",
					actionCode);
		}
		// category
		else if (element instanceof CategoryType) {
			changeElementInList(element, topElement, "getCategoryList",
					actionCode);
		}
		// code scheme
		else if (element instanceof CodeSchemeType) {
			changeElementInList(element, topElement, "getCodeSchemeList",
					actionCode);
		}
		// code
		else if (element instanceof CodeType) {
			changeElementInList(element, topElement, "getCodeList", actionCode);
		}
		// variable scheme
		else if (element instanceof VariableSchemeType) {
			changeElementInList(element, topElement, "getVariableSchemeList",
					actionCode);
		}
		// variable
		else if (element instanceof VariableType) {
			changeElementInList(element, topElement, "getVariableList",
					actionCode);
		}
		// // physical data product
		else if (element instanceof PhysicalDataProductType) {
			changeElementInList(element, newStudy,
					"getPhysicalDataProductList", actionCode);
		}
		// physical structure scheme
		else if (element instanceof PhysicalStructureSchemeType) {
			changeElementInList(element, topElement,
					"getPhysicalStructureSchemeList", actionCode);
		}
		// physical structure scheme
		else if (element instanceof PhysicalStructureSchemeType) {
			changeElementInList(element, topElement,
					"getPhysicalStructureSchemeList", actionCode);
		}
		// gross record structure
		else if (element instanceof GrossRecordStructureType) {
			changeElementInList(element, topElement,
					"getGrossRecordStructureList", actionCode);
		}
		// physical record segment
		else if (element instanceof PhysicalRecordSegmentType) {
			changeElementInList(element, topElement,
					"getPhysicalRecordSegmentList", actionCode);
		}
		// record layout scheme
		else if (element instanceof RecordLayoutSchemeType) {
			changeElementInList(element, topElement,
					"getRecordLayoutSchemeList", actionCode);
		}
		// record layout
		else if (nodeName.equals("RecordLayout")
				&& element instanceof BaseRecordLayoutType) {
			changeElementInList(element, topElement, "getRecordLayoutList",
					actionCode);
		}
		// data item
		else if (nodeName.equals("DataItem") && element instanceof DataItemType) {
			changeElementInList(element, topElement, "getDataItemList",
					actionCode);
		} else {
			log
					.error(element.getClass().getName()
							+ " is not supported, contact the DDIFTP for implementation.");
		}
	}

	protected void changeElement(AbstractIdentifiableType element,
			XmlObject topElement, String setMethodName, String unsetMethodName,
			ActionCodeType.Enum action) throws DDIFtpException, Exception {

		// add
		if (action.equals(ActionCodeType.ADD)) {
			ReflectionUtil.invokeMethod(topElement, setMethodName, true,
					element);
		}

		// delete
		if (action.equals(ActionCodeType.DELETE)) {
			ReflectionUtil.invokeMethod(topElement, unsetMethodName, false);
		}

		// update
		if (action.equals(ActionCodeType.UPDATE)) {
			AbstractIdentifiableType backupElement = (AbstractIdentifiableType) element
					.copy();
			if (unsetMethodName != null) {
				ReflectionUtil.invokeMethod(topElement, unsetMethodName, false);
			}
			ReflectionUtil.invokeMethod(topElement, setMethodName, true,
					element);
		}
	}

	protected void changeElementInList(AbstractIdentifiableType element,
			XmlObject topElement, String listMethodName,
			ActionCodeType.Enum action) throws DDIFtpException, Exception {
		if (topElement == null) {
			if (log.isDebugEnabled()) {
				log.debug("Top element is null, " + element.xmlText());
			}
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("Element: " + element.getClass() + ", topElement: "
					+ topElement.getClass());
		}
		List<AbstractIdentifiableType> topElements = (List<AbstractIdentifiableType>) ReflectionUtil
				.invokeMethod(topElement, listMethodName, false);

		// add
		if (action.equals(ActionCodeType.ADD)) {
			addToList(topElement, topElements, element);
		}

		// delete
		if (action.equals(ActionCodeType.DELETE)) {
			deleteFromList(topElements, element);
		}

		// update
		if (action.equals(ActionCodeType.UPDATE)) {
			AbstractIdentifiableType backupElement = (AbstractIdentifiableType) element
					.copy();
			deleteFromList(topElements, element);
			addToList(topElement, topElements, backupElement);
		}
	}

	private void addToList(XmlObject topElement,
			List<AbstractIdentifiableType> topElements,
			AbstractIdentifiableType element) throws DDIFtpException {
		if (element.getAction() != null) {
			element.unsetAction();
		}
		topElements.add(element);
		if (log.isDebugEnabled()) {
			log
					.debug("ActionCode adding: "
							+ element.getId()
							+ " to: "
							+ (topElement instanceof AbstractIdentifiableType ? ((AbstractIdentifiableType) topElement)
									.getId()
									: topElement.getClass().getName()));
		}
	}

	private void deleteFromList(List<AbstractIdentifiableType> topElements,
			AbstractIdentifiableType element) throws DDIFtpException {
		AbstractIdentifiableType tester;
		for (Iterator<AbstractIdentifiableType> iterator = topElements
				.iterator(); iterator.hasNext();) {
			tester = (AbstractIdentifiableType) iterator.next();

			if (tester.getId().equals(element.getId())) {
				if (log.isDebugEnabled()) {
					log.debug("AtionCode deleting: " + element.getId());
				}
				iterator.remove();
				break;
			}
		}
	}

	/**
	 * Cleans out actions
	 */
	private void cleanAction() {
		XmlCursor xmlCursor = newStudy.newCursor();
		XmlObject current;
		while (xmlCursor != null && xmlCursor.hasNextToken()) {
			xmlCursor.toNextToken();
			if (xmlCursor.currentTokenType().isStart()) {
				current = xmlCursor.getObject();

				// clean up add, delete, replace tags
				if (current instanceof AbstractIdentifiableType) {
					if (((AbstractIdentifiableType) current).getAction() != null) {
						((AbstractIdentifiableType) current).unsetAction();
					}
				}
			}
		}
		xmlCursor.dispose();
	}
}
