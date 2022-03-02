package org.bahmni.feed.openelis.feed.event;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSEncounter;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.bahmni.webclients.HttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.siteinformation.valueholder.SiteInformation;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class, EncounterFeedWorker.class})
public class EncounterFeedWorkerTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private OpenMRSEncounter openMRSEncounter;

    @Mock
    private Logger logger;

    @Mock
    private LoginDAOImpl loginDAO;

    @Mock
    private SiteInformationDAOImpl siteInformationDAO;

    @Mock
    private AuditingService auditingService;

    @Mock
    private SiteInformation siteInformation;

    @Mock
    private GroupByEncounterFeedProcessor groupByEncounterFeedProcessor;

    @Mock
    private GroupBySampleTypeFeedProcessor groupBySampleTypeFeedProcessor;

    @Before
    public void setUp() throws Exception {
        mockStatic(Logger.class);
        whenNew(LoginDAOImpl.class).withNoArguments().thenReturn(loginDAO);
        whenNew(SiteInformationDAOImpl.class).withNoArguments().thenReturn(siteInformationDAO);
        whenNew(AuditingService.class).withArguments(loginDAO, siteInformationDAO).thenReturn(auditingService);

        when(LogManager.getLogger(EncounterFeedWorker.class)).thenReturn(logger);
        when(openMRSEncounter.getEncounterUuid()).thenReturn("encounter uuid");
        doNothing().when(logger).info("Processing encounter with ID='encounter uuid'");
        when(auditingService.getSysUserId()).thenReturn("1");
    }

    @Test
    public void shouldCallGroupByEncounterProcessWhenThereIsNoAccessionStrategy() throws Exception {
        whenNew(GroupByEncounterFeedProcessor.class).withAnyArguments().thenReturn(groupByEncounterFeedProcessor);

        when(siteInformationDAO.getSiteInformationByName("accessionStrategy")).thenReturn(null);
        doNothing().when(groupByEncounterFeedProcessor).process(openMRSEncounter, "1");

        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(httpClient, "");
        encounterFeedWorker.process(openMRSEncounter);

        verify(groupByEncounterFeedProcessor, times(1)).process(openMRSEncounter, "1");
        verify(siteInformationDAO, times(1)).getSiteInformationByName("accessionStrategy");
        verify(openMRSEncounter, times(1)).getEncounterUuid();
        verify(auditingService, times(1)).getSysUserId();
    }

    @Test
    public void shouldCallGroupBySampleProcessWhenThereIsGroupBySampleAccessionStrategy() throws Exception {
        whenNew(GroupBySampleTypeFeedProcessor.class).withAnyArguments().thenReturn(groupBySampleTypeFeedProcessor);

        when(siteInformationDAO.getSiteInformationByName("accessionStrategy")).thenReturn(siteInformation);
        when(siteInformation.getValue()).thenReturn("groupBySample");
        doNothing().when(groupBySampleTypeFeedProcessor).process(openMRSEncounter, "1");

        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(httpClient, "");
        encounterFeedWorker.process(openMRSEncounter);

        verify(groupBySampleTypeFeedProcessor, times(1)).process(openMRSEncounter, "1");
        verify(siteInformationDAO, times(1)).getSiteInformationByName("accessionStrategy");
        verify(openMRSEncounter, times(1)).getEncounterUuid();
        verify(auditingService, times(1)).getSysUserId();
    }
}