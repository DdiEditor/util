package org.ddialliance.ddiftp.util.xml;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlCursor.TokenType;
import org.ddialliance.ddiftp.util.Config;
import org.ddialliance.ddiftp.util.DDIFtpException;
import org.ddialliance.ddiftp.util.ReflectionUtil;
import org.ddialliance.ddiftp.util.Translator;
import org.ddialliance.ddiftp.util.log.Log;
import org.ddialliance.ddiftp.util.log.LogFactory;
import org.ddialliance.ddiftp.util.log.LogType;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A collection of XmlBean utilities to access the DDI
 */
public class XmlBeansUtil {
	private static Log logSystem = LogFactory.getLog(LogType.SYSTEM,
			XmlBeansUtil.class);

	/**
	 * Open a DDI document
	 * 
	 * @param <T>
	 *            of type xml object
	 * @param file
	 *            to open
	 * @return xml object of type document
	 * @throws DDIFtpException
	 */
	public static <T extends XmlObject> T openDDIDoc(File file)
			throws DDIFtpException {

		// get first element
		String className = null;

		// parse input
		DefaultHandler handler = new DocHandler();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = null;
		try {
			saxParser = factory.newSAXParser();
		} catch (Exception e) {
			throw new DDIFtpException("sax.factory.exception", e);
		}

		try {
			saxParser.parse(file, handler);
		} catch (SAXException e) {
			// ddi versin not supported
			int check = e.getMessage().indexOf("***");
			if (check > -1) {
				throw new DDIFtpException(e.getMessage().substring(3), e);
			}
			// get the first element name
			className = e.getMessage();
		} catch (IOException e) {
			throw new DDIFtpException("file.not.found", file.getAbsoluteFile(),
					e);
		}
		return (T) openDDI(file, null, className);
	}

	/**
	 * Open a DDI resource file or xml text
	 * 
	 * @param <T>
	 *            of type XmlObject
	 * @param ddi
	 *            resource to open
	 * @param ddiVersion
	 *            version of DDI current 3.0
	 * @param className
	 *            name of DDI element if the form module.element ~
	 *            instance.DDIInstance
	 * @return xml object of type document
	 * @throws DDIFtpException
	 */
	public static <T extends XmlObject> T openDDI(Object ddi,
			String ddiVersion, String className) throws DDIFtpException {
		// check classname
		if (className.indexOf(".") < 0) {
			throw new DDIFtpException("xmlbeanutil.open.classname",
					new Object[] { className });
		}
		className = Config.get(Config.DDI3_XMLBEANS_BASEPACKAGE) + className
				+ "Document";
		if (logSystem.isDebugEnabled()) {
			logSystem.debug("Classname: " + className);
		}

		Object obj = null;
		XmlOptions options = new XmlOptions();
		options.setLoadLineNumbers();
		options.setLoadLineNumbers(XmlOptions.LOAD_LINE_NUMBERS_END_ELEMENT);
		try {
			obj = ReflectionUtil.invokeStaticMethod(className + "$Factory",
					"parse", ddi, options);
		} catch (Exception e) {
			String errorResoure = null;
			if (ddi instanceof File) {
				errorResoure = ((File) ddi).getAbsolutePath();
			} else if (ddi instanceof String) {
				errorResoure = (String) ddi;
			} else {
				errorResoure = "n/a";
			}
			DDIFtpException wrapedException = new DDIFtpException(
					"xmlbeanutil.open.error", errorResoure);
			wrapedException.setRealThrowable(e);
			throw wrapedException;
		}
		return (T) obj;
	}

	public static void saveDDIDoc(XmlObject xmlObject, File file)
			throws Exception {
		if (!file.exists()) {
			file.createNewFile();
		}

		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSaveAggressiveNamespaces();
		xmlOptions.setSaveOuter();
		xmlOptions.setSavePrettyPrint();

		xmlObject.save(file, xmlOptions);
	}

	/**
	 * WC3 DOM node to XmlBeans XmlObject
	 * 
	 * @param c
	 *            class of type xml bean to transform to
	 * @param node
	 *            to transform
	 * @param <T>
	 *            of type XmlObject
	 * @return xml object
	 */
	public static <T extends XmlObject> T nodeToXmlObject(
			Class<? extends XmlObject> c, Node node) {
		// XmlObject.Factory.parse(node) does not return proper mapping of
		// child nodes. Work around:

		XmlCursor xmlCursor = XmlBeans.nodeToCursor(node);
		XmlObject xmlObject = xmlCursor.getObject();
		xmlCursor.dispose();

		return (T) xmlObject;
	}

	/**
	 * Set text on a mixed content element at first position after attributs
	 * 
	 * @param xmlObject
	 *            to set text on
	 * @param text
	 *            to set
	 * @return altered xml object
	 */
	public static XmlObject setTextOnMixedElement(XmlObject xmlObject,
			String text) {
		XmlCursor xmlCursor = xmlObject.newCursor();

		// insert new text
		xmlCursor.toFirstContentToken();
		xmlCursor.insertChars(text);

		// remove old text
		String result = xmlCursor.getChars();
		xmlCursor.removeChars(result.length());
		xmlCursor.dispose();

		return xmlObject;
	}

	public static void debugXmlCursor(XmlObject xmlObject) {
		XmlCursor xmlCursor = xmlObject.newCursor();
		while (!xmlCursor.toNextToken().isNone()) {
			System.out.println("debug "
					+ xmlCursor.currentTokenType().toString());
			if (xmlCursor.currentTokenType().equals(TokenType.START)) {
				System.out.println("debug "
						+ xmlCursor.getName().getLocalPart());
				System.out.println(xmlCursor.xmlText());
			}
		}
		xmlCursor.dispose();
	}

	/**
	 * Retrieve text on a mixed content element
	 * 
	 * @param xmlObject
	 *            to retrieve text from
	 * @return text
	 */
	public static String getTextOnMixedElement(XmlObject xmlObject) {
		XmlCursor xmlCursor = xmlObject.newCursor();
		// toLastAttribute does not skip namespaces - so continue
		// until none empty TEXT token
		xmlCursor.toLastAttribute();
		TokenType token = xmlCursor.toNextToken();
		if (token.equals(TokenType.END) || token.equals(TokenType.ENDDOC)) {
			return "";
		}

		String text = null;
		try {
			System.out.println(token);
			text = xmlCursor.getTextValue().trim();
		} catch (IllegalMonitorStateException e) {
			return "";
		}

		while (!token.equals(XmlCursor.TokenType.TEXT)
				|| (token.equals(XmlCursor.TokenType.TEXT) && text.length() == 0)) {
			token = xmlCursor.toNextToken();
			text = xmlCursor.getTextValue().trim();
		}
		xmlCursor.dispose();
		return text;
	}

	public static XmlObject getElementInElementStructure(String localName,
			XmlObject xmlObject) {
		if (localName == null || localName.equals("")) {
			return xmlObject;
		}

		XmlCursor xmlCursor = xmlObject.newCursor();
		XmlObject result = null;
		while (!xmlCursor.toNextToken().isNone()) {
			if (xmlCursor.currentTokenType().equals(TokenType.START)) {
				if (xmlCursor.getName().getLocalPart().equals(localName)) {
					result = xmlCursor.getObject();
					break;
				}
			}
		}
		xmlCursor.dispose();
		return result;
	}

	/**
	 * Check if a XmlObject is of type Document
	 * 
	 * @param xmlObject
	 *            to check for document type
	 * @param throwable
	 *            throwable reference to checking method
	 * @throws DDIFtpException
	 */
	public static void instanceOfXmlBeanDocument(XmlObject xmlObject,
			Throwable throwable) throws DDIFtpException {
		if (xmlObject.getClass().getName().indexOf("Document") <= -1) {
			DDIFtpException exception = new DDIFtpException("XmlBean: "
					+ xmlObject.getClass().getName() + " must be of type: "
					+ xmlObject.getDomNode().getLocalName() + "Document",
					throwable);
			throw exception;
		}
	}

	/**
	 * Generically retrieve a xml object type from a xml object document
	 * 
	 * @param xmlObject
	 *            xml object document
	 * @param throwable
	 *            of calling method
	 * @return xml object type
	 * @throws Exception
	 */
	public static XmlObject getXmlObjectTypeFromXmlDocument(
			XmlObject xmlObject, Throwable throwable) throws DDIFtpException {
		instanceOfXmlBeanDocument(xmlObject, throwable);

		// retrieve local name of node
		String localName = xmlObject.getClass().getName().substring(0,
				xmlObject.getClass().getName().indexOf("Document"));
		localName = localName.substring(localName.lastIndexOf(".") + 1);

		// retrieve xml object type
		try {
			return (XmlObject) ReflectionUtil.invokeMethod(xmlObject, "get"
					+ localName, false, null);
		} catch (Exception e) {
			throw new DDIFtpException("Error on xmlObjet get", e);
		}
	}

	public XmlObject getXmlObjectByXmlText(String xmlText, String elementName) {

		return null;
	}

	/**
	 * Get the value of an attribute by substringing whole xml text
	 * 
	 * @param xml
	 * @param attrQuery
	 *            defined as 'attibuteName="'
	 * @return attribute value
	 */
	public static String getXmlAttributeValue(String xml, String attrQuery) {
		// Hack, prop with xmlbeans to handle qNames on <xml-fragment ....>
		// xml objects. Fixed with simple string parsing.
		int index, start, end;
		index = xml.indexOf(attrQuery);
		// guard
		if (index == -1) {
			return null;
		}
		start = index + attrQuery.length();
		end = xml.indexOf("\"", start);
		String result = xml.substring(start, end);
		// guard
		if (result.equals("")) {
			return null;
		}
		return result;
	}

	/**
	 * Add translation attributes: lang, translated, translateable to DDI
	 * elements
	 * 
	 * @param <T>
	 *            return type
	 * @param xmlObject
	 *            DDI element
	 * @param lang
	 * @param translated
	 * @param translateable
	 * @return DDI element with added attributes
	 * @throws DDIFtpException
	 */
	public static <T extends XmlObject> T addTranslationAttributes(T xmlObject,
			String lang, boolean translated, boolean translateable)
			throws DDIFtpException {
		try {
			ReflectionUtil.invokeMethod(xmlObject, "setTranslated", false,
					translated);
			ReflectionUtil.invokeMethod(xmlObject, "setTranslatable", false,
					translateable);
			ReflectionUtil.invokeMethod(xmlObject, "setLang", false, lang);
		} catch (Exception e) {
			throw new DDIFtpException("Set translations args error on: {0}",
					new Object[] { xmlObject.getClass().getName() }, e);
		}
		return xmlObject;
	}

	/**
	 * Return the element with the chosen specified language to display
	 * 
	 * @param list
	 *            of elements to iterate
	 * @return selected element
	 */
	public static Object getDefaultLangElement(List<?> list)
			throws DDIFtpException {
		if (list == null) { // guard
			return null;
		}

		String defaultLang = Translator.getLocale().getLanguage();
		String tmpLang = null;
		Object defaultObj = null;
		Object fallback = null;
		for (Object obj : list) {
			tmpLang = getXmlAttributeValue(obj.toString(), "lang=\"");
			if (tmpLang == null) {
				if (fallback != null) {
					// if two or more elements do not have any lang attr
					// no choice is possible
					throw new DDIFtpException(
							"Two or more elements do not have any lang attr no choice is possible",
							new Throwable());
				}
				fallback = obj;
			}

			if (tmpLang != null && tmpLang.indexOf("en") > -1) {
				defaultObj = obj;
			}
			if (tmpLang != null && tmpLang.indexOf(defaultLang) > -1) {
				return obj;
			}
		}
		return (defaultObj == null ? fallback : defaultObj);
	}

	/**
	 * Defines if an element is of the chosen specified language to display
	 * 
	 * @param xmlObject
	 *            to check
	 * @return result
	 */
	public static boolean isDefaultLangElement(XmlObject xmlObject) {
		return Translator.getLocale().getLanguage().equals(
				getXmlAttributeValue(xmlObject.xmlText(), "lang=\""));

	}

	public static XmlObject getNotTranslated(List<?> items) {
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		XmlObject xmlObject = null;
		for (Object item : items) {
			if (!Boolean.parseBoolean(XmlBeansUtil.getXmlAttributeValue(
					((XmlObject) item).xmlText(xmlOptions), "translated=\""))) {
				xmlObject = (XmlObject) item;
				// break;
			}
		}
		return xmlObject;
	}
}
