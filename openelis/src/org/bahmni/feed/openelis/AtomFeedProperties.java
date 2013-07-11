package org.bahmni.feed.openelis;


import java.util.ResourceBundle;

public class AtomFeedProperties {



    private static final String LAB_TEST_EVENT = "productType.labTest";
    private static final String PANEL_EVENT = "productType.panel";

    public static final String DEFAULT_PROPERTY_FILENAME = "atomfeed";

    private ResourceBundle resourceBundle;

    AtomFeedProperties(String propertyFilename) {
        this.resourceBundle = ResourceBundle.getBundle(propertyFilename);
    }

    public AtomFeedProperties() {
        this(DEFAULT_PROPERTY_FILENAME);
    }


    public String getSchedulerDelay() {
        return resourceBundle.getString("scheduler.fixed.delay");
    }

    public String getFeedUri(String feedname) {
        return resourceBundle.getString(feedname);
    }

    public String getProductTypeLabTest(){
        return resourceBundle.getString(LAB_TEST_EVENT);
    }

    public String getProductTypePanel(){
        return resourceBundle.getString(PANEL_EVENT);
    }

}
