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
* Copyright (C) CIRG, University of Washington, Seattle WA.  All Rights Reserved.
*
*/
package us.mn.state.health.lims.common.provider.query;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;

import us.mn.state.health.lims.address.dao.AddressPartDAO;
import us.mn.state.health.lims.address.dao.PersonAddressDAO;
import us.mn.state.health.lims.address.daoimpl.AddressPartDAOImpl;
import us.mn.state.health.lims.address.daoimpl.PersonAddressDAOImpl;
import us.mn.state.health.lims.address.valueholder.AddressPart;
import us.mn.state.health.lims.address.valueholder.PersonAddress;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.common.util.XMLUtil;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.util.PatientUtil;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.util.PatientIdentityTypeMap;
import us.mn.state.health.lims.patienttype.dao.PatientPatientTypeDAO;
import us.mn.state.health.lims.patienttype.daoimpl.PatientPatientTypeDAOImpl;
import us.mn.state.health.lims.patienttype.valueholder.PatientType;
import us.mn.state.health.lims.person.valueholder.Person;
import us.mn.state.health.lims.sample.dao.SearchResultsDAO;
import us.mn.state.health.lims.sample.daoimpl.SearchResultsDAOImp;

public class PatientSearchPopulateProvider extends BaseQueryProvider {
	private static PatientDAO patientDAO = new PatientDAOImpl();
	private static PersonAddressDAO addressDAO = new PersonAddressDAOImpl();
	private static String ADDRESS_PART_VILLAGE_ID;
	private static String ADDRESS_PART_COMMUNE_ID;
	private static String ADDRESS_PART_DEPT_ID;

	static{
		AddressPartDAO addressPartDAO = new AddressPartDAOImpl();
		List<AddressPart> partList = addressPartDAO.getAll();

		for( AddressPart addressPart : partList){
			if( "department".equals(addressPart.getPartName())){
				ADDRESS_PART_DEPT_ID = addressPart.getId();
			}else if( "commune".equals(addressPart.getPartName())){
				ADDRESS_PART_COMMUNE_ID = addressPart.getId();
			}else if( "village".equals(addressPart.getPartName())){
				ADDRESS_PART_VILLAGE_ID = addressPart.getId();
			}
		}
	}


	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		String nationalId = (String) request.getParameter("nationalID");
        String externalId = (String) request.getParameter("externalID");
		String patientKey = (String) request.getParameter("personKey");
		String stNumber = (String) request.getParameter("stNumber");
		StringBuilder xml = new StringBuilder();
		String result = null;
		if (nationalId != null) {
		    result = createSearchResultXML(patientDAO.getPatientByNationalId(nationalId), xml);
		} else if (externalId != null ) {
            result = createSearchResultXML(patientDAO.getPatientByExternalId(externalId), xml);
		} else if(stNumber != null) {
            List<Patient> patients = patientDAO.getPatientsByPatientIdentityValue(PatientIdentityTypeMap.getInstance().getIDForType("ST"), stNumber);
            if(patients.size() > 0) {
                result = createSearchResultXML(patients.get(0), xml);
            }

        } else {
            result = createSearchResultXML(getPatientForID(patientKey), xml);
		}
		if (!result.equals(VALID)) {
		    result = StringUtil.getMessageForKey("patient.message.patientNotFound");
		    xml.append("empty");
		}
		ajaxServlet.sendData(xml.toString(), result, request, response);
	}

	/**
	 * building the XML and the status return.
     * @return
     */
    private String createSearchResultXML(Patient patient, StringBuilder xml) {
        if (patient == null) {
            return INVALID;
        }
        createReturnXML(patient, xml);
        return VALID;
    }

	private Patient getPatientForID(String personKey) {

		Patient patient = new Patient();
		patient.setId(personKey);

		PatientDAO dao = new PatientDAOImpl();

		dao.getData(patient);
		if (patient.getId() == null)  {
		    return null;
		} else {
		    return patient;
		}
	}

	private void createReturnXML(Patient patient, StringBuilder xml) {
          new PatientXmlCreator().createXml(patient, xml);
	}





}
