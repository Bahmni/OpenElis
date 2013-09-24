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

package org.bahmni.feed.openelis.utils;

import us.mn.state.health.lims.login.dao.LoginDAO;
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
