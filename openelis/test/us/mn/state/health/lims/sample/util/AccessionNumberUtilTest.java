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