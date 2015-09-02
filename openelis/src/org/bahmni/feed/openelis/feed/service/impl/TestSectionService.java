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

package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.MinimalResource;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataDepartment;
import org.bahmni.feed.openelis.utils.AuditingService;
import us.mn.state.health.lims.common.exception.LIMSException;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.organization.dao.OrganizationDAO;
import us.mn.state.health.lims.organization.daoimpl.OrganizationDAOImpl;
import us.mn.state.health.lims.organization.valueholder.Organization;
import us.mn.state.health.lims.siteinformation.dao.SiteInformationDAO;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.siteinformation.valueholder.SiteInformation;
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.test.valueholder.TestSection;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

public class TestSectionService {

    private OrganizationDAO organizationDAO;
    private TestService testService;
    private PanelService panelService;
    private TestSectionDAO testSectionDAO;
    private AuditingService auditingService;
    private SiteInformationDAO siteInformationDAO;

    public TestSectionService() {
        this(new TestSectionDAOImpl(), new OrganizationDAOImpl(), new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl()), new TestService(), new PanelService(), new SiteInformationDAOImpl());
    }

    public TestSectionService(TestSectionDAO testSectionDAO, OrganizationDAO organizationDAO, AuditingService auditingService, TestService testService, PanelService panelService, SiteInformationDAO siteInformationDAO) {
        this.testSectionDAO = testSectionDAO;
        this.auditingService = auditingService;
        this.organizationDAO = organizationDAO;
        this.testService = testService;
        this.panelService = panelService;
        this.siteInformationDAO = siteInformationDAO;
    }

    public void createOrUpdate(ReferenceDataDepartment department) throws IOException, LIMSException {
        try {
            String sysUserId = auditingService.getSysUserId();
            TestSection testSection = testSectionDAO.getTestSectionByUUID(department.getId());
            SiteInformation defaultOrganizationName = siteInformationDAO.getSiteInformationByName("defaultOrganizationName");
            if(defaultOrganizationName == null){
                throw new LIMSRuntimeException("no property found for 'defaultOrganizationName' in site_information table");
            }
            final String organizationName = defaultOrganizationName.getValue();

            Organization organization = organizationDAO.getOrganizationByName(new Organization() {{
                this.setOrganizationName(organizationName);
            }}, true);

            if (organization == null) {
                throw new LIMSRuntimeException("organization does not exist" + organizationName);
            }

            if (testSection == null) {
                create(department, organization, sysUserId);
            } else {
                update(testSection, organization, department, sysUserId);
            }
            updateTests(department, sysUserId);
        } catch (Exception e) {
            throw new LIMSException(String.format("Error while saving Department - %s", department.getName()), e);
        }
    }

    private void updateTests(ReferenceDataDepartment department, String sysUserId) throws IOException, LIMSException {
        for (MinimalResource testData : department.getTests()) {
            Test test = testService.updateTestSection(testData.getName(), department.getId(), sysUserId);
            if (test == null) {
                throw new LIMSRuntimeException(String.format("Error while updating test section - %s on test  - %s", department.getName(), testData.getName()));
            }
        }
    }

    private void update(TestSection testSection, Organization organization, ReferenceDataDepartment department, String sysUserId) {
        testSection = populateTestSection(testSection, department, organization, sysUserId);
        testSectionDAO.updateData(testSection);
    }

    private void create(ReferenceDataDepartment department, Organization organization, String sysUserId) {
        TestSection testSection = new TestSection();
        testSection = populateTestSection(testSection, department, organization, sysUserId);
        testSectionDAO.insertData(testSection);
    }

    private TestSection populateTestSection(TestSection testSection, ReferenceDataDepartment department, Organization organization, String sysUserId) {
        testSection.setUUID(department.getId());
        testSection.setTestSectionName(department.getName());
        testSection.setDescription(department.getName());
        String isActive = department.getIsActive() ? "Y" : "N";
        testSection.setIsActive(isActive);
        testSection.setLastupdated(new Timestamp(new Date().getTime()));
        testSection.setSysUserId(sysUserId);
        testSection.setOrganization(organization);
        return testSection;
    }

}
