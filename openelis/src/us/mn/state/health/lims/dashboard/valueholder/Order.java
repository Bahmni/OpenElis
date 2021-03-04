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

package us.mn.state.health.lims.dashboard.valueholder;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bahmni.feed.openelis.utils.JsonTimeSerializer;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.common.util.resources.ResourceLocator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Order {
    private String accessionNumber;
    private String uuid;
    private String orderId;
    private String stNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String source;
    private int pendingTestCount;
    private int pendingValidationCount;
    private int referredTestCount;
    private int totalTestCount;
    private Date collectionDate;
    private boolean isPrinted;
    private boolean isCompleted;
    private Date enteredDate;
    private String comments;
    private String sectionNames;
    private String sampleType;
    private String priority;

    public Order() {
    }

    public Order(String accessionNumber, String uuid, String orderId, String stNumber, String firstName, String middleName, String lastName, String source, boolean isCompleted, boolean isPrinted,
                 int pendingTestCount, int pendingValidationCount, int referredTestCount, int totalTestCount, Date collectionDate, Date enteredDate, String comments, String sectionNames, String sampleType, String priority) {
        this.accessionNumber = accessionNumber;
        this.uuid = uuid;
        this.orderId = orderId;
        this.stNumber = stNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.source = source;
        this.isCompleted = isCompleted;
        this.isPrinted = isPrinted;
        this.pendingTestCount = pendingTestCount;
        this.pendingValidationCount = pendingValidationCount;
        this.referredTestCount = referredTestCount;
        this.totalTestCount = totalTestCount;
        this.collectionDate = collectionDate;
        this.enteredDate = enteredDate;
        this.comments = comments;
        this.sectionNames = sectionNames;
        this.sampleType = sampleType;
        this.priority = priority;
    }

    public Order(String accessionNumber, String uuid, String orderId, String stNumber, String firstName, String middleName, String lastName, String source, boolean isCompleted, boolean isPrinted,
                 int pendingTestCount, int pendingValidationCount,int referredTestCount, int totalTestCount, Date collectionDate, Date enteredDate, String comments, String sectionNames) {
        this.accessionNumber = accessionNumber;
        this.uuid = uuid;
        this.orderId = orderId;
        this.stNumber = stNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.source = source;
        this.isCompleted = isCompleted;
        this.isPrinted = isPrinted;
        this.pendingTestCount = pendingTestCount;
        this.pendingValidationCount = pendingValidationCount;
        this.referredTestCount = referredTestCount;
        this.totalTestCount = totalTestCount;
        this.collectionDate = collectionDate;
        this.enteredDate = enteredDate;
        this.comments = comments;
        this.sectionNames = sectionNames;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getStNumber() {
        return stNumber;
    }

    public void setStNumber(String stNumber) {
        this.stNumber = stNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSource() {
        return source;
    }

    public int getReferredTestCount() {
        return referredTestCount;
    }

    public void setReferredTestCount(int referredTestCount) {
        this.referredTestCount = referredTestCount;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getPendingTestCount() {
        return pendingTestCount;
    }

    public void setPendingTestCount(int pendingTestCount) {
        this.pendingTestCount = pendingTestCount;
    }

    public int getTotalTestCount() {
        return totalTestCount;
    }

    public void setTotalTestCount(int totalTestCount) {
        this.totalTestCount = totalTestCount;
    }

    public int getPendingValidationCount() {
        return pendingValidationCount;
    }

    public boolean getIsPrinted() {
        return isPrinted;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCollectionDateString()
    {
        if(this.collectionDate!=null) {
            return DateUtil.formatDateAsText(this.collectionDate);
        }
        return "";
    }

    @JsonSerialize(using = JsonTimeSerializer.class)
    public Date getEnteredDate() {
        return enteredDate;
    }

    public void setEnteredDate(Date enteredDate) {
        this.enteredDate = enteredDate;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @JsonSerialize(using = JsonTimeSerializer.class)
    public Date getCollectionDate() {
        return collectionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return pendingTestCount == order.pendingTestCount &&
                pendingValidationCount == order.pendingValidationCount &&
                referredTestCount==order.referredTestCount &&
                totalTestCount == order.totalTestCount &&
                isPrinted == order.isPrinted &&
                isCompleted == order.isCompleted &&
                Objects.equals(accessionNumber, order.accessionNumber) &&
                Objects.equals(uuid, order.uuid) &&
                Objects.equals(orderId, order.orderId) &&
                Objects.equals(stNumber, order.stNumber) &&
                Objects.equals(firstName, order.firstName) &&
                Objects.equals(middleName, order.middleName) &&
                Objects.equals(lastName, order.lastName) &&
                Objects.equals(source, order.source) &&
                Objects.equals(collectionDate, order.collectionDate) &&
                Objects.equals(enteredDate, order.enteredDate) &&
                Objects.equals(comments, order.comments) &&
                Objects.equals(sectionNames, order.sectionNames) &&
                Objects.equals(sampleType, order.sampleType) &&
                Objects.equals(priority, order.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessionNumber, uuid, orderId, stNumber, firstName, middleName, lastName, source,
                pendingTestCount, pendingValidationCount, referredTestCount, totalTestCount, collectionDate, isPrinted, isCompleted,
                enteredDate, comments, sectionNames, sampleType, priority);
    }

    public String getSectionNames() {
        return sectionNames;
    }

    public void setSectionNames(String sectionNames) {
        this.sectionNames = sectionNames;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
