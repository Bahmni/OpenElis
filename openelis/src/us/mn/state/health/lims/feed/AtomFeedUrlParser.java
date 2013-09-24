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

