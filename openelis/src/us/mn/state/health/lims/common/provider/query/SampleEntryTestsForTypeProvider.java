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
 * Copyright (C) CIRG, University of Washington, Seattle WA.  All Rights Reserved.
 *
 */
package us.mn.state.health.lims.common.provider.query;

import org.apache.commons.lang3.StringEscapeUtils;
import us.mn.state.health.lims.common.util.XMLUtil;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panelitem.dao.PanelItemDAO;
import us.mn.state.health.lims.panelitem.daoimpl.PanelItemDAOImpl;
import us.mn.state.health.lims.panelitem.valueholder.PanelItem;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.test.valueholder.TestComparator;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSamplePanelDAO;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSamplePanelDAOImpl;
import us.mn.state.health.lims.typeofsample.util.TypeOfSampleUtil;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSamplePanel;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class SampleEntryTestsForTypeProvider extends BaseQueryProvider {
    private TestDAO testDAO = new TestDAOImpl();
    private PanelDAO panelDAO = new PanelDAOImpl();
    private static final String USER_TEST_SECTION_ID;

    static {
        USER_TEST_SECTION_ID = new TestSectionDAOImpl().getTestSectionByName("user").getId();
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String sampleType = request.getParameter("sampleType");
        String labOrderType = request.getParameter("labOrderType");

        StringBuilder xml = new StringBuilder();

        String result = createSearchResultXML(sampleType, labOrderType, xml);

        ajaxServlet.sendData(xml.toString(), result, request, response);

    }

    private String createSearchResultXML(String sampleType, String labOrderType, StringBuilder xml) {

        String success = VALID;

        List<Test> tests = TypeOfSampleUtil.getTestListBySampleTypeId(sampleType, labOrderType, true);

        // http://stackoverflow.com/questions/8327514/comparison-method-violates-its-general-contract
        Collections.sort(tests, TestComparator.NAME_COMPARATOR); // Secondary Sorting
        Collections.sort(tests, TestComparator.SORT_ORDER_COMPARATOR); // Primary Sorting
        addTests(tests, xml);

        List<TypeOfSamplePanel> panelList = getPanelList(sampleType);
        List<PanelTestMap> panelMap = linkTestsToPanels(panelList, tests);

        addPanels(panelMap, xml);

        return success;
    }

    private void addTests(List<Test> tests, StringBuilder xml) {
        xml.append("<tests>");
        for (Test test : tests) {
            addTest(test, xml);
        }

        xml.append("</tests>");
    }

    private void addTest(Test test, StringBuilder xml) {
        xml.append("<test>");
        XMLUtil.appendKeyValue("name", StringEscapeUtils.escapeXml(test.getTestName()), xml);
        XMLUtil.appendKeyValue("id", test.getId(), xml);
        XMLUtil.appendKeyValue("userBenchChoice", String.valueOf(USER_TEST_SECTION_ID.equals(test.getTestSection().getId())), xml);
        xml.append("</test>");
    }

    private void addPanels(List<PanelTestMap> panelMap, StringBuilder xml) {
        panelMap = sortPanels(panelMap);

        xml.append("<panels>");
        for (PanelTestMap testMap : panelMap) {
            addPanel(testMap, xml);
        }
        xml.append("</panels>");
    }

    private List<PanelTestMap> sortPanels(List<PanelTestMap> panelMap) {

        Collections.sort(panelMap, new Comparator<PanelTestMap>() {
            @Override
            public int compare(PanelTestMap o1, PanelTestMap o2) {
                return o1.getPanelOrder() - o2.getPanelOrder();
            }
        });

        return panelMap;
    }

    private void addPanel(PanelTestMap testMap, StringBuilder xml) {
        xml.append("<panel>");
        XMLUtil.appendKeyValue("name", testMap.getName(), xml);
        XMLUtil.appendKeyValue("id", testMap.getPanelId(), xml);
        XMLUtil.appendKeyValue("testMap", testMap.getTestMaps(), xml);
        xml.append("</panel>");
    }

    private List<TypeOfSamplePanel> getPanelList(String sampleType) {
        TypeOfSamplePanelDAO samplePanelDAO = new TypeOfSamplePanelDAOImpl();
        return samplePanelDAO.getTypeOfSamplePanelsForSampleType(sampleType);
    }

    private List<PanelTestMap> linkTestsToPanels(List<TypeOfSamplePanel> panelList, List<Test> tests) {
        List<PanelTestMap> selected = new ArrayList<PanelTestMap>();

        Map<String, Integer> testNameOrderMap = new HashMap<String, Integer>();
        PanelDAO panelDAO = new PanelDAOImpl();

        for (int i = 0; i < tests.size(); i++) {
            testNameOrderMap.put(tests.get(i).getTestName(), new Integer(i));
        }

        PanelItemDAO panelItemDAO = new PanelItemDAOImpl();

        for (TypeOfSamplePanel samplePanel : panelList) {
            String panelName = panelDAO.getNameForPanelId(samplePanel.getPanelId());
            if(panelName == null || panelName.isEmpty())
                continue;
            String matchTests = getTestIndexesForPanels(samplePanel.getPanelId(), testNameOrderMap, panelItemDAO);
            int panelOrder = panelDAO.getPanelById(samplePanel.getPanelId()).getSortOrderInt();
            selected.add(new PanelTestMap(samplePanel.getPanelId(), panelOrder, panelName, matchTests));
        }

        return selected;
    }

    @SuppressWarnings("unchecked")
    private String getTestIndexesForPanels(String panelId, Map<String, Integer> testIdOrderMap, PanelItemDAO panelItemDAO) {
        StringBuilder indexes = new StringBuilder();
        List<PanelItem> items = panelItemDAO.getPanelItemsForPanel(panelId);

        for (PanelItem item : items) {
            String derivedNameFromPanel = getDerivedNameFromPanel(item);
            if (derivedNameFromPanel != null) {
                Integer index = testIdOrderMap.get(derivedNameFromPanel);

                if (index != null) {
                    indexes.append(index.toString());
                    indexes.append(",");
                }
            }
        }

        String withExtraComma = indexes.toString();
        return withExtraComma.length() > 0 ? withExtraComma.substring(0, withExtraComma.length() - 1) : "";
    }

    private String getDerivedNameFromPanel(PanelItem item) {
        //This cover the transition in the DBbetween the panel_item being linked by name
        //to being linked by id
        if (item.getTest() != null) {
            Test test = testDAO.getTestById(item.getTest().getId());
            if (test != null) {
                return test.getTestName();
            }
        } else {
            return item.getTestName();
        }

        return null;
    }

    public class PanelTestMap {
        private String name;
        private String testMaps;
        private String panelId;
        private int panelOrder;

        public PanelTestMap(String panelId, int panelOrder, String panelName, String map) {
            name = panelName;
            testMaps = map;
            this.panelId = panelId;
            this.panelOrder = panelOrder;
        }

        public String getName() {
            return name;
        }

        public String getTestMaps() {
            return testMaps;
        }

        public String getPanelId() {
            return panelId;
        }

        public int getPanelOrder() {
            return panelOrder;
        }
    }

}
