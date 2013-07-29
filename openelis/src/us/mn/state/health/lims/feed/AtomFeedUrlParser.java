package us.mn.state.health.lims.feed;

public class AtomFeedUrlParser {
    private String requestUrl;
    private final String URL_WITH_CATEGORY = ".*/feed/(.*)/(.*)";
    private final String URL_WITHOUT_CATEGORY = ".*/feed/([^/]*)";
    private final String RECENT_FEED = ".*/recent";

    public AtomFeedUrlParser(String requestUrl) {
        this.requestUrl = trimTrailingSlashes(requestUrl);
    }

    public String getCategory() {
        return urlHasCategory() ? substringFromUrl(requestUrl, URL_WITH_CATEGORY, "$1") : null;
    }

    public int getFeedMarker() {
        if(urlHasCategory())
            return feedMarkerForUrlWithCategory();
        if(urlHasNoCategory())
            return feedMarkerForUrlWithOutCategory();
        return 0;
     }

    public boolean isForRecentFeed() {
        return requestUrl.matches(RECENT_FEED);
    }

    private String trimTrailingSlashes(String requestUrl) {
        String url = requestUrl.trim();
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    private String substringFromUrl(String requestUrl, String regex, String substringRegex) {
        return requestUrl.replaceAll(regex, substringRegex);
    }
    private int asInteger(String str) {
        return Integer.parseInt(str);
    }

    private boolean urlHasCategory() {
        return requestUrl.matches(URL_WITH_CATEGORY);
    }

    private boolean urlHasNoCategory() {
        return requestUrl.matches(URL_WITHOUT_CATEGORY);
    }

    private int feedMarkerForUrlWithCategory() {
        String feedMarker = substringFromUrl(requestUrl, URL_WITH_CATEGORY, "$2");
        return feedMarker.matches("[0-9]*") ? asInteger(feedMarker) : 0;
    }

    private int feedMarkerForUrlWithOutCategory() {
        String feedMarker = substringFromUrl(requestUrl, URL_WITHOUT_CATEGORY, "$1");
        return feedMarker.matches("[0-9]*") ? asInteger(feedMarker) : 0;
    }
}

