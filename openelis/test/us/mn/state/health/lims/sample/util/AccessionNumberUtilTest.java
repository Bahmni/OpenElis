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

package us.mn.state.health.lims.sample.util;

import junit.framework.Assert;
import org.junit.Test;

public class AccessionNumberUtilTest {
    @Test
    public void getSortOrder_for_community_health_program_accession_numbers() {
        String communityHealthAccessionNumber = "123456sdf-55";
        int communityHealthSortOrder = AccessionNumberUtil.getSortOrder(communityHealthAccessionNumber);
        Assert.assertEquals(55, communityHealthSortOrder);
    }

    @Test
    public void getSortOrder_for_normal_accession_numbers() {
        String communityHealthAccessionNumber = "123456-003-58";
        int communityHealthSortOrder = AccessionNumberUtil.getSortOrder(communityHealthAccessionNumber);
        Assert.assertEquals(58, communityHealthSortOrder);
    }

    @Test
    public void getSortOrder_for_any_accession_number() {
        String communityHealthAccessionNumber = "12345sadfsafd-asdf6-003-99";
        int communityHealthSortOrder = AccessionNumberUtil.getSortOrder(communityHealthAccessionNumber);
        Assert.assertEquals(99, communityHealthSortOrder);
    }
}
