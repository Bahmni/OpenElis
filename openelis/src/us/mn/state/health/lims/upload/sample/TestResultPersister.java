package us.mn.state.health.lims.upload.sample;

import org.apache.commons.lang3.StringUtils;
import org.bahmni.csv.EntityPersister;
import org.bahmni.csv.RowResult;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.daoimpl.HealthCenterDAOImpl;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;
import us.mn.state.health.lims.upload.service.TestResultPersisterService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestResultPersister implements EntityPersister<CSVSample> {
    private HealthCenterDAO healthCenterDAO;
    private TestDAO testDAO;
    private TestResultPersisterService testResultPersisterService;
    private List<String> healthCenterCodes;

    private List<String> testNames;

    public TestResultPersister() {
        this(new HealthCenterDAOImpl(), new TestDAOImpl(), new TestResultPersisterService());
    }

    public TestResultPersister(HealthCenterDAO healthCenterDAO, TestDAO testDAO, TestResultPersisterService testResultPersisterService) {
        this.healthCenterDAO = healthCenterDAO;
        this.testDAO = testDAO;
        this.testResultPersisterService = testResultPersisterService;
    }

    @Override
    public RowResult<CSVSample> persist(CSVSample csvSample) {
        return testResultPersisterService.persist(csvSample);
    }

    @Override
    public RowResult<CSVSample> validate(CSVSample csvSample) {
        StringBuilder errorMessage = new StringBuilder();
        if(isEmpty(csvSample.healthCenter) || !getHealthCenterCodes().contains(csvSample.healthCenter)) {
            errorMessage.append("Invalid Subcenter code.\n");
        }

        errorMessage.append(validateTestNames(csvSample.testResults));
        errorMessage.append(validateAtLeastOneTestIsNonEmpty(csvSample.testResults));
        errorMessage.append(validateAllTestResultsAreValid(csvSample.testResults));

        if(isEmpty(csvSample.accessionNumber)){
            errorMessage.append("AccessionNumber should not be blank.\n");
        }

        if(!csvSample.accessionNumber.matches("^[\\d-]+$")){
            errorMessage.append("AccessionNumber format is invalid.\n");
        }

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            simpleDateFormat.setLenient(false);
            simpleDateFormat.parse(csvSample.sampleDate);
        } catch (ParseException e) {
            errorMessage.append("Date should be in dd-mm-yyyy format.\n");
        }

        if (isEmpty(errorMessage.toString()))
            return new RowResult<>(csvSample);

        return new RowResult<>(csvSample, errorMessage.toString());
    }


    private String validateAllTestResultsAreValid(List<CSVTestResult> testResults) {
        for (CSVTestResult testResult : testResults) {
            if(!testResult.isValid()) {
                return "All Tests should have a result.\n";
            }
        }
        return "";
    }

    private String validateAtLeastOneTestIsNonEmpty(List<CSVTestResult> testResults) {
        for (CSVTestResult testResult : testResults) {
            if(!testResult.isEmpty()) {
                return "";
            }
        }
        return "There should be atleast one Test with a Result.\n";
    }

    private String validateTestNames(List<CSVTestResult> testResults) {
        List<String> invalidTestNames = new ArrayList<>();
        for (CSVTestResult testResult : testResults) {
            if(!testResult.isEmpty() && !getTestNames().contains(testResult.test)){
                invalidTestNames.add(testResult.test);
            }
        }
        return invalidTestNames.isEmpty() ? "" : "Invalid test names: " + StringUtils.join(invalidTestNames.iterator(), ",") + ".\n";
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    private List<String> getHealthCenterCodes() {
        if(healthCenterCodes != null) {
            return healthCenterCodes;
        }
        healthCenterCodes = new ArrayList<>();
        List<HealthCenter> healthCenters = healthCenterDAO.getAll();
        for (HealthCenter healthCenter : healthCenters) {
            healthCenterCodes.add(healthCenter.getName());
        }
        return healthCenterCodes;
    }

    public List<String> getTestNames() {
        if(testNames != null) {
            return testNames;
        }
        testNames = new ArrayList<>();
        List<Test> tests = testDAO.getAllTests(false);
        for (Test test : tests) {
            testNames.add(test.getTestName());
        }
        return testNames;
    }

}
