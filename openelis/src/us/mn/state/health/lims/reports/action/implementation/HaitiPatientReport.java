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
package us.mn.state.health.lims.reports.action.implementation;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.validator.GenericValidator;
import us.mn.state.health.lims.address.dao.PersonAddressDAO;
import us.mn.state.health.lims.address.daoimpl.AddressPartDAOImpl;
import us.mn.state.health.lims.address.daoimpl.PersonAddressDAOImpl;
import us.mn.state.health.lims.address.valueholder.AddressPart;
import us.mn.state.health.lims.address.valueholder.PersonAddress;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.services.SampleService;
import us.mn.state.health.lims.common.services.TestIdentityService;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.ConfigurationProperties.Property;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.laborder.dao.LabOrderTypeDAO;
import us.mn.state.health.lims.laborder.daoimpl.LabOrderTypeDAOImpl;
import us.mn.state.health.lims.laborder.valueholder.LabOrderType;
import us.mn.state.health.lims.note.dao.NoteDAO;
import us.mn.state.health.lims.note.daoimpl.NoteDAOImpl;
import us.mn.state.health.lims.note.util.NoteUtil;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.observationhistory.dao.ObservationHistoryDAO;
import us.mn.state.health.lims.observationhistory.daoimpl.ObservationHistoryDAOImpl;
import us.mn.state.health.lims.observationhistory.valueholder.ObservationHistory;
import us.mn.state.health.lims.observationhistorytype.daoImpl.ObservationHistoryTypeDAOImpl;
import us.mn.state.health.lims.observationhistorytype.valueholder.ObservationHistoryType;
import us.mn.state.health.lims.organization.dao.OrganizationDAO;
import us.mn.state.health.lims.organization.daoimpl.OrganizationDAOImpl;
import us.mn.state.health.lims.organization.valueholder.Organization;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.util.PatientUtil;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.dao.PatientIdentityTypeDAO;
import us.mn.state.health.lims.patientidentitytype.daoimpl.PatientIdentityTypeDAOImpl;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;
import us.mn.state.health.lims.person.dao.PersonDAO;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.person.valueholder.Person;
import us.mn.state.health.lims.provider.dao.ProviderDAO;
import us.mn.state.health.lims.provider.daoimpl.ProviderDAOImpl;
import us.mn.state.health.lims.provider.valueholder.Provider;
import us.mn.state.health.lims.referral.dao.ReferralDAO;
import us.mn.state.health.lims.referral.dao.ReferralReasonDAO;
import us.mn.state.health.lims.referral.dao.ReferralResultDAO;
import us.mn.state.health.lims.referral.daoimpl.ReferralDAOImpl;
import us.mn.state.health.lims.referral.daoimpl.ReferralReasonDAOImpl;
import us.mn.state.health.lims.referral.daoimpl.ReferralResultDAOImpl;
import us.mn.state.health.lims.referral.valueholder.Referral;
import us.mn.state.health.lims.referral.valueholder.ReferralResult;
import us.mn.state.health.lims.reports.action.implementation.reportBeans.HaitiClinicalPatientData;
import us.mn.state.health.lims.requester.dao.RequesterTypeDAO;
import us.mn.state.health.lims.requester.dao.SampleRequesterDAO;
import us.mn.state.health.lims.requester.daoimpl.RequesterTypeDAOImpl;
import us.mn.state.health.lims.requester.daoimpl.SampleRequesterDAOImpl;
import us.mn.state.health.lims.requester.valueholder.RequesterType;
import us.mn.state.health.lims.requester.valueholder.SampleRequester;
import us.mn.state.health.lims.result.dao.ResultDAO;
import us.mn.state.health.lims.result.daoimpl.ResultDAOImpl;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.*;

public abstract class HaitiPatientReport extends Report {

    private static final String RESULT_REFERENCE_TABLE_ID = NoteUtil.getTableReferenceId("RESULT");
    private static final DecimalFormat twoDecimalFormat = new DecimalFormat("#.##");
    protected static final String REFERRAL_STATUS_ID = StatusOfSampleUtil.getStatusID(AnalysisStatus.ReferedOut);
    private static String ADDRESS_DEPT_ID;
    private static String ADDRESS_COMMUNE_ID;
    protected String currentContactInfo = "";
    protected String currentSiteInfo = "";

    protected SampleHumanDAO sampleHumanDAO = new SampleHumanDAOImpl();
    protected DictionaryDAO dictionaryDAO = new DictionaryDAOImpl();
    private ResultDAO resultDAO = new ResultDAOImpl();
    protected SampleDAO sampleDAO = new SampleDAOImpl();
    protected PatientDAO patientDAO = new PatientDAOImpl();
    protected PersonDAO personDAO = new PersonDAOImpl();
    protected ProviderDAO providerDAO = new ProviderDAOImpl();
    protected SampleRequesterDAO requesterDAO = new SampleRequesterDAOImpl();
    protected TestDAO testDAO = new TestDAOImpl();
    protected ReferralReasonDAO referralReasonDAO = new ReferralReasonDAOImpl();
    protected ReferralDAO referralDao = new ReferralDAOImpl();
    protected ReferralResultDAO referralResultDAO = new ReferralResultDAOImpl();
    protected ObservationHistoryDAO observationDAO = new ObservationHistoryDAOImpl();
    protected NoteDAO noteDAO = new NoteDAOImpl();
    protected PersonAddressDAO addressDAO = new PersonAddressDAOImpl();
    private LabOrderTypeDAO labOrderTypeDAO = new LabOrderTypeDAOImpl();
    private List<String> handledOrders;

    private String lowerNumber;
    private String upperNumber;
    protected String STNumber = null;
    protected String subjectNumber = null;
    protected String healthRegion = null;
    protected String healthDistrict = null;
    protected String patientName = null;
    protected String patientDOB = null;
    protected String currentConclusion = null;

    protected String patientDept = null;
    protected String patientCommune = null;
    protected String patientMed = null;

    protected Patient reportPatient;
    protected Sample reportSample;
    protected Provider currentProvider;
    protected Analysis reportAnalysis;
    protected String reportReferralResultValue;
    protected List<HaitiClinicalPatientData> reportItems;
    protected String compleationDate;
    protected SampleService currentSampleService;

    protected static String ST_NUMBER_IDENTITY_TYPE_ID = "0";
    protected static String SUBJECT_NUMBER_IDENTITY_TYPE_ID = "0";
    protected static String HEALTH_REGION_IDENTITY_TYPE_ID = "0";
    protected static String HEALTH_DISTRICT_IDENTITY_TYPE_ID = "0";
    protected static String PRIMARYRELATIVE_IDENTITY_TYPE_ID = "0";
    protected static String LAB_TYPE_OBSERVATION_ID = "0";
    protected static String LAB_SUBTYPE_OBSERVATION_ID = "0";
    protected static Long PROVIDER_REQUESTER_TYPE_ID;
    protected static Long ORGANIZATION_REQUESTER_TYPE_ID;
    protected Map<String, Boolean> sampleCompleteMap;


    static {
        PatientIdentityTypeDAO identityTypeDAO = new PatientIdentityTypeDAOImpl();
        List<PatientIdentityType> typeList = identityTypeDAO.getAllPatientIdenityTypes();

        for (PatientIdentityType identityType : typeList) {
            if ("ST".equals(identityType.getIdentityType())) {
                ST_NUMBER_IDENTITY_TYPE_ID = identityType.getId();
            } else if ("SUBJECT".equals(identityType.getIdentityType())) {
                SUBJECT_NUMBER_IDENTITY_TYPE_ID = identityType.getId();
            } else if ("HEALTH REGION".equals(identityType.getIdentityType())) {
                HEALTH_REGION_IDENTITY_TYPE_ID = identityType.getId();
            } else if ("HEALTH DISTRICT".equals(identityType.getIdentityType())) {
                HEALTH_DISTRICT_IDENTITY_TYPE_ID = identityType.getId();
            } else if ("PRIMARYRELATIVE".equals(identityType.getIdentityType())) {
                PRIMARYRELATIVE_IDENTITY_TYPE_ID = identityType.getId();
            }
        }

        RequesterTypeDAO requesterTypeDAO = new RequesterTypeDAOImpl();
        RequesterType type = requesterTypeDAO.getRequesterTypeByName("provider");
        if (type != null) {
            PROVIDER_REQUESTER_TYPE_ID = Long.parseLong(type.getId());
        }

        type = requesterTypeDAO.getRequesterTypeByName("organization");
        if (type != null) {
            ORGANIZATION_REQUESTER_TYPE_ID = Long.parseLong(type.getId());
        }

        List<AddressPart> partList = new AddressPartDAOImpl().getAll();
        for (AddressPart part : partList) {
            if ("department".equals(part.getPartName())) {
                ADDRESS_DEPT_ID = part.getId();
            } else if ("commune".equals(part.getPartName())) {
                ADDRESS_COMMUNE_ID = part.getId();
            }
        }

        ObservationHistoryType ohType = new ObservationHistoryTypeDAOImpl().getByName("primaryOrderType");
        LAB_TYPE_OBSERVATION_ID = ohType == null ? null : ohType.getId();

        ohType = new ObservationHistoryTypeDAOImpl().getByName("secondaryOrderType");
        LAB_SUBTYPE_OBSERVATION_ID = ohType == null ? null : ohType.getId();

    }

    abstract protected String getReportNameForParameterPage();

    abstract protected String getSiteLogo();

    abstract protected void postSampleBuild();

    abstract protected void createReportItems();

    abstract protected void setReferredResult(HaitiClinicalPatientData data, Result result);

    protected boolean appendUOMToRange() {
        return true;
    }

    protected boolean augmentResultWithFlag() {
        return true;
    }

    protected boolean useReportingDescription() {
        return true;
    }

    @Override
    protected String errorReportFileName() {
        return HAITI_ERROR_REPORT;
    }

    public void setRequestParameters(BaseActionForm dynaForm) {
        try {
            PropertyUtils.setProperty(dynaForm, "reportName", getReportNameForParameterPage());

            PropertyUtils.setProperty(dynaForm, "useAccessionDirect", Boolean.TRUE);
            PropertyUtils.setProperty(dynaForm, "useHighAccessionDirect", Boolean.TRUE);
            PropertyUtils.setProperty(dynaForm, "usePatientNumberDirect", Boolean.TRUE);

            PropertyUtils.setProperty(dynaForm, "exportOptions", getExportOptions());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

            public void initializeReport(BaseActionForm dynaForm) {
                super.initializeReport();
                errorFound = false;
                lowerNumber = dynaForm.getString("accessionDirect");
                upperNumber = dynaForm.getString("highAccessionDirect");
                String patientNumber = dynaForm.getString("patientNumberDirect").toUpperCase();

                handledOrders = new ArrayList<String>();

                createReportParameters();

                boolean valid = false;
                List<Sample> reportSampleList = new ArrayList<Sample>();
                List<Sample> sampleListWithLabNUmber = new ArrayList<Sample>();

                if (GenericValidator.isBlankOrNull(lowerNumber) && GenericValidator.isBlankOrNull(upperNumber)) {
                    valid = findPatientByPatientNumber(patientNumber);

                    if (valid) {
                        reportSampleList = findReportSamplesForReportPatient();
                    }
                } else {
                    valid = validateAccessionNumbers();

                    if (valid) {
                        reportSampleList = findReportSamples(lowerNumber, upperNumber);
                    }
                }

                sampleCompleteMap = new HashMap<String, Boolean>();
                initializeReportItems();

                    if (reportSampleList.isEmpty()) {
                        add1LineErrorMessage("report.error.message.noPrintableItems");
                        } else {
                         for(Sample sample: reportSampleList){
                             if(sample.getAccessionNumber() != null)
                                 sampleListWithLabNUmber.add(sample);
                         }
                        if(sampleListWithLabNUmber.size() != 0) {
                            for (Sample sample : sampleListWithLabNUmber) {
                                currentSampleService = new SampleService(sample);
                                handledOrders.add(sample.getId());
                                reportSample = sample;
                                sampleCompleteMap.put(sample.getAccessionNumber(), Boolean.TRUE);
                                findCompleationDate();
                                findPatientFromSample();
                                findContactInfo();
                                findPatientInfo();
                                createReportItems();
                            }
                            postSampleBuild();
                        }
                        else
                            add1LineErrorMessage("report.no.results");
                        }
            }

    private void findCompleationDate() {
        Date date = currentSampleService.getCompletedDate();
        compleationDate = date == null ? null : DateUtil.convertSqlDateToStringDate(date);
    }

    private void findPatientInfo() {
        patientDept = "";
        patientCommune = "";
        if (ADDRESS_DEPT_ID != null) {
            PersonAddress deptAddress = addressDAO.getByPersonIdAndPartId(reportPatient.getPerson().getId(), ADDRESS_DEPT_ID);

            if (deptAddress != null && !GenericValidator.isBlankOrNull(deptAddress.getValue())) {
                patientDept = dictionaryDAO.getDictionaryById(deptAddress.getValue()).getDictEntry();
            }
        }

        if (ADDRESS_COMMUNE_ID != null) {
            PersonAddress deptAddress = addressDAO.getByPersonIdAndPartId(reportPatient.getPerson().getId(), ADDRESS_COMMUNE_ID);

            if (deptAddress != null) {
                patientCommune = deptAddress.getValue();
            }
        }

    }

    private void findContactInfo() {
        List<SampleRequester> requesters = requesterDAO.getRequestersForSampleId(reportSample.getId());
        currentContactInfo = "";
        currentSiteInfo = "";
        currentProvider = null;

        for (SampleRequester requester : requesters) {
            if (ORGANIZATION_REQUESTER_TYPE_ID == requester.getRequesterTypeId()) {
                OrganizationDAO organizationDAO = new OrganizationDAOImpl();
                Organization org = organizationDAO.getOrganizationById(String.valueOf(requester.getRequesterId()));
                if (org != null) {
                    currentSiteInfo = org.getOrganizationName();
                }
                break;
            }

            if (PROVIDER_REQUESTER_TYPE_ID == requester.getRequesterTypeId()) {
                StringBuffer buffer = new StringBuffer();
                currentProvider = providerDAO.get(((Long) requester.getRequesterId()).toString());
                Person person = currentProvider.getPerson();

                if (person.getId() != null) {
                    buffer.append(person.getLastName());
                    buffer.append(", ");
                    buffer.append(person.getFirstName());
                }

                currentContactInfo = buffer.toString();
            }
        }
    }

    private boolean findPatientByPatientNumber(String patientNumber) {
        PatientIdentityDAO patientIdentityDAO = new PatientIdentityDAOImpl();
        reportPatient = patientDAO.getPatientByNationalId(patientNumber);

        if (reportPatient == null) {
            List<PatientIdentity> identities = patientIdentityDAO.getPatientIdentitiesByValueAndType(patientNumber, ST_NUMBER_IDENTITY_TYPE_ID);

            if (identities.isEmpty()) {
                identities = patientIdentityDAO.getPatientIdentitiesByValueAndType(patientNumber, SUBJECT_NUMBER_IDENTITY_TYPE_ID);
            }

            if (!identities.isEmpty()) {
                String reportPatientId = identities.get(0).getPatientId();
                reportPatient = new Patient();
                reportPatient.setId(reportPatientId);
                patientDAO.getData(reportPatient);
            }
        }

        return reportPatient != null;
    }

    private List<Sample> findReportSamplesForReportPatient() {
        return sampleHumanDAO.getSamplesForPatient(reportPatient.getId());
    }

    private boolean validateAccessionNumbers() {

        if (GenericValidator.isBlankOrNull(lowerNumber) && GenericValidator.isBlankOrNull(upperNumber)) {
            add1LineErrorMessage("report.error.message.noParameters");
            return false;
        }

        if (GenericValidator.isBlankOrNull(lowerNumber)) {
            lowerNumber = upperNumber;
        } else if (GenericValidator.isBlankOrNull(upperNumber)) {
            upperNumber = lowerNumber;
        }

        if (lowerNumber.compareToIgnoreCase(upperNumber) > 0) {
            String temp = upperNumber;
            upperNumber = lowerNumber;
            lowerNumber = temp;
        }

        return true;
    }

    private List<Sample> findReportSamples(String lowerNumber, String upperNumber) {
        List<Sample> sampleList = sampleDAO.getSamplesByAccessionRange(lowerNumber, upperNumber);
        return sampleList == null ? new ArrayList<Sample>() : sampleList;
    }

    protected void findPatientFromSample() {
        Patient patient = sampleHumanDAO.getPatientForSample(reportSample);

        if (reportPatient == null || !patient.getId().equals(reportPatient.getId())) {
            STNumber = null;
            patientDOB = null;
        }

        reportPatient = patient;
    }

    protected void createReportParameters() {
        super.createReportParameters();
        reportParameters.put("siteId", ConfigurationProperties.getInstance().getPropertyValue(Property.SiteCode));
        reportParameters.put("siteLogo", useLogo ? getSiteLogo() : null);
        if (ConfigurationProperties.getInstance().isPropertyValueEqual(Property.configurationName, "CI LNSP")) {
            reportParameters.put("headerName", "CILNSPHeader.jasper");
        } else if (ConfigurationProperties.getInstance().isPropertyValueEqual(Property.configurationName, "Haiti LNSP")) {
            reportParameters.put("useSTNumber", Boolean.FALSE);
        } else {
            reportParameters.put("useSTNumber", Boolean.TRUE);
        }
    }

    protected String getPatientDOB() {
        if (patientDOB == null) {
            if (reportPatient != null) {
                patientDOB = reportPatient.getBirthDateForDisplay();
            } else {
                patientDOB = " ";
            }

        }

        return patientDOB;
    }

    protected String getLazyPatientIdentity(String identity, String id) {
        if (identity == null) {
            identity = " ";
            if (reportPatient != null) {
                List<PatientIdentity> identities = PatientUtil.getIdentityListForPatient(reportPatient);
                for (PatientIdentity patientIdentity : identities) {
                    if (patientIdentity.getIdentityTypeId().equals(id)) {
                        identity = patientIdentity.getIdentityData();
                        break;
                    }
                }
            }
        }

        return identity;
    }


    protected void setPatientName(HaitiClinicalPatientData data) {
        if (reportPatient == null) {
            return;
        }
        String firstName = reportPatient.getPerson().getFirstName();
        String middleName = " ";
        String lastName = reportPatient.getPerson().getLastName();
        data.setFirstName(firstName);
        data.setLastName(lastName);
        data.setPatientName(firstName + middleName + lastName);
        if (!"true".equals(ConfigurationProperties.getInstance().getPropertyValue(Property.SHOW_MIDDLENAME_ON_REPORT_PRINT))) {
            return;
        }
        if (reportPatient.getPerson().getMiddleName() != null) {
            middleName += reportPatient.getPerson().getMiddleName() + " ";
        }
        data.setPatientName(firstName + middleName + lastName);
    }

    @SuppressWarnings("unchecked")
    protected void reportResultAndConclusion(HaitiClinicalPatientData data) {
        List<Result> resultList = resultDAO.getResultsByAnalysis(reportAnalysis);
        Test test = reportAnalysis.getTest();

        data.setTestSection(reportAnalysis.getTestSection().getLocalizedName());
        data.setTestSortOrder(GenericValidator.isBlankOrNull(test.getSortOrder()) ? Integer.MAX_VALUE : Integer.parseInt(test.getSortOrder()));
        data.setSectionSortOrder(test.getTestSection().getSortOrderInt());

        if (StatusOfSampleUtil.getStatusID(AnalysisStatus.Canceled).equals(reportAnalysis.getStatusId())) {
            data.setResult(StringUtil.getMessageForKey("report.test.status.canceled"));
        } else if (REFERRAL_STATUS_ID.equals(reportAnalysis.getStatusId())) {
            if (noResults(resultList)) {
                setNote(data, resultList);
                data.setResult(StringUtil.getMessageForKey("report.test.status.referredOut"));
            } else {
                setAppropriateResults(resultList, data);
                setReferredResult(data, resultList.get(0));
                setNormalRange(data, test, resultList.get(0));
            }
        } else if (noResults(resultList) || !(StatusOfSampleUtil.getStatusID(AnalysisStatus.Finalized).equals(reportAnalysis.getStatusId()) || StatusOfSampleUtil.getStatusID(AnalysisStatus.FinalizedRO).equals(reportAnalysis.getStatusId()))) {
            sampleCompleteMap.put(reportSample.getAccessionNumber(), Boolean.FALSE);
            setNote(data, resultList);
            data.setResult(StringUtil.getMessageForKey("report.test.status.inProgress"));
        } else {
            setAppropriateResults(resultList, data);

            Result result = resultList.get(0);
            setNormalRange(data, test, result);
            data.setResult(getAugmentedResult(data, result));
            data.setFinishDate(reportAnalysis.getCompletedDateForDisplay());
            data.setNote(getResultNote(result));
            Referral referral = referralDao.getReferralByAnalysisId(reportAnalysis.getId());
            String imbed = referral == null? null : "R";
            data.setAlerts(getResultFlag(result, imbed));
        }

        if(resultList.size() > 0){
            setAbnormal(data, resultList.get(0));
        }
        data.setConclusion(currentConclusion);
    }

    private void setAbnormal(HaitiClinicalPatientData data, Result result) {
        data.setAbnormal(result.getAbnormal());
    }

    private void setNote(HaitiClinicalPatientData data, List<Result> resultList) {
        if (resultList.size() > 0)
            data.setNote(getResultNote(resultList.get(0)));
    }

    private boolean noResults(List<Result> resultList) {
        if (resultList.isEmpty()) {
            return true;
        }

        Result result = resultList.get(0);

        if (GenericValidator.isBlankOrNull(resultList.get(0).getValue())) {
            return true;
        }

        if ("M".equals(result.getResultType()) || "D".equals(result.getResultType())) {
            return "0".equals(result.getValue());
        }

        return false;
    }

    private void setNormalRange(HaitiClinicalPatientData data, Test test, Result result) {
        String uom = getUnitOfMeasure(result, test);
        data.setTestRefRange(addIfNotEmpty(getRange(result), appendUOMToRange() ? uom : null));
        data.setUom(uom);
    }

    private String getAugmentedResult(HaitiClinicalPatientData data, Result result) {
        String resultValue = data.getResult();
        if (TestIdentityService.isTestNumericViralLoad(reportAnalysis.getTest())) {
            try {
                resultValue += " (" + twoDecimalFormat.format(Math.log10(Double.parseDouble(resultValue))) + ")log ";
            } catch (IllegalFormatException e) {
                // no-op
            }
        }

        return resultValue + (augmentResultWithFlag() ? getResultFlag(result, null) : "");
    }

    protected String getResultNote(Result result) {
        if (result != null) {
            List<Note> notes = NoteUtil.getNotesForObjectAndTable(result.getId(), RESULT_REFERENCE_TABLE_ID);

            if (!(notes == null || notes.isEmpty())) {

                Collections.sort(notes, new Comparator<Note>() {
                    @Override
                    public int compare(Note o1, Note o2) {
                        return Integer.parseInt(o1.getId()) - Integer.parseInt(o2.getId());
                    }
                });

                StringBuilder noteBuilder = new StringBuilder();

                for (Note note : notes) {
                    noteBuilder.append(note.getText());
                    noteBuilder.append("<br/>");
                }

                noteBuilder.setLength(noteBuilder.lastIndexOf("<br/>"));

                return noteBuilder.toString();
            }
        }
        return null;
    }

    protected String getResultFlag(Result result, String imbed) {
        String flag = "";
        try {
            if ("N".equals(result.getResultType()) && !GenericValidator.isBlankOrNull(result.getValue())) {
                if (result.getMinNormal() != null & result.getMaxNormal() != null
                        && (result.getMinNormal() != 0.0 || result.getMaxNormal() != 0.0)) {
                    if (Double.valueOf(result.getValue()) < result.getMinNormal()) {
                        flag = "B";
                    } else if (Double.valueOf(result.getValue()) > result.getMaxNormal()) {
                        flag = "A";
                    }

                    if (!GenericValidator.isBlankOrNull(flag)) {
                        if (!GenericValidator.isBlankOrNull(imbed)) {
                            return " (<b>" + flag + "," + imbed + "</b>)";
                        } else {
                            return " (<b>" + flag + "</b>)";
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            // no-op
        }

        if (!GenericValidator.isBlankOrNull(imbed)) {
            return " (<b>" + imbed + "</b>)";
        }

        return "";
    }

    protected String getRange(Result result) {
        String range = "";
        if ("N".equals(result.getResultType())) {
            if (result.getMinNormal() != null && result.getMaxNormal() != null && !result.getMinNormal().equals(result.getMaxNormal())) {
                range = result.getMinNormal() + "-" + result.getMaxNormal();
            }
        }
        return range;
    }

    protected String getUnitOfMeasure(Result result, Test test) {
        String uom = "";
        if ("N".equals(result.getResultType())) {
            if (test != null && test.getUnitOfMeasure() != null) {
                uom = test.getUnitOfMeasure().getName();
            }
        }
        return uom;
    }

    private void setAppropriateResults(List<Result> resultList, HaitiClinicalPatientData data) {
        Result result = resultList.get(0);
        String reportResult = "";
        data.setResultType(result.getResultType());
        if ("D".equals(result.getResultType())) {
            Dictionary dictionary = new Dictionary();
            for (Result siblingResult : resultList) {
                dictionary.setId(siblingResult.getValue());
                dictionaryDAO.getData(dictionary);
                if (siblingResult.getAnalyte() != null && "Conclusion".equals(siblingResult.getAnalyte().getAnalyteName())) {
                    currentConclusion = dictionary.getId() != null ? dictionary.getLocalizedName() : "";
                } else {
                    reportResult = dictionary.getId() != null ? dictionary.getLocalizedName() : "";
                }
            }
        } else if ("Q".equals(result.getResultType())) {
            List<Result> childResults = resultDAO.getChildResults(result.getId());
            String childResult = childResults.get(0).getValue();

            Dictionary dictionary = new Dictionary();
            dictionary.setId(result.getValue());
            dictionaryDAO.getData(dictionary);

            reportResult = (dictionary.getId() != null ? dictionary.getLocalizedName() : "") + ": " + ("".equals(childResult) ? "n/a" : childResult);

        } else if ("M".equals(result.getResultType())) {
            Dictionary dictionary = new Dictionary();
            StringBuilder multiResult = new StringBuilder();

            Collections.sort(resultList, new Comparator<Result>() {
                @Override
                public int compare(Result o1, Result o2) {
                    return Integer.parseInt(o1.getSortOrder()) - Integer.parseInt(o2.getSortOrder());
                }
            });

            for (Result subResult : resultList) {
                dictionary.setId(subResult.getValue());
                dictionaryDAO.getData(dictionary);

                if (dictionary.getId() != null) {
                    multiResult.append(dictionary.getLocalizedName());
                    multiResult.append(", ");
                }
            }

            if (multiResult.length() > 1) {
                // remove last ","
                multiResult.setLength(multiResult.length() - 2);
            }

            reportResult = multiResult.toString();
        } else {
            reportResult = result.getValue();
        }
        data.setResult(reportResult);

        data.setHasRangeAndUOM("N".equals(result.getResultType()));
    }

    /**
     * @see us.mn.state.health.lims.reports.action.implementation.HaitiPatientReport#initializeReportItems()
     */
    protected void initializeReportItems() {
        reportItems = new ArrayList<>();
    }


    /**
     * If you have a string that you wish to add a suffix like units of measure,
     * use this.
     *
     * @param base something
     * @param plus something to add, if the above is not null or blank.
     * @return the two args put together, or the original if it was blank to
     *         begin with.
     */
    protected String addIfNotEmpty(String base, String plus) {
        return (!GenericValidator.isBlankOrNull(plus)) ? base + " " + plus : base;
    }

    /**
     * Pushes all of the information about a patient, analysis, result and the
     * conclusion into a new reporting object
     *
     * @return
     */
    protected HaitiClinicalPatientData reportAnalysisResults() {
        HaitiClinicalPatientData data = new HaitiClinicalPatientData();
        String testName = null;
        String sortOrder = "";

        boolean doAnalysis = reportAnalysis != null;

        if (doAnalysis) {
            testName = getTestName();
        }

        data.setContactInfo(currentContactInfo);
        data.setSiteInfo(currentSiteInfo);
        data.setReceivedDate(reportSample.getReceivedDateForDisplay());
        data.setDob(getPatientDOB());
        data.setAge(createReadableAge(data.getDob()));
        data.setGender(reportPatient.getGender());
        data.setNationalId(reportPatient.getNationalId());
        setPatientName(data);
        data.setDept(patientDept);
        data.setCommune(patientCommune);
        data.setStNumber(getLazyPatientIdentity(STNumber, ST_NUMBER_IDENTITY_TYPE_ID));
        data.setPrimaryRelative(getLazyPatientIdentity(null, PRIMARYRELATIVE_IDENTITY_TYPE_ID));
        data.setSubjectNumber(getLazyPatientIdentity(subjectNumber, SUBJECT_NUMBER_IDENTITY_TYPE_ID));
        data.setHealthRegion(getLazyPatientIdentity(healthRegion, HEALTH_REGION_IDENTITY_TYPE_ID));
        data.setHealthDistrict(getLazyPatientIdentity(healthDistrict, HEALTH_DISTRICT_IDENTITY_TYPE_ID));
        data.setTestName(testName);
        if (currentProvider != null) {
            data.setPatientSiteNumber(currentProvider.getExternalId());
        }

        if (doAnalysis) {
            data.setPanel(reportAnalysis.getPanel());
            if (reportAnalysis.getPanel() != null) {
                data.setPanelName(reportAnalysis.getPanel().getPanelName());
            }
            data.setTestDate(DateUtil.convertSqlDateToStringDate(reportAnalysis.getCompletedDate()));
            sortOrder = reportAnalysis.getSampleItem().getSortOrder();
            data.setOrderFinishDate(compleationDate);
            data.setOrderDate(DateUtil.convertSqlDateToStringDate(currentSampleService.getOrderedDate()));
        }

        data.setAccessionNumber(reportSample.getAccessionNumber() + "-" + sortOrder);
        data.setLabOrderType(createLabOrderType());

        if (doAnalysis) {
            reportResultAndConclusion(data);
        }

        return data;
    }

    private String getTestName() {
        String testName;

        if (useReportingDescription()) {
            testName = reportAnalysis.getTest().getReportingDescription();
        } else {
            testName = reportAnalysis.getTest().getTestName();
        }

        if (GenericValidator.isBlankOrNull(testName)) {
            testName = reportAnalysis.getTest().getTestName();
        }
        return testName;
    }


    private String createLabOrderType() {
        if (LAB_TYPE_OBSERVATION_ID == null) {
            return "";
        }

        List<ObservationHistory> primaryOrderTypes = observationDAO.getAll(reportPatient, reportSample, LAB_TYPE_OBSERVATION_ID);
        if (primaryOrderTypes.isEmpty()) {
            return "";
        }

        LabOrderType primaryLabOrderType = labOrderTypeDAO.getLabOrderTypeByType(primaryOrderTypes.get(0).getValue());
        if (primaryLabOrderType == null) {
            return "";
        }

        if (LAB_SUBTYPE_OBSERVATION_ID == null) {
            return primaryLabOrderType.getLocalizedName();
        }

        List<ObservationHistory> subOrderTypes = observationDAO.getAll(reportPatient, reportSample, LAB_SUBTYPE_OBSERVATION_ID);
        if (subOrderTypes.isEmpty()) {
            return primaryLabOrderType.getLocalizedName();
        } else {
            return primaryLabOrderType.getLocalizedName() + " : " + subOrderTypes.get(0).getValue();
        }

    }

    /**
     * Given a list of referralResults for a particular analysis, generated the
     * next displayable value made of from one or more of the values from the
     * list starting at the given index. It uses multiresult form the list when
     * the results are for the same test.
     *
     * @param referralResultsForReferral
     * @param i                          starting index.
     * @return last index actually used. If you start with 2 and this routine
     *         uses just item #2, then return result is 2, but if there are two
     *         results for the same test (e.g. a multi-select result) and those
     *         are in item item 2 and item 3 this routine returns #3.
     */
    protected int reportReferralResultValue(List<ReferralResult> referralResultsForReferral, int i) {
        ReferralResult referralResult = referralResultsForReferral.get(i);
        reportReferralResultValue = "";
        String currTestId = referralResult.getTestId();
        do {
            reportReferralResultValue += findDisplayableReportResult(referralResult.getResult()) + ", ";
            i++;
            if (i >= referralResultsForReferral.size()) {
                break;
            }
            referralResult = referralResultsForReferral.get(i);
        } while (currTestId.equals(referralResult.getTestId()));
        reportReferralResultValue = reportReferralResultValue.substring(0, reportReferralResultValue.length() - 2);
        i--;
        return i;
    }

    /**
     * Derive the appropriate displayable string results, either dictionary
     * result or direct value.
     *
     * @param result
     * @return a reportable result string.
     */
    private String findDisplayableReportResult(Result result) {
        String reportResult = "";
        if (result == null) {
            return reportResult;
        }
        String type = result.getResultType();
        String value = result.getValue();
        if (value == null) {
            return reportResult;
        }
        if ("D".equals(type) || "M".equals(type)) {
            Dictionary dictionary = new Dictionary();
            dictionary.setId(result.getValue());
            dictionaryDAO.getData(dictionary);
            reportResult = dictionary.getId() != null ? dictionary.getLocalizedName() : "";
        } else {
            reportResult = result.getValue();
        }
        return reportResult;
    }

    private String createReadableAge(String dob) {
        if (GenericValidator.isBlankOrNull(dob)) {
            return "";
        }

        dob = dob.replaceAll("xx", "01");
        Date dobDate = DateUtil.convertStringDateToSqlDate(dob);
        int months = DateUtil.getAgeInMonths(dobDate, DateUtil.getNowAsSqlDate());
        if (months > 35) {
            return (months / 12) + " Y";
        } else if (months > 0) {
            return months + " M";
        } else {
            int days = DateUtil.getAgeInDays(dobDate, DateUtil.getNowAsSqlDate());
            return days + " J";
        }

    }

    @Override
    public List<String> getReportedOrders() {
        return handledOrders;
    }
}
