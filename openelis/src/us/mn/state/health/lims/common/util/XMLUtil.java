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
*/

package us.mn.state.health.lims.common.util;

import org.apache.commons.validator.GenericValidator;

public class XMLUtil {

	public static void appendKeyValue(String key, String value, StringBuilder xml) {

		if (!GenericValidator.isBlankOrNull(value)) {
			value = value.trim();

			if (value.length() > 0) {
				xml.append("<");
				xml.append(key);
				xml.append(">");
				xml.append(value);
				xml.append("</");
				xml.append(key);
				xml.append(">");
			}
		}
	}
	
	public static void appendKeyValueAttribute(String key, String value, StringBuilder xml) {

		if (!GenericValidator.isBlankOrNull(value)) {
			value = value.trim();

			if (value.length() > 0) {
				xml.append(key);
				xml.append("=\"");
				xml.append(value);
				xml.append("\" ");
			}
		}
	}
}
