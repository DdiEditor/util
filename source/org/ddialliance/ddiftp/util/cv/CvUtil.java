package org.ddialliance.ddiftp.util.cv;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlCursor.TokenType;
import org.apache.xmlbeans.XmlObject;
import org.ddialliance.ddi3.xml.xmlbeans.reusable.CodeValueType;
import org.ddialliance.ddiftp.util.DDIFtpException;
import org.ddialliance.ddiftp.util.xml.XmlBeansUtil;
import org.oasisOpen.docs.codelist.ns.genericode.AnyOtherContent;
import org.oasisOpen.docs.codelist.ns.genericode.CodeListDocument;
import org.oasisOpen.docs.codelist.ns.genericode.Row;
import org.oasisOpen.docs.codelist.ns.genericode.Value;

public class CvUtil {
	public static String DDA_CV_PATH = ".dda.dk-";

	public static String CODE_COLUMN_REF = "code";
	public static String DESCRIPTION_COLUMN_REF = "description";
	public static String DDA_CODE_COLUMN_REF = "ddacode";

	public static CodeListDocument loadInCvFromFile(File file)
			throws DDIFtpException {
		if (!file.exists()) {
			throw new DDIFtpException("File not found: "
					+ file.getAbsolutePath(), new Throwable());
		}
		CodeListDocument codeListDoc = null;
		try {
			codeListDoc = CodeListDocument.Factory.parse(file);
		} catch (Exception e) {
			throw new DDIFtpException("Parse error on: "
					+ file.getAbsolutePath(), e);
		}
		return codeListDoc;
	}

	public static CodeListDocument getDdaCv(DdaCv ddaCv) throws DDIFtpException {
		String path = "resources" + File.separator + "cv" + File.separator
				+ ddaCv.getName() + DDA_CV_PATH + ddaCv.getVersion() + ".cv";
		File file = new File(path);
		return loadInCvFromFile(file);
	}

	public static CodeValueType setCvOnCodeValue(CodeListDocument cv,
			CodeValueType codeValue, String value) {
		// code value
		if (value != null) {
			XmlBeansUtil.setTextOnMixedElement(codeValue, value);
		}

		// code list agency id
		codeValue.setCodeListAgencyName(cv.getCodeList().getIdentification()
				.getAgency().getIdentifierList().get(0).getStringValue());

		// code list id
		codeValue.setCodeListID(cv.getCodeList().getIdentification()
				.getShortName().getStringValue());

		// code list version
		codeValue.setCodeListVersionID(cv.getCodeList().getIdentification()
				.getVersion());

		// code scheme urn
		codeValue
				.setCodeListSchemeURN("http://docs.oasis-open.org/codelist/ns/genericode/1.0/");

		// code list canonical version uri
		codeValue.setCodeListURN(cv.getCodeList().getIdentification()
				.getCanonicalVersionUri());

		return codeValue;
	}

	public static Row getRowByIdCode(CodeListDocument cv, String codeValue) {
		String keyCode = defineKeyCode(cv);
		return getRowByIdCode(cv, keyCode, codeValue);
	}

	public static Row getRowByIdCode(CodeListDocument cv, String columnRef,
			String codeValue) {
		for (Row row : cv.getCodeList().getSimpleCodeList().getRowList()) {
			for (Value value : row.getValueList()) {
				if (value.getColumnRef().equals(columnRef)) {
					if (value.getSimpleValue().getStringValue()
							.equals(codeValue)) {
						return row;
					}
				}
			}
		}

		return null;
	}

	public static String defineKeyCode(CodeListDocument cv) {
		return cv.getCodeList().getColumnSet().getKeyList().get(0)
				.getColumnRefList().get(0).getRef();
	}

	public static Value getValueByRowId(Row row, String id) {
		for (Value value : row.getValueList()) {
			if (value.getColumnRef().equals(id)) {
				return value;
			}
		}
		return null;
	}

	public static Map<String, String> getComplexValues(Value value) {
		Map<String, String> result = new HashMap<String, String>();
		AnyOtherContent any = value.getComplexValue();
		if (any == null) {
			return result;
		}
		XmlCursor xmlCursor = any.newCursor();
		while (!xmlCursor.toNextToken().isNone()) {
			if (xmlCursor.currentTokenType().equals(TokenType.START)) {
				QName qName = xmlCursor.getName();
				if (!qName.getLocalPart().equals("Value")
						|| !qName.getPrefix().equals("ddi-cv")) {
					break;
				}
				getChildValues(xmlCursor.getObject(), result);
			}
		}
		xmlCursor.dispose();

		return result;
	}

	private static void getChildValues(XmlObject xmlObject,
			Map<String, String> m) {
		String attr = XmlBeansUtil.getXmlAttributeValue(xmlObject.xmlText(),
				"xml:lang=\"");
		String value = XmlBeansUtil.getTextOnMixedElement(xmlObject);
		m.put(attr, value);
	}

	/**
	 * Scrubs leading '0' return from journal database
	 * 
	 * @param value
	 *            to scrub
	 * @return scrubed value
	 */
	public static String scrubDbCvValue(String value) {
		if (value.startsWith("0")) {
			value = value.substring(1);
		}
		return value;
	}
}
