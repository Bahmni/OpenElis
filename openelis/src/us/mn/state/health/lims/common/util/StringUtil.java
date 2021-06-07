/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is OpenELIS code.
 *
 * Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
 *
 * Contributor(s): CIRG, University of Washington, Seattle WA.
 */
package us.mn.state.health.lims.common.util;

import org.owasp.encoder.Encode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.validator.GenericValidator;

import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.common.util.ConfigurationProperties.Property;
import us.mn.state.health.lims.common.util.resources.ResourceLocator;

/**
 * @author diane benz
 *
 *         To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class StringUtil {


	private static final String COMMA = ",";
	private static final Character CHAR_COMA = ',';
	private static final Character CHAR_TIDDLE = '~';
	private static final String TIDDLE = "~";
	private static final String QUOTE = "\"";
	private static String STRING_KEY_SUFFIX = null;
	private static Pattern INTEGER_REG_EX = Pattern.compile("^-?\\d+$");

	// private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * bugzilla 2311 request parameter values coming back as string "null" when
	 * checked for isNullorNill in an Action class need to return true
	 */
	public static boolean isNullorNill(String string) {
		if (string == null || string.equals("") || string.equals("null")) {
			return true;
		}
		return false;
	}

	/**
	 * Search for tags in a String with oldValue tags and replace the tag with
	 * the newValue text.
	 *
	 * @param input
	 * @param oldValue
	 * @param newValue
	 * @return String of the text after replacement is completed
	 */
	public static String replace(String input, String oldValue, String newValue) {

		StringBuffer retValue = new StringBuffer();
		String retString = null;

		if (input != null) {
			// Set up work string. (Extra space is so substring will work
			// without
			// extra coding when a oldValue label appears at the very end.)
			String workValue = input + " ";
			int pos = 0;
			int pos_end = 0;

			// Loop through the original text while there are still oldValue
			// tags
			// to be processed.
			pos = workValue.indexOf(oldValue);
			while (pos >= 0) {
				// If the tag is not the first character take all the text up to
				// the tag and append it to the new value.
				if (pos > 0) {
					retValue.append(workValue.substring(0, pos));
				}

				// Find the closing marker for the tag
				pos_end = pos + (oldValue.length() - 1);

				// Put in the new value
				retValue.append(newValue);

				// Truncate the translated text off the front of the string and
				// continue
				workValue = workValue.substring(pos_end + 1);

				pos = workValue.indexOf(oldValue);

			}

			// Now append any remaining text in the work string.
			retValue.append(workValue);
			retString = retValue.toString().trim();
		}
		return retString;

	}

	public static String replaceNullWithEmptyString(String in) {
		return in == null ? " " : in;
	}

	public static String[] toArray(String str) {
		String retArr[];

		if (null == str) {
			retArr = new String[0];
		} else {
			StringTokenizer tokenizer = new StringTokenizer(str, COMMA);

			retArr = new String[tokenizer.countTokens()];

			// String token;
			int idx = 0;

			while (tokenizer.hasMoreTokens()) {
				retArr[idx] = tokenizer.nextToken().trim();
				idx++;
			}
		}

		return retArr;
	}

	public static String[] toArray(String str, String delimiter) {
		String retArr[];

		if (null == str) {
			retArr = new String[0];
		} else {
			StringTokenizer tokenizer = new StringTokenizer(str, delimiter);

			retArr = new String[tokenizer.countTokens()];

			// String token;
			int idx = 0;

			while (tokenizer.hasMoreTokens()) {
				retArr[idx] = tokenizer.nextToken().trim();
				idx++;
			}
		}

		return retArr;
	}

	// from format (999)999-9999 and ext to 999/999-9999.ext
	public static String formatPhone(String phone, String ext) throws LIMSRuntimeException {
		String returnPhone = null;
		if (phone != null) {
			try {
				String area = phone.substring(1, 4);
				String pre = phone.substring(5, 8);
				String post = phone.substring(9, 13);
				returnPhone = area + "/" + pre + "-" + post;
			} catch (Exception e) {
				// bugzilla 2154
				LogEvent.logError("StringUtil", "formatPhone()", e.toString());
			}

		}
		if (!StringUtil.isNullorNill(ext)) {
			returnPhone = returnPhone + "." + ext;
		}
		// System.out.println("This is phone " + returnPhone);
		return returnPhone;
	}

	// from format 999/999-9999.ext to (999)999-9999
	public static String formatPhoneForDisplay(String phone) throws LIMSRuntimeException {
		String returnPhone = null;
		if (phone != null) {
			try {
				String area = phone.substring(0, 3);
				String pre = phone.substring(4, 7);
				String post = phone.substring(8, 12);
				returnPhone = "(" + area + ")" + pre + "-" + post;
			} catch (Exception e) {
				LogEvent.logError("StringUtil", "formatPhoneForDisplay()", e.toString());
			}

		}

		return returnPhone;
	}

	// from format 999/999-9999.ext to ext
	public static String formatExtensionForDisplay(String phone) throws LIMSRuntimeException {
		String returnPhone = null;
		if (phone != null) {
			try {
				String ext = phone.substring(13);
				returnPhone = ext;
			} catch (Exception e) {
				LogEvent.logError("StringUtil", "formatExtensionForDisplay()", e.toString());
			}

		}

		return returnPhone;
	}

	// Returns true if string s is blank from position p to the end.
	public static boolean isRestOfStringBlank(String s, int p) {
		while (p < s.length() && Character.isWhitespace(s.charAt(p))) {
			p++;
		}
		return p >= s.length();
	}

	public static String convertStringToRegEx(String str) throws LIMSRuntimeException {
		try {
			String strArr[] = str.split("");
			StringBuffer sb = new StringBuffer();
			// discard first token
			for (int i = 1; i < strArr.length; i++) {
				sb.append("\\" + strArr[i]);
			}
			return sb.toString();
		} catch (Exception e) {
			LogEvent.logError("StringUtil", "convertStringToRegEx()", e.toString());
			throw new LIMSRuntimeException("Error converting string to regular expression ", e);
		}
	}

	public static String trim(String obj) throws LIMSRuntimeException {
		try {
			if (obj != null) {
				return obj.trim();
			}
			return "";
		} catch (Exception e) {
			// bugzilla 2154
			LogEvent.logError("StringUtil", "trim()", e.toString());
			throw new LIMSRuntimeException("Error trimming string ", e);
		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List loadListFromStringOfElements(String str, String textSeparator, boolean validate) throws Exception {
		List list = new ArrayList();
		String arr[] = str.split(textSeparator);

		for (int i = 0; i < arr.length; i++) {
			String element = arr[i];
			element = element.trim();
			if (validate && StringUtil.isNullorNill(element)) {
				throw new Exception("empty data");
			}
			list.add(element.trim());
		}
		return list;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List createChunksOfText(String text, int maxWidth, boolean observeSpaces) throws Exception {
		List list = new ArrayList();
		int indx = -1;
		while (text != null && text.length() > 0) {
			if (text.length() <= maxWidth) {
				list.add(text);
				text = "";
			} else {
				if (observeSpaces) {
					indx = text.indexOf(" ", maxWidth);
					if (indx >= 0) {
						list.add(text.substring(0, indx));
						text = text.substring(indx);
					} else {
						list.add(text);
						text = "";
					}
				} else {
					list.add(text.substring(0, maxWidth));
					text = text.substring(maxWidth);
				}
			}
		}
		return list;
	}

	public static String getMessageForKey(String messageKey) {
		if (null == messageKey) {
			return null;
		}
		String locale = SystemConfiguration.getInstance().getDefaultLocale().toString();
		return ResourceLocator.getInstance().getMessageResources().getMessage(new Locale(locale), messageKey);
	}

	public static String getMessageForKey(String messageKey, String arg) {
		if (null == messageKey) {
			return null;
		}

		String locale = SystemConfiguration.getInstance().getDefaultLocale().toString();

		return ResourceLocator.getInstance().getMessageResources().getMessage(new Locale(locale), messageKey, arg);
	}

	public static String getMessageForKey(String messageKey, String arg0, String arg1) {
		if (null == messageKey) {
			return null;
		}

		String locale = SystemConfiguration.getInstance().getDefaultLocale().toString();

		return ResourceLocator.getInstance().getMessageResources().getMessage(new Locale(locale), messageKey, arg0, arg1);
	}

	public static String getContextualMessageForKey(String messageKey) {
		if (null == messageKey) {
			return null;
		}

		// Note that if there is no suffix then the suffix key will be the same
		// as the message key
		// and the first search will be successful, there is no reason to test
		// for the suffix
		String suffixedKey = messageKey + getSuffix();

		String suffixedValue = getMessageForKey(suffixedKey);

		if (!GenericValidator.isBlankOrNull(suffixedValue)) {
			return suffixedValue;
		} else {
			return getMessageForKey(messageKey);
		}
	}

	private static String getSuffix() {
		if (STRING_KEY_SUFFIX == null) {
			STRING_KEY_SUFFIX = ConfigurationProperties.getInstance().getPropertyValue(Property.StringContext);
			if (!GenericValidator.isBlankOrNull(STRING_KEY_SUFFIX)) {
				STRING_KEY_SUFFIX = "." + STRING_KEY_SUFFIX.trim();
			}
		}

		return STRING_KEY_SUFFIX;
	}

	public static String getContextualKeyForKey(String key) {
		if (null == key) {
			return null;
		}

		// Note that if there is no suffix then the suffix key will be the same
		// as the message key
		// and the first search will be successful, there is no reason to test
		// for the suffix
		String suffixedKey = key + getSuffix();

		String suffixedValue = getMessageForKey(suffixedKey);

		return GenericValidator.isBlankOrNull(suffixedValue) ? key : suffixedKey;
	}

	/*
	 * No bounds checking for size
	 */
	public static boolean isInteger(String result) {
		return INTEGER_REG_EX.matcher(result).matches();
	}

	public static boolean textInCommaSeperatedValues(String target, String csv) {
		if (!GenericValidator.isBlankOrNull(csv)) {
			String[] seperatedText = csv.split(COMMA);

			for (String text : seperatedText) {
				if (text.trim().equals(target)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Limit the chars in a string to the simple Java identifiers, A-Z, a-z, $,
	 * _ etc. code taken from
	 * http://www.exampledepot.com/egs/java.lang/IsJavaId.html
	 *
	 * @see Character#isJavaIdentifierPart(char) etc. for details.
	 * @param s
	 *            - some string to test
	 * @return True => all is well
	 */
	public static boolean isJavaIdentifier(String s) {
		if (s.length() == 0 || !Character.isJavaIdentifierStart(s.charAt(0))) {
			return false;
		}
		for (int i = 1; i < s.length(); i++) {
			if (!Character.isJavaIdentifierPart(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * A realy dumb CSV column value escaper. It deals with imbedded commas and
	 * double quotes, that is all. Commas means the string needs quotes around
	 * it. Including a quote means we need to double up that character for
	 * Excell.
	 */
	public static String escapeCSVValue(String original) {
		// quotes
		StringBuilder out = new StringBuilder();
		if (GenericValidator.isBlankOrNull(original) || !original.contains(QUOTE) && !original.contains(COMMA)) {
			return original;
		}
		out.append(QUOTE);
		for (int i = 0; i < original.length(); i++) {
			Character c = original.charAt(i);
			if (c == '"') {
				out.append('"');
			}
			out.append(c);
		}
		out.append(QUOTE);
		return out.toString();
	}

	/**
	 * Solves the problem of how to deal with commas within quoted strings for csv.  I couldn't figure out a regex that would cover
	 * it so we're doing it the hard way.  It will stumble if '~' is embedded in the string.  This will fail on mixed fields such as
	 * 1,2,"something, else", 4,5
	 *
	 */
	public static String[] separateCSVWithEmbededQuotes(String line){

		String[] breakOnQuotes = line.split(QUOTE);

		StringBuffer substitutedString = new StringBuffer();
		for( String subString : breakOnQuotes){
			if(subString.startsWith(COMMA)){
				substitutedString.append( subString.replace(CHAR_COMA, CHAR_TIDDLE));
			}else{
				substitutedString.append(QUOTE);
				substitutedString.append(subString);
				substitutedString.append(QUOTE);
			}
		}

		return substitutedString.toString().split(TIDDLE);
	}

	/**
	 * Similar to separateCSVWithEmbededQuotes(String line) but deals with mixed fields
	 * i.e. 1,2,"something, else", 4,5 , "more of that thing", 8
	 *
	 *
	 */
	public static String[] separateCSVWithMixedEmbededQuotes(String line){

		String[] breakOnQuotes = line.split(QUOTE);

		StringBuffer substitutedString = new StringBuffer();
		for( String subString : breakOnQuotes){
			if(subString.startsWith(COMMA) || subString.endsWith(COMMA)){
				substitutedString.append( subString.replace(CHAR_COMA, CHAR_TIDDLE));
			}else{
				substitutedString.append(QUOTE);
				substitutedString.append(subString);
				substitutedString.append(QUOTE);
			}
		}

		return substitutedString.toString().split(TIDDLE);
	}

	/**
	 * Compare two strings returning the appropriate -1,0,1; but deal with
	 * possible null strings which will compare the same as "", aka before any
	 * other string.
	 *
	 * @author pahill
	 * @param left
	 * @param right
	 * @return -1 if left is lexically less then right; 0 if they are equal; 1
	 *         if left is lexically greater than right.
	 */
	public static int compareWithNulls(String left, String right) {
		if (left == null) {
			left = "";
		}
		if (right == null) {
			right = "";
		}
		return left.compareTo(right);
	}

	public static String replaceAllChars(String text, char replacement) {
		if (text == null) {
			return null;
		}

		StringBuilder boringString = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			boringString.append(replacement);
		}

		return boringString.toString();
	}

	public static boolean containsOnly(String text, char target) {
		if (text == null) {
			return false;
		}

		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) != target) {
				return false;
			}
		}

		return true;
	}

	public static String strip(String string, String toBeRemoved){

		if( string.contains(toBeRemoved)){
			String[] subStrings = string.trim().split(toBeRemoved);

			StringBuffer reconsituted = new StringBuffer();

			for( String subString : subStrings){
				reconsituted.append(subString);
			}

			return reconsituted.toString();
		}else{
			return string;
		}
	}

	public static String blankIfNull(String string) {
		return string == null ? "" : string;
	}

	public static String ellipsisString( String text, int ellipsisAt){
		if( text.length() > ellipsisAt){
			return text.substring(0, ellipsisAt) + "...";
		}else{
			return text;
		}
	}

	public static String join(Collection<String> collection, String separator){
		String constructed = "";
		if( collection.isEmpty()){
			return constructed;
		}

		for (String item : collection) {
			constructed += item + separator;
		}

		return constructed.substring(0, constructed.length() - separator.length());
	}

	public static String encode(String value) {
		return value != null ? Encode.forHtml(value) : null;
	}

	public static Object encode(Object o) {
		if (o instanceof String) {
			return Encode.forHtml((String) o);
		}
		return o;
	}
}
