package org.ddialliance.ddiftp.util.xml;

import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ddialliance.ddiftp.util.Config;
import org.ddialliance.ddiftp.util.DDIFtpException;
import org.ddialliance.ddiftp.util.Translator;
import org.ddialliance.ddiftp.util.log.Log;
import org.ddialliance.ddiftp.util.log.LogFactory;
import org.ddialliance.ddiftp.util.log.LogType;
import org.ddialliance.ddiftp.util.xml.ddi_3_0.UrnUtilDDI3;

/**
 * Following the standard: <br>
 * urn=urn:ddi:3_0:<br>
 * <Maintainable Object Class.Object Class>=<Agency ID>:<br>
 * <ID of maintained object>[<Major Version>.<Minor Version>]. optional[{<ID of
 * versioned object>[<Major Version>.<Minor Version>].}2] optional[<ID of
 * contained object>]
 */
public class Urn {
	private static Log log = LogFactory.getLog(LogType.SYSTEM, Urn.class
			.getName());
	private String prefix = "ddi";
	private String schemaVersion;
	private String identifingAgency;

	private String maintainableElement = null;
	private String maintainableId;
	private String maintainableVersion;

	private String containedElement;
	private String versionableElementId;
	private String versionableElementVersion;

	private String nestedVersionableElementId;
	private String nestedVersionableElementVersion;

	private String containedElementId;

	public Urn() {
	}

	/**
	 * @deprecated
	 */
	public Urn(String prefix, String schemaVersion, String elementName,
			String identifingAgency, String maintainableId,
			String maintainableVersion, String elementVersion, String elementId) {
		this.prefix = prefix;
		this.schemaVersion = schemaVersion;
		this.containedElement = elementName;
		this.identifingAgency = identifingAgency;
		this.maintainableId = maintainableId;
		this.maintainableVersion = maintainableVersion;
		this.versionableElementVersion = elementVersion;
		this.versionableElementId = elementId;
	}

	/**
	 * Construct an URN using all arguments
	 * 
	 * @param schemaVersion
	 * @param identifingAgency
	 * @param maintainableElement
	 * @param maintainableId
	 * @param maintainableVersion
	 * @param versionableElement
	 * @param versionableElementId
	 * @param versionableElementVersion
	 * @param nestedVersionableElementId
	 * @param nestedVersionableElementVersion
	 * @param containedElementId
	 */
	public Urn(String schemaVersion, String identifingAgency,
			String maintainableElement, String maintainableId,
			String maintainableVersion, String containedElement,
			String versionableElementId, String versionableElementVersion,
			String nestedVersionableElementId,
			String nestedVersionableElementVersion, String containedElementId) {
		this.schemaVersion = schemaVersion;
		this.identifingAgency = identifingAgency;
		this.maintainableElement = maintainableElement;
		this.maintainableId = maintainableId;
		this.maintainableVersion = maintainableVersion;
		this.containedElement = containedElement;
		this.versionableElementId = versionableElementId;
		this.versionableElementVersion = versionableElementVersion;
		this.nestedVersionableElementId = nestedVersionableElementId;
		this.nestedVersionableElementVersion = nestedVersionableElementVersion;
		this.containedElementId = containedElementId;
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

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSchemaVersion() {
		return schemaVersion;
	}

	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}

	public String getContainedElement() {
		return containedElement;
	}

	public void setContainedElement(String containedElement) {
		this.containedElement = containedElement;
	}

	public String getIdentifingAgency() {
		return identifingAgency;
	}

	public void setIdentifingAgency(String identifingAgency) {
		this.identifingAgency = identifingAgency;
	}

	public String getMaintainableId() {
		return maintainableId;
	}

	public void setMaintainableId(String maintainableId) {
		this.maintainableId = maintainableId;
	}

	public String getElementId() {
		return versionableElementId;
	}

	public void setVersionableElementId(String versionableElementId) {
		this.versionableElementId = versionableElementId;
	}

	public String getVersionableElementVersion() {
		return versionableElementVersion;
	}

	public void setVersionableElementVersion(String versionableElementVersion) {
		this.versionableElementVersion = versionableElementVersion;
	}

	public String getMaintainableVersion() {
		return maintainableVersion;
	}

	public void setMaintainableVersion(String maintainableVersion) {
		this.maintainableVersion = maintainableVersion;
	}

	public String getMaintainableElement() {
		return maintainableElement;
	}

	public void setMaintainableElement(String maintainableElement) {
		this.maintainableElement = maintainableElement;
	}

	public String getNestedVersionableElementId() {
		return nestedVersionableElementId;
	}

	public void setNestedVersionableElementId(String nestedVersionableElementId) {
		this.nestedVersionableElementId = nestedVersionableElementId;
	}

	public String getNestedVersionableElementVersion() {
		return nestedVersionableElementVersion;
	}

	public void setNestedVersionableElementVersion(
			String nestedVersionableElementVersion) {
		this.nestedVersionableElementVersion = nestedVersionableElementVersion;
	}

	public String getContainedElementId() {
		return containedElementId;
	}

	public void setContainedElementId(String containedElementId) {
		this.containedElementId = containedElementId;
	}

	public String getVersionableElementId() {
		return versionableElementId;
	}

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
		Pattern idPattern = Pattern
				.compile("(\\d)+(\\.)+(\\d)+((\\.)+(\\d)+)*");
		Matcher matcher = idPattern.matcher(version);
		return matcher.matches();
	}

	public static boolean validateIdString(String id) {
		if (id == null) {
			return false;
		}
		Pattern idPattern = Pattern
				.compile("([A-Z]|[a-z]|\\*|@|[0-9]|_|$|\\-)*");
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
		String tmp = null;

		// object class
		int index = urnString.indexOf(Config.get(Config.DDI3_XML_VERSION));
		if (index < 0) {
			throw new DDIFtpException("urn.schema.invalid", Config
					.get(Config.DDI3_XML_VERSION), new Throwable());
		}

		// maintainable
		int count = index + Config.get(Config.DDI3_XML_VERSION).length() + 1;
		int endIndex = urnString.indexOf("=");
		if (endIndex > -1) {
			tmp = urnString.substring(count, endIndex);
			int containedIndex = tmp.indexOf(".");
			if (containedIndex > -1) {
				setMaintainableElement(tmp.substring(0, containedIndex));
				setContainedElement(tmp.substring(containedIndex + 1));
			} else {
				setMaintainableElement(tmp);
			}
			if (tmp.equals("")) {
				throw new DDIFtpException("urn.class.invalid", tmp,
						new Throwable());
			}
			tmp = null;
		} else {
			throw new DDIFtpException("urn.class.invalid", null,
					new Throwable());
		}

		// agency id
		index = endIndex + 1;
		endIndex = urnString.indexOf(":", endIndex);
		if (endIndex > -1) {
			tmp = urnString.substring(index, endIndex);
			setIdentifingAgency(tmp);
			if (!Urn.validateAgencyString(tmp)) {
				throw new DDIFtpException("urn.agency.invalid", tmp,
						new Throwable());
			}
			tmp = null;
		} else {
			throw new DDIFtpException("urn.agency.invalid", "null",
					new Throwable());
		}

		// maintainable id
		tmp = urnString.substring(endIndex + 1);
		String[] identifiables = tmp.split("\\]\\.");
		for (int i = 0; i < identifiables.length; i++) {
			IdVersion idVersion = splitIdVersion(identifiables[i]);

			// maintainable
			if (i == 0) {
				setMaintainableId(idVersion.id);
				setMaintainableVersion(idVersion.version);
			}

			// versionable
			if (i == 1 && idVersion.version != null) {
				setVersionableElementId(idVersion.id);
				setVersionableElementVersion(idVersion.version);
			} else if (i == 1) {
				setContainedElementId(idVersion.id);
			}

			// nested versionable
			if (i == 2 && idVersion.version != null) {
				setNestedVersionableElementId(idVersion.id);
				setNestedVersionableElementVersion(idVersion.version);
			} else if (i == 2) {
				setContainedElementId(idVersion.id);
			}

			// contained
			if (i == 3) {
				setContainedElementId(idVersion.id);
			}
		}

		setSchemaVersion(Config.get(Config.DDI3_XML_VERSION));

		if (log.isDebugEnabled()) {
			log.debug("Parsed urn: " + this);
		}
	}

	public class IdVersion {
		public String id;
		public String version;

		public IdVersion() {
		};

		public IdVersion(String id, String version) {
			this.id = id;
			this.version = version;
		}
	}

	private IdVersion splitIdVersion(String idVersionStr)
			throws DDIFtpException {
		IdVersion idVersion = new IdVersion();
		idVersionStr = idVersionStr.replaceAll("\\]", "");
		int index = idVersionStr.indexOf("[");
		if (index == -1) {
			idVersion.id = idVersionStr;
		} else {
			idVersion.id = idVersionStr.substring(0, index - 1);
			idVersion.version = idVersionStr.substring(index + 1, idVersionStr
					.length());
			if (!Urn.validateVersionString(idVersion.version)) {
				throw new DDIFtpException("urn.version.invalid",
						idVersion.version, new Throwable());
			}
		}

		if (!Urn.validateIdString(idVersion.id)) {
			throw new DDIFtpException("urn.id.invalid", idVersion.id,
					new Throwable());
		}
		return idVersion;
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
		result.append(":");
		result.append(prefix);
		result.append(":");
		result.append(schemaVersion);
		result.append(":");

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
			error.append(Translator.trans("urn.agency.invalid",
					new Object[] { identifingAgency }));
			error.append(" ");
		}

		// id n' version
		// maintain
		result.append(maintainableId);
		if (!validateIdString(maintainableId)) {
			error.append(Translator.trans("urn.id.invalid",
					new Object[] { maintainableId }));
			error.append(" ");
		}
		result.append("[");
		result.append(maintainableVersion);
		if (!validateVersionString(maintainableVersion)) {
			error.append(Translator.trans("urn.version.invalid",
					new Object[] { maintainableVersion }));
			error.append(" ");
		}
		result.append("]");

		// versionable
		if (versionableElementId != null || versionableElementVersion != null) {
			result.append(".");
			result.append(versionableElementId);
			if (!validateIdString(versionableElementId)) {
				error.append(Translator.trans("urn.id.invalid",
						new Object[] { versionableElementId }));
				error.append(" ");
			}

			result.append("[");
			result.append(versionableElementVersion);
			result.append("]");
			if (!validateVersionString(versionableElementVersion)) {
				error.append(Translator.trans("urn.version.invalid",
						new Object[] { versionableElementVersion }));
				error.append(" ");
			}
		}

		// nested versionable
		if (nestedVersionableElementId != null
				|| nestedVersionableElementVersion != null) {
			result.append(".");
			result.append(nestedVersionableElementId);
			if (!validateIdString(nestedVersionableElementId)) {
				error.append(Translator.trans("urn.id.invalid",
						new Object[] { nestedVersionableElementId }));
				error.append(" ");
			}

			result.append("[");
			result.append(nestedVersionableElementVersion);
			result.append("]");
			if (!validateVersionString(nestedVersionableElementVersion)) {
				error.append(Translator.trans("urn.version.invalid",
						new Object[] { nestedVersionableElementVersion }));
				error.append(" ");
			}
		}

		// contained
		if (containedElementId != null && !containedElementId.equals("")) {
			result.append(".");
			result.append(containedElementId);
			if (!validateIdString(containedElementId)) {
				error.append(Translator.trans("urn.id.invalid",
						new Object[] { containedElementId }));
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
		result.append("prefix: ");
		result.append(prefix);
		result.append(", schemaVersion: ");
		result.append(schemaVersion);
		result.append(", identifingAgency: ");
		result.append(identifingAgency);
		result.append("maintainableElement: ");
		result.append(maintainableElement);
		result.append(", containedElement: ");
		result.append(containedElement);
		result.append(

		", maintainableId: ");
		result.append(maintainableId);
		result.append(", maintainableVersion: ");
		result.append(maintainableVersion);
		result.append(

		", versionableElementId: ");
		result.append(versionableElementId);
		result.append(", versionableElementVersion: ");
		result.append(versionableElementVersion

		);
		result.append(", nestedVersionableElementId: ");
		result.append(nestedVersionableElementId);
		result.append(", nestedVersionableElementVersion: ");
		result.append(nestedVersionableElementVersion);
		result.append(", containedElementId: ");
		result.append(containedElementId);
		return result.toString();
	}
}
