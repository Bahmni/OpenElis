package us.mn.state.health.lims.ws.handler;

public class WebServicesInput {
    private final String url;
    private final String URL_PATTERN = ".*/ws/rest/([^/]*)/([^/]*).*";
    private final String RESOURCE_NAME_LOCATION = "$1";
    private final String RESOURCE_UUID_LOCATION = "$2";

    public WebServicesInput(String url) {
        this.url = trimSpacesAndTrailingSlash(url);
    }

    public String getResourceName() {
        return matchingValueIfUrlIsGood(RESOURCE_NAME_LOCATION);
    }

    public String getResourceUuid() {
        return matchingValueIfUrlIsGood(RESOURCE_UUID_LOCATION);
    }

    private String matchingValueIfUrlIsGood(String location) {
        return urlMatchesPattern() ? matchingValueAt(location) : null;
    }

    private String matchingValueAt(String location) {
        return url.replaceAll(URL_PATTERN, location);
    }

    private boolean urlMatchesPattern() {
        return url.matches(URL_PATTERN);
    }

    private String trimSpacesAndTrailingSlash(String request) {
        request = request.trim();
        if (request.endsWith("/")) request = request.substring(0, request.length() -1);
        return request;
    }
}
