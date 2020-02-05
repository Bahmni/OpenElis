package us.mn.state.health.lims.typeofteststatus.valueholder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum AllowedTestStatusTypes {
    SAMPLE("SAMPLE"),
    TEST("TEST");
    private String statusType;

    AllowedTestStatusTypes(String statusType) {
        this.statusType = statusType;
    }

    public String getStatusType() {
        return statusType;
    }

    public static List<String> getAllAllowedTestStatusTypes() {
        List<String> allowedStatusTypes = new ArrayList<>();
        for (AllowedTestStatusTypes statusTypeOb: AllowedTestStatusTypes.values()) {
            allowedStatusTypes.add(statusTypeOb.getStatusType());
        }
        return allowedStatusTypes;
    }
}
