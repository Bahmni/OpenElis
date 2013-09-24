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
* Copyright (C) ITECH, University of Washington, Seattle WA.  All Rights Reserved.
*
*/
package us.mn.state.health.lims.common.util;

import us.mn.state.health.lims.role.dao.RoleDAO;
import us.mn.state.health.lims.role.daoimpl.RoleDAOImpl;
import us.mn.state.health.lims.role.valueholder.Role;
import us.mn.state.health.lims.siteinformation.valueholder.SiteInformation;

public class ConfigurationSideEffects {
	private static RoleDAO roleDAO = new RoleDAOImpl();
	
	public void siteInformationChanged( SiteInformation siteInformation){
		if( "modify results role".equals(siteInformation.getName())){
			Role modifierRole = roleDAO.getRoleByName("Results modifier");
			
			if( modifierRole != null && modifierRole.getId() != null){
				modifierRole.setActive("true".equals(siteInformation.getValue()));
				modifierRole.setSysUserId(siteInformation.getSysUserId());
				roleDAO.updateData(modifierRole);
			}
				
		}
	}
}
