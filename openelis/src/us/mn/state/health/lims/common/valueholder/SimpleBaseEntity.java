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

package us.mn.state.health.lims.common.valueholder;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This interface provides the most rudimentary interface to entities (DB Rows) in OpenElis.
 * If an object implements this interface then we can use a generic DAO implementation to read/write/update
 * and properly track changes.  The changes are tracked as appropriate in the application, DB logs and the entity itself. 
 * @author pahill
 *
 * @param <T>  the class of the index
 */

public interface SimpleBaseEntity<T> extends Serializable {
	
	T getId();
	void setId(T key);
	
	Timestamp getLastupdated();
	void setLastupdated(Timestamp lastupdated);
	
	void setSysUserId(String sysUserId);
	public String getSysUserId();
}
