package us.mn.state.health.lims.dashboard.valueholder;

public class TodayStat {
    private int awaitingValidationCount;
    private int completedTestCount;
    private int totalSamplesCount;
    private int awaitingTestCount;

    public int getAwaitingValidationCount() {
        return awaitingValidationCount;
    }

    public int getCompletedTestCount() {
        return completedTestCount;
    }

    public int getTotalSamplesCount() {
        return totalSamplesCount;
    }

    public int getAwaitingTestCount() {
        return awaitingTestCount;
    }

    public void incrementPendingTestsCount() {
        awaitingTestCount++;
    }

    public void incrementPendingValidationCount() {
        awaitingValidationCount++;
    }

    public void incrementCompletedTestsCount() {
        completedTestCount++;
    }

    public void incrementSamplesCount() {
        totalSamplesCount++;
    }
}
