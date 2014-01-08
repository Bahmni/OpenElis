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

package org.bahmni.feed.openelis.externalreference.dao;

import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import us.mn.state.health.lims.common.dao.BaseDAO;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;

public interface ExternalReferenceDao extends BaseDAO {

    ExternalReference getData(String externalReferenceId, String category) throws LIMSRuntimeException;

    ExternalReference getDataByItemId(String itemId, String category) throws LIMSRuntimeException;

    boolean insertData(ExternalReference externalReference) throws LIMSRuntimeException;

    void deleteData(ExternalReference data) throws LIMSRuntimeException;
}
