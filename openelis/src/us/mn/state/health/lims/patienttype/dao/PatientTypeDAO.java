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

/**
 * Project : LIS<br>
 * File name : PatientTypeDAO.java<br>
 * Description : 
 * @author TienDH
 * @date Aug 20, 2007
 */
package us.mn.state.health.lims.patienttype.dao;

import java.util.List;
import us.mn.state.health.lims.common.dao.BaseDAO;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.patienttype.valueholder.PatientType;


public interface PatientTypeDAO extends BaseDAO {

	public boolean insertData(PatientType patientType) throws LIMSRuntimeException;

	public void deleteData(List patientType) throws LIMSRuntimeException;

	public List getAllPatientTypes() throws LIMSRuntimeException;

	public List getPageOfPatientType(int startingRecNo) throws LIMSRuntimeException;

	public void getData(PatientType patientType) throws LIMSRuntimeException;

	public void updateData(PatientType patientType) throws LIMSRuntimeException;

	public List getPatientTypes(String filter) throws LIMSRuntimeException;

	public List getNextPatientTypeRecord(String id) throws LIMSRuntimeException;

	public List getPreviousPatientTypeRecord(String id) throws LIMSRuntimeException;

	public PatientType getPatientTypeByName(PatientType patientType) throws LIMSRuntimeException;
	
	public Integer getTotalPatientTypeCount() throws LIMSRuntimeException; 
}
