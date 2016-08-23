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

package us.mn.state.health.lims.upload.patient;

import org.bahmni.csv.CSVEntity;
import org.bahmni.csv.CSVHeader;

public class CSVPatient extends CSVEntity {
    @CSVHeader(name="Registration No")
    public String registrationNumber;
    @CSVHeader(name="First Name")
    public String firstName;
    @CSVHeader(name="Middle Name")
    public String middleName;
    @CSVHeader(name="Last Name")
    public String lastName;
    @CSVHeader(name="Age")
    public String age;
    @CSVHeader(name="DOB")
    public String dob;
    @CSVHeader(name="Gender")
    public String gender;
    @CSVHeader(name="H.No./Street")
    public String houseStreetName;
    @CSVHeader(name="Village")
    public String cityVillage;
    @CSVHeader(name="Gram Panchayat")
    public String gramPanchayat;
    @CSVHeader(name="Tehsil")
    public String tehsil;
    @CSVHeader(name="District")
    public String district;
    @CSVHeader(name="State")
    public String state;
    @CSVHeader(name="Father/Husband's Name")
    public String fatherOrHusbandsName;
    @CSVHeader(name="Occupation")
    public String occupation;
}
