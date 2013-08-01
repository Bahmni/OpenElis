package org.bahmni.feed.openelis;

import us.mn.state.health.lims.common.log.LogEvent;

import java.io.InputStream;
import java.util.Properties;

public class AtomFeedProperties {
    private static final String LAB_TEST_EVENT = "productType.labTest";
    private static final String PANEL_EVENT = "productType.panel";

    private static final String FEED_CONNECT_TIMEOUT = "feed.connectionTimeoutInMilliseconds";
    private static final String FEED_REPLY_TIMEOUT = "feed.replyTimeoutInMilliseconds";
    private static final String FEED_MAX_FAILED_EVENTS = "feed.maxFailedEvents";

    public static final String DEFAULT_PROPERTY_FILENAME = "/atomfeed.properties";

    private Properties properties;

    private static AtomFeedProperties atomFeedProperties;

    private AtomFeedProperties() {
        InputStream propertyStream = null;
        try {
            propertyStream = this.getClass().getResourceAsStream(DEFAULT_PROPERTY_FILENAME);
            properties = new Properties();
            properties.load(propertyStream);

        } catch (Exception e) {
            LogEvent.logError("AtomFeedProperties", "Constructor", e.toString());
        } finally {
            if (null != propertyStream) {
                try {
                    propertyStream.close();
                    propertyStream = null;
                } catch (Exception e) {
                    LogEvent.logError("AtomFeedProperties","Constructor final",e.toString());
                }
            }

        }
    }

    public static AtomFeedProperties getInstance() {
        if (atomFeedProperties == null) {
            synchronized (AtomFeedProperties.class) {
                if (atomFeedProperties == null) {
                    atomFeedProperties = new AtomFeedProperties();
                }
            }
        }
        return atomFeedProperties;
    }


    public String getProperty(String feedname) {
        return properties.getProperty(feedname);
    }

    public String getProductTypeLabTest(){
        return properties.getProperty(LAB_TEST_EVENT);
    }

    public String getProductTypePanel(){
        return properties.getProperty(PANEL_EVENT);
    }

    public String getFeedConnectionTimeout(){
        return properties.getProperty(FEED_CONNECT_TIMEOUT);
    }

    public String getFeedReplyTimeout(){
        return properties.getProperty(FEED_REPLY_TIMEOUT);
    }

    public String getMaxFailedEvents() {
        return properties.getProperty(FEED_MAX_FAILED_EVENTS);
    }
}
