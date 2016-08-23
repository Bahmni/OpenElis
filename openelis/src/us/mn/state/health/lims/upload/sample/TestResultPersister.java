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

package us.mn.state.health.lims.upload.sample;

import org.apache.commons.lang3.StringUtils;
import org.bahmni.csv.EntityPersister;
import org.bahmni.csv.RowResult;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.samplesource.dao.SampleSourceDAO;
import us.mn.state.health.lims.samplesource.daoimpl.SampleSourceDAOImpl;
import us.mn.state.health.lims.samplesource.valueholder.SampleSource;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.upload.service.TestResultPersisterService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TestResultPersister implements EntityPersister<CSVSample> {
    private TestResultPersisterService testResultPersisterService;

    private List<String> testNames;
    private SampleSourceDAO sampleSourceDAO;
    private ArrayList<String> sampleSourceNames;
    private TestDAO testDAO;

    public TestResultPersister(String contextPath) {
        this(new SampleSourceDAOImpl(), new TestDAOImpl(), new TestResultPersisterService(contextPath));
    }

    public TestResultPersister(SampleSourceDAO sampleSourceDAO, TestDAO testDAO, TestResultPersisterService testResultPersisterService) {
        this.sampleSourceDAO = sampleSourceDAO;
        this.testDAO = testDAO;
        this.testResultPersisterService = testResultPersisterService;
        sampleSourceNames = new ArrayList<>();
    }

    @Override
    public RowResult<CSVSample> persist(CSVSample csvSample) {
        return testResultPersisterService.persist(csvSample);
    }

    @Override
    public RowResult<CSVSample> validate(CSVSample csvSample) {
        String registrationNumberFormat = getStNumberFormat();
        registrationNumberFormat = registrationNumberFormat.substring(1, registrationNumberFormat.length()-1);
        String fullRegistrationNumber = csvSample.patientRegistrationNumber;
        StringBuilder errorMessage = new StringBuilder();

        if (isEmpty(csvSample.patientRegistrationNumber))
            errorMessage.append("Registration Number is mandatory.\n");

        if(!fullRegistrationNumber.matches(registrationNumberFormat))
            errorMessage.append("PatientID does not conform to the allowed format.\n");

        if (isEmpty(csvSample.sampleSource) || !getSampleSources().contains(csvSample.sampleSource)) {
            errorMessage.append("Invalid Sample source.\n");
        }

        errorMessage.append(validateTestNames(csvSample.testResults));
//        errorMessage.append(validateAtLeastOneTestIsNonEmpty(csvSample.testResults));
//        errorMessage.append(validateAllTestResultsAreValid(csvSample.testResults));

        if (isEmpty(csvSample.accessionNumber)) {
            errorMessage.append("AccessionNumber should not be blank.\n");
        }

        if (!csvSample.accessionNumber.matches("^[\\d-]+$")) {
            errorMessage.append("AccessionNumber format is invalid.\n");
        }

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            simpleDateFormat.setLenient(false);
            simpleDateFormat.parse(csvSample.sampleDate);
        } catch (ParseException e) {
            errorMessage.append("Date should be in dd-mm-yyyy format and should be a valid date.\n");
        }

        if (isEmpty(errorMessage.toString()))
            return new RowResult<>(csvSample);

        return new RowResult<>(csvSample, errorMessage.toString());
    }

    protected String getStNumberFormat() {
        return ConfigurationProperties.getInstance().getPropertyValue(ConfigurationProperties.Property.ST_NUMBER_FORMAT);
    }

    private List<String> getSampleSources() {
        if (!sampleSourceNames.isEmpty()) {
            return sampleSourceNames;
        }
        List<SampleSource> sampleSources = sampleSourceDAO.getAll();
        for (SampleSource sampleSource : sampleSources) {
            sampleSourceNames.add(sampleSource.getName());
        }
        return sampleSourceNames;
    }

    private String validateTestNames(List<CSVTestResult> testResults) {
        List<String> invalidTestNames = new ArrayList<>();
        for (CSVTestResult testResult : testResults) {
            if (!testResult.isEmpty() && !getTestNames().contains(testResult.test.toLowerCase())) {
                invalidTestNames.add(testResult.test);
            }
        }
        return invalidTestNames.isEmpty() ? "" : "Invalid test names: " + StringUtils.join(invalidTestNames.iterator(), ",") + ".\n";
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    public List<String> getTestNames() {
        if (testNames != null && !testNames.isEmpty()) {
            return testNames;
        }
        testNames = new ArrayList<>();
        List<Test> tests = testDAO.getAllActiveTests(false);
        for (Test test : tests) {
            testNames.add(test.getTestName().toLowerCase());
        }
        return testNames;
    }

}
