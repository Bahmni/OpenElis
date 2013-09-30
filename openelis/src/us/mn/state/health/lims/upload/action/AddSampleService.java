package us.mn.state.health.lims.upload.action;

import us.mn.state.health.lims.address.daoimpl.OrganizationAddressDAOImpl;
import us.mn.state.health.lims.address.valueholder.OrganizationAddress;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.observationhistory.dao.ObservationHistoryDAO;
import us.mn.state.health.lims.observationhistory.daoimpl.ObservationHistoryDAOImpl;
import us.mn.state.health.lims.observationhistory.valueholder.ObservationHistory;
import us.mn.state.health.lims.organization.daoimpl.OrganizationDAOImpl;
import us.mn.state.health.lims.organization.daoimpl.OrganizationOrganizationTypeDAOImpl;
import us.mn.state.health.lims.organization.valueholder.Organization;
import us.mn.state.health.lims.project.dao.ProjectDAO;
import us.mn.state.health.lims.project.daoimpl.ProjectDAOImpl;
import us.mn.state.health.lims.project.valueholder.Project;
import us.mn.state.health.lims.requester.dao.SampleRequesterDAO;
import us.mn.state.health.lims.requester.daoimpl.SampleRequesterDAOImpl;
import us.mn.state.health.lims.requester.valueholder.SampleRequester;
import us.mn.state.health.lims.sample.bean.SampleTestCollection;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.util.AnalysisBuilder;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleproject.dao.SampleProjectDAO;
import us.mn.state.health.lims.sampleproject.daoimpl.SampleProjectDAOImpl;
import us.mn.state.health.lims.sampleproject.valueholder.SampleProject;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;

import java.util.List;

import static org.apache.commons.validator.GenericValidator.isBlankOrNull;

public class AddSampleService {

    public void persist(AnalysisBuilder analysisBuilder, boolean useInitialSampleCondition,
                        Organization newOrganization, SampleRequester requesterSite,
                        List<OrganizationAddress> orgAddressExtra,
                        Sample sample, List<SampleTestCollection> sampleItemsTests,
                        List<ObservationHistory> observations, SampleHuman sampleHuman,
                        String patientId, String projectId,
                        String providerId, String currentUserId,
                        long provider_requester_type_id, String referring_org_type_id) {
        persistOrganizationData(newOrganization, requesterSite, orgAddressExtra, referring_org_type_id);

//			persistProviderData();
        persistSampleData(analysisBuilder, sample, projectId, sampleItemsTests, sampleHuman, patientId, providerId, currentUserId);
        persistRequesterData(providerId, sample, currentUserId, requesterSite, newOrganization, provider_requester_type_id);
        if (useInitialSampleCondition) {
            persistInitialSampleConditions(sampleItemsTests, patientId, currentUserId);
        }

        persistObservations(observations, sample, patientId);
    }

    private void persistOrganizationData(Organization newOrganization, SampleRequester requesterSite, List<OrganizationAddress> orgAddressExtra, String referring_org_type_id) {
        if (newOrganization != null) {
            new OrganizationDAOImpl().insertData(newOrganization);
            new OrganizationOrganizationTypeDAOImpl().linkOrganizationAndType(newOrganization, referring_org_type_id);
            if (requesterSite != null) {
                requesterSite.setRequesterId(newOrganization.getId());
            }

            for (OrganizationAddress address : orgAddressExtra) {
                address.setOrganizationId(newOrganization.getId());
                new OrganizationAddressDAOImpl().insert(address);
            }
        }
    }

    private static void persistSampleData(AnalysisBuilder analysisBuilder, Sample sample, String projectId,
                                          List<SampleTestCollection> sampleItemsTests,
                                          SampleHuman sampleHuman, String patientId, String providerId, String currentUserId) {
        SampleDAO sampleDAO = new SampleDAOImpl();
        SampleHumanDAO sampleHumanDAO = new SampleHumanDAOImpl();
        SampleItemDAO sampleItemDAO = new SampleItemDAOImpl();
        AnalysisDAO analysisDAO = new AnalysisDAOImpl();
        TestDAO testDAO = new TestDAOImpl();
        String analysisRevision = SystemConfiguration.getInstance().getAnalysisDefaultRevision();

        sampleDAO.insertDataWithAccessionNumber(sample);

        if (!isBlankOrNull(projectId)) {
            persistSampleProject(projectId, sample, currentUserId);
        }

        for (SampleTestCollection sampleTestPair : sampleItemsTests) {

            sampleItemDAO.insertData(sampleTestPair.item);

            for (Test test : sampleTestPair.tests) {
                testDAO.getData(test);

                Analysis analysis = analysisBuilder.populateAnalysis(analysisRevision, sampleTestPair, test);
                analysisDAO.insertData(analysis, false); // false--do not check
                // for duplicates
            }

        }

        sampleHuman.setSampleId(sample.getId());
        sampleHuman.setPatientId(patientId);
        sampleHuman.setProviderId(providerId);

        sampleHumanDAO.insertData(sampleHuman);
    }

    private static void persistSampleProject(String projectId, Sample sample, String currentUserId) throws LIMSRuntimeException {
        SampleProjectDAO sampleProjectDAO = new SampleProjectDAOImpl();
        ProjectDAO projectDAO = new ProjectDAOImpl();
        Project project = new Project();
        project.setId(projectId);
        projectDAO.getData(project);

        SampleProject sampleProject = new SampleProject();
        sampleProject.setProject(project);
        sampleProject.setSample(sample);
        sampleProject.setSysUserId(currentUserId);
        sampleProjectDAO.insertData(sampleProject);
    }

    private static void persistRequesterData(String providerId, Sample sample, String currentUserId, SampleRequester requesterSite, Organization newOrganization, long provider_requester_type_id) {
        SampleRequesterDAO sampleRequesterDAO = new SampleRequesterDAOImpl();
        if (providerId != null && !isBlankOrNull(providerId)) {
            SampleRequester sampleRequester = new SampleRequester();
            sampleRequester.setRequesterId(providerId);
            sampleRequester.setRequesterTypeId(provider_requester_type_id);
            sampleRequester.setSampleId(sample.getId());
            sampleRequester.setSysUserId(currentUserId);
            sampleRequesterDAO.insertData(sampleRequester);
        }

        if (requesterSite != null) {
            requesterSite.setSampleId(sample.getId());
            if( newOrganization != null){
                requesterSite.setRequesterId(newOrganization.getId());
            }
            sampleRequesterDAO.insertData(requesterSite);
        }
    }

    private static void persistInitialSampleConditions(List<SampleTestCollection> sampleItemsTests, String patientId, String currentUserId) {
        ObservationHistoryDAO ohDAO = new ObservationHistoryDAOImpl();

        for (SampleTestCollection sampleTestCollection : sampleItemsTests) {
            List<ObservationHistory> initialConditions = sampleTestCollection.initialSampleConditionIdList;

            if (initialConditions != null) {
                for (ObservationHistory observation : initialConditions) {
                    observation.setSampleId(sampleTestCollection.item.getSample().getId());
                    observation.setSampleItemId(sampleTestCollection.item.getId());
                    observation.setPatientId(patientId);
                    observation.setSysUserId(currentUserId);
                    ohDAO.insertData(observation);
                }
            }
        }
    }

    private static void persistObservations(List<ObservationHistory> observations, Sample sample, String patientId) {
        ObservationHistoryDAO observationDAO = new ObservationHistoryDAOImpl();
        for (ObservationHistory observation : observations) {
            observation.setSampleId(sample.getId());
            observation.setPatientId(patientId);
            observationDAO.insertData(observation);
        }
    }
}
