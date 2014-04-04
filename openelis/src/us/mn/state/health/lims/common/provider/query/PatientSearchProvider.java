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
package us.mn.state.health.lims.common.provider.query;

import org.apache.commons.validator.GenericValidator;
import us.mn.state.health.lims.common.provider.query.workerObjects.PatientSearchLocalAndClinicWorker;
import us.mn.state.health.lims.common.provider.query.workerObjects.PatientSearchLocalWorker;
import us.mn.state.health.lims.common.provider.query.workerObjects.PatientSearchWorker;
import us.mn.state.health.lims.common.servlet.validation.AjaxServlet;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.ConfigurationProperties.Property;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PatientSearchProvider extends BaseQueryProvider {

	protected AjaxServlet ajaxServlet = null;

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String lastName = request.getParameter("lastName");
		String firstName = request.getParameter("firstName");
		String middleName = request.getParameter("middleName");
		String STNumber = request.getParameter("STNumber");
		String subjectNumber = request.getParameter("subjectNumber"); //N.B. This is a bad name, it is other than STnumber
		String nationalID = request.getParameter("nationalID");
		String labNumber = request.getParameter("labNumber");
		String patientID = null;

		// if there is no subject number and there is a lab number then get the
		// subject number from the labNumber
		if (GenericValidator.isBlankOrNull(nationalID) && !GenericValidator.isBlankOrNull(labNumber)) {
			Patient patient = getPatientForLabNumber(labNumber);
			nationalID = getSubjectNumber(patient);
			// but if we find a blank nationalID, we can explicitly use the
			// patient ID (PK)
			if (GenericValidator.isBlankOrNull(nationalID)) {
				patientID = patient.getId();
			}
		}

		StringBuilder xml = new StringBuilder();

		PatientSearchWorker worker = getAppropriateWorker();
		String result = VALID;

		if (worker != null) {
			result = worker.createSearchResultXML(lastName, firstName, middleName, STNumber, subjectNumber, nationalID, patientID, xml);
		} else {
			result = INVALID;
			xml.append("System is not configured correctly for searching for patients. Contact Administrator");
		}

		ajaxServlet.sendData(xml.toString(), result, request, response);

	}

	private Patient getPatientForLabNumber(String labNumber) {

		SampleDAO sampleDAO = new SampleDAOImpl();
		Sample sample = sampleDAO.getSampleByAccessionNumber(labNumber);

		if (sample != null && !GenericValidator.isBlankOrNull(sample.getId())) {
			SampleHumanDAO sampleHumanDAO = new SampleHumanDAOImpl();
			return sampleHumanDAO.getPatientForSample(sample);
		}

		return new Patient();
	}

	private String getSubjectNumber(Patient patient) {
		if (patient == null) {
			return null;
		}
		if (GenericValidator.isBlankOrNull(patient.getNationalId())) {
			return patient.getExternalId();
		} else {
			return patient.getNationalId();
		}
	}

	private PatientSearchWorker getAppropriateWorker() {
        return shouldGetExternalPatientInfo()? new PatientSearchLocalWorker(): new PatientSearchLocalAndClinicWorker();
	}

    private boolean shouldGetExternalPatientInfo() {
        return "false".equals(ConfigurationProperties.getInstance().getPropertyValueLowerCase(Property.UseExternalPatientInfo));
    }

    @Override
	public void setServlet(AjaxServlet as) {
		this.ajaxServlet = as;
	}

	@Override
	public AjaxServlet getServlet() {
		return this.ajaxServlet;
	}

}
