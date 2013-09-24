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

package us.mn.state.health.lims.patientidentity.dao;

import java.util.List;

import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;

public interface PatientIdentityDAO {

    public boolean insertData(PatientIdentity patientIdentity) throws LIMSRuntimeException;

    public void updateData(PatientIdentity patientIdentity) throws LIMSRuntimeException;

    public void delete(String patientIdentityId, String activeUserId) throws LIMSRuntimeException;

    public List<PatientIdentity> getPatientIdentitiesForPatient( String id )throws LIMSRuntimeException;

    public List<PatientIdentity> getPatientIdentitiesByValueAndType( String value, String identityType )throws LIMSRuntimeException;

	public PatientIdentity getPatitentIdentityForPatientAndType(String patientId, String identityTypeId) throws LIMSRuntimeException;
}
