package org.bahmni.feed.openelis.utils;

import us.mn.state.health.lims.login.dao.LoginDAO;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.login.valueholder.Login;
import us.mn.state.health.lims.siteinformation.dao.SiteInformationDAO;
import us.mn.state.health.lims.siteinformation.valueholder.SiteInformation;

public class AuditingService {
    private LoginDAO loginDao;
    private SiteInformationDAO siteInformationDao;

    public AuditingService(LoginDAO loginDao, SiteInformationDAO siteInformationDao) {
        this.loginDao = loginDao;
        this.siteInformationDao = siteInformationDao;
    }

    public String getSysUserId() {
        SiteInformation adminUser = siteInformationDao.getSiteInformationByName("AdminUser");
        if (adminUser != null) {
            String adminUserName = adminUser.getValue();
            Login userProfile = loginDao.getUserProfile(adminUserName);
            if (userProfile != null) {
                if (userProfile.getSysUserId() != null)
                    return userProfile.getSysUserId();
                return userProfile.getId();
            }
        }
        return null;
    }
}