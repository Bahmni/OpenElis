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

import org.bahmni.feed.openelis.utils.JsonTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

public class Order {
    private static final long serialVersionUID = 1L;

    private String accessionNumber;
    private String stNumber;
    private String firstName;
    private String lastName;
    private String source;
    private Date completedDate;
    private Integer pendingTestCount;
    private Integer pendingValidationCount;
    private Integer totalTestCount;
    public Order() {
    }
    public Order(String accessionNumber, String stNumber, String firstName, String lastName, String source, Integer pendingTestCount, Integer pendingValidationCount, Integer totalTestCount) {
        this.accessionNumber = accessionNumber;
        this.stNumber = stNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.source = source;
        this.pendingTestCount = pendingTestCount;
        this.pendingValidationCount = pendingValidationCount;
        this.totalTestCount = totalTestCount;
    }

    public Order(String accessionNumber, String stNumber, String firstName, String lastName, String source, Date completedDate) {
        this.accessionNumber = accessionNumber;
        this.stNumber = stNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.source = source;
        this.completedDate = completedDate;
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

    public Integer getPendingTestCount() {
        return pendingTestCount;
    }

    public void setPendingTestCount(Integer pendingTestCount) {
        this.pendingTestCount = pendingTestCount;
    }

    public Integer getTotalTestCount() {
        return totalTestCount;
    }

    public void setTotalTestCount(Integer totalTestCount) {
        this.totalTestCount = totalTestCount;
    }

    @JsonSerialize(using=JsonTimeSerializer.class)
    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public Integer getPendingValidationCount() {
        return pendingValidationCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (accessionNumber != null ? !accessionNumber.equals(order.accessionNumber) : order.accessionNumber != null)
            return false;
        if (completedDate != null ? !completedDate.equals(order.completedDate) : order.completedDate != null)
            return false;
        if (firstName != null ? !firstName.equals(order.firstName) : order.firstName != null) return false;
        if (lastName != null ? !lastName.equals(order.lastName) : order.lastName != null) return false;
        if (pendingTestCount != null ? !pendingTestCount.equals(order.pendingTestCount) : order.pendingTestCount != null)
            return false;
        if (pendingValidationCount != null ? !pendingValidationCount.equals(order.pendingValidationCount) : order.pendingValidationCount != null)
            return false;
        if (source != null ? !source.equals(order.source) : order.source != null) return false;
        if (stNumber != null ? !stNumber.equals(order.stNumber) : order.stNumber != null) return false;
        if (totalTestCount != null ? !totalTestCount.equals(order.totalTestCount) : order.totalTestCount != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = accessionNumber != null ? accessionNumber.hashCode() : 0;
        result = 31 * result + (stNumber != null ? stNumber.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (completedDate != null ? completedDate.hashCode() : 0);
        result = 31 * result + (pendingTestCount != null ? pendingTestCount.hashCode() : 0);
        result = 31 * result + (pendingValidationCount != null ? pendingValidationCount.hashCode() : 0);
        result = 31 * result + (totalTestCount != null ? totalTestCount.hashCode() : 0);
        return result;
    }
}
