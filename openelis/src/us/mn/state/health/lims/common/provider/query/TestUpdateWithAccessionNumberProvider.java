package us.mn.state.health.lims.common.provider.query;

import org.bahmni.feed.openelis.feed.service.EventPublishers;
import org.bahmni.feed.openelis.feed.service.impl.OpenElisUrlPublisher;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class TestUpdateWithAccessionNumberProvider extends BaseQueryProvider {
    private SampleDAO sampleDao = new SampleDAOImpl();
    private AnalysisDAO analysisDAO = new AnalysisDAOImpl();
    private SampleItemDAO sampleItemDao = new SampleItemDAOImpl();
    private TypeOfSampleDAO typeOfSampleDAO = new TypeOfSampleDAOImpl();
    private TestDAO testDAO = new TestDAOImpl();
    private OpenElisUrlPublisher accessionPublisher = new EventPublishers().accessionPublisher();

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accessionNumber = request.getParameter("accessionNumber");
        String sampleId = request.getParameter("sampleId");

        Sample sample = sampleDao.getSampleByID(sampleId);
        sampleDao.updateData(sample);
        sample.setAccessionNumber(accessionNumber);
        String sysUserId = sample.getSysUserId();

        Set includedSampleStatusList = new HashSet<Integer>();
        includedSampleStatusList.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered)));

        List<SampleItem> sampleItems = sampleItemDao.getSampleItemsBySampleIdAndStatus(sampleId,includedSampleStatusList);
        int noOfSampleItems = sampleItems.size();

        StringBuilder xml = new StringBuilder();
        Map<String, String[]> allTypeAndTestIdsFromForm = new HashMap<>();
        Map<String, List<String>> alreadyExistingTypeAndTestIds = new HashMap<>();

        try {
            JSONObject formData = (JSONObject) new JSONParser().parse(request.getParameter("typeAndTestIds"));
            JSONObject formTestData = (JSONObject) formData.get("tests");
            JSONObject formTypeData = (JSONObject) formData.get("types");
            int size = formTypeData.size();
            for (int i = 0; i < size; i++) {
                String value = Integer.toString(i);
                String type = (String) formTypeData.get(value);
                String tests = (String) formTestData.get(value);
                allTypeAndTestIdsFromForm.put(type, tests.split(","));
            }
            for (SampleItem sampleItem : sampleItems) {
                List<Analysis> analysesBySampleItems = new ArrayList<>();
                List<String> existingTestIds = new ArrayList<>();
                analysesBySampleItems.addAll(analysisDAO.getAnalysesBySampleItem(sampleItem));
                for (Analysis analysis : analysesBySampleItems) {
                    existingTestIds.add(analysis.getTest().getId());
                }
                alreadyExistingTypeAndTestIds.put(sampleItem.getTypeOfSampleId(), existingTestIds);
            }

            for (Map.Entry<String, String[]> entry : allTypeAndTestIdsFromForm.entrySet()) {
                String typeOfSampleId = entry.getKey();
                List<String> allTestsFromForm = Arrays.asList(entry.getValue());
                if (alreadyExistingTypeAndTestIds.containsKey(typeOfSampleId)) {
                    //add new analysis for existing sample item
                    List<String> alreadyExistingTestIds = alreadyExistingTypeAndTestIds.get(typeOfSampleId);

                    List<String> newlyAddedTestIds = new ArrayList<>();
                    newlyAddedTestIds.addAll(allTestsFromForm);

                    List<String> removedTestIds = new ArrayList<>();
                    removedTestIds.addAll(alreadyExistingTestIds);

                    removedTestIds.removeAll(allTestsFromForm);
                    newlyAddedTestIds.removeAll(alreadyExistingTestIds);

                    deleteAnalysisForRemovedTests(accessionNumber, sysUserId, removedTestIds);

                    addNewAnalysisForExistingSampleItem(sysUserId, sampleItems, typeOfSampleId, newlyAddedTestIds);
                } else {
                    noOfSampleItems = addNewSampleItemAndAnalysis(sample, sysUserId, noOfSampleItems, typeOfSampleId, allTestsFromForm);
                }
            }

            List<String> removedSampleTypeIds = new ArrayList<>();
            removedSampleTypeIds.addAll(alreadyExistingTypeAndTestIds.keySet());

            removedSampleTypeIds.removeAll(allTypeAndTestIdsFromForm.keySet());
            deleteRemovedSampleItemAndAnalysis(sysUserId, sampleItems, removedSampleTypeIds);

            if(!StringUtil.isNullorNill(sample.getAccessionNumber())){
                accessionPublisher.publish(sample.getUUID(), request.getContextPath());
            }

            ajaxServlet.sendData(xml.toString(), VALID, request, response);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private int addNewSampleItemAndAnalysis(Sample sample, String sysUserId, int noOfSampleItems, String typeOfSampleId, List<String> testsForSampleItem) {
        SampleItem sampleItem = buildSampleItem(sysUserId, sample, ++noOfSampleItems, typeOfSampleDAO.getTypeOfSampleById(typeOfSampleId));
        sampleItemDao.insertData(sampleItem);
        for (String newTest : testsForSampleItem) {
            Analysis analysis;
            Test test = testDAO.getTestById(newTest);

            analysis = populateAnalysis("0", sampleItem, test, sysUserId, DateUtil.getNowAsSqlDate());
            analysisDAO.insertData(analysis, false);
        }
        return noOfSampleItems;
    }

    private void deleteAnalysisForRemovedTests(String accessionNumber, String sysUserId, List<String> deletedTests) {
        for (String deletedTest : deletedTests) {
            List<Analysis> analysisByAccessionAndTestId = analysisDAO.getAnalysisByAccessionAndTestId(accessionNumber, deletedTest);
            setCancelledStatusForAnalysisList(analysisByAccessionAndTestId, sysUserId);
        }
    }

    private void setCancelledStatusForAnalysisList(List<Analysis> analysisByAccessionAndTestId, String sysUserId){
        for (Analysis analysis : analysisByAccessionAndTestId) {
            analysis.setSysUserId(sysUserId);
            analysis.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled));
            analysisDAO.updateData(analysis);
        }
    }

    private void addNewAnalysisForExistingSampleItem(String sysUserId, List<SampleItem> sampleItems, String typeOfSampleId, List<String> newlyAddedTests) {
        for (String newlyAddedTest : newlyAddedTests) {
            SampleItem sampleItem = getSampleItemByTypeOfSampleIdFromSampleItems(typeOfSampleId, sampleItems);
            Analysis analysis;
            Test test = testDAO.getTestById(newlyAddedTest);
            analysis = populateAnalysis("0", sampleItem, test, sysUserId, DateUtil.getNowAsSqlDate());
            analysisDAO.insertData(analysis, false);
        }
    }

    private void deleteRemovedSampleItemAndAnalysis(String sysUserId, List<SampleItem> sampleItems, List<String> deletedSampleTypeIds) {
        for (String deletedSampleTypeId : deletedSampleTypeIds) {
            SampleItem sampleItem = getSampleItemByTypeOfSampleIdFromSampleItems(deletedSampleTypeId, sampleItems);

            List<Analysis> analysesBySampleItem = analysisDAO.getAnalysesBySampleItem(sampleItem);
            setCancelledStatusForAnalysisList(analysesBySampleItem, sysUserId);

            sampleItem.setSysUserId(sysUserId);
            sampleItem.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Canceled));
            sampleItemDao.updateData(sampleItem);

        }
    }

    private SampleItem getSampleItemByTypeOfSampleIdFromSampleItems(String typeOfSampleId, List<SampleItem> sampleItems) {
        for (SampleItem sampleItem : sampleItems) {
            if (sampleItem.getTypeOfSampleId().equals(typeOfSampleId)) {
                return sampleItem;
            }
        }
        return null;
    }

    private SampleItem buildSampleItem(String sysUserId, Sample sample, int sampleItemIdIndex, TypeOfSample typeOfSample) {
        String collector = "";
        SampleItem item = new SampleItem();
        item.setSysUserId(sysUserId);
        item.setSample(sample);
        item.setTypeOfSample(typeOfSample);
        item.setSortOrder(Integer.toString(sampleItemIdIndex));
        item.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered));
        item.setCollector(collector);
        return item;
    }

    private Analysis populateAnalysis(String analysisRevision, SampleItem sampleItem, Test test, String sysUserId, java.sql.Date collectionDate) {

        Analysis analysis = new Analysis();
        analysis.setTest(test);
        analysis.setIsReportable(test.getIsReportable());
        analysis.setAnalysisType("MANUAL");
        analysis.setSampleItem(sampleItem);
        analysis.setSysUserId(sysUserId);
        analysis.setRevision(analysisRevision);
        analysis.setStartedDate(collectionDate);
        analysis.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.NotTested));
        analysis.setTestSection(test.getTestSection());
        return analysis;
    }

}
