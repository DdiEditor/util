package org.ddialliance.ddiftp.util.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ddialliance.ddiftp.util.Config;
import org.ddialliance.ddiftp.util.DDIFtpException;
import org.ddialliance.ddiftp.util.Translator;
import org.ddialliance.ddiftp.util.log.Log;
import org.ddialliance.ddiftp.util.log.LogFactory;
import org.ddialliance.ddiftp.util.log.LogType;

/**
 * Following the standard: <br>
 * urn:ddi:[agency]:[maintainable element type].[maintainable id](:[versionable
 * or identifiable element type].[versionable or identifiable id])?
 */
public class Urn {
	private static Log log = LogFactory.getLog(LogType.SYSTEM, Urn.class.getName());
	private String identifingAgency;

	private String maintainableElement = null;
	private String maintainableId;
	private String maintainableVersion;

	private String containedElement;
	private String containedElementId;
	private String containedElementVersion;

	public Urn() {
	}

	/**
	 * Construct an URN using all arguments
	 * 
	 * @param schemaVersion
	 * @param identifingAgency
	 * @param maintainableElement
	 * @param maintainableId
	 * @param maintainableVersion
	 * @param containedElement
	 * @param containedElementId
	 * @param containedElementVersion
	 */
	public Urn(String schemaVersion, String identifingAgency, String maintainableElement, String maintainableId,
			String maintainableVersion, String containedElement, String containedElementId,
			String containedElementVersion) {
		this.identifingAgency = identifingAgency;
		this.maintainableElement = maintainableElement;
		this.maintainableId = maintainableId;
		this.maintainableVersion = maintainableVersion;
		this.containedElement = containedElement;
		this.containedElementId = containedElementId;
		this.containedElementVersion = containedElementVersion;
	}

	/**
	 * Construct an URN based on an URN string
	 * 
	 * @param urnString
	 *            to parse
	 * @throws DDIFtpException
	 */
	public Urn(String urnString) throws DDIFtpException {
		parseUrn(urnString);
	}

	public String getIdentifingAgency() {
		return identifingAgency;
	}

	public void setIdentifingAgency(String identifingAgency) {
		this.identifingAgency = identifingAgency;
	}

	public String getMaintainableElement() {
		return maintainableElement;
	}

	public void setMaintainableElement(String maintainableElement) {
		this.maintainableElement = maintainableElement;
	}

	public String getMaintainableId() {
		return maintainableId;
	}

	public void setMaintainableId(String maintainableId) {
		this.maintainableId = maintainableId;
	}

	public String getMaintainableVersion() {
		return maintainableVersion;
	}

	public void setMaintainableVersion(String maintainableVersion) {
		this.maintainableVersion = maintainableVersion;
	}

	public String getContainedElement() {
		return containedElement;
	}

	public void setContainedElement(String containedElement) {
		this.containedElement = containedElement;
	}

	public String getContainedElementId() {
		return containedElementId;
	}

	public void setContainedElementId(String containedElementId) {
		this.containedElementId = containedElementId;
	}

	public String getContainedElementVersion() {
		return containedElementVersion;
	}

	public void setContainedElementVersion(String containedElementVersion) {
		this.containedElementVersion = containedElementVersion;
	}

	/*
[Uu][Rr][Nn]:
[Dd][Dd][Ii]:
agency [A-Za-z]+\.[A-Za-z][A-Za-z0-9\-]*
element type [A-Z|a-z]+
id [A-Z|a-z]+[A-Z|a-z|0-9|_|\-]*
version ([0-9]+\.[0-9]+\.[0-9]+|[0-9]+\.[0-9]+\.L|[0-9]+\.L\.L|L\.L\.L)

	 * */
	public static boolean validateAgencyString(String agency) {
		if (agency == null) {
			return false;
		}
		Pattern idPattern = Pattern.compile("((\\w)+((\\.)?(\\w)*(\\.)?)*)");
		Matcher matcher = idPattern.matcher(agency);
		return matcher.matches();
	}

	/**
	 * Version number of the referenced object, expressed as a two-part numeric
	 * string composed of two positive integers separated by a period. The first
	 * number indicates a major version, the second a minor one: 1.0.
	 * Optionally, a third integer may indicate sub-version: 1.0.2.
	 * 
	 * @param version
	 * @return
	 */
	public static boolean validateVersionString(String version) {
		if (version == null) {
			return false;
		}
		Pattern idPattern = Pattern.compile("(\\d)+(\\.)+(\\d)+((\\.)+(\\d)+)*");
		Matcher matcher = idPattern.matcher(version);
		return matcher.matches();
	}

	public static boolean validateIdString(String id) {
		if (id == null) {
			return false;
		}
		Pattern idPattern = Pattern.compile("([A-Z]|[a-z]|\\*|@|[0-9]|_|$|\\-)*");
		Matcher matcher = idPattern.matcher(id);
		return matcher.matches();
	}

	/**
	 * Parse an urn following the standard: <br>
	 * urn=urn:ddi:3_0:<br>
	 * <Maintainable Object Class.Object Class>=<Agency ID>:<br>
	 * <ID of maintained object>[<Major Version>.<Minor Version>]. optional[{<ID
	 * of versioned object>[<Major Version>.<Minor Version>].}2] optional[<ID of
	 * contained object>]
	 * 
	 * @param urnString
	 *            to parse
	 * @throws DDIFtpException
	 *             if URN string is not well formed
	 */
	public void parseUrn(String urnString) throws DDIFtpException {
		if (urnString == null) {
			throw new DDIFtpException("exception.null", "URN");
		} else if (urnString.equals("")) {
			throw new DDIFtpException("exception.null", "URN");
		} else if (log.isDebugEnabled()) {
			log.debug(urnString);
		}
		String[] splitColon = urnString.split(":");
		// 0 urn:
		// 1 ddi:
		// 2 us.icpsr:
		// 3 DataCollection.DC_5698.2.4.0:
		// 4 TimeMethod_1.1.0.0

		// maintainable
		String[] maintainableSplit = splitColon[3].split("\\.");
		maintainableElement = maintainableSplit[0];
		maintainableId = maintainableSplit[1];
		StringBuilder versionBuilder = new StringBuilder();
		for (int i = 2; i < maintainableSplit.length; i++) {
			versionBuilder.append(maintainableSplit[i]);
			if (i < 4) {
				versionBuilder.append(".");
			}
		}
		maintainableVersion = versionBuilder.toString();
		// validate maintainable

		// sub element
		if (splitColon.length > 4) {
			String[] containedSplit = splitColon[4].split("\\.");
			containedElement = containedSplit[0];
			containedElementId = containedSplit[1];
			versionBuilder.delete(0, maintainableVersion.length());
			versionBuilder = new StringBuilder();
			for (int i = 2; i < containedSplit.length; i++) {
				versionBuilder.append(containedSplit[i]);
				if (i < 4) {
					versionBuilder.append(".");
				}
			}
			containedElementVersion = versionBuilder.toString();
		}
		// validate contained element
		
//		String tmp = null;
//
//		// object class
//		int index = urnString.indexOf(Config.get(Config.DDI3_XML_VERSION));
//		if (index < 0) {
//			throw new DDIFtpException("urn.schema.invalid", Config.get(Config.DDI3_XML_VERSION), new Throwable());
//		}
//
//		// maintainable
//		int count = index + Config.get(Config.DDI3_XML_VERSION).length() + 1;
//		int endIndex = urnString.lastIndexOf(":");
//		if (endIndex > -1) {
//			tmp = urnString.substring(count, endIndex);
//			int containedIndex = tmp.indexOf(".");
//			if (containedIndex > -1) {
//				setMaintainableElement(tmp.substring(0, containedIndex));
//				setContainedElement(tmp.substring(containedIndex + 1));
//			} else {
//				setMaintainableElement(tmp);
//			}
//			if (tmp.equals("")) {
//				throw new DDIFtpException("urn.class.invalid", tmp, new Throwable());
//			}
//			tmp = null;
//		} else {
//			throw new DDIFtpException("urn.class.invalid", null, new Throwable());
//		}
//
//		// agency id
//		index = endIndex + 1;
//		endIndex = urnString.indexOf(":", endIndex);
//		if (endIndex > -1) {
//			tmp = urnString.substring(index, endIndex);
//			setIdentifingAgency(tmp);
//			if (!Urn.validateAgencyString(tmp)) {
//				throw new DDIFtpException("urn.agency.invalid", tmp, new Throwable());
//			}
//			tmp = null;
//		} else {
//			throw new DDIFtpException("urn.agency.invalid", "null", new Throwable());
//		}

//		// maintainable id
//		tmp = urnString.substring(endIndex + 1);
//		String[] identifiables = tmp.split("\\]\\.");
//		for (int i = 0; i < identifiables.length; i++) {
//			IdVersion idVersion = splitIdVersion(identifiables[i]);
//
//			// maintainable
//			if (i == 0) {
//				setMaintainableId(idVersion.id);
//				setMaintainableVersion(idVersion.version);
//			}
//
//			// versionable
//			if (i == 1 && idVersion.version != null) {
//				setVersionableElementId(idVersion.id);
//				setVersionableElementVersion(idVersion.version);
//			} else if (i == 1) {
//				setContainedElementId(idVersion.id);
//			}
//
//			// nested versionable
//			if (i == 2 && idVersion.version != null) {
//				setNestedVersionableElementId(idVersion.id);
//				setNestedVersionableElementVersion(idVersion.version);
//			} else if (i == 2) {
//				setContainedElementId(idVersion.id);
//			}
//
//			// contained
//			if (i == 3) {
//				setContainedElementId(idVersion.id);
//			}
//		}

		

		if (log.isDebugEnabled()) {
			log.debug("Parsed urn: " + this);
		}
	}

	

	/**
	 * Build formated URN string
	 * 
	 * @return URN string
	 * @throws DDIFtpException
	 *             if not required are present or in case of agency, id, version
	 *             format violation
	 */
	public String toUrnString() throws DDIFtpException {
		StringBuilder error = new StringBuilder();
		StringBuilder result = new StringBuilder();

		// header
		result.append("urn");
		result.append(":ddi:");
		result.append(this.identifingAgency);

		// element types
		if (maintainableElement == null || maintainableElement.equals("")) {
			error.append(Translator.trans("urn.maintained.null"));
			error.append(" ");
		}
		result.append(maintainableElement);

		if (containedElement != null && !containedElement.equals("")) {
			result.append(".");
			result.append(containedElement);
		}

		// agency
		result.append("=");
		result.append(identifingAgency);
		result.append(":");
		if (!validateAgencyString(identifingAgency)) {
			error.append(Translator.trans("urn.agency.invalid", new Object[] { identifingAgency }));
			error.append(" ");
		}

		// id n' version
		// maintain
		result.append(maintainableId);
		if (!validateIdString(maintainableId)) {
			error.append(Translator.trans("urn.id.invalid", new Object[] { maintainableId }));
			error.append(" ");
		}
		result.append("[");
		result.append(maintainableVersion);
		if (!validateVersionString(maintainableVersion)) {
			error.append(Translator.trans("urn.version.invalid", new Object[] { maintainableVersion }));
			error.append(" ");
		}
		result.append("]");

		// versionable
		if (containedElementId != null || containedElementVersion != null) {
			result.append(".");
			result.append(containedElementId);
			if (!validateIdString(containedElementId)) {
				error.append(Translator.trans("urn.id.invalid", new Object[] { containedElementId }));
				error.append(" ");
			}

			result.append("[");
			result.append(containedElementVersion);
			result.append("]");
			if (!validateVersionString(containedElementVersion)) {
				error.append(Translator.trans("urn.version.invalid", new Object[] { containedElementVersion }));
				error.append(" ");
			}
		}

		// contained
		if (containedElementId != null && !containedElementId.equals("")) {
			result.append(".");
			result.append(containedElementId);
			if (!validateIdString(containedElementId)) {
				error.append(Translator.trans("urn.id.invalid", new Object[] { containedElementId }));
				error.append(" ");
			}
		}

		if (error.length() > 0) {
			log.error(result.toString());
			DDIFtpException e = new DDIFtpException(error.toString());
			e.setRealThrowable(new Throwable());
			throw e;
		}
		return result.toString();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(", identifingAgency: ");
		result.append(identifingAgency);

		result.append("maintainableElement: ");
		result.append(maintainableElement);
		result.append(", maintainableId: ");
		result.append(maintainableId);
		result.append(", maintainableVersion: ");
		result.append(maintainableVersion);

		result.append(", containedElement: ");
		result.append(containedElement);
		result.append(", containedElementId: ");
		result.append(containedElementId);
		result.append(", containedElementVersion: ");
		result.append(containedElementVersion);

		return result.toString();
	}
}
