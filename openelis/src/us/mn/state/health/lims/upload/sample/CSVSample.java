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

import org.bahmni.csv.CSVEntity;
import org.bahmni.csv.CSVHeader;
import org.bahmni.csv.CSVRepeatingHeaders;

import java.util.List;

public class CSVSample extends CSVEntity {
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

    public CSVSample(String patientRegistrationNumber, String accessionNumber, String sampleDate, String sampleSource, List<CSVTestResult> testResults) {
        this.patientRegistrationNumber = patientRegistrationNumber;
        this.accessionNumber = accessionNumber;
        this.sampleDate = sampleDate;
        this.sampleSource = sampleSource;
        this.testResults = testResults;
    }
}
