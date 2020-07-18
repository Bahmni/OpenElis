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
package us.mn.state.health.lims.typeofteststatus.dao;

import us.mn.state.health.lims.common.dao.BaseDAO;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.typeofteststatus.valueholder.TypeOfTestStatus;


import java.util.List; 

/**
 * @author Srivathsala 
 */
public interface TypeOfTestStatusDAO extends BaseDAO {

	public boolean insertData(TypeOfTestStatus typeOfTestStatus) throws LIMSRuntimeException;

	public void deleteData(List typeOfTestStatuss) throws LIMSRuntimeException;

	public List getAllTypeOfTestStatus() throws LIMSRuntimeException;

	public List getPageOfTypeOfTestStatus(int startingRecNo) throws LIMSRuntimeException;

	public void getData(TypeOfTestStatus typeOfTestStatus) throws LIMSRuntimeException;

	public void updateData(TypeOfTestStatus typeOfTestStatus) throws LIMSRuntimeException;
	
	public List getNextTypeOfTestStatusRecord(String id) throws LIMSRuntimeException;

	public List getPreviousTypeOfTestStatusRecord(String id) throws LIMSRuntimeException;
	
	public Integer getTotalTypeOfTestStatusCount() throws LIMSRuntimeException;
	
	public TypeOfTestStatus getTypeOfTestStatusByStatusType(TypeOfTestStatus typeOfTestStatus) throws LIMSRuntimeException;

    public TypeOfTestStatus getTypeOfTestStatusByStatusType(String statusType) throws LIMSRuntimeException;

	public TypeOfTestStatus getTypeOfTestStatusByStatusName(String typeOfTestStatusName) throws LIMSRuntimeException;

    public TypeOfTestStatus getTypeOfTestStatusById(String totsId) throws LIMSRuntimeException;
    
    public List getAllActiveTestStatus() throws LIMSRuntimeException;
    
 }
