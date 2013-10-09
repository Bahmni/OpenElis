package us.mn.state.health.lims.dashboard.valueholder;

import org.bahmni.feed.openelis.utils.JsonTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

public class Order {
    private String accessionNumber;
    private String stNumber;
    private String firstName;
    private String lastName;
    private String source;
    private Integer pendingTestCount;
    private Integer pendingValidationCount;
    private Integer totalTestCount;
    private boolean isPrinted;
    private boolean isCompleted;

    public Order() {
    }

    public Order(String accessionNumber, String stNumber, String firstName, String lastName, String source, boolean isCompleted, boolean isPrinted,
                 int pendingTestCount, int pendingValidationCount, int totalTestCount) {
        this.accessionNumber = accessionNumber;
        this.stNumber = stNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.source = source;
        this.isCompleted = isCompleted;
        this.isPrinted = isPrinted;
        this.pendingTestCount = pendingTestCount;
        this.pendingValidationCount = pendingValidationCount;
        this.totalTestCount = totalTestCount;
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

    public Integer getPendingValidationCount() {
        return pendingValidationCount;
    }

    public boolean getIsPrinted() {
        return isPrinted;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (isCompleted != order.isCompleted) return false;
        if (isPrinted != order.isPrinted) return false;
        if (accessionNumber != null ? !accessionNumber.equals(order.accessionNumber) : order.accessionNumber != null)
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
        result = 31 * result + (pendingTestCount != null ? pendingTestCount.hashCode() : 0);
        result = 31 * result + (pendingValidationCount != null ? pendingValidationCount.hashCode() : 0);
        result = 31 * result + (totalTestCount != null ? totalTestCount.hashCode() : 0);
        result = 31 * result + (isPrinted ? 1 : 0);
        result = 31 * result + (isCompleted ? 1 : 0);
        return result;
    }
}
