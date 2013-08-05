package us.mn.state.health.lims.common.util;

import org.apache.commons.validator.GenericValidator;

public class IntegerUtil {

    public static Integer getParsedValueOrDefault(String value, int defaultValue) {
        if (GenericValidator.isBlankOrNull(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException e) {
            return defaultValue;
        }
    }
}
