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
 * Copyright (C) ITECH, University of Washington, Seattle WA.  All Rights Reserved.
 *
 */
package us.mn.state.health.lims.qaevent.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessages;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;

import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.formfields.FormFields;
import us.mn.state.health.lims.common.services.QAService;
import us.mn.state.health.lims.common.services.QAService.QAObservationType;
import us.mn.state.health.lims.common.services.QAService.QAObservationValueType;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.validator.ActionError;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.note.dao.NoteDAO;
import us.mn.state.health.lims.note.daoimpl.NoteDAOImpl;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.observationhistory.dao.ObservationHistoryDAO;
import us.mn.state.health.lims.observationhistory.daoimpl.ObservationHistoryDAOImpl;
import us.mn.state.health.lims.observationhistory.valueholder.ObservationHistory;
import us.mn.state.health.lims.observationhistory.valueholder.ObservationHistory.ValueType;
import us.mn.state.health.lims.organization.dao.OrganizationDAO;
import us.mn.state.health.lims.organization.dao.OrganizationOrganizationTypeDAO;
import us.mn.state.health.lims.organization.daoimpl.OrganizationDAOImpl;
import us.mn.state.health.lims.organization.daoimpl.OrganizationOrganizationTypeDAOImpl;
import us.mn.state.health.lims.organization.daoimpl.OrganizationTypeDAOImpl;
import us.mn.state.health.lims.organization.valueholder.Organization;
import us.mn.state.health.lims.organization.valueholder.OrganizationType;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.util.PatientUtil;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.person.dao.PersonDAO;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.person.valueholder.Person;
import us.mn.state.health.lims.project.dao.ProjectDAO;
import us.mn.state.health.lims.project.daoimpl.ProjectDAOImpl;
import us.mn.state.health.lims.project.valueholder.Project;
import us.mn.state.health.lims.provider.dao.ProviderDAO;
import us.mn.state.health.lims.provider.daoimpl.ProviderDAOImpl;
import us.mn.state.health.lims.provider.valueholder.Provider;
import us.mn.state.health.lims.qaevent.action.retroCI.NonConformityAction;
import us.mn.state.health.lims.qaevent.dao.QaObservationDAO;
import us.mn.state.health.lims.qaevent.daoimpl.QaObservationDAOImpl;
import us.mn.state.health.lims.qaevent.valueholder.QaObservation;
import us.mn.state.health.lims.qaevent.valueholder.retroCI.QaEventItem;
import us.mn.state.health.lims.requester.dao.SampleRequesterDAO;
import us.mn.state.health.lims.requester.daoimpl.SampleRequesterDAOImpl;
import us.mn.state.health.lims.requester.valueholder.SampleRequester;
import us.mn.state.health.lims.sample.action.SamplePatientEntrySaveAction;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.sampleproject.dao.SampleProjectDAO;
import us.mn.state.health.lims.sampleproject.daoimpl.SampleProjectDAOImpl;
import us.mn.state.health.lims.sampleproject.valueholder.SampleProject;
import us.mn.state.health.lims.sampleqaevent.dao.SampleQaEventDAO;
import us.mn.state.health.lims.sampleqaevent.daoimpl.SampleQaEventDAOImpl;
import us.mn.state.health.lims.sampleqaevent.valueholder.SampleQaEvent;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.OrderStatus;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.SampleStatus;
import us.mn.state.health.lims.systemuser.dao.SystemUserDAO;
import us.mn.state.health.lims.systemuser.daoimpl.SystemUserDAOImpl;
import us.mn.state.health.lims.systemuser.valueholder.SystemUser;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;

public class NonConformityUpdateWorker {

	private Sample sample;
	private SampleHuman sampleHuman;
	private Patient patient;
	private ObservationHistory doctorObservation;
	private ObservationHistory serviceObservation;
	private Map<String, SampleItem> sampleItemsByType = new HashMap<String, SampleItem>();

	private List<SampleQaEvent> sampleQAEventInsertList;
	private List<SampleQaEvent> sampleQAEventDeleteList;
	private Map<QaObservation, SampleQaEvent> qaObservationList;
	private List<NoteSet> insertableNotes;
	private List<Note> updateableNotes;
	private List<Note> deleteableNotes;
	private SystemUser systemUser;
	private SampleProject sampleProject;

	private boolean insertPatient = false;
	private boolean insertDoctorObservation = false;
	private boolean insertServiceObservation = false;
	private boolean insertSampleItems = false;
	private boolean insertSampleProject = false;
	private boolean insertSampleRequester = false;

	private boolean updatePatient = false;
	private boolean useFullProviderInfo;
	private boolean updateSampleHuman;

	public static final String NOTE_TYPE = "I";
	public static final String NOTE_SUBJECT = "QaEvent Note";
	private static String REFERRING_ORG_TYPE_ID;

	private ObservationHistoryDAO observationDAO = new ObservationHistoryDAOImpl();
	private SampleDAO sampleDAO = new SampleDAOImpl();
	private PatientDAO patientDAO = new PatientDAOImpl();
	private SampleHumanDAO sampleHumanDAO = new SampleHumanDAOImpl();
	private SampleItemDAO sampleItemDAO = new SampleItemDAOImpl();
	private NoteDAO noteDAO = new NoteDAOImpl();
	private SampleQaEvent sampleQaEvent;
	private SampleQaEventDAO sampleQaEventDAO = new SampleQaEventDAOImpl();
	private SampleRequester sampleRequester = null;
	private SampleRequesterDAO sampleRequesterDAO = new SampleRequesterDAOImpl();
	private TypeOfSampleDAO typeOfSampleDAO = new TypeOfSampleDAOImpl();
	private QaObservationDAO qaObservationDAO = new QaObservationDAOImpl();
	private OrganizationDAO orgDAO = new OrganizationDAOImpl();
	private OrganizationOrganizationTypeDAO orgOrgTypeDAO = new OrganizationOrganizationTypeDAOImpl();
	private Provider provider;
	private Person providerPerson;
	private boolean insertProvider = false;
	private ActionErrors errors = null;
	private Organization newOrganization= null;
	private boolean insertNewOrganizaiton = false;

	private final NonConformityUpdateData webData;
	private final static boolean REJECT_IF_EMPTY = true;

	static{
		OrganizationType orgType = new OrganizationTypeDAOImpl().getOrganizationTypeByName("referring clinic");
		if (orgType != null) {
			REFERRING_ORG_TYPE_ID = orgType.getId();
		}
	}
	
	public NonConformityUpdateWorker(NonConformityUpdateData data) {
		webData = data;
	}

	public String update() {
		useFullProviderInfo = FormFields.getInstance().useField(FormFields.Field.QAFullProviderInfo);

		createSystemUser();
		clearMembers();

		boolean isNewSample = GenericValidator.isBlankOrNull(webData.getSampleId());

		sampleDAO = new SampleDAOImpl();
		if (isNewSample) {
			createNewArtifacts();
		} else {
			updateArtifacts();
		}

		try {
			if (insertPatient) {
				patientDAO.insertData(patient);
			} else if (updatePatient) {
				patientDAO.updateData(patient);
			}

			if (insertProvider) {
				persistProviderData();
			}

			if (isNewSample) {
				sampleDAO.insertDataWithAccessionNumber(sample);

				sampleHuman.setPatientId(patient.getId());
				sampleHuman.setSampleId(sample.getId());
				sampleHuman.setProviderId((provider == null) ? null : provider.getId());
				sampleHumanDAO.insertData(sampleHuman);
			} else {
				sampleDAO.updateData(sample);
			}

			if (updateSampleHuman) {
				sampleHuman.setProviderId(provider.getId());
				sampleHuman.setSampleId(sample.getId());
				sampleHuman.setPatientId(patient.getId());
				sampleHuman.setSysUserId(webData.getCurrentSysUserId());
				sampleHumanDAO.updateData(sampleHuman);
			}

			if (insertSampleItems) {
				for (SampleItem si : sampleItemsByType.values()) {
					sampleItemDAO.insertData(si);
				}
			}

			if (insertSampleProject) {
				SampleProjectDAO sampleProjectDAO = new SampleProjectDAOImpl();
				sampleProjectDAO.insertData(sampleProject);
			}

			if (insertDoctorObservation) {
				doctorObservation.setPatientId(patient.getId());
				doctorObservation.setSampleId(sample.getId());
				observationDAO.insertData(doctorObservation);
			}

			if (insertServiceObservation) {
				serviceObservation.setPatientId(patient.getId());
				serviceObservation.setSampleId(sample.getId());
				observationDAO.insertData(serviceObservation);
			}

			if(insertNewOrganizaiton){
				orgDAO.insertData(newOrganization);
				orgOrgTypeDAO.linkOrganizationAndType(newOrganization, REFERRING_ORG_TYPE_ID);
			}
			
			if (insertSampleRequester) {
				if(insertNewOrganizaiton){
					sampleRequester.setRequesterId(newOrganization.getId());
				}
				sampleRequester.setSampleId(sample.getId());
				sampleRequesterDAO.insertData(sampleRequester);
			}

			for (SampleQaEvent event : sampleQAEventInsertList) {
				event.setSample(sample);
				sampleQaEventDAO.insertData(event);
			}

			for (NoteSet noteSet : insertableNotes) {
				if (noteSet.referencedEvent != null) {
					noteSet.note.setReferenceId(noteSet.referencedEvent.getId());
				} else if (noteSet.referencedSample != null) {
					noteSet.note.setReferenceId(noteSet.referencedSample.getId());
				} else {
					continue;
				}
				noteDAO.insertData(noteSet.note);
			}

			for (Note note : updateableNotes) {
				noteDAO.updateData(note);
			}

			noteDAO.deleteData(deleteableNotes);
			sampleQaEventDAO.deleteData(sampleQAEventDeleteList);

			for (QaObservation qa : qaObservationList.keySet()) {
				if (qa.getId() == null) {
					qa.setObservedId(qaObservationList.get(qa).getId());
					qaObservationDAO.insertData(qa);
				} else {
					qaObservationDAO.updateData(qa);
				}
			}

		} catch (LIMSRuntimeException lre) {

			ActionError error = null;
			if (lre.getException() instanceof StaleObjectStateException) {
				error = new ActionError("errors.OptimisticLockException", null, null);
			} else {
				lre.printStackTrace();
				error = new ActionError("errors.UpdateException", null, null);
			}

			errors = new ActionErrors();
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);

			return IActionConstants.FWD_FAIL;
		}

		return IActionConstants.FWD_SUCCESS;
	}

	public ActionErrors getErrors() {
		return errors;
	}

	private void createSystemUser() {
		systemUser = new SystemUser();
		systemUser.setId(webData.getCurrentSysUserId());
		SystemUserDAO systemUserDAO = new SystemUserDAOImpl();
		systemUserDAO.getData(systemUser);
	}

	private void clearMembers() {
		sample = null;
		sampleHuman = null;
		patient = null;
		doctorObservation = null;
		serviceObservation = null;
		sampleItemsByType = new HashMap<String, SampleItem>();
		sampleQAEventInsertList = new ArrayList<SampleQaEvent>();
		sampleQAEventDeleteList = new ArrayList<SampleQaEvent>();
		qaObservationList = new HashMap<QaObservation, SampleQaEvent>();
		insertableNotes = new ArrayList<NoteSet>();
		updateableNotes = new ArrayList<Note>();
		deleteableNotes = new ArrayList<Note>();
		sampleProject = null;

		insertPatient = false;
		insertDoctorObservation = false;
		insertServiceObservation = false;
		insertSampleItems = false;
		insertSampleProject = false;
		insertSampleRequester = false;
		updateSampleHuman = false;
	}

	private void createNewArtifacts() {
		sample = new Sample();
		sample.setSysUserId(webData.getCurrentSysUserId());
		sample.setAccessionNumber(webData.getLabNo());
		sample.setDomain("H");
		sample.setEnteredDate(DateUtil.convertStringDateToTimestamp(getCompleteDateTime()));
		sample.setReceivedDate(DateUtil.convertStringDateToTimestamp(getCompleteDateTime()));
		sample.setStatusId(StatusOfSampleUtil.getStatusID(OrderStatus.Entered));

		sampleHuman = new SampleHuman();
		sampleHuman.setSysUserId(webData.getCurrentSysUserId());

		if (!(GenericValidator.isBlankOrNull(webData.getProjectId()) || webData.getProjectId().equals("0"))) {
			insertSampleProject = true;
			sampleProject = new SampleProject();

			Project project = new Project();
			project.setId(webData.getProjectId());

			ProjectDAO projectDAO = new ProjectDAOImpl();
			projectDAO.getData(project);
			sampleProject.setProject(project);
			sampleProject.setSample(sample);
			sampleProject.setSysUserId(webData.getCurrentSysUserId());
		}

		setPatient();
		addDoctorIfNeeded();
		addServiceIfNeeded();
		addAllSampleQaEvents();
		addNoteToSampleIfNeeded();
	}

	private String getCompleteDateTime() {
		String receivedDateTime = webData.getReceivedDate();
		if (!GenericValidator.isBlankOrNull(webData.getReceivedTime())) {
			receivedDateTime += " " + webData.getReceivedTime();
		}else{
			receivedDateTime += " 00:00";
		}
		return receivedDateTime;
	}

	private void updateArtifacts() {
		sample = new Sample();
		sample.setId(webData.getSampleId());
		sampleDAO.getData(sample);
		sample.setSysUserId(webData.getCurrentSysUserId());

		sampleHuman = new SampleHuman();
		sampleHuman.setSampleId(sample.getId());
		sampleHumanDAO.getDataBySample(sampleHuman);

		String projectId = webData.getProjectId();
		if (!GenericValidator.isBlankOrNull(projectId)) {
			if (!(GenericValidator.isBlankOrNull(projectId) || projectId.equals("0"))) {
				insertSampleProject = true;
				sampleProject = new SampleProject();

				Project project = new Project();
				project.setId(projectId);

				ProjectDAO projectDAO = new ProjectDAOImpl();
				projectDAO.getData(project);
				sampleProject.setProject(project);
				sampleProject.setSample(sample);
				sampleProject.setSysUserId(webData.getCurrentSysUserId());
			}
		} else {
			insertSampleProject = false;
			sampleProject = null;
		}

		// case for being able to add subject
		if (webData.getNewSubject() && !GenericValidator.isBlankOrNull(webData.getSubjectNo())) { // number
			updatePatient = true;
			patient = sampleHumanDAO.getPatientForSample(sample);
			patient.setSysUserId(webData.getCurrentSysUserId());
			patient.setNationalId(webData.getSubjectNo());
		} else {
			setPatient();
		}

		addDoctorIfNeeded();
		addServiceIfNeeded();
		addAllSampleQaEvents();
		addNoteToSampleIfNeeded();
	}

	private void persistProviderData() {
		if (providerPerson != null) {
			PersonDAO personDAO = new PersonDAOImpl();
			providerPerson.setSysUserId(webData.getCurrentSysUserId());
			personDAO.insertData(providerPerson);
		}
		if (provider != null) {
			ProviderDAO providerDAO = new ProviderDAOImpl();
			provider.setSysUserId(webData.getCurrentSysUserId());
			if (providerPerson != null) {
				provider.setPerson(providerPerson);
			}
			providerDAO.insertData(provider);
		}
	}


	/**
	 * This is for when patients can not be added through the form
	 * 
	 * @param dynaForm
	 */
	private void setPatient() {

		if (!GenericValidator.isBlankOrNull(webData.getPatientId())) {
			patient = patientDAO.readPatient(webData.getPatientId());
		} else if (!GenericValidator.isBlankOrNull(webData.getSubjectNo())) {
			patient = patientDAO.getPatientByExternalId(webData.getSubjectNo());
			if (patient == null) {
				patient = patientDAO.getPatientByNationalId(webData.getSubjectNo());
			}
		}

		if (patient == null) {
			insertPatient = true;
			patient = new Patient();
			patient.setExternalId(webData.getSubjectNo());
			patient.setPerson(PatientUtil.getUnknownPerson());
		}

		patient.setSysUserId(webData.getCurrentSysUserId());
	}

	private void addDoctorIfNeeded() {

		if (useFullProviderInfo) {
			insertProvider = true;
			initProvider();
		} else {
			String doctor = newOrBlankFieldValue(webData.getNewDoctor(), webData.getDoctor());
			if (GenericValidator.isBlankOrNull(doctor)) {
				return;
			}
			insertDoctorObservation = true;
			doctorObservation = new ObservationHistory();
			doctorObservation.setValue(doctor);
			doctorObservation.setValueType(ValueType.LITERAL);
			doctorObservation.setSysUserId(webData.getCurrentSysUserId());
			doctorObservation.setObservationHistoryTypeId(NonConformityAction.DOCTOR_OBSERVATION_TYPE_ID);
		}
	}

	private void initProvider() {

		if (webData.noRequesterInformation()) {
			Person person = PatientUtil.getUnknownPerson();
			provider = new Provider();
			provider.setPerson(person);
			providerPerson = null;
			updateSampleHuman = true;
			insertProvider = true;
		} else {
			providerPerson = new Person();
			provider = new Provider();

			providerPerson.setFirstName(webData.getRequesterFirstName());
			providerPerson.setLastName(webData.getRequesterLastName());
			providerPerson.setWorkPhone(webData.getRequesterPhoneNumber());
			providerPerson.setSysUserId(webData.getCurrentSysUserId());

			provider.setExternalId(webData.getRequesterSpecimanID());
			provider.setSysUserId(webData.getCurrentSysUserId());
			updateSampleHuman = true;
			sampleHuman.setSysUserId(webData.getCurrentSysUserId());
			insertProvider = true;
		}

	}

	private void addServiceIfNeeded() {
		
		if (!webData.getNewService()) {
			return;
		}
		
		String service = webData.getService();
		if( GenericValidator.isBlankOrNull(service)){
			service = webData.getNewServiceName();
			if( GenericValidator.isBlankOrNull(service)){
				return;
			}
			
			if (useFullProviderInfo) {
				newOrganization = new Organization();
				newOrganization.setIsActive("Y");
				newOrganization.setOrganizationName(service);
				newOrganization.setSysUserId(webData.getCurrentSysUserId());
				newOrganization.setMlsSentinelLabFlag("N");
				insertNewOrganizaiton = true;
			}
		}
		
		
		if (useFullProviderInfo) {
			sampleRequester = new SampleRequester();
			if( !insertNewOrganizaiton){
				sampleRequester.setRequesterId(service);
			}
			sampleRequester.setRequesterTypeId(SamplePatientEntrySaveAction.ORGANIZATION_REQUESTER_TYPE_ID);
			sampleRequester.setSysUserId(webData.getCurrentSysUserId());
			insertSampleRequester = true;
		} else {
			serviceObservation = new ObservationHistory();
			serviceObservation.setValue(service);
			serviceObservation.setValueType(ValueType.LITERAL);
			serviceObservation.setSysUserId(webData.getCurrentSysUserId());
			serviceObservation.setObservationHistoryTypeId(NonConformityAction.SERVICE_OBSERVATION_TYPE_ID);
			insertServiceObservation = true;
		}
	}

	private void addAllSampleQaEvents() {

		for (QaEventItem item : webData.getQaEvents()) {
			// New event
			if (isNonBlankNewEvent(item)) {
				String sampleType = item.getSampleType();
				if (sampleType.equals("0")) {
					addSampleQaEvent(item, null);
				} else {
					insertSampleItems = true;
					SampleItem sampleItem = sampleItemsByType.get(sampleType);
					sampleItem = addSampleQaEventForItem(item, sampleItem, sampleType);
					sampleItemsByType.put(sampleType, sampleItem);
				}
				// Marked for removal
			} else if (isOldForRemoval(item)) {
				sampleQaEvent = new SampleQaEvent();
				sampleQaEvent.setId(item.getId());
				sampleQaEvent.setSysUserId(webData.getCurrentSysUserId());
				this.sampleQAEventDeleteList.add(sampleQaEvent);
				Note existingNote = findExistingQANote(item.getId());
				if (existingNote != null) {
					existingNote.setSysUserId(webData.getCurrentSysUserId());
					this.deleteableNotes.add(existingNote);
				}
				// Updated note
			} else {
				sampleQaEvent = new SampleQaEvent();
				sampleQaEvent.setId(item.getId());
				Note existingNote = findExistingQANote(item.getId());
				if (existingNote == null) {
					addNoteIfNeeded(item.getNote(), sampleQaEvent);
				} else {
					if ( (existingNote.getText() != null && !existingNote.getText().equals(item.getNote())) || 
						 (existingNote.getText() == null && item.getNote() != null ) ) {
						existingNote.setText(item.getNote());
						existingNote.setSystemUserId(webData.getCurrentSysUserId());
						existingNote.setSysUserId(webData.getCurrentSysUserId());
						updateableNotes.add(existingNote);
					}
				}
			}
		}
	}

	private void addNoteToSampleIfNeeded() {
		String noteText = webData.getNoteText();// newOrBlankFieldValue(webData.getNewNoteText(),
												// webData.getNoteText());
		if (!GenericValidator.isBlankOrNull(noteText)) {
			Note note = findExistingSampleNote();
			if (note != null) {
				if (!note.getText().equals(noteText)) {
					note.setText(noteText);
					note.setSysUserId(webData.getCurrentSysUserId());
					note.setSystemUserId(webData.getCurrentSysUserId());
					updateableNotes.add(note);
				} else {
					// edited to the same old value, so skip updating.
				}
			} else {
				NoteSet noteSet = new NoteSet();
				noteSet.referencedSample = sample;
				noteSet.note = createNote(noteText);
				noteSet.note.setReferenceTableId(NonConformityAction.SAMPLE_TABLE_ID);
				insertableNotes.add(noteSet);
			}
		}

	}

	private SampleQaEvent addSampleQaEvent(QaEventItem item, SampleItem sampleItem) {
		QAService qaService = new QAService(new SampleQaEvent());
		qaService.setCurrentUserId(webData.getCurrentSysUserId());
		qaService.setReportTime(getCompleteDateTime());
		qaService.setQaEventById(item.getQaEvent());
		qaService.setObservation(QAObservationType.SECTION, item.getSection(), QAObservationValueType.LITERAL, REJECT_IF_EMPTY);
		qaService.setObservation(QAObservationType.AUTHORIZER, item.getAuthorizer(), QAObservationValueType.LITERAL, REJECT_IF_EMPTY);
		qaService.setObservation(QAObservationType.DOC_NUMBER, item.getRecordNumber(), QAObservationValueType.LITERAL, REJECT_IF_EMPTY);
		qaService.setSampleItem(sampleItem);
		addNoteIfNeeded(item.getNote(), qaService.getSampleQaEvent());

		sampleQAEventInsertList.add(qaService.getSampleQaEvent());

		for (QaObservation observation : qaService.getUpdatedObservations()) {
			qaObservationList.put(observation, qaService.getSampleQaEvent());
		}

		return qaService.getSampleQaEvent();
	}

	/**
	 * 
	 * @param item
	 * @param sampleItem
	 * @param sampleTypeId
	 * @param sortOrder
	 * @return if the return is null, the there was already a sampleItem on the
	 *         Sample for the given sampleTypeId
	 */
	private SampleItem addSampleQaEventForItem(QaEventItem item, SampleItem sampleItem, String sampleTypeId) {
		List<SampleItem> sampleItemsOfType = new ArrayList<SampleItem>();
		if (sampleItem == null) {

			TypeOfSample typeOfSample = typeOfSampleDAO.getTypeOfSampleById(sampleTypeId);

			if (sample.getId() != null) {
				sampleItemsOfType = sampleItemDAO.getSampleItemsBySampleIdAndType(sample.getId(), typeOfSample);
			}
			if (sampleItemsOfType != null && sampleItemsOfType.size() > 0) {
				// ignoring any sample which actually has more than one
				// sampleItem of the same typeOfSample
				sampleItem = sampleItemsOfType.get(0);
			} else {
				sampleItem = new SampleItem();
				sampleItem.setSample(sample);
				sampleItem.setSysUserId(webData.getCurrentSysUserId());
				sampleItem.setTypeOfSample(typeOfSample);
				sampleItem.setSortOrder(getNextSampleItemSortOrder());
				sampleItem.setStatusId(StatusOfSampleUtil.getStatusID(SampleStatus.Entered));
			}
		}
		addSampleQaEvent(item, sampleItem);
		// if the DB already has this sample type don't bother returning it to
		// the caller (who will want to save it later), because this update
		// action never updates sampleItems
		return (sampleItemsOfType.size() > 0) ? null : sampleItem;
	}



	private Note findExistingSampleNote() {
		if (sample.getId() == null) {
			return null;
		}

		List<Note> notes = noteDAO.getNoteByRefIAndRefTableAndSubject(sample.getId(), NonConformityAction.SAMPLE_TABLE_ID, NOTE_SUBJECT);
		return notes.isEmpty() ? null : notes.get(0);
	}

	private Note findExistingQANote(String sampleQAEventId ) {
		if (sampleQAEventId == null || !sampleQAEventId.matches("\\d+")) {
			return null;
		}

		List<Note> notes = noteDAO.getNoteByRefIAndRefTableAndSubject(sampleQAEventId, NonConformityAction.SAMPLE_QAEVENT_TABLE_ID, NOTE_SUBJECT);
		return notes.isEmpty() ? null : notes.get(0);
	}
	
	private void addNoteIfNeeded(String noteText, SampleQaEvent event) {
		if (!GenericValidator.isBlankOrNull(noteText)) {
			NoteSet noteSet = new NoteSet();
			noteSet.referencedEvent = event;
			noteSet.note = createNote(noteText);
			noteSet.note.setReferenceTableId(QAService.SAMPLE_QAEVENT_TABLE_ID);
			insertableNotes.add(noteSet);
		}
	}

	private Note createNote(String noteText) {
		Note note = new Note();
		note.setText(noteText);
		note.setSysUserId(webData.getCurrentSysUserId());
		note.setNoteType(NOTE_TYPE);
		note.setSubject(NOTE_SUBJECT);
		note.setSystemUser(systemUser);
		note.setSystemUserId(webData.getCurrentSysUserId());

		return note;
	}

	private String getNextSampleItemSortOrder() {
		String sampleId = sample.getId();
		int max = 0;
		if (!GenericValidator.isBlankOrNull(sampleId)) {
			List<SampleItem> sampleItems = sampleItemDAO.getSampleItemsBySampleId(sampleId);
			for (SampleItem sampleItem : sampleItems) {
				Integer curr = Integer.valueOf(sampleItem.getSortOrder());
				max = (curr > max) ? curr : max;
			}
		}
		max++;
		return String.valueOf(max);
	}

	private String newOrBlankFieldValue(Boolean newValue, String value) {
		if (newValue) {
			return value == null ? "" : value;
		}
		return null;
	}

	/**
	 * 
	 * @param item
	 * @return TRUE if is new, contains some reason and isn't marked for delete
	 */
	private boolean isNonBlankNewEvent(QaEventItem item) {
		return "NEW".equals(item.getId()) && !item.isRemove() && !"0".equals(item.getQaEvent());
	}

	/**
	 * @param item
	 * @return TRUE if Is not new and is marked for removal.
	 */
	private boolean isOldForRemoval(QaEventItem item) {
		return item.getId() != null && !"NEW".equals(item.getId()) && item.isRemove();
	}

	private class NoteSet {
		public SampleQaEvent referencedEvent;
		public Sample referencedSample;
		public Note note;
	}
}
