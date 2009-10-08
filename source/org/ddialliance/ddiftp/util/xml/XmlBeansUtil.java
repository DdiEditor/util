package org.ddialliance.ddiftp.util.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
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
			System.out.println("debug " + xmlCursor.toString());
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
		xmlCursor.toLastAttribute();
		xmlCursor.toNextToken();
		String result = xmlCursor.getChars();
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
}
