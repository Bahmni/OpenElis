package us.mn.state.health.lims.upload.patient;

import org.bahmni.csv.CSVEntity;
import org.bahmni.csv.CSVHeader;

public class CSVPatient extends CSVEntity {
    @CSVHeader(name="Health Centre")
    public String healthCenter;
    @CSVHeader(name="Registration No")
    public String registrationNumber;
    @CSVHeader(name="First Name")
    public String firstName;
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
