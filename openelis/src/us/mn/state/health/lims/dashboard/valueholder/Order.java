package us.mn.state.health.lims.dashboard.valueholder;

public class Order {
    private String accessionNumber;
    private String stNumber;
    private String firstName;
    private String lastName;
    private String source;
    private Integer pendingTestCount;
    private Integer totalTestCount;

    public Order() {
    }

    public Order(String accessionNumber, String stNumber, String firstName, String lastName, String source, Integer pendingTestCount, Integer totalTestCount) {
        this.accessionNumber = accessionNumber;
        this.stNumber = stNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.source = source;
        this.pendingTestCount = pendingTestCount;
        this.totalTestCount = totalTestCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (accessionNumber != null ? !accessionNumber.equals(order.accessionNumber) : order.accessionNumber != null)
            return false;
        if (firstName != null ? !firstName.equals(order.firstName) : order.firstName != null) return false;
        if (lastName != null ? !lastName.equals(order.lastName) : order.lastName != null) return false;
        if (pendingTestCount != null ? !pendingTestCount.equals(order.pendingTestCount) : order.pendingTestCount != null)
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
        result = 31 * result + (totalTestCount != null ? totalTestCount.hashCode() : 0);
        return result;
    }
}
