package us.mn.state.health.lims.typeofsample.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class TypeOfSampleUtil {

	private static Map<String, List<Test>> sampleIdTestMap = new HashMap<String, List<Test>>();
	private static Map<String, String> typeOfSampleIdToNameMap;
	private static Map<String, List<String>> labOrderTypeToTestMap = null;
	private static Map<String, TypeOfSample> testIdToTypeOfSampleMap = null;

	public static List<Test> getTestListBySampleTypeId(String sampleTypeId, String labOrderTypeId, boolean orderableOnly) {

		List<Test> testList = sampleIdTestMap.get(sampleTypeId);

		if (testList == null) {
			testList = createSampleIdTestMap(sampleTypeId);
		}

		if( GenericValidator.isBlankOrNull(labOrderTypeId) || "none".equals(labOrderTypeId)){
			if( orderableOnly){
				return filterByOrderable( testList);
			}else{
				return testList;
			}
		}
		
		if( labOrderTypeToTestMap == null){
			createLabOrderToTestMap();
		}

		List<Test> filteredList = new ArrayList<Test>();
		List<String> labOrderTests = labOrderTypeToTestMap.get(labOrderTypeId);
		
		for( Test test : testList){
			if( labOrderTests.contains(test.getId()) && (!orderableOnly || test.getOrderable() )){
				filteredList.add(test);
			}
		}
		return filteredList;
	}

	private static List<Test> filterByOrderable(List<Test> testList) {
		List<Test> filteredList = new ArrayList<Test>();
		
		for( Test test : testList){
			if( test.getOrderable()){
				filteredList.add(test);
			}
		}
		
		return filteredList;
	}

	public static TypeOfSample getTypeOfSampleForTest(String testId){
		if( testIdToTypeOfSampleMap == null){
			createTestIdToTypeOfSampleMap();
		}
		
		return testIdToTypeOfSampleMap.get(testId);
	}
	
	
	private static void createTestIdToTypeOfSampleMap() {
		testIdToTypeOfSampleMap = new HashMap<String, TypeOfSample>( );
		
		List<TypeOfSampleTest> typeOfSampleTestList = new TypeOfSampleTestDAOImpl().getAllTypeOfSampleTests();

		TypeOfSampleDAO typeOfSampleDAO = new TypeOfSampleDAOImpl();

		for( TypeOfSampleTest typeTest : typeOfSampleTestList){
			String testId = typeTest.getTestId();
			TypeOfSample typeOfSample = typeOfSampleDAO.getTypeOfSampleById(typeTest.getTypeOfSampleId());
			testIdToTypeOfSampleMap.put(testId, typeOfSample);
		}
	}

	private static void createLabOrderToTestMap() {
		labOrderTypeToTestMap = new HashMap<String, List<String>>();
		String testTableId = new ReferenceTablesDAOImpl().getReferenceTableByName("TEST").getId();
		
		List<LabOrderItem> orderItems = new LabOrderItemDAOImpl().getLabOrderItemsByTableAndAction(testTableId, "DISPLAY");
		
		for( LabOrderItem item : orderItems){
			List<String> tests = labOrderTypeToTestMap.get(item.getLabOrderTypeId());
			
			if( tests == null){
				tests = new ArrayList<String>();
				labOrderTypeToTestMap.put(item.getLabOrderTypeId(), tests);
			}

			tests.add(item.getRecordId());
			
		}
	}


	private static List<Test> createSampleIdTestMap(String sampleTypeId) {
		List<Test> testList;
		TypeOfSampleTestDAO sampleTestsDAO = new TypeOfSampleTestDAOImpl();
		List<TypeOfSampleTest> tests = sampleTestsDAO.getTypeOfSampleTestsForSampleType(sampleTypeId);

		TestDAO testDAO = new TestDAOImpl();

		testList = new ArrayList<Test>();

		for (TypeOfSampleTest link : tests) {
			Test test = testDAO.getActiveTestById(Integer.valueOf(link.getTestId()));
			if (test != null) {
			    testList.add(test);
			}
		}

		Collections.sort(testList, TestComparator.NAME_COMPARATOR);

		sampleIdTestMap.put(sampleTypeId, testList);
		return testList;
	}

	/**
	 * This class keeps lists of tests for each type of sample.  If the DB of tests changes, we need to invalidate such lists.
	 */
	public static void clearTestCache() {
	    sampleIdTestMap.clear();
	    typeOfSampleIdToNameMap = null;
	    testIdToTypeOfSampleMap = null;
	}

	@SuppressWarnings("unchecked")
	public static String getTypeOfSampleNameForId( String id){
		if( typeOfSampleIdToNameMap == null){
			typeOfSampleIdToNameMap = new HashMap<String, String>();

			TypeOfSampleDAO tosDAO = new TypeOfSampleDAOImpl();
			List<TypeOfSample> allTypes = tosDAO.getAllTypeOfSamples();
			for( TypeOfSample typeOfSample : allTypes){
				typeOfSampleIdToNameMap.put(typeOfSample.getId(), typeOfSample.getLocalizedName());
			}
		}

		return typeOfSampleIdToNameMap.get(id);
	}
}
