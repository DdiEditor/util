package org.ddialliance.ddiftp.util.xml.ddi_3_0;

import java.io.File;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.ddialliance.ddi3.xml.xmlbeans.reusable.AbstractIdentifiableType;
import org.ddialliance.ddi3.xml.xmlbeans.reusable.IdentifiableType;
import org.ddialliance.ddi3.xml.xmlbeans.reusable.MaintainableType;
import org.ddialliance.ddi3.xml.xmlbeans.reusable.ReferenceType;
import org.ddialliance.ddi3.xml.xmlbeans.reusable.VersionableType;
import org.ddialliance.ddi3.xml.xmlbeans.reusable.impl.IdentifiableTypeImpl;
import org.ddialliance.ddi3.xml.xmlbeans.reusable.impl.MaintainableTypeImpl;
import org.ddialliance.ddi3.xml.xmlbeans.reusable.impl.VersionableTypeImpl;
import org.ddialliance.ddiftp.util.Config;
import org.ddialliance.ddiftp.util.DDIFtpException;
import org.ddialliance.ddiftp.util.log.Log;
import org.ddialliance.ddiftp.util.log.LogFactory;
import org.ddialliance.ddiftp.util.log.LogType;
import org.ddialliance.ddiftp.util.xml.Urn;
import org.ddialliance.ddiftp.util.xml.UrnUtil;
import org.ddialliance.ddiftp.util.xml.XQueryUtil;
import org.ddialliance.ddiftp.util.xml.XmlBeansUtil;
import org.w3c.dom.Node;

/**
 * DDI3 URN access utility
 */
public class UrnUtilDDI3 implements UrnUtil {
	public static Log logSystem = LogFactory.getLog(LogType.SYSTEM,
			UrnUtilDDI3.class);

	public enum IdentifierType {
		VERSION, AGENCY, ID
	}

	/**
	 * Generate an URN based on a reference <br>
	 * Note the URN generated is not a fully valid URN because a reference is
	 * not holding all needed information!
	 * 
	 * @param reference
	 *            to generate URN from
	 * @return urn
	 */
	public static Urn getUrn(ReferenceType reference) throws DDIFtpException {
		if (!reference.getURNList().isEmpty()) {
			Urn urn = new Urn();
			urn.parseUrn(reference.getURNArray(0)
					.getStringValue());
			return urn;
		} else if (!reference.getIDList().isEmpty()) {
			Urn urn = new Urn();
			if (!reference.getIdentifyingAgencyList().isEmpty()) {
				urn.setIdentifingAgency(reference.getIdentifyingAgencyArray(0));
			}
			if (!reference.getVersionList().isEmpty()) {
				urn.setMaintainableVersion(reference.getVersionArray(0)
						.getStringValue());
				urn.setVersionableElementVersion(reference.getVersionArray(0)
						.getStringValue());
			}
			urn.setVersionableElementId(reference.getIDArray(0).getStringValue());
			return urn;
		}
		return null;
	}

	/**
	 * Generate the URN of an element of the type abstract identifiable Auto
	 * generation will seek out missing identifying agency and versions. If
	 * these are missing an exception is thrown.
	 * 
	 * @param element
	 *            abstract identifiable
	 * @return urn generated
	 * @throws Exception
	 */
	public static Urn getUrn(AbstractIdentifiableType element)
			throws DDIFtpException {
		if (element.getUrn() != null) {			
			Urn urn = new Urn();
			urn.parseUrn(element.getUrn());
			return urn;
		}
		if (element.getId() == null) {
			throw new DDIFtpException("urn.id.invalid", "null");
		}

		Urn urn = new Urn();
		urn.setSchemaVersion(Config.get(Config.DDI3_XML_VERSION));
		urn.setContainedElement(element.getDomNode().getLocalName());

		MaintainableType maintainIdentifier = null;
		MaintainableType checkMaintainIdentifier = null;
		VersionableType checkVersionIdentifier = null;
		Boolean check = null;
		Node checkNode = null;

		// maintainable
		if (element instanceof MaintainableTypeImpl) {
			maintainIdentifier = (MaintainableTypeImpl) element;

			// check id
			check = validateMaintainable(maintainIdentifier, IdentifierType.ID);
			if (check) {
				urn.setMaintainableId(maintainIdentifier.getId());
			}

			// check agency identification
			checkMaintainIdentifier = maintainIdentifier;
			check = validateMaintainable(checkMaintainIdentifier,
					IdentifierType.AGENCY);
			if (check) {
				urn.setIdentifingAgency(checkMaintainIdentifier.getAgency());
			}
			while (check != null && !check) {
				checkNode = checkMaintainIdentifier.getDomNode();
				checkMaintainIdentifier = getParentElement(checkNode);
				check = validateMaintainable(checkMaintainIdentifier,
						IdentifierType.AGENCY);
				if (check == null) {
					DDIFtpException e = new DDIFtpException("Missing agency: "
							+ checkNode.getLocalName() + " on "
							+ maintainIdentifier.getId());
					e.setValue(checkNode);
					throw e;
				} else if (check) {
					urn
							.setIdentifingAgency(checkMaintainIdentifier
									.getAgency());
				}
			}

			// check version identification
			checkNode = null;
			checkMaintainIdentifier = maintainIdentifier;
			check = validateMaintainable(checkMaintainIdentifier,
					IdentifierType.VERSION);
			if (check) {
				urn
						.setMaintainableVersion(checkMaintainIdentifier
								.getVersion());
			}
			while (check != null && !check) {
				checkNode = checkMaintainIdentifier.getDomNode();
				checkMaintainIdentifier = getParentElement(checkNode);
				check = validateMaintainable(checkMaintainIdentifier,
						IdentifierType.VERSION);
				if (check == null) {
					DDIFtpException e = new DDIFtpException("Missing version: "
							+ checkNode.getLocalName());
					e.setValue(checkNode);
					throw e;
				} else if (check) {
					urn.setMaintainableVersion(checkMaintainIdentifier
							.getVersion());
				}
			}
		}

		// versionable
		else if (element instanceof VersionableTypeImpl) {
			checkVersionIdentifier = (VersionableTypeImpl) element;
			if (checkVersionIdentifier.getId() != null) {
				urn.setVersionableElementId(checkVersionIdentifier.getId());
			}

			// check version identification
			check = validateVersionable(checkVersionIdentifier);
			if (check) {
				urn.setVersionableElementVersion(checkVersionIdentifier.getVersion());
				check = false;
			}

			generateUrn(check, urn, checkVersionIdentifier,
					checkMaintainIdentifier);
		}

		// identifiable
		else if (element instanceof IdentifiableTypeImpl) {
			IdentifiableType identifiable = (IdentifiableType) element;
			if (identifiable.getId() != null) {
				urn.setVersionableElementId(identifiable.getId());
			}

			try {
				checkVersionIdentifier = getParentElement(identifiable
						.getDomNode());
				check = validateVersionable(checkVersionIdentifier);
			} catch (Exception e) {
				try {
					checkMaintainIdentifier = getParentElement(identifiable
							.getDomNode());
					check = false;
					check = validateMaintainable(checkMaintainIdentifier,
							IdentifierType.AGENCY);
					if (check) {
						urn.setIdentifingAgency(checkMaintainIdentifier
								.getAgency());
					}
					if (checkMaintainIdentifier.getId() != null) {
						urn.setMaintainableId(checkMaintainIdentifier.getId());
					}
					check = validateMaintainable(checkMaintainIdentifier,
							IdentifierType.VERSION);
					if (check) {
						urn.setMaintainableVersion(checkMaintainIdentifier
								.getVersion());
					}
				} catch (Exception ex) {
					// parent is identifiable
					try {
						checkVersionIdentifier = getParentElement(identifiable
								.getDomNode().getParentNode());
						check = validateVersionable(checkVersionIdentifier);
					} catch (Exception exc) {
						checkMaintainIdentifier = getParentElement(identifiable
								.getDomNode().getParentNode());
						check = validateMaintainable(checkMaintainIdentifier,
								IdentifierType.AGENCY);
						if (check) {
							urn.setIdentifingAgency(checkMaintainIdentifier
									.getAgency());
						}
						if (checkMaintainIdentifier.getId() != null) {
							urn.setMaintainableId(checkMaintainIdentifier
									.getId());
						}
					}
				}
			}

			// resolve parent inheritance
			generateUrn(check, urn, checkVersionIdentifier,
					checkMaintainIdentifier);
		}
		return urn;
	}

	/**
	 * Generate and check an URN according to the standard Resolve parental
	 * inheritance on maintainable and version
	 * 
	 * @param check
	 * @param urn
	 * @param tmpVersion
	 * @param checkVersionIdentifier
	 * @param checkMaintainIdentifier
	 * @return urn
	 * @throws DDIFtpException
	 */
	private static Urn generateUrn(Boolean check, Urn urn,
			VersionableType checkVersionIdentifier,
			MaintainableType checkMaintainIdentifier) throws DDIFtpException {
		MaintainableType idMaintainableID = null;
		Node checkNode = null;
		Node parent = null;
		XmlObject checkIdentifier = null;
		DDIFtpException ddiFtpException = null;
		boolean notGroupParent = true;

		while (check != null && !check) {
			if (notGroupParent && checkVersionIdentifier != null) {
				checkNode = checkVersionIdentifier.getDomNode();
			} else if (notGroupParent) {
				// maintainable mode
				checkNode = checkMaintainIdentifier.getDomNode();
			}

			parent = checkNode.getParentNode();
			if (parent != null && parent.getLocalName() != null) {
				checkIdentifier = XmlBeansUtil.nodeToXmlObject(XmlObject.class,
						parent);
				// versionable mode
				if (checkIdentifier instanceof VersionableTypeImpl) {
					checkVersionIdentifier = (VersionableType) checkIdentifier;
					check = validateVersionable(checkVersionIdentifier);
					if (check == null) {
						ddiFtpException = new DDIFtpException(
								"Missing version identifier");
						ddiFtpException.setValue(checkNode);
						throw ddiFtpException;
					}
				}
				// maintainable mode
				else if (checkIdentifier instanceof MaintainableTypeImpl) {
					checkVersionIdentifier = null;
					checkMaintainIdentifier = getParentElement(checkNode);

					// maintain id
					if (idMaintainableID == null
							&& validateMaintainable(checkMaintainIdentifier,
									IdentifierType.ID)) {
						idMaintainableID = checkMaintainIdentifier;
					}

					// version
					check = validateMaintainable(checkMaintainIdentifier,
							IdentifierType.VERSION);
					if (check == null) {
						ddiFtpException = new DDIFtpException(
								"Missing version identifier");
						ddiFtpException.setValue(checkNode);
						throw ddiFtpException;
					} else if (check && urn.getVersionableElementVersion() == null) {
						urn.setVersionableElementVersion(checkMaintainIdentifier
								.getVersion());
					}

					// agency
					check = validateMaintainable(checkMaintainIdentifier,
							IdentifierType.AGENCY);
					if (check == null) {
						ddiFtpException = new DDIFtpException(
								"Missing agency identifier");
						ddiFtpException.setValue(checkNode);
						throw ddiFtpException;
					}
				} else {
					// move a node up to skip group element
					checkMaintainIdentifier = getParentElement(checkNode);
				}
			} else {
				ddiFtpException = new DDIFtpException(
						"Error resolving maintainable identifier");
				ddiFtpException.setValue(checkNode);
				throw ddiFtpException;
			}

			// generate urn
			if (check) {
				if (checkMaintainIdentifier != null) {
					urn
							.setIdentifingAgency(checkMaintainIdentifier
									.getAgency());
					if (urn.getMaintainableId() == null
							&& idMaintainableID != null) {
						urn.setMaintainableId(idMaintainableID.getId());
					}
					if (urn.getMaintainableVersion() == null
							&& checkMaintainIdentifier.getVersion() != null) {
						urn.setMaintainableVersion(checkMaintainIdentifier
								.getVersion());
						if (urn.getElementId() != null
								&& urn.getVersionableElementVersion() == null) {
							urn.setVersionableElementVersion(checkMaintainIdentifier
									.getVersion());
						}
					}
				} else if (checkVersionIdentifier != null
						&& checkVersionIdentifier.getVersion() != null) {
					urn.setMaintainableVersion(checkVersionIdentifier
							.getVersion());
					if (urn.getElementId() != null
							&& urn.getVersionableElementVersion() == null) {
						urn.setVersionableElementVersion(checkVersionIdentifier
								.getVersion());
					}
				}
			}
		}
		return urn;
	}

	/**
	 * Retrieve the parent element of a Node
	 * 
	 * @param node
	 *            to retrieve parent identifier from
	 * @return parent identifier
	 */
	public static <T extends AbstractIdentifiableType> T getParentElement(
			Node node) {
		XmlObject xmlObject = null;
		Node parent = node.getParentNode();
		if (parent != null && parent.getLocalName() != null) {
			xmlObject = XmlBeansUtil.nodeToXmlObject(XmlObject.class, parent);
		}
		// if (logSystem.isDebugEnabled()) {
		// logSystem.debug("GetParentElm, node: "+node.getLocalName()+", parent:
		// "+parent.getLocalName()+", result:
		// "+xmlObject.getClass().getSimpleName());
		//		 }
		if (!(xmlObject instanceof AbstractIdentifiableType)) {
			if (parent.getParentNode() == null) {
				return null;
			}
			return (T) getParentElement(parent);
		}
		return (T) xmlObject;
	}

	private static Boolean validateMaintainable(MaintainableType identifier,
			IdentifierType identifierType) {

		if (identifier != null) {
			if (identifierType.equals(IdentifierType.AGENCY)
					&& identifier.getAgency() == null) {
				return false;
			} else if (identifierType.equals(IdentifierType.VERSION)
					&& identifier.getVersion() == null) {
				return false;
			} else if (identifierType.equals(IdentifierType.ID)
					&& identifier.getId() == null) {
				return false;
			}
			return true;
		}
		return null;
	}

	private static Boolean validateVersionable(VersionableType identifier) {
		if (identifier != null && identifier.getVersion() == null) {
			return false;
		} else if (identifier != null) {
			return true;
		}
		return null;
	}

	/**
	 * Generate an URN based on a XPath to a DDI3 document
	 * 
	 * @param file
	 *            of DDI3 document
	 * @param xpath
	 * @return urn
	 * @throws DDIFtpException
	 * @throws Exception
	 */
	public static Urn getUrnByXpath(File file, String xpath)
			throws DDIFtpException, Exception {
		XmlObject xmlObject = XmlBeansUtil.openDDIDoc(file);
		return getUrnByXpath(xmlObject, xpath);
	}

	/**
	 * Generate an URN based on a XPath to a DDI3 document
	 * 
	 * @param xmlObject
	 *            DDI3 XmlObject
	 * @param xpath
	 *            XPath expression
	 * @return urn
	 * @throws DDIFtpException
	 * @throws Exception
	 */
	public static Urn getUrnByXpath(XmlObject xmlObject, String xpath)
			throws DDIFtpException, Exception {
		XQueryUtil xQueryUtil = new XQueryUtil(xmlObject);
		XmlCursor xmlCursor = xQueryUtil.selectXPath(xpath, xmlObject, true);
		if (xmlCursor.toNextSelection()) {
			XmlObject foundXmlObject = xmlCursor.getObject();
			xmlCursor.dispose();
			return UrnUtilDDI3
					.getUrn((AbstractIdentifiableType) foundXmlObject);
		} else {
			return null;
		}
	}

	/**
	 * Retrieve any DDI3 XmlObject by its URN
	 * 
	 * @param file
	 *            to search in
	 * @param urnString
	 *            to search for
	 * @return found xml object if not found return null
	 * @throws Exception
	 */
	public static XmlObject getXmlObject(File file, String urnString)
			throws DDIFtpException, Exception {
		XmlObject xmlObject = XmlBeansUtil.openDDIDoc(file);
		return getXmlObject(xmlObject, urnString);
	}

	/**
	 * Retrieve any DDI3 XmlObject by its URN
	 * 
	 * @param xmlObject
	 *            to search in
	 * @param urnString
	 *            to search by
	 * @return found xml object
	 * @throws Exception
	 */
	public static XmlObject getXmlObject(XmlObject xmlObject, String urnString)
			throws DDIFtpException {
		Urn urn = new Urn();
		urn.parseUrn(urnString);
		
		XmlCursor xmlCursor = xmlObject.newCursor();

		// search for right object type
		while (xmlCursor.hasNextToken()) {
			xmlCursor.toNextToken();
			if (xmlCursor.currentTokenType().isStart()) {
				if (urn.getContainedElement().equals(
						xmlCursor.getName().getLocalPart())) {
					xmlObject = xmlCursor.getObject();

					if (xmlObject instanceof AbstractIdentifiableType) {
						Urn checkUrn = getUrn((AbstractIdentifiableType) xmlObject);
						if (checkUrn.toUrnString().equals(urnString)) {
							xmlCursor.dispose();

							// return found object
							return xmlObject;
						}
					}
				}
			}
		}
		xmlCursor.dispose();
		return null;
	}

	/**
	 * Parse an urn following the standard: <br>
	 * urn:ddi:[ddi_schemaversion]:[class]=<br>
	 * [agency_id]:[maintainable_container_id]([version]).[contained_item_id]([version])
	 * 
	 * @param urnString
	 *            to parse
	 * @return urn
	 * @throws DDIFtpException
	 *             if URN string is not well formed
	 * @deprecated Use {@link Urn#parseUrn(String)} instead
	 */
	public static Urn parseUrn(String urnString) throws DDIFtpException {
		Urn urn = new Urn();
		urn.parseUrn(urnString);
		return urn;
	}

	/**
	 * @deprecated Use {@link Urn#validateIdString(String)} instead
	 */
	public static boolean validateIdString(String id) {
		return Urn.validateIdString(id);
	}

	/**
	 * @deprecated Use {@link Urn#validateVersionString(String)} instead
	 */
	public static boolean validateVersionString(String version) {
		return Urn.validateVersionString(version);
	}

	/**
	 * @deprecated Use {@link Urn#validateAgencyString(String)} instead
	 */
	public static boolean validateAgencyString(String agency) {
		return Urn.validateAgencyString(agency);
	}
}
