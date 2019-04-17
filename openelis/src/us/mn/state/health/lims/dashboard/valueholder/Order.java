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

import java.util.Date;

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
    private int totalTestCount;
    private Date collectionDate;
    private boolean isPrinted;
    private boolean isCompleted;
    private Date enteredDate;
    private String comments;
    private String sectionNames;

    public Order() {
    }

    public Order(String accessionNumber, String uuid, String orderId, String stNumber, String firstName, String middleName, String lastName, String source, boolean isCompleted, boolean isPrinted,
                 int pendingTestCount, int pendingValidationCount, int totalTestCount, Date collectionDate, Date enteredDate, String comments, String sectionNames) {
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

        if (isCompleted != order.isCompleted) return false;
        if (isPrinted != order.isPrinted) return false;
        if (pendingTestCount != order.pendingTestCount) return false;
        if (pendingValidationCount != order.pendingValidationCount) return false;
        if (totalTestCount != order.totalTestCount) return false;
        if (accessionNumber != null ? !accessionNumber.equals(order.accessionNumber) : order.accessionNumber != null)
            return false;
        if (collectionDate != null ? !collectionDate.equals(order.collectionDate) : order.collectionDate != null)
            return false;
        if (enteredDate != null ? !enteredDate.equals(order.enteredDate) : order.enteredDate != null)
            return false;
        if (firstName != null ? !firstName.equals(order.firstName) : order.firstName != null) return false;
        if (lastName != null ? !lastName.equals(order.lastName) : order.lastName != null) return false;
        if (middleName != null ? !middleName.equals(order.middleName) : order.middleName != null) return false;
        if (source != null ? !source.equals(order.source) : order.source != null) return false;
        if (stNumber != null ? !stNumber.equals(order.stNumber) : order.stNumber != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = accessionNumber != null ? accessionNumber.hashCode() : 0;
        result = 31 * result + (stNumber != null ? stNumber.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + pendingTestCount;
        result = 31 * result + pendingValidationCount;
        result = 31 * result + totalTestCount;
        result = 31 * result + (collectionDate != null ? collectionDate.hashCode() : 0);
        result = 31 * result + (enteredDate != null ? enteredDate.hashCode() : 0);
        result = 31 * result + (isPrinted ? 1 : 0);
        result = 31 * result + (isCompleted ? 1 : 0);
        return result;
    }

    public String getSectionNames() {
        return sectionNames;
    }

    public void setSectionNames(String sectionNames) {
        this.sectionNames = sectionNames;
    }
}
