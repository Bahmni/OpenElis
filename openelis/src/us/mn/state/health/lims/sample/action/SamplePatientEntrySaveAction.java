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
package us.mn.state.health.lims.sample.action;

import org.apache.struts.Globals;
import org.apache.struts.action.*;
import org.bahmni.feed.openelis.feed.contract.SampleTestOrderCollection;
import org.bahmni.feed.openelis.feed.service.EventPublishers;
import org.bahmni.feed.openelis.feed.service.impl.OpenElisUrlPublisher;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.StaleObjectStateException;
import us.mn.state.health.lims.address.daoimpl.AddressPartDAOImpl;
import us.mn.state.health.lims.address.valueholder.AddressPart;
import us.mn.state.health.lims.address.valueholder.OrganizationAddress;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.exception.LIMSDuplicateRecordException;
import us.mn.state.health.lims.common.exception.LIMSInvalidSTNumberException;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.exception.LIMSValidationException;
import us.mn.state.health.lims.common.formfields.FormFields;
import us.mn.state.health.lims.common.formfields.FormFields.Field;
import us.mn.state.health.lims.common.provider.validation.IAccessionNumberValidator;
import us.mn.state.health.lims.common.provider.validation.IAccessionNumberValidator.ValidationResults;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.ConfigurationProperties.Property;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.common.util.resources.ResourceLocator;
import us.mn.state.health.lims.common.util.validator.ActionError;
import us.mn.state.health.lims.laborder.dao.LabOrderTypeDAO;
import us.mn.state.health.lims.laborder.daoimpl.LabOrderTypeDAOImpl;
import us.mn.state.health.lims.laborder.valueholder.LabOrderType;
import us.mn.state.health.lims.observationhistory.valueholder.ObservationHistory;
import us.mn.state.health.lims.observationhistory.valueholder.ObservationHistory.ValueType;
import us.mn.state.health.lims.observationhistorytype.dao.ObservationHistoryTypeDAO;
import us.mn.state.health.lims.observationhistorytype.daoImpl.ObservationHistoryTypeDAOImpl;
import us.mn.state.health.lims.observationhistorytype.valueholder.ObservationHistoryType;
import us.mn.state.health.lims.organization.daoimpl.OrganizationTypeDAOImpl;
import us.mn.state.health.lims.organization.valueholder.Organization;
import us.mn.state.health.lims.organization.valueholder.OrganizationType;
import us.mn.state.health.lims.patient.action.IPatientUpdate;
import us.mn.state.health.lims.patient.action.PatientManagementUpdateAction;
import us.mn.state.health.lims.patient.action.bean.PatientManagmentInfo;
import us.mn.state.health.lims.requester.dao.RequesterTypeDAO;
import us.mn.state.health.lims.requester.daoimpl.RequesterTypeDAOImpl;
import us.mn.state.health.lims.requester.valueholder.RequesterType;
import us.mn.state.health.lims.requester.valueholder.SampleRequester;
import us.mn.state.health.lims.sample.bean.SampleTestCollection;
import us.mn.state.health.lims.sample.util.AccessionNumberUtil;
import us.mn.state.health.lims.sample.util.AnalysisBuilder;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.samplesource.dao.SampleSourceDAO;
import us.mn.state.health.lims.samplesource.daoimpl.SampleSourceDAOImpl;
import us.mn.state.health.lims.samplesource.valueholder.SampleSource;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.OrderStatus;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.SampleStatus;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.upload.action.AddSampleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.apache.commons.validator.GenericValidator.isBlankOrNull;

public class SamplePatientEntrySaveAction extends BaseAction {

	public static long ORGANIZATION_REQUESTER_TYPE_ID;
	private static long PROVIDER_REQUESTER_TYPE_ID;
    private boolean savePatient = false;
    private String providerId;
    //	private Person providerPerson;
    //	private Provider provider;
	private String patientId;
	private String accessionNumber;
	private Sample sample;
	private SampleHuman sampleHuman;
	private SampleRequester requesterSite;
	private List<SampleTestCollection> sampleItemsTests;
    private ActionMessages patientErrors;
	private Organization newOrganization = null;
    private SampleSource sampleSource;
    private OpenElisUrlPublisher accessionPublisher = new EventPublishers().accessionPublisher();


    private boolean useReceiveDateForCollectionDate = false;
	private boolean useReferringSiteId = false;
	private String collectionDateFromRecieveDate = null;
	private TypeOfSampleDAO typeOfSampleDAO = new TypeOfSampleDAOImpl();
	private LabOrderTypeDAO labOrderTypeDAO = new LabOrderTypeDAOImpl();

	private List<ObservationHistory> observations;
	private List<OrganizationAddress> orgAddressExtra;
    private SampleSourceDAO sampleSourceDAO = new SampleSourceDAOImpl();



    private static String INITIAL_CONDITION_OBSERVATION_ID;
	private static String PATIENT_PAYMENT_OBSERVATION_ID;
	private static String REQUEST_DATE_ID;
	private static String NEXT_VISIT_DATE_ID;
	private static String PRIMARY_ORDER_TYPE_ID;
	private static String SECONDARY_ORDER_TYPE_ID;
	private static String OTHER_ORDER_TYPE_ID;
	private static String REFERRING_ORG_TYPE_ID;
	private static String ADDRESS_COMMUNE_ID;
	private static String ADDRESS_FAX_ID;
	private static String ADDRESS_PHONE_ID;
	private static String ADDRESS_STREET_ID;

	static {
		ObservationHistoryTypeDAO ohtDAO = new ObservationHistoryTypeDAOImpl();

		INITIAL_CONDITION_OBSERVATION_ID = getObservationHistoryTypeId(ohtDAO, "initialSampleCondition");
		PATIENT_PAYMENT_OBSERVATION_ID = getObservationHistoryTypeId(ohtDAO, "paymentStatus");
		REQUEST_DATE_ID = getObservationHistoryTypeId(ohtDAO, "requestDate");
		NEXT_VISIT_DATE_ID = getObservationHistoryTypeId(ohtDAO, "nextVisitDate");
		PRIMARY_ORDER_TYPE_ID = getObservationHistoryTypeId(ohtDAO, "primaryOrderType");
		SECONDARY_ORDER_TYPE_ID = getObservationHistoryTypeId(ohtDAO, "secondaryOrderType");
		OTHER_ORDER_TYPE_ID = getObservationHistoryTypeId(ohtDAO, "otherSecondaryOrderType");

		RequesterTypeDAO requesterTypeDAO = new RequesterTypeDAOImpl();
		RequesterType type = requesterTypeDAO.getRequesterTypeByName("organization");
		if (type != null) {
			ORGANIZATION_REQUESTER_TYPE_ID = Long.parseLong(type.getId());
		}

		type = requesterTypeDAO.getRequesterTypeByName("provider");
		if (type != null) {
			PROVIDER_REQUESTER_TYPE_ID = Long.parseLong(type.getId());
		}

		
		OrganizationType orgType = new OrganizationTypeDAOImpl().getOrganizationTypeByName("referring clinic");
		if (orgType != null) {
			REFERRING_ORG_TYPE_ID = orgType.getId();
		}

		List<AddressPart> parts = new AddressPartDAOImpl().getAll();
		for (AddressPart part : parts) {
			if ("commune".equals(part.getPartName())) {
				ADDRESS_COMMUNE_ID = part.getId();
			} else if ("fax".equals(part.getPartName())) {
				ADDRESS_FAX_ID = part.getId();
			} else if ("phone".equals(part.getPartName())) {
				ADDRESS_PHONE_ID = part.getId();
			} else if ("street".equals(part.getPartName())) {
				ADDRESS_STREET_ID = part.getId();
			}
		}
	}

    private static String getObservationHistoryTypeId(ObservationHistoryTypeDAO ohtDAO, String name) {
		ObservationHistoryType oht;
		oht = ohtDAO.getByName(name);
		if (oht != null) {
			return oht.getId();
		}

		return null;
	}

	@Override
	protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String forward = FWD_SUCCESS;

        AnalysisBuilder analysisBuilder = new AnalysisBuilder();
        orgAddressExtra = new ArrayList<>();
		observations = new ArrayList<>();
		boolean useInitialSampleCondition = FormFields.getInstance().useField(Field.InitialSampleCondition);
		BaseActionForm dynaForm = (BaseActionForm) form;
		PatientManagmentInfo patientInfo = (PatientManagmentInfo) dynaForm.get("patientProperties");

		ActionMessages errors = new ActionMessages();

		String receivedDateForDisplay = dynaForm.getString("receivedDateForDisplay");
		useReceiveDateForCollectionDate = !FormFields.getInstance().useField(Field.CollectionDate);

		useReferringSiteId = FormFields.getInstance().useField(Field.RequesterSiteList);
		boolean trackPayments = ConfigurationProperties.getInstance().isPropertyValueEqual(Property.trackPatientPayment, "true");

		if (useReceiveDateForCollectionDate) {
			collectionDateFromRecieveDate = receivedDateForDisplay + " 00:00:00";
		}

		String receivedTime = dynaForm.getString("recievedTime");
		if (!isBlankOrNull(receivedTime)) {
			receivedDateForDisplay += " " + receivedTime;
		}

		requesterSite = null;
		if (useReferringSiteId) {
			requesterSite = initSampleRequester(dynaForm);
		}

		IPatientUpdate patientUpdate = new PatientManagementUpdateAction();
		testAndInitializePatientForSaving(mapping, request, patientInfo, patientUpdate);

		initAccesionNumber(dynaForm);
//		initProvider(dynaForm);
		initSampleData(dynaForm, receivedDateForDisplay, useInitialSampleCondition, trackPayments,
				!isBlankOrNull(receivedTime), analysisBuilder);
		initSampleHumanData();
		validateSample(errors);

		if (errors.size() > 0) {
			saveErrors(request, errors);
			request.setAttribute(Globals.ERROR_KEY, errors);
			return mapping.findForward(FWD_FAIL);
		}

		try {
            patientId = patientUpdate.getPatientId(dynaForm);
            if (savePatient) {
                patientUpdate.persistPatientData(patientInfo, request.getContextPath());
            }

            AddSampleService addSampleService = new AddSampleService(true);
            addSampleService.persist(analysisBuilder, useInitialSampleCondition, newOrganization, requesterSite,
                    orgAddressExtra, sample, getTestOrderList(sampleItemsTests), observations, sampleHuman, patientId,
                    null, providerId, currentUserId,
                    PROVIDER_REQUESTER_TYPE_ID, REFERRING_ORG_TYPE_ID);
			if(!StringUtil.isNullorNill(sample.getAccessionNumber())) {
				accessionPublisher.publish(sample.getUUID(), request.getContextPath());
			}

        } catch (LIMSInvalidSTNumberException e) {
            request.setAttribute(IActionConstants.REQUEST_FAILED, true);
            return addErrorMessageAndForward(mapping, request, new ActionError("errors.InvalidStNumber", null, null));
        } catch (LIMSValidationException e) {
            request.setAttribute(IActionConstants.REQUEST_FAILED, true);
            return addErrorMessageAndForward(mapping, request, new ActionError(e.getMessage(), null, null));
        } catch (LIMSDuplicateRecordException e){
            request.setAttribute(IActionConstants.REQUEST_FAILED, true);
            return addErrorMessageAndForward(mapping, request, new ActionError("errors.duplicate.STNumber", null, null));
        }catch (LIMSRuntimeException lre) {
            request.setAttribute(IActionConstants.REQUEST_FAILED, true);
            if (lre.getException() instanceof StaleObjectStateException) {
                return addErrorMessageAndForward(mapping, request, new ActionError("errors.OptimisticLockException", null, null));
            } else {
                return addErrorMessageAndForward(mapping, request, new ActionError("errors.UpdateException", null, null));
            }
		}

		setSuccessFlag(request, forward);

		return mapping.findForward(forward);
	}

	private List<SampleTestOrderCollection> getTestOrderList(List<SampleTestCollection> sampleTestCollectionList) {
		List<SampleTestOrderCollection> list = new ArrayList<>();
		for (SampleTestCollection sampleTestCollection : sampleTestCollectionList) {
			list.add(SampleTestOrderCollection.getTestOrderCollectionFrom(sampleTestCollection));
		}
		return list;
	}

    private ActionForward addErrorMessageAndForward(ActionMapping mapping, HttpServletRequest request, ActionError error) {
        ActionMessages errors = new ActionMessages();
        errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        saveErrors(request, errors);
        request.setAttribute(Globals.ERROR_KEY, errors);
        request.setAttribute(ALLOW_EDITS_KEY, "false");
        return mapping.findForward(FWD_FAIL);
    }

	private SampleRequester initSampleRequester(BaseActionForm dynaForm) {
		SampleRequester requester = null;
		newOrganization = null;
		
		String orgId = dynaForm.getString("referringSiteId");
		String newOrgName = dynaForm.getString("newRequesterName");

		if (!isBlankOrNull(orgId)) {
			requester = createSiteRequester(orgId);
		} else if (!isBlankOrNull(newOrgName)) {
			//will be corrected after newOrg is persisted
			requester = createSiteRequester("0"); 

			newOrganization = new Organization();
			if (FormFields.getInstance().useField(Field.SampleEntryHealthFacilityAddress)) {
				String phone = dynaForm.getString("facilityPhone");
				String fax = dynaForm.getString("facilityFax");
				String street = dynaForm.getString("facilityAddressStreet");
				String commune = dynaForm.getString("facilityAddressCommune");

				addOrgAddressExtra(phone, "T", ADDRESS_PHONE_ID);
				addOrgAddressExtra(fax, "T", ADDRESS_FAX_ID);
				addOrgAddressExtra(commune, "T", ADDRESS_COMMUNE_ID);
				addOrgAddressExtra(street, "T", ADDRESS_STREET_ID);
			}

			String siteCode = dynaForm.getString("referringSiteCode");

			newOrganization.setIsActive("Y");
			newOrganization.setOrganizationName(newOrgName);
			newOrganization.setShortName(siteCode);
			// this was left as a warning for copy and paste -- it causes a null
			// pointer exception in session.flush()
			// newOrganization.setOrganizationTypes(ORG_TYPE_SET);
			newOrganization.setSysUserId(currentUserId);
			newOrganization.setMlsSentinelLabFlag("N");

		}

		return requester;
	}

	private SampleRequester createSiteRequester(String orgId) {
		SampleRequester requester;
		requester = new SampleRequester();
		 requester.setRequesterId(orgId);
		requester.setRequesterTypeId(ORGANIZATION_REQUESTER_TYPE_ID);
		requester.setSysUserId(currentUserId);
		return requester;
	}

	private void addOrgAddressExtra(String value, String type, String addressPart) {
		if (!isBlankOrNull(value)) {
			OrganizationAddress orgAddress = new OrganizationAddress();
			orgAddress.setSysUserId(currentUserId);
			orgAddress.setType(type);
			orgAddress.setValue(value);
			orgAddress.setAddressPartId(addressPart);
			orgAddressExtra.add(orgAddress);
		}
	}

	private void validateSample(ActionMessages errors) {
		// assure accession number
		ValidationResults result = AccessionNumberUtil.checkAccessionNumberValidity(accessionNumber, null, null, null);

		if (result != IAccessionNumberValidator.ValidationResults.SUCCESS) {
			String message = AccessionNumberUtil.getInvalidMessage(result);
			errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError(message));
		}

		// assure that there is at least 1 sample
		if (sampleItemsTests.isEmpty()) {
			errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError("errors.no.sample"));
		}

		// assure that all samples have tests
		if (!allSamplesHaveTests()) {
			errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError("errors.samples.with.no.tests"));
		}

		// check patient errors
		if (patientErrors.size(ActionErrors.GLOBAL_MESSAGE) > 0) {
			errors.add(patientErrors);
		}

	}

	private boolean allSamplesHaveTests() {

		for (SampleTestCollection sampleTest : sampleItemsTests) {
			if (sampleTest.tests.size() == 0) {
				return false;
			}
		}

		return true;
	}

	private void initAccesionNumber(BaseActionForm dynaForm) {
		accessionNumber = (String) dynaForm.get("labNo");
	}

	private void initSampleData(BaseActionForm dynaForm, String recievedDate, boolean useInitialSampleCondition, boolean trackPayments,
                                boolean useReceiveTimestamp, AnalysisBuilder analysisBuilder) {
		sampleItemsTests = new ArrayList<>();
        String sampleSourceId = dynaForm.getString("sampleSourceId");
        if (sampleSourceId != null) {
            sampleSource = sampleSourceDAO.get(sampleSourceId);
        }
        providerId = dynaForm.getString("providerId");
        sample = createPopulatedSample(recievedDate, useReceiveTimestamp);
        addObservations(dynaForm, trackPayments);

		try {
			Document sampleDom = DocumentHelper.parseText(dynaForm.getString("sampleXML"));
			int sampleItemIdIndex = 0;

			for (@SuppressWarnings("rawtypes")
			Iterator i = sampleDom.getRootElement().elementIterator("sample"); i.hasNext();) {
				sampleItemIdIndex++;

				Element sampleItem = (Element) i.next();

				List<ObservationHistory> initialConditionList = null;

				if (useInitialSampleCondition) {
					initialConditionList = addInitialSampleConditions(sampleItem, initialConditionList);
				}

				SampleItem item = new SampleItem();
				item.setSysUserId(currentUserId);
				item.setSample(sample);
				item.setTypeOfSample(typeOfSampleDAO.getTypeOfSampleById(sampleItem.attributeValue("sampleID")));
				item.setSortOrder(Integer.toString(sampleItemIdIndex));
				item.setStatusId(StatusOfSampleUtil.getStatusID(SampleStatus.Entered));
				item.setCollector(sampleItem.attributeValue("collector"));

                // panel
                String panelIDs = sampleItem.attributeValue("panels");
                analysisBuilder.augmentPanelIdToPanelMap(panelIDs);

                //test
                String collectionDateTime = sampleItem.attributeValue("date").trim() + " " + sampleItem.attributeValue("time").trim();
                String testIDs = sampleItem.attributeValue("tests");
				List<Test> tests = addTests(testIDs);
				sampleItemsTests.add(new SampleTestCollection(item, tests, useReceiveDateForCollectionDate ? collectionDateFromRecieveDate
						: collectionDateTime, initialConditionList));
				
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<Test> addTests(String testIDs) {
        List<Test> tests = new ArrayList<>();
		StringTokenizer tokenizer = new StringTokenizer(testIDs, ",");

		while (tokenizer.hasMoreTokens()) {
			Test test = new Test();
			test.setId(tokenizer.nextToken().trim());
			tests.add(test);
		}
        return tests;
	}

	private void addObservations(BaseActionForm dynaForm, boolean trackPayments) {
		if (trackPayments) {
			createObservation(dynaForm, "paymentOptionSelection", PATIENT_PAYMENT_OBSERVATION_ID, ValueType.DICTIONARY);
		}

		createObservation(dynaForm, "requestDate", REQUEST_DATE_ID, ValueType.LITERAL);
		createObservation(dynaForm, "nextVisitDate", NEXT_VISIT_DATE_ID, ValueType.LITERAL);
		createOrderTypeObservation(dynaForm, "orderType", PRIMARY_ORDER_TYPE_ID, ValueType.LITERAL);
		createOrderTypeObservation(dynaForm, "followupPeriodOrderType", SECONDARY_ORDER_TYPE_ID, ValueType.LITERAL);
		createOrderTypeObservation(dynaForm, "initialPeriodOrderType", SECONDARY_ORDER_TYPE_ID, ValueType.LITERAL);
		createObservation(dynaForm, "otherPeriodOrder", OTHER_ORDER_TYPE_ID, ValueType.LITERAL);
	}

	private List<ObservationHistory> addInitialSampleConditions(Element sampleItem, List<ObservationHistory> initialConditionList) {
		String initialSampleConditionIdString = sampleItem.attributeValue("initialConditionIds");
		if (!isBlankOrNull(initialSampleConditionIdString)) {
			String[] initialSampleConditionIds = initialSampleConditionIdString.split(",");
			initialConditionList = new ArrayList<>();

            for (String initialSampleConditionId : initialSampleConditionIds) {
                ObservationHistory initialSampleConditions = new ObservationHistory();
                initialSampleConditions.setValue(initialSampleConditionId);
                initialSampleConditions.setValueType(ValueType.DICTIONARY);
                initialSampleConditions.setObservationHistoryTypeId(INITIAL_CONDITION_OBSERVATION_ID);
                initialConditionList.add(initialSampleConditions);
            }
		}
		return initialConditionList;
	}

	private void createOrderTypeObservation(BaseActionForm dynaForm, String property, String observationType, ValueType valueType) {
		String observationData = dynaForm.getString(property);
		if (!isBlankOrNull(observationData) && !isBlankOrNull(observationType)) {
			LabOrderType labOrderType = labOrderTypeDAO.getLabOrderTypeById(observationData);
			// should notify end user if null
			if (labOrderType != null) {
				ObservationHistory observation = new ObservationHistory();
				observation.setObservationHistoryTypeId(observationType);
				observation.setSysUserId(currentUserId);
				observation.setValue(labOrderType.getType());
				observation.setValueType(valueType);
				observations.add(observation);
			}
		}
	}

	private void createObservation(BaseActionForm dynaForm, String property, String observationType, ValueType valueType) {
		String observationData = dynaForm.getString(property);
		if (!isBlankOrNull(observationData) && !isBlankOrNull(observationType)) {
			ObservationHistory observation = new ObservationHistory();
			observation.setObservationHistoryTypeId(observationType);
			observation.setSysUserId(currentUserId);
			observation.setValue(observationData);
			observation.setValueType(valueType);
			observations.add(observation);
		}
	}

	private Sample createPopulatedSample(String receivedDate, boolean useReceiveTimestamp) {
		Sample sample = new Sample();
		sample.setSysUserId(currentUserId);
		sample.setAccessionNumber(accessionNumber);
        sample.setSampleSource(sampleSource);
        sample.setUUID(UUID.randomUUID().toString());

        sample.setEnteredDate(new java.util.Date());
		if (useReceiveTimestamp) {
			sample.setReceivedTimestamp(DateUtil.convertStringDateToTimestamp(receivedDate));
		} else {
			sample.setReceivedDateForDisplay(receivedDate);

            Locale locale = SystemConfiguration.getInstance().getDefaultLocale();
            String datePattern = ResourceLocator.getInstance().getMessageResources().getMessage(locale, "date.format.formatKey");
            sample.setReceivedTimestamp(DateUtil.convertStringDateToTimestampWithPatternNoLocale(receivedDate, datePattern));
		}
		if (useReceiveDateForCollectionDate) {
			sample.setCollectionDateForDisplay(collectionDateFromRecieveDate);
		}

		sample.setDomain(SystemConfiguration.getInstance().getHumanDomain());
		sample.setStatusId(StatusOfSampleUtil.getStatusID(OrderStatus.Entered));
        return sample;
	}

	private void initSampleHumanData() {
		sampleHuman = new SampleHuman();
		sampleHuman.setSysUserId(currentUserId);
	}

	private void testAndInitializePatientForSaving(ActionMapping mapping, HttpServletRequest request, PatientManagmentInfo patientInfo,
			IPatientUpdate patientUpdate) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		patientUpdate.setPatientUpdateStatus(patientInfo);
		savePatient = patientUpdate.getPatientUpdateStatus() != PatientManagementUpdateAction.PatientUpdateStatus.NO_ACTION;

		if (savePatient) {
			patientErrors = patientUpdate.preparePatientData(mapping, request, patientInfo);
		} else {
			patientErrors = new ActionMessages();
		}
	}

//	private void initProvider(BaseActionForm dynaForm) {
//
//        String requesterSpecimanID = dynaForm.getString("requesterSampleID");
//		String requesterFirstName = dynaForm.getString("providerFirstName");
//		String requesterLastName = dynaForm.getString("providerLastName");
//		String requesterPhoneNumber = dynaForm.getString("providerWorkPhone");
//		String requesterFax = dynaForm.getString("providerFax");
//		String requesterEmail = dynaForm.getString("providerEmail");
//
//		if (noRequesterInformation(requesterSpecimanID, requesterFirstName, requesterLastName, requesterPhoneNumber, requesterFax, requesterEmail)) {
//			provider = PatientUtil.getUnownProvider();
//		} else {
//			providerPerson = new Person();
//			provider = new Provider();
//
//			providerPerson.setFirstName(requesterFirstName);
//			providerPerson.setLastName(requesterLastName);
//			providerPerson.setWorkPhone(requesterPhoneNumber);
//			providerPerson.setFax(requesterFax);
//			providerPerson.setEmail(requesterEmail);
//			providerPerson.setSysUserId(currentUserId);
//			provider.setExternalId(requesterSpecimanID);
//		}
//
//		provider.setSysUserId(currentUserId);
//	}

	
//	private boolean noRequesterInformation(String requesterSpecimanID, String requesterFirstName, String requesterLastName,
//			String requesterPhoneNumber, String requesterFax, String requesterEmail) {
//
//		return (isBlankOrNull(requesterFirstName) && isBlankOrNull(requesterPhoneNumber)
//				&& isBlankOrNull(requesterLastName) && isBlankOrNull(requesterSpecimanID)
//				&& isBlankOrNull(requesterFax) && isBlankOrNull(requesterEmail));
//	}


//	private void persistProviderData() {
//		if (providerPerson != null && provider != null) {
//			PersonDAO personDAO = new PersonDAOImpl();
//			ProviderDAO providerDAO = new ProviderDAOImpl();
//
//			personDAO.insertData(providerPerson);
//			provider.setPerson(providerPerson);
//
//			providerDAO.insertData(provider);
//		}
//	}


    @Override
	protected String getPageTitleKey() {
		return "sample.entry.title";
	}

	@Override
	protected String getPageSubtitleKey() {
		return "sample.entry.title";
	}


}
