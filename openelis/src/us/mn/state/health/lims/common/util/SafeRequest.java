package us.mn.state.health.lims.common.util;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SafeRequest extends HttpServletRequestWrapper {
    private final Logger logger = LogManager.getLogger(SafeRequest.class);
    private List<String> ignoreEncodingForParams = Arrays.asList("sampleXML");

    public SafeRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        HttpServletRequest request = (HttpServletRequest) this.getRequest();
        String encodedValue = getEncodedParamValue(name, request.getParameter(name));
        logger.debug(String.format("Intercepted action request: %s, name: %s, value= %s", request.getServletPath(), name, encodedValue));
        return encodedValue;
    }

    private String getEncodedParamValue(String name, String value) {
        if (ignoreEncodingForParams.contains(name)) {
            return value;
        }
        return StringUtil.encode(value);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        logger.debug("getParameterMap");
        Map<String, String[]> newParameterMap = new HashMap<>();
        Map<String, String[]> existingParameterMap = super.getParameterMap();
        for (String key : existingParameterMap.keySet()) {
            newParameterMap.put(key, getParameterValues(key));
        }
        return newParameterMap;
    }

    @Override
    public String[] getParameterValues(String name) {
        logger.debug("getParameterValues");
        String[] existingValues = super.getParameterValues(name);
        String[] newValues = new String[existingValues.length];
        for (int i = 0; i < existingValues.length; i++) {
            newValues[i] = getEncodedParamValue(name, existingValues[i]);
            logger.debug(String.format("Encoded param name: %s, value= %s", name, name, newValues[i]));
        }
        return newValues;
    }
}
