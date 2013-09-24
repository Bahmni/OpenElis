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

package us.mn.state.health.lims.typeofsample.util;

import org.apache.commons.validator.GenericValidator;
import us.mn.state.health.lims.laborder.daoimpl.LabOrderItemDAOImpl;
import us.mn.state.health.lims.laborder.valueholder.LabOrderItem;
import us.mn.state.health.lims.referencetables.daoimpl.ReferenceTablesDAOImpl;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.test.valueholder.TestComparator;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleTestDAO;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleTestDAOImpl;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSampleTest;

import java.util.*;

public class TypeOfSampleUtil {

    private static Map<String, List<Test>> sampleIdTestMap = new HashMap<>();
    private static Map<String, String> typeOfSampleIdToNameMap = new HashMap<>();
    private static Map<String, List<String>> labOrderTypeToTestMap = new HashMap<>();
    private static Map<String, TypeOfSample> testIdToTypeOfSampleMap = new HashMap<>();
    private static final Object LOCK_OBJECT = new Object();

    public static List<Test> getTestListBySampleTypeId(String sampleTypeId, String labOrderTypeId, boolean orderableOnly) {
        if (isEmpty(sampleIdTestMap) || hasNoEntryFor(sampleTypeId, sampleIdTestMap)) {
            createSampleIdTestMap(sampleTypeId);
        }
        List<Test> testList = sampleIdTestMap.get(sampleTypeId);
        if (hasNoLabOrderType(labOrderTypeId)) {
            return orderableOnly ? getOrderableTests(testList) : testList;
        }
        return getTestsForOrderType(labOrderTypeId, orderableOnly, testList);
    }

    private static List<Test> getTestsForOrderType(String labOrderTypeId, boolean orderableOnly, List<Test> testList) {
        HashMap<String, List<String>> labOrderTestsMap = new HashMap<>(labOrderTypeToTestMap);
        if (isEmpty(labOrderTestsMap)) {
            createLabOrderToTestMap();
            labOrderTestsMap = new HashMap<>(labOrderTypeToTestMap);
        }

        List<Test> filteredList = new ArrayList<>();
        List<String> labOrderTests = labOrderTestsMap.get(labOrderTypeId);

        for (Test test : testList) {
            if (labOrderTests.contains(test.getId()) && (!orderableOnly || test.getOrderable())) {
                filteredList.add(test);
            }
        }
        return filteredList;
    }

    public static TypeOfSample getTypeOfSampleForTest(String testId) {
        HashMap<String, TypeOfSample> stringTypeOfSampleHashMap = new HashMap<>(testIdToTypeOfSampleMap);
        if (isEmpty(stringTypeOfSampleHashMap)) {
            createTestIdToTypeOfSampleMap();
            stringTypeOfSampleHashMap = new HashMap<>(testIdToTypeOfSampleMap);
        }
        return stringTypeOfSampleHashMap.get(testId);
    }

    @SuppressWarnings("unchecked")
    public static String getTypeOfSampleNameForId(String id) {
        HashMap<String, String> map = new HashMap<>(typeOfSampleIdToNameMap);
        if (isEmpty(map)) {
            createTypeOfSampleNameForIdMap();
        }
        return map.get(id);
    }

    private static boolean hasNoEntryFor(String key, Map<String, List<Test>> map) {
        return map.get(key) == null;
    }

    private static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    private static List<Test> getOrderableTests(List<Test> testList) {
        List<Test> filteredList = new ArrayList<>();
        for (Test test : testList) {
            if (test.getOrderable() != null && test.getOrderable()) {
                filteredList.add(test);
            }
        }
        return filteredList;
    }

    private static void createTestIdToTypeOfSampleMap() {
        synchronized (LOCK_OBJECT) {
            List<TypeOfSampleTest> typeOfSampleTestList = new TypeOfSampleTestDAOImpl().getAllTypeOfSampleTests();
            TypeOfSampleDAO typeOfSampleDAO = new TypeOfSampleDAOImpl();
            for (TypeOfSampleTest typeTest : typeOfSampleTestList) {
                String testId = typeTest.getTestId();
                TypeOfSample typeOfSample = typeOfSampleDAO.getTypeOfSampleById(typeTest.getTypeOfSampleId());
                testIdToTypeOfSampleMap.put(testId, typeOfSample);
            }
        }
    }

    private static void createLabOrderToTestMap() {
        synchronized (LOCK_OBJECT) {
            String testTableId = new ReferenceTablesDAOImpl().getReferenceTableByName("TEST").getId();
            List<LabOrderItem> orderItems = new LabOrderItemDAOImpl().getLabOrderItemsByTableAndAction(testTableId, "DISPLAY");
            List<String> tests = new ArrayList<>();
            for (LabOrderItem item : orderItems) {
                tests.add(item.getRecordId());
                labOrderTypeToTestMap.put(item.getLabOrderTypeId(), tests);
            }
        }
    }

    private static void createSampleIdTestMap(String sampleTypeId) {
        synchronized (LOCK_OBJECT) {
            TypeOfSampleTestDAO sampleTestsDAO = new TypeOfSampleTestDAOImpl();
            List<TypeOfSampleTest> typeOfSampleTests = sampleTestsDAO.getTypeOfSampleTestsForSampleType(sampleTypeId);

            TestDAO testDAO = new TestDAOImpl();
            List<Test> testList = new ArrayList<>();
            for (TypeOfSampleTest link : typeOfSampleTests) {
                Test test = testDAO.getActiveTestById(Integer.valueOf(link.getTestId()));
                if (test != null) {
                    testList.add(test);
                }
            }
            Collections.sort(testList, TestComparator.NAME_COMPARATOR);
            sampleIdTestMap.put(sampleTypeId, testList);
        }
    }

    private static void createTypeOfSampleNameForIdMap() {
        synchronized (LOCK_OBJECT) {
            TypeOfSampleDAO tosDAO = new TypeOfSampleDAOImpl();
            List<TypeOfSample> allTypes = tosDAO.getAllTypeOfSamples();
            for (TypeOfSample typeOfSample : allTypes) {
                typeOfSampleIdToNameMap.put(typeOfSample.getId(), typeOfSample.getLocalizedName());
            }
        }
    }

    private static boolean hasNoLabOrderType(String labOrderTypeId) {
        return GenericValidator.isBlankOrNull(labOrderTypeId) || "none".equals(labOrderTypeId);
    }

    /**
     * This class keeps lists of tests for each type of sample.  If the DB of tests changes, we need to invalidate such lists.
     */
    public static void clearTestCache() {
        synchronized (LOCK_OBJECT) {
            sampleIdTestMap.clear();
            typeOfSampleIdToNameMap.clear();
            testIdToTypeOfSampleMap.clear();
            labOrderTypeToTestMap.clear();
        }
    }
}
