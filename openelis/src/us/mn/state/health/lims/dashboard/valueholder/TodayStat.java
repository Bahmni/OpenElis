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
