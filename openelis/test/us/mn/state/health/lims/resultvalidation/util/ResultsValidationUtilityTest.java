package us.mn.state.health.lims.resultvalidation.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import us.mn.state.health.lims.analyte.daoimpl.AnalyteDAOImpl;
import us.mn.state.health.lims.analyte.valueholder.Analyte;
import us.mn.state.health.lims.common.services.TestIdentityService;
import us.mn.state.health.lims.observationhistorytype.daoImpl.ObservationHistoryTypeDAOImpl;
import us.mn.state.health.lims.observationhistorytype.valueholder.ObservationHistoryType;
import us.mn.state.health.lims.referencetables.daoimpl.ReferenceTablesDAOImpl;
import us.mn.state.health.lims.referencetables.valueholder.ReferenceTables;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.result.daoimpl.ResultDAOImpl;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.resultvalidation.action.util.ResultValidationItem;
import us.mn.state.health.lims.resultvalidation.bean.AnalysisItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({StatusOfSampleUtil.class, ResultsValidationUtility.class, TestIdentityService.class})
@SuppressStaticInitializationFor({"us.mn.state.health.lims.result.action.util.ResultsLoadUtility", "us.mn.state.health.lims.common.services.TestIdentityService"})
public class ResultsValidationUtilityTest {

    @Mock
    private AnalyteDAOImpl analyteDAO;

    @Mock
    private Analyte conclusionAnalyte;

    @Mock
    private Analyte cd4CountAnalyte;

    @Mock
    private TestDAOImpl testDAO;

    @Mock
    private ReferenceTablesDAOImpl referenceTablesDAO;

    @Mock
    private ReferenceTables referenceTables;

    @Mock
    private ObservationHistoryTypeDAOImpl observationHistoryTypeDAO;

    @Mock
    private ObservationHistoryType observationHistoryType;

    @Mock
    private ResultsLoadUtility resultsLoadUtility;

    @Mock
    private ResultDAOImpl resultDAO;

    @Mock
    private Result result;

    private ResultsValidationUtility resultsValidationUtility;

    @Before
    public void setUp() throws Exception {
        mockStatic(StatusOfSampleUtil.class);
        mockStatic(TestIdentityService.class);
        whenNew(AnalyteDAOImpl.class).withNoArguments().thenReturn(analyteDAO);
        whenNew(Analyte.class).withNoArguments()
                .thenReturn(conclusionAnalyte)
                .thenReturn(cd4CountAnalyte);
        whenNew(TestDAOImpl.class).withNoArguments().thenReturn(testDAO);
        whenNew(ReferenceTablesDAOImpl.class).withNoArguments().thenReturn(referenceTablesDAO);
        whenNew(ReferenceTables.class).withNoArguments().thenReturn(referenceTables);
        whenNew(ObservationHistoryTypeDAOImpl.class).withNoArguments().thenReturn(observationHistoryTypeDAO);
        whenNew(ResultsLoadUtility.class).withNoArguments().thenReturn(resultsLoadUtility);
        whenNew(ResultDAOImpl.class).withNoArguments().thenReturn(resultDAO);
        when(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Finalized)).thenReturn("10");
        when(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.TechnicalRejected)).thenReturn("19");
        when(analyteDAO.getAnalyteByName(conclusionAnalyte, false)).thenReturn(conclusionAnalyte);
        when(analyteDAO.getAnalyteByName(cd4CountAnalyte, false)).thenReturn(cd4CountAnalyte);
        when(conclusionAnalyte.getId()).thenReturn("100");
        when(cd4CountAnalyte.getId()).thenReturn("110");
        when(referenceTablesDAO.getReferenceTableByName(referenceTables)).thenReturn(referenceTables);
        when(referenceTables.getId()).thenReturn("120");
        when(observationHistoryTypeDAO.getByName("SampleRecordStatus")).thenReturn(observationHistoryType);
        when(resultDAO.getResultById("210")).thenReturn(result);
        when(resultDAO.getResultById("220")).thenReturn(result);
        when(resultDAO.getResultById("230")).thenReturn(result);
        when(result.getValue()).thenReturn("1001");

        resultsValidationUtility = new ResultsValidationUtility();
    }

    @Test
    public void shouldReturnSortedAnalysisItemListByAccessionNumber() {
        ResultValidationItem resultValidationItem1 = new ResultValidationItem();
        resultValidationItem1.setAccessionNumber("10");
        resultValidationItem1.setSequenceNumber("1");
        resultValidationItem1.setResult(result);
        resultValidationItem1.setResultId("210");
        resultValidationItem1.setTestName("testName1");

        ResultValidationItem resultValidationItem2 = new ResultValidationItem();
        resultValidationItem2.setAccessionNumber("30");
        resultValidationItem2.setSequenceNumber("3");
        resultValidationItem2.setResult(result);
        resultValidationItem2.setResultId("220");
        resultValidationItem2.setTestName("testName2");

        ResultValidationItem resultValidationItem3 = new ResultValidationItem();
        resultValidationItem3.setAccessionNumber("20");
        resultValidationItem3.setSequenceNumber("2");
        resultValidationItem3.setResult(result);
        resultValidationItem3.setResultId("230");
        resultValidationItem3.setTestName("testName3");

        List<AnalysisItem> analysisItems = resultsValidationUtility.testResultListToAnalysisItemList(Arrays.asList(resultValidationItem1, resultValidationItem2, resultValidationItem3));

        assertEquals(resultValidationItem2.getAccessionNumber(), analysisItems.get(0).getAccessionNumber());
        assertEquals(resultValidationItem3.getAccessionNumber(), analysisItems.get(1).getAccessionNumber());
        assertEquals(resultValidationItem1.getAccessionNumber(), analysisItems.get(2).getAccessionNumber());
    }

    @Test
    public void shouldReturnSortedAnalysisItemByTestSortNumberWhenAccessionNumberIsSame() {
        ResultValidationItem resultValidationItem1 = new ResultValidationItem();
        resultValidationItem1.setAccessionNumber("10");
        resultValidationItem1.setSequenceNumber("1");
        resultValidationItem1.setResult(result);
        resultValidationItem1.setResultId("210");
        resultValidationItem1.setTestName("testName1");
        resultValidationItem1.setTestSortNumber("313");

        ResultValidationItem resultValidationItem2 = new ResultValidationItem();
        resultValidationItem2.setAccessionNumber("10");
        resultValidationItem2.setSequenceNumber("1");
        resultValidationItem2.setResult(result);
        resultValidationItem2.setResultId("220");
        resultValidationItem2.setTestName("testName2");
        resultValidationItem2.setTestSortNumber("300");

        ResultValidationItem resultValidationItem3 = new ResultValidationItem();
        resultValidationItem3.setAccessionNumber("10");
        resultValidationItem3.setSequenceNumber("1");
        resultValidationItem3.setResult(result);
        resultValidationItem3.setResultId("230");
        resultValidationItem3.setTestName("testName3");
        resultValidationItem3.setTestSortNumber("315");

        List<AnalysisItem> analysisItems = resultsValidationUtility.testResultListToAnalysisItemList(Arrays.asList(resultValidationItem1, resultValidationItem2, resultValidationItem3));

        assertEquals(resultValidationItem2.getTestSortNumber(), analysisItems.get(0).getTestSortNumber());
        assertEquals(resultValidationItem1.getTestSortNumber(), analysisItems.get(1).getTestSortNumber());
        assertEquals(resultValidationItem3.getTestSortNumber(), analysisItems.get(2).getTestSortNumber());
    }

    @Test
    public void shouldReturnSortedAnalysisItemByTestNameWhenTestSortNumberIsNotANumber() {
        ResultValidationItem resultValidationItem1 = new ResultValidationItem();
        resultValidationItem1.setAccessionNumber("10");
        resultValidationItem1.setSequenceNumber("1");
        resultValidationItem1.setResult(result);
        resultValidationItem1.setResultId("210");
        resultValidationItem1.setTestName("testName3");
        resultValidationItem1.setTestSortNumber("315");

        ResultValidationItem resultValidationItem2 = new ResultValidationItem();
        resultValidationItem2.setAccessionNumber("10");
        resultValidationItem2.setSequenceNumber("1");
        resultValidationItem2.setResult(result);
        resultValidationItem2.setResultId("220");
        resultValidationItem2.setTestName("testName1");
        resultValidationItem2.setTestSortNumber("300");

        ResultValidationItem resultValidationItem3 = new ResultValidationItem();
        resultValidationItem3.setAccessionNumber("10");
        resultValidationItem3.setSequenceNumber("1");
        resultValidationItem3.setResult(result);
        resultValidationItem3.setResultId("230");
        resultValidationItem3.setTestName("testName2");
        resultValidationItem3.setTestSortNumber("315-3");

        List<AnalysisItem> analysisItems = resultsValidationUtility.testResultListToAnalysisItemList(Arrays.asList(resultValidationItem1, resultValidationItem2, resultValidationItem3));

        assertEquals(resultValidationItem2.getTestSortNumber(), analysisItems.get(0).getTestSortNumber());
        assertEquals(resultValidationItem3.getTestSortNumber(), analysisItems.get(1).getTestSortNumber());
        assertEquals(resultValidationItem1.getTestSortNumber(), analysisItems.get(2).getTestSortNumber());
    }

    @Test
    public void shouldReturnSortedAnalysisItemByTestNameWhenTestSortNumberIsBlankOrNull() {
        ResultValidationItem resultValidationItem1 = new ResultValidationItem();
        resultValidationItem1.setAccessionNumber("10");
        resultValidationItem1.setSequenceNumber("1");
        resultValidationItem1.setResult(result);
        resultValidationItem1.setResultId("210");
        resultValidationItem1.setTestName("testName3");
        resultValidationItem1.setTestSortNumber(null);

        ResultValidationItem resultValidationItem2 = new ResultValidationItem();
        resultValidationItem2.setAccessionNumber("10");
        resultValidationItem2.setSequenceNumber("1");
        resultValidationItem2.setResult(result);
        resultValidationItem2.setResultId("220");
        resultValidationItem2.setTestName("testName1");
        resultValidationItem2.setTestSortNumber("");

        ResultValidationItem resultValidationItem3 = new ResultValidationItem();
        resultValidationItem3.setAccessionNumber("10");
        resultValidationItem3.setSequenceNumber("1");
        resultValidationItem3.setResult(result);
        resultValidationItem3.setResultId("230");
        resultValidationItem3.setTestName("testName2");
        resultValidationItem3.setTestSortNumber("");

        List<AnalysisItem> analysisItems = resultsValidationUtility.testResultListToAnalysisItemList(Arrays.asList(resultValidationItem1, resultValidationItem2, resultValidationItem3));

        assertEquals(resultValidationItem2.getTestName(), analysisItems.get(0).getTestName());
        assertEquals(resultValidationItem3.getTestName(), analysisItems.get(1).getTestName());
        assertEquals(resultValidationItem1.getTestName(), analysisItems.get(2).getTestName());
    }
}