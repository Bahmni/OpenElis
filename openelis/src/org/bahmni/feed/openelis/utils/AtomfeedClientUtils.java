package org.bahmni.feed.openelis.utils;

import us.mn.state.health.lims.login.dao.LoginDAO;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.login.valueholder.Login;
import us.mn.state.health.lims.siteinformation.dao.SiteInformationDAO;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.siteinformation.valueholder.SiteInformation;

public class AtomfeedClientUtils {

    private static LoginDAO loginDao;

    private static SiteInformationDAO siteInformationDao;

    public static String getSysUserId() {
        if(siteInformationDao == null){
            siteInformationDao = new SiteInformationDAOImpl();
        }

        SiteInformation adminUser = siteInformationDao.getSiteInformationByName("AdminUser");
        if(adminUser != null){
            String adminUserName = adminUser.getValue();
            if(loginDao == null){
                loginDao = new LoginDAOImpl();
            }
            Login userProfile = loginDao.getUserProfile(adminUserName);
            if (userProfile != null) {
                return userProfile.getSysUserId();
            }
        }
        return null;
    }

    public static void setLoginDao(LoginDAO loginDao) {
        AtomfeedClientUtils.loginDao = loginDao;
    }

    public static void setSiteInformationDao(SiteInformationDAO siteInformationDao) {
        AtomfeedClientUtils.siteInformationDao = siteInformationDao;
    }
}