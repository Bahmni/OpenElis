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
package us.mn.state.health.lims.patient.valueholder;

import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.common.valueholder.BaseObject;
import us.mn.state.health.lims.common.valueholder.ValueHolder;
import us.mn.state.health.lims.common.valueholder.ValueHolderInterface;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.person.valueholder.Person;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Set;

public class Patient extends BaseObject {

	private static final long serialVersionUID = 1L;

	private String id;

    private Set<PatientIdentity> patientIdentities;

	private String race;

	private String gender;

	private Timestamp birthDate;

	private String birthDateForDisplay;

	private String epiFirstName;

	private String epiMiddleName;

	private String epiLastName;

	private Date birthTime;

	private String birthTimeForDisplay;

	private Date deathDate;

	private String deathDateForDisplay;

	private String nationalId;

	private String ethnicity;

	private String schoolAttend;

	private String medicareId;

	private String medicaidId;

	private String birthPlace;

	private ValueHolderInterface person;

	private String externalId;

	private String selectedPersonId;
	
	private String chartNumber;

    private HealthCenter healthCenter;

    private String uuid;

    public String getChartNumber() {
		return chartNumber;
	}

	public void setChartNumber(String chartNumber) {
		this.chartNumber = chartNumber;
	}

	public Patient() {
		super();
		person = new ValueHolder();

	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Timestamp getBirthDate() {
		return birthDate;
	}


	public void setBirthDate(Timestamp birthDate) {
		this.birthDate = birthDate;
		birthDateForDisplay = DateUtil.convertTimestampToStringDate(birthDate);
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public Date getBirthTime() {
		return birthTime;
	}

	public void setBirthTime(Date birthTime) {
		this.birthTime = birthTime;
		String locale = SystemConfiguration.getInstance().getDefaultLocale().toString();
		birthTimeForDisplay = DateUtil.convertSqlDateToStringDate( birthTime, locale );
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
		String locale = SystemConfiguration.getInstance().getDefaultLocale().toString();
		deathDateForDisplay = DateUtil.convertSqlDateToStringDate(	deathDate, locale);
	}

	public String getEpiFirstName() {
		return epiFirstName;
	}

	public void setEpiFirstName(String epiFirstName) {
		this.epiFirstName = epiFirstName;
	}

	public String getEpiLastName() {
		return epiLastName;
	}

	public void setEpiLastName(String epiLastName) {
		this.epiLastName = epiLastName;
	}

	public String getEpiMiddleName() {
		return epiMiddleName;
	}

	public void setEpiMiddleName(String epiMiddleName) {
		this.epiMiddleName = epiMiddleName;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMedicaidId() {
		return medicaidId;
	}

	public void setMedicaidId(String medicaidId) {
		this.medicaidId = medicaidId;
	}

	public String getMedicareId() {
		return medicareId;
	}

	public void setMedicareId(String medicareId) {
		this.medicareId = medicareId;
	}

	public String getNationalId() {
		return nationalId;
	}

	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}

	public Person getPerson() {
		return (Person) this.person.getValue();
	}

	protected ValueHolderInterface getPersonHolder() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person.setValue(person);
	}

	protected void setPersonHolder(ValueHolderInterface person) {
		this.person = person;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getSchoolAttend() {
		return schoolAttend;
	}

	public void setSchoolAttend(String schoolAttend) {
		this.schoolAttend = schoolAttend;
	}

	public String getBirthDateForDisplay() {
		return birthDateForDisplay;
	}

	public void setBirthDateForDisplay(String birthDateForDisplay) {
		this.birthDateForDisplay = birthDateForDisplay;

		if( birthDateForDisplay != null && birthDateForDisplay.trim().length() > 0){
			birthDate = DateUtil.convertAmbiguousStringDateToTimestamp(birthDateForDisplay);
		}
	}

	public String getBirthTimeForDisplay() {
		return birthTimeForDisplay;
	}

	public void setBirthTimeForDisplay(String birthTimeForDisplay) {
		this.birthTimeForDisplay = birthTimeForDisplay;
		this.birthTime = DateUtil.convertStringDateToSqlDate(	birthTimeForDisplay);
	}

	public String getDeathDateForDisplay() {
		return deathDateForDisplay;
	}

	public void setDeathDateForDisplay(String deathDateForDisplay) {
		this.deathDateForDisplay = deathDateForDisplay;
		this.deathDate = DateUtil.convertStringDateToSqlDate(	deathDateForDisplay);
	}

	public void setSelectedPersonId(String selectedPersonId) {
		this.selectedPersonId = selectedPersonId;
	}

	public String getSelectedPersonId() {
		return this.selectedPersonId;
	}

    public HealthCenter getHealthCenter() {
        return healthCenter;
    }

    public void setHealthCenter(HealthCenter healthCenter) {
        this.healthCenter = healthCenter;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public double getAge() {
        double currPatientAge = 0;
        if (getBirthDate() != null) {
            Calendar dob = Calendar.getInstance();
            dob.setTime(getBirthDate());
            currPatientAge = DateUtil.getAgeInYears(getBirthDate(), new java.util.Date());
        }
        return currPatientAge;
    }

    public Set<PatientIdentity> getPatientIdentities() {
        return patientIdentities;
    }

    public void setPatientIdentities(Set<PatientIdentity> patientIdentities) {
        this.patientIdentities = patientIdentities;
    }
}
