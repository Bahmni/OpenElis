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

package us.mn.state.health.lims.ws.handler;

import org.bahmni.feed.openelis.feed.service.impl.TestResultService;
import org.bahmni.openelis.domain.TestResultDetails;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;

import java.sql.SQLException;

public class TestResultHandler implements Handler<TestResultDetails> {
    private final String RESULT = "RESULT";
    private TestResultService testResultService;

    public TestResultHandler(TestResultService testResultService) {
        this.testResultService = testResultService;
    }

    @Override
    public boolean canHandle(String resourceName) {
        return resourceName.equalsIgnoreCase(RESULT);
    }

    @Override
    public TestResultDetails handle(String resultId) {
        try {
            return this.testResultService.detailsFor(resultId);
        } catch (SQLException e) {
            throw new LIMSRuntimeException("Exception getting details for result: ", e);
        }
    }
}
