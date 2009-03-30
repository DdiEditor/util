package org.ddialliance.ddiftp.util.xml.ddi_3_0;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.xmlbeans.XmlObject;
import org.ddialliance.ddi_3_0.xml.xmlbeans.archive.ArchiveDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.archive.ArchiveType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.conceptualcomponent.ConceptType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.conceptualcomponent.ConceptualComponentDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.conceptualcomponent.ConceptualComponentType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.datacollection.DataCollectionDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.datacollection.DataCollectionType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.group.GroupDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.group.GroupType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.group.ResourcePackageDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.group.ResourcePackageType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.instance.DDIInstanceDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.instance.DDIInstanceType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.BaseLogicalProductDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.BaseLogicalProductType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.LogicalProductDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.LogicalProductType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.NCubeLogicalProductDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.logicalproduct.NCubeLogicalProductType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.physicaldataproduct.PhysicalDataProductDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.physicaldataproduct.PhysicalDataProductType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.physicalinstance.PhysicalInstanceDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.physicalinstance.PhysicalInstanceType;
import org.ddialliance.ddi_3_0.xml.xmlbeans.studyunit.StudyUnitDocument;
import org.ddialliance.ddi_3_0.xml.xmlbeans.studyunit.StudyUnitType;
import org.ddialliance.ddiftp.util.DDIFtpException;
import org.ddialliance.ddiftp.util.xml.XmlBeansUtil;

/**
 * DDI3 parser to easily access parts or subsets of a DDI3 element
 */
public class ParserDDI3 {
	protected XmlObject xmlObject = null;
	private Boolean groupedContent;
	private List<StudyUnitType> studyUnitList = null;
	private List<ResourcePackageType> resourcePackageList = null;
	private List<GroupType> groupList = null;
	
	private boolean writable = false;

	/**
	 * Construct the DDI parser with a DDI file
	 * 
	 * @param file
	 *            to parse
	 * @throws Exception
	 */
	public ParserDDI3(File xmlFile, Boolean groupedContent) throws DDIFtpException {
		xmlObject = XmlBeansUtil.openDDIDoc(xmlFile);
		initialize(groupedContent);
	}

	/**
	 * Construct the DDI parser with a XML object
	 * 
	 * @param xmlObject
	 *            XML object
	 */
	public ParserDDI3(XmlObject xmlObject, Boolean groupedContent) {
		this.xmlObject = xmlObject;
		initialize(groupedContent);
	}

	private void initialize(Boolean groupedContent) {
		this.groupedContent = groupedContent;
		resourcePackageList = getResourcePackages();
		groupList = getGroups();
		studyUnitList = getStudyUnits();
	}

	// public boolean isWritable() {
	// return writable;
	// }
	//
	// public void setWritable(boolean writable) {
	// this.writable = writable;
	//	}

	public Boolean getGroupedContent() {
		return groupedContent;
	}

	public void setGroupedContent(Boolean groupedContent) {
		if (groupedContent == null) {
			groupedContent = true;
		}
		this.groupedContent = groupedContent;
	}

	/**
	 * Retrieve the loaded XML object
	 * 
	 * @return XML object
	 */
	public XmlObject getXmlObject() {
		return xmlObject;
	}

	public List<StudyUnitType> getStudyUnits() {
		List<StudyUnitType> result = new ArrayList<StudyUnitType>();
		if (xmlObject instanceof DDIInstanceDocument) {
			result.addAll(((DDIInstanceDocument) xmlObject).getDDIInstance()
					.getStudyUnitList());
		} else if (xmlObject instanceof DDIInstanceType) {
			result.addAll(((DDIInstanceType) xmlObject).getStudyUnitList());
		}

		if (groupedContent) {
			for (GroupType group : getGroups()) {
				for (org.ddialliance.ddi_3_0.xml.xmlbeans.group.StudyUnitType studyUnitGroup : group
						.getStudyUnitList()) {
					if (studyUnitGroup.getStudyUnit() != null) {
						result.add(studyUnitGroup.getStudyUnit());
					}
				}
			}
		}

		if (xmlObject instanceof StudyUnitDocument) {
			result.add(((StudyUnitDocument) xmlObject).getStudyUnit());
		} else if (xmlObject instanceof StudyUnitType) {
			result.add((StudyUnitType) xmlObject);
		}
		return result;
	}

	public List<ResourcePackageType> getResourcePackages() {
		List<ResourcePackageType> result = new ArrayList<ResourcePackageType>();
		if (xmlObject instanceof DDIInstanceDocument) {
			result.addAll(((DDIInstanceDocument) xmlObject).getDDIInstance()
					.getResourcePackageList());
		} else if (xmlObject instanceof DDIInstanceType) {
			result.addAll(((DDIInstanceType) xmlObject)
					.getResourcePackageList());
		}

		if (xmlObject instanceof ResourcePackageDocument) {
			result.add(((ResourcePackageDocument) xmlObject)
					.getResourcePackage());
		}
		return result;
	}

	public List<GroupType> getGroups() {
		List<GroupType> result = new ArrayList<GroupType>();
		if (xmlObject instanceof DDIInstanceDocument) {
			return ((DDIInstanceDocument) xmlObject).getDDIInstance()
					.getGroupList();
		} else if (xmlObject instanceof DDIInstanceType) {
			return ((DDIInstanceType) xmlObject).getGroupList();
		} else if (xmlObject instanceof GroupDocument) {
			result.add(((GroupDocument) xmlObject).getGroup());
		} else if (xmlObject instanceof GroupType) {
			result.add((GroupType) xmlObject);
		}
		return result;
	}

	public DDIInstanceType getDDIInstance() {
		if (xmlObject instanceof DDIInstanceDocument) {
			return ((DDIInstanceDocument) xmlObject).getDDIInstance();
		} else if (xmlObject instanceof DDIInstanceType) {
			return (DDIInstanceType) xmlObject;
		}
		return null;
	}

	// conceptual components
	public List<ConceptualComponentType> getConceptualComponents() {
		List<ConceptualComponentType> result = new ArrayList<ConceptualComponentType>();		
		for (StudyUnitType studyUnit : (writable?getStudyUnits():studyUnitList)) {
			if (studyUnit.getConceptualComponentList() != null) {
				result.addAll(studyUnit.getConceptualComponentList());
			}
		}

		if (xmlObject instanceof ConceptualComponentDocument) {
			result.add(((ConceptualComponentDocument) xmlObject)
					.getConceptualComponent());
		}

		if (groupedContent) {
			for (GroupType group : (writable?getGroups():groupList)) {
				for (org.ddialliance.ddi_3_0.xml.xmlbeans.group.ConceptType groupConcept : group
						.getConceptsList()) {
					if (groupConcept.getConceptualComponent() != null) {
						result.add(groupConcept.getConceptualComponent());
					}
				}
			}
		}
		
		for (ResourcePackageType resourcePackage : (writable?getResourcePackages():resourcePackageList)) {
			for (org.ddialliance.ddi_3_0.xml.xmlbeans.group.ConceptType conceptGroup : resourcePackage.getConceptsList()) {
				if (conceptGroup.getConceptualComponent() != null) {
					result.add(conceptGroup.getConceptualComponent());
				}
			}
		}
		return result;
	}

	// data collection
	public List<DataCollectionType> getDataCollections() {
		List<DataCollectionType> dataCollectionList = new ArrayList<DataCollectionType>();

		for (StudyUnitType studyUnit : (writable?getStudyUnits():studyUnitList)) {
			if (studyUnit.getDataCollectionList() != null) {
				dataCollectionList.addAll(studyUnit.getDataCollectionList());
			}
		}

		if (xmlObject instanceof DataCollectionDocument) {
			dataCollectionList.add(((DataCollectionDocument) xmlObject)
					.getDataCollection());
		}

		if (groupedContent) {
			for (GroupType group : (writable?getGroups():groupList)) {
				for (org.ddialliance.ddi_3_0.xml.xmlbeans.group.DataCollectionType groupDataCollection : group
						.getDataCollectionList()) {
					if (groupDataCollection.getDataCollection() != null) {
						dataCollectionList.add(groupDataCollection
								.getDataCollection());
					}
				}
			}
		}

		for (ResourcePackageType resourcePackage : (writable?getResourcePackages():resourcePackageList)) {
			for (org.ddialliance.ddi_3_0.xml.xmlbeans.group.DataCollectionType groupDataCollection : resourcePackage
					.getDataCollectionList()) {
				if (groupDataCollection.getDataCollection() != null) {
					dataCollectionList.add(groupDataCollection
							.getDataCollection());
				}
			}
		}
		return dataCollectionList;
	}

	// logical product
	public List<LogicalProductType> getLogicalProducts() {
		List<LogicalProductType> result = new ArrayList<LogicalProductType>();
		for (BaseLogicalProductType baseLogicalProduct : getBaseLogicalProducts()) {
			if (baseLogicalProduct instanceof LogicalProductType) {
				result.add((LogicalProductType) baseLogicalProduct.substitute(
						LogicalProductDocument.type.getDocumentElementName(),
						LogicalProductType.type));
			}
		}
		return result;
	}
	
	// ncube logical product
	public List<NCubeLogicalProductType> getNCubeLogicalProducts() {
		List<NCubeLogicalProductType> result = new ArrayList<NCubeLogicalProductType>();
		for (BaseLogicalProductType baseLogicalProduct : getBaseLogicalProducts()) {
			if (baseLogicalProduct instanceof NCubeLogicalProductType) {
				result.add((NCubeLogicalProductType) baseLogicalProduct.substitute(
						NCubeLogicalProductDocument.type.getDocumentElementName(),
						NCubeLogicalProductType.type));
			}
		}
		return result;
	}

	private List<BaseLogicalProductType> getBaseLogicalProducts() {
		List<BaseLogicalProductType> result = new ArrayList<BaseLogicalProductType>();

		for (StudyUnitType studyUnit : (writable?getStudyUnits():studyUnitList)) {
			if (studyUnit.getBaseLogicalProductList() != null) {
				result.addAll(studyUnit.getBaseLogicalProductList());
			}
		}

		if (xmlObject instanceof BaseLogicalProductDocument) {
			result.add(((BaseLogicalProductDocument) xmlObject)
					.getBaseLogicalProduct());
		}

		if (groupedContent) {
			for (GroupType group : (writable?getGroups():groupList)) {
				Iterator<org.ddialliance.ddi_3_0.xml.xmlbeans.group.LogicalProductType> iter = group
						.getLogicalProductList().iterator();
				while (iter.hasNext()) {
					result.add(iter.next().getBaseLogicalProduct());
				}
			}
		}

		for (ResourcePackageType resourcePackage : (writable?getResourcePackages():resourcePackageList)) {
			Iterator<org.ddialliance.ddi_3_0.xml.xmlbeans.group.LogicalProductType> iter = resourcePackage
					.getLogicalProductList().iterator();
			while (iter.hasNext()) {
				result.add(iter.next().getBaseLogicalProduct());
			}
		}
		return result;
	}

	// physical data product
	public List<PhysicalDataProductType> getPhysicalDataProducts() {
		List<PhysicalDataProductType> result = new ArrayList<PhysicalDataProductType>();
		for (StudyUnitType studyUnit : (writable?getStudyUnits():studyUnitList)) {
			if (studyUnit.getPhysicalDataProductList() != null) {
				result.addAll(studyUnit.getPhysicalDataProductList());
			}
		}

		if (xmlObject instanceof PhysicalDataProductDocument) {
			result.add(((PhysicalDataProductDocument) xmlObject)
					.getPhysicalDataProduct());
		}

		if (groupedContent) {
			for (GroupType group : (writable?getGroups():groupList)) {
				for (org.ddialliance.ddi_3_0.xml.xmlbeans.group.PhysicalDataProductType groupPhysicalDataProduct : group
						.getPhysicalDataProductList()) {
					if (groupPhysicalDataProduct.getPhysicalDataProduct() != null) {
						result.add(groupPhysicalDataProduct
								.getPhysicalDataProduct());
					}
				}
			}
		}

		for (ResourcePackageType resourcePackage : (writable?getResourcePackages():resourcePackageList)) {			
			for (org.ddialliance.ddi_3_0.xml.xmlbeans.group.PhysicalDataProductType groupPhysicalDataProduct : resourcePackage
					.getPhysicalDataProductList()) {
				if (groupPhysicalDataProduct.getPhysicalDataProduct() != null) {
					result.add(groupPhysicalDataProduct
							.getPhysicalDataProduct());
				}
			}
		}
		return result;
	}

	// physical instance
	public List<PhysicalInstanceType> getPhysicalInstances() {
		List<PhysicalInstanceType> result = new ArrayList<PhysicalInstanceType>();

		for (StudyUnitType studyUnit : (writable?getStudyUnits():studyUnitList)) {
			if (studyUnit.getPhysicalInstanceList() != null) {
				result.addAll(studyUnit.getPhysicalInstanceList());
			}
		}

		if (xmlObject instanceof PhysicalInstanceDocument) {
			result.add(((PhysicalInstanceDocument) xmlObject)
					.getPhysicalInstance());
		}
		return result;
	}

	// archive
	public List<ArchiveType> getArchives() {
		List<ArchiveType> result = new ArrayList<ArchiveType>();

		for (StudyUnitType studyUnit : (writable?getStudyUnits():studyUnitList)) {
			if (studyUnit.getArchive() != null) {
				result.add(studyUnit.getArchive());
			}
		}

		if (xmlObject instanceof ArchiveDocument) {
			result.add(((ArchiveDocument) xmlObject).getArchive());
		}
		 
		if (groupedContent) {			
			for (GroupType group : (writable?getGroups():groupList)) {
				if (group.getArchive() != null) {
					result.add(group.getArchive());
				}
			}
		}

		for (ResourcePackageType resourcePackage : (writable?getResourcePackages():resourcePackageList)) {
			if (resourcePackage.getArchive() != null) {
				result.add(resourcePackage.getArchive());
			}
		}
		return result;
	}
}
