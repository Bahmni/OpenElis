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
package us.mn.state.health.lims.sample.valueholder;

import org.apache.commons.validator.GenericValidator;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.common.valueholder.EnumValueItemImpl;
import us.mn.state.health.lims.common.valueholder.ValueHolder;
import us.mn.state.health.lims.common.valueholder.ValueHolderInterface;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.samplesource.valueholder.SampleSource;
import us.mn.state.health.lims.systemuser.valueholder.SystemUser;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Sample extends EnumValueItemImpl {

	private static final long serialVersionUID = 1407388492068629053L;

	private String id;
	private String accessionNumber;
	private String packageId;
	private String domain;
	private String nextItemSequence;
	private String revision;
	private java.util.Date enteredDate;
	private String enteredDateForDisplay;
	private Timestamp receivedTimestamp;
	private String receivedDateForDisplay;
	private String receivedTimeForDisplay;
	private String referredCultureFlag;
	private Timestamp collectionDate;
	private String collectionDateForDisplay;
	private String collectionTimeForDisplay;
	private String clientReference;
	private String status;
	private Date releasedDate;
	private String releasedDateForDisplay;
	private String stickerReceivedFlag;
	private String sysUserId;
	private String barCode;
	private Date transmissionDate;
	private String transmissionDateForDisplay;
	private ValueHolderInterface systemUser;
    private Set<SampleItem> sampleItems;
    private Patient patient;

    private String uuid;

	// testing one-to-many
	//this is for HSE I  and II - ability to enter up to two projects
	private List sampleProjects;

	private String statusId;
    private SampleSource sampleSource;

    public SampleSource getSampleSource() {
        return sampleSource;
    }

    public void setSampleSource(SampleSource sampleSource) {
        this.sampleSource = sampleSource;
    }

    public Sample() {
		super();
		this.systemUser = new ValueHolder();
		this.sampleProjects = new ArrayList();
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getAccessionNumber() {
		return accessionNumber;
	}

	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getClientReference() {
		return clientReference;
	}

	public void setClientReference(String clientReference) {
		this.clientReference = clientReference;
	}

	public Timestamp getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(Timestamp collectionDate) {
		this.collectionDate = collectionDate;
		// also update String date
		String locale = SystemConfiguration.getInstance().getDefaultLocale()
				.toString();
		this.collectionDateForDisplay = DateUtil
				.convertTimestampToStringDate(collectionDate, locale);

		// also update String time
		this.collectionTimeForDisplay = DateUtil
				.convertTimestampToStringTime(collectionDate, locale);
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}


	public java.util.Date getEnteredDate() {
		return enteredDate;
	}

	public void setEnteredDate(java.util.Date enteredDate) {
		this.enteredDate = enteredDate;
		// also update String date
		this.enteredDateForDisplay = DateUtil.formatDateAsText(enteredDate, SystemConfiguration.getInstance().getDefaultLocale());
	}

	public String getNextItemSequence() {
		return nextItemSequence;
	}

	public void setNextItemSequence(String nextItemSequence) {
		this.nextItemSequence = nextItemSequence;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public Date getReceivedDate() {
		return receivedTimestamp != null ? DateUtil.convertTimestampToSqlDate(receivedTimestamp) : null;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDateForDisplay = DateUtil.convertSqlDateToStringDate(receivedDate);
		this.receivedTimestamp = DateUtil.convertSqlDateToTimestamp(receivedDate);
	}
	
	public String getReceivedTimeForDisplay() {
            return receivedTimeForDisplay;
	}

	public String getReferredCultureFlag() {
		return referredCultureFlag;
	}

	public void setReferredCultureFlag(String referredCultureFlag) {
		this.referredCultureFlag = referredCultureFlag;
	}

	public Date getReleasedDate() {
		return releasedDate;
	}

	public void setReleasedDate(Date releasedDate) {
		this.releasedDate = releasedDate;
		// also update String date
		String locale = SystemConfiguration.getInstance().getDefaultLocale()
				.toString();
		this.releasedDateForDisplay = DateUtil.convertSqlDateToStringDate(
				releasedDate, locale);
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStickerReceivedFlag() {
		return stickerReceivedFlag;
	}

	public void setStickerReceivedFlag(String stickerReceivedFlag) {
		this.stickerReceivedFlag = stickerReceivedFlag;
	}

	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}

	public SystemUser getSystemUser() {
		return (SystemUser) this.systemUser.getValue();
	}

	protected ValueHolderInterface getSystemUserHolder() {
		return this.systemUser;
	}

	public void setSystemUser(SystemUser systemUser) {
		this.systemUser.setValue(systemUser);
	}

	protected void setSystemUserHolder(ValueHolderInterface systemUser) {
		this.systemUser = systemUser;
	}

	public Date getTransmissionDate() {
		return transmissionDate;
	}

	public void setTransmissionDate(Date transmissionDate) {
		this.transmissionDate = transmissionDate;
		// also update String date
		String locale = SystemConfiguration.getInstance().getDefaultLocale()
				.toString();
		this.transmissionDateForDisplay = DateUtil
				.convertSqlDateToStringDate(transmissionDate, locale);
	}

	public String getCollectionDateForDisplay() {
		return collectionDateForDisplay;
	}

	public void setCollectionDateForDisplay(String collectionDateForDisplay) {
		this.collectionDateForDisplay = collectionDateForDisplay;
		this.collectionDate = DateUtil.convertStringDateToTruncatedTimestamp(collectionDateForDisplay);
	}

	public String getEnteredDateForDisplay() {
		return enteredDateForDisplay;
	}

	public void setEnteredDateForDisplay(String enteredDateForDisplay) {
		this.enteredDateForDisplay = enteredDateForDisplay;
		//this.enteredDate = DateUtil.formatStringToDateTime(enteredDateForDisplay);
	}

	public String getReceivedDateForDisplay() {
		if( GenericValidator.isBlankOrNull(receivedDateForDisplay)){
			return receivedTimestamp != null ? DateUtil.convertTimestampToStringDate(receivedTimestamp) : null;
		}
		return receivedDateForDisplay;
	}

	public void setReceivedDateForDisplay(String receivedDateForDisplay) {
		this.receivedDateForDisplay = receivedDateForDisplay;
	}

	public String getReleasedDateForDisplay() {
		return releasedDateForDisplay;
	}

	public void setReleasedDateForDisplay(String releasedDateForDisplay) {
		this.releasedDateForDisplay = releasedDateForDisplay;
		// also update the java.sql.Date
		String locale = SystemConfiguration.getInstance().getDefaultLocale()
				.toString();
		this.releasedDate = DateUtil.convertStringDateToSqlDate(
				releasedDateForDisplay, locale);

	}

	public String getTransmissionDateForDisplay() {
		return transmissionDateForDisplay;
	}

	public void setTransmissionDateForDisplay(String transmissionDateForDisplay) {
		this.transmissionDateForDisplay = transmissionDateForDisplay;
		// also update the java.sql.Date
		String locale = SystemConfiguration.getInstance().getDefaultLocale()
				.toString();
		this.transmissionDate = DateUtil.convertStringDateToSqlDate(
				transmissionDateForDisplay, locale);

	}

	public void setCollectionTimeForDisplay(String collectionTimeForDisplay) {
		this.collectionTimeForDisplay = collectionTimeForDisplay;

		// also update the java.sql.Date
		String locale = SystemConfiguration.getInstance().getDefaultLocale()
				.toString();
		this.collectionDate = DateUtil.convertStringTimeToTimestamp(
				this.collectionDate, collectionTimeForDisplay, locale);
		// System.out.println("I am in setColelctionTimeForDisplay and this is
		// collecitonTime " + this.collectionDate);
	}

	public String getCollectionTimeForDisplay() {
		return collectionTimeForDisplay;
	}

	public List getSampleProjects() {
		return sampleProjects;
	}

	public void setSampleProjects(List sampleProjects) {
		this.sampleProjects = sampleProjects;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getStatusId() {
		return statusId;
	}

	public Timestamp getReceivedTimestamp() {
		return receivedTimestamp;
	}

	public void setReceivedTimestamp(Timestamp receivedTimestamp) {
		this.receivedTimestamp = receivedTimestamp;
        // also update String date
       
        this.receivedDateForDisplay = DateUtil
                .convertTimestampToStringDate(receivedTimestamp);

        // also update String time
        this.receivedTimeForDisplay = DateUtil
                .convertTimestampToStringTime(receivedTimestamp);
	}
	
    public void setReceivedTimeForDisplay(String receivedTimeForDisplay) {
        this.receivedTimeForDisplay = receivedTimeForDisplay;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public Set<SampleItem> getSampleItems() {
        if (sampleItems == null) sampleItems = new HashSet<>();
        return sampleItems;
    }

    public void setSampleItems(Set<SampleItem> sampleItems) {
        this.sampleItems = sampleItems;
    }

    public void addSampleItem(SampleItem sampleItem) {
        getSampleItems().add(sampleItem);
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
