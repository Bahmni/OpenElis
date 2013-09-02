package us.mn.state.health.lims.upload.sample;

import org.bahmni.csv.CSVEntity;
import org.bahmni.csv.CSVHeader;
import org.bahmni.csv.CSVRepeatingHeaders;

import java.util.List;

public class CSVSample extends CSVEntity {
    @CSVHeader(name = "Health Centre")
    public String healthCenter;
    @CSVHeader(name = "Registration No")
    public String patientRegistrationNumber;
    @CSVHeader(name = "Accession Number")
    public String accessionNumber;
    @CSVHeader(name = "Date of Test")
    public String sampleDate;
    @CSVHeader(name = "Sample Source")
    public String sampleSource;
    @CSVRepeatingHeaders(names = {"Test", "Result"}, type = CSVTestResult.class)
    public List<CSVTestResult> testResults;

    public CSVSample() { }

    public CSVSample(String healthCenter, String patientRegistrationNumber, String accessionNumber, String sampleDate, String sampleSource, List<CSVTestResult> testResults) {
        this.healthCenter = healthCenter;
        this.patientRegistrationNumber = patientRegistrationNumber;
        this.accessionNumber = accessionNumber;
        this.sampleDate = sampleDate;
        this.sampleSource = sampleSource;
        this.testResults = testResults;
    }
}
