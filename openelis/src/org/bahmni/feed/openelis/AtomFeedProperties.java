package org.bahmni.feed.openelis;


import java.util.ResourceBundle;

public class AtomFeedProperties {

//    private @Value("${feed.generator.uri}") String feedUri;
//    private @Value("${openerp.host}") String host;
//    private @Value("${openerp.port}") int port;
//    private @Value("${openerp.database}") String database;
//    private @Value("${openerp.user}") String user;
//    private @Value("${openerp.password}") String password;
//    private @Value("${scheduler.fixed.delay}") String schedulerDelay;


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

   /* @Override
    public String getHost() {
        return resourceBundle.getString("openerp.host");
    }

    @Override
    public int getPort() {
        return Integer.parseInt(resourceBundle.getString("openerp.port"));
    }

    @Override
    public String getDatabase() {
        return resourceBundle.getString("openerp.database");
    }

    @Override
    public String getUser() {
        return resourceBundle.getString("openerp.user");
    }

    @Override
    public String getPassword() {
        return resourceBundle.getString("openerp.password");
    }*/
}
