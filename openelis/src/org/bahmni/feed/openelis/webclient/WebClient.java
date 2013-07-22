package org.bahmni.feed.openelis.webclient;

import us.mn.state.health.lims.common.exception.LIMSRuntimeException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

public class WebClient {
    public String get(URI uri, Map<String, String> headers) {
        HttpURLConnection connection = null;
        StringBuilder stringBuilder = new StringBuilder();
//        int responseCode = 0;
        try {
            connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("GET");
            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
            }
            connection.setDoOutput(true);
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append(System.getProperty("line.separator"));
            }
//            responseCode = connection.getResponseCode();
        } catch (Exception e) {
            throw new LIMSRuntimeException(e.getMessage(), e);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return stringBuilder.toString();
    }
}