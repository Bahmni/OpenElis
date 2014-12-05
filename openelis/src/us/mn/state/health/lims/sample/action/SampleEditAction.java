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
package us.mn.state.health.lims.sample.action;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.formfields.FormFields;
import us.mn.state.health.lims.common.services.DisplayListService;
import us.mn.state.health.lims.common.services.DisplayListService.ListType;
import us.mn.state.health.lims.common.services.PatientService;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.ConfigurationProperties.Property;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.IdValuePair;
import us.mn.state.health.lims.common.util.IntegerUtil;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.observationhistory.daoimpl.ObservationHistoryDAOImpl;
import us.mn.state.health.lims.observationhistory.valueholder.ObservationHistory;
import us.mn.state.health.lims.observationhistorytype.daoImpl.ObservationHistoryTypeDAOImpl;
import us.mn.state.health.lims.observationhistorytype.valueholder.ObservationHistoryType;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.sample.bean.SampleEditItem;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.util.PossibleSample;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.SampleStatus;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleTestDAO;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleTestDAOImpl;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSampleTest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SampleEditAction extends BaseAction {

	private String accessionNumber;
	private Sample sample;
	private List<SampleItem> sampleItemList;
	private static final TypeOfSampleDAO typeOfSampleDAO = new TypeOfSampleDAOImpl();
	private static final AnalysisDAO analysisDAO = new AnalysisDAOImpl();
	private boolean isEditable = false;
	private static Set<Integer> excludedAnalysisStatusList;
	private static Set<Integer> includedSampleStatusList;
	private static String PAYMENT_STATUS_OBSERVATION_ID = null;
	private String maxAccessionNumber;

	static {
		excludedAnalysisStatusList = new HashSet<Integer>();
		excludedAnalysisStatusList.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.ReferredIn)));
		excludedAnalysisStatusList.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.Canceled)));

		includedSampleStatusList = new HashSet<Integer>();
		includedSampleStatusList.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(SampleStatus.Entered)));

		ObservationHistoryType observationType = new ObservationHistoryTypeDAOImpl().getByName("paymentStatus");
		if (observationType != null) {
			PAYMENT_STATUS_OBSERVATION_ID = observationType.getId();
		}

	}

	protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String forward = "success";

		request.getSession().setAttribute(SAVE_DISABLED, TRUE);

		DynaActionForm dynaForm = (DynaActionForm) form;

		accessionNumber = request.getParameter("accessionNumber");

		if( GenericValidator.isBlankOrNull(accessionNumber)){
			accessionNumber = getMostRecentAccessionNumberForPaitient( request.getParameter("patientID"));
		}

		dynaForm.initialize(mapping);

		isEditable = "readwrite".equals((String) request.getSession().getAttribute(IActionConstants.SAMPLE_EDIT_WRITABLE))
				|| "readwrite".equals(request.getParameter("type"));
		PropertyUtils.setProperty(dynaForm, "isEditable", isEditable);
		if (!GenericValidator.isBlankOrNull(accessionNumber)) {

			PropertyUtils.setProperty(dynaForm, "accessionNumber", accessionNumber);
			PropertyUtils.setProperty(dynaForm, "searchFinished", Boolean.TRUE);

			getSample();

			if (sample != null && !GenericValidator.isBlankOrNull(sample.getId())) {

				getSampleItems();
				setPatientInfo(dynaForm);
                List<SampleEditItem> sampleEditItems = setCurrentTestInfo(dynaForm);
                setAddableTestInfo(dynaForm,sampleEditItems);
				setAddableSampleTypes(dynaForm);
				PropertyUtils.setProperty(dynaForm, "maxAccessionNumber", maxAccessionNumber);
			} else {
				PropertyUtils.setProperty(dynaForm, "noSampleFound", Boolean.TRUE);
			}
		} else {
			PropertyUtils.setProperty(dynaForm, "searchFinished", Boolean.FALSE);
			request.getSession().setAttribute(IActionConstants.SAMPLE_EDIT_WRITABLE, request.getParameter("type"));
		}

		if (ConfigurationProperties.getInstance().isPropertyValueEqual(Property.trackPatientPayment, "true")) {
			setDictionaryList((BaseActionForm) dynaForm, "paymentOptions", "PP", true);

			if (sample != null) {
				ObservationHistory paymentObservation = new ObservationHistoryDAOImpl().getObservationHistoriesBySampleIdAndType(sample.getId(), PAYMENT_STATUS_OBSERVATION_ID);
				if (paymentObservation != null) {
					PropertyUtils.setProperty(dynaForm, "paymentOptionSelection", paymentObservation.getValue() );
				}
			}
		}

		if (FormFields.getInstance().useField(FormFields.Field.InitialSampleCondition)) {
			PropertyUtils.setProperty(dynaForm, "initialSampleConditionList", DisplayListService.getList(ListType.INITIAL_SAMPLE_CONDITION));
		}

		PropertyUtils.setProperty(form, "currentDate", DateUtil.getCurrentDateAsText());

		return mapping.findForward(forward);
	}

	private String getMostRecentAccessionNumberForPaitient(String patientID) {
		String accessionNumber = null;
		if( !GenericValidator.isBlankOrNull(patientID)){
			List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patientID);

			int maxId = 0;
			for( Sample sample : samples){
				if( Integer.parseInt(sample.getId()) > maxId){
					maxId = Integer.parseInt(sample.getId());
					accessionNumber = sample.getAccessionNumber();
				}
			}

		}
		return accessionNumber;
	}

	private void getSample() {
		SampleDAO sampleDAO = new SampleDAOImpl();
		sample = sampleDAO.getSampleByAccessionNumber(accessionNumber);
	}

	private void getSampleItems() {
		SampleItemDAO sampleItemDAO = new SampleItemDAOImpl();

		sampleItemList = sampleItemDAO.getSampleItemsBySampleIdAndStatus(sample.getId(), includedSampleStatusList);
	}

	private void setPatientInfo(DynaActionForm dynaForm) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

		Patient patient = new SampleHumanDAOImpl().getPatientForSample(sample);
		PatientService patientService = new PatientService(patient);

		PropertyUtils.setProperty(dynaForm, "firstName", patientService.getFirstName());
		PropertyUtils.setProperty(dynaForm, "lastName", patientService.getLastName());
		PropertyUtils.setProperty(dynaForm, "dob", patientService.getDOB());
		PropertyUtils.setProperty(dynaForm, "gender", patientService.getGender());
		PropertyUtils.setProperty(dynaForm, "nationalId", patientService.getNationalId());
	}

	private List<SampleEditItem> setCurrentTestInfo(DynaActionForm dynaForm) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		List<SampleEditItem> currentTestList = new ArrayList<SampleEditItem>();

		for (SampleItem sampleItem : sampleItemList) {
			addCurrentTestsToList(sampleItem, currentTestList);
		}

		PropertyUtils.setProperty(dynaForm, "existingTests", currentTestList);
        return currentTestList;
	}

	private void addCurrentTestsToList(SampleItem sampleItem, List<SampleEditItem> currentTestList) {

		TypeOfSample typeOfSample = new TypeOfSample();
		typeOfSample.setId(sampleItem.getTypeOfSampleId());
		typeOfSampleDAO.getData(typeOfSample);

		List<Analysis> analysisList = analysisDAO.getAnalysesBySampleItemsExcludingByStatusIds(sampleItem, excludedAnalysisStatusList);

		List<SampleEditItem> analysisSampleItemList = new ArrayList<SampleEditItem>();

		boolean canRemoveSample = true;
		for (Analysis analysis : analysisList) {
			SampleEditItem sampleEditItem = new SampleEditItem();

			sampleEditItem.setTestId(analysis.getTest().getId());
			sampleEditItem.setTestName(analysis.getTest().getTestName());
			sampleEditItem.setSampleItemId(sampleItem.getId());
			boolean canCancel = canCancel(analysis);
			if( !canCancel){
				canRemoveSample = false;
			}
			sampleEditItem.setCanCancel(canCancel);
			sampleEditItem.setAnalysisId(analysis.getId());
			sampleEditItem.setStatus(StatusOfSampleUtil.getStatusNameFromId(analysis.getStatusId()));
			sampleEditItem.setSortOrder(analysis.getTest().getSortOrder());
            sampleEditItem.setSampleType(typeOfSample.getLocalizedName());
            if (isPanelItem(analysis)) {
                handleTestsBelongingToPanel(analysisSampleItemList, analysis, sampleEditItem);
            }else{
                analysisSampleItemList.add(sampleEditItem);
            }
		}

		if (!analysisSampleItemList.isEmpty()) {
			Collections.sort(analysisSampleItemList, SampleEditItemComparator.NAME_COMPARATOR); // Secondary Sorting
			Collections.sort(analysisSampleItemList, SampleEditItemComparator.SORT_ORDER_COMPARATOR); // Primary Sorting

			analysisSampleItemList.get(0).setAccessionNumber(accessionNumber + "-" + sampleItem.getSortOrder());
			analysisSampleItemList.get(0).setShouldDisplaySampleTypeInformation(true);
			analysisSampleItemList.get(0).setCanRemoveSample(canRemoveSample);
			maxAccessionNumber = analysisSampleItemList.get(0).getAccessionNumber();
			currentTestList.addAll(analysisSampleItemList);
		}
	}

    private boolean canCancel(Analysis analysis) {
        return !analysis.getStatusId().equals(StatusOfSampleUtil.getStatusID(AnalysisStatus.Canceled))
                && analysis.getStatusId().equals(StatusOfSampleUtil.getStatusID(AnalysisStatus.NotTested));
    }

    private void handleTestsBelongingToPanel(List<SampleEditItem> analysisSampleItemList, Analysis analysis, SampleEditItem sampleEditItem) {
        SampleEditItem alreadyAddedPanel = getAlreadyAddedPanel(analysis.getPanel().getPanelName(), analysisSampleItemList);
        if (alreadyAddedPanel != null) {
            alreadyAddedPanel.addPanelTest(sampleEditItem);
            boolean canCancelPanel = alreadyAddedPanel.isCanCancel() && canCancel(analysis);
            alreadyAddedPanel.setCanCancel(canCancelPanel);
        }
        else{
            SampleEditItem panelItem = new SampleEditItem();
            panelItem.setPanelName(analysis.getPanel().getPanelName());
            panelItem.addPanelTest(sampleEditItem);
            panelItem.setSortOrder(analysis.getPanel().getSortOrder());
            panelItem.setCanCancel(canCancel(analysis));
            panelItem.setSampleItemId(sampleEditItem.getSampleItemId());
            panelItem.setSampleType(sampleEditItem.getSampleType());
            analysisSampleItemList.add(panelItem);
        }
    }

    private SampleEditItem getAlreadyAddedPanel(String panelName, List<SampleEditItem> analysisSampleItemList) {
        for (SampleEditItem sampleEditItem : analysisSampleItemList) {
            if(sampleEditItem.getPanelName() != null && sampleEditItem.getPanelName().equals(panelName))
                return sampleEditItem;
        }
        return null;
    }

    private boolean isPanelItem(Analysis analysis) {
        return analysis.getPanel() != null;
    }

    private void setAddableTestInfo(DynaActionForm dynaForm, List<SampleEditItem> currentlyExistingTests) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		List<PossibleSample> possibleSampleList = new ArrayList<>();

		for (SampleItem sampleItem : sampleItemList) {
            addPossibleSamplesToList(sampleItem, possibleSampleList, currentlyExistingTests);
        }



		PropertyUtils.setProperty(dynaForm, "possibleSamples", possibleSampleList);
		PropertyUtils.setProperty(dynaForm, "testSectionList", DisplayListService.getList(ListType.TEST_SECTION));
	}

    private void setAddableSampleTypes(DynaActionForm dynaForm) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        HashSet<IdValuePair> sampleTypes = new HashSet<>(DisplayListService.getList(ListType.SAMPLE_TYPE));
        HashSet<IdValuePair> alreadyAddedSampleTypes = new HashSet<>();
        for (SampleItem item : sampleItemList) {
            TypeOfSample typeOfSample = item.getTypeOfSample();
            alreadyAddedSampleTypes.add(new IdValuePair(typeOfSample.getId(), typeOfSample.getLocalizedName()));
        }
        sampleTypes.removeAll(alreadyAddedSampleTypes);
        PropertyUtils.setProperty(dynaForm, "sampleTypes", sampleTypes);
	}

    private void addPossibleSamplesToList(SampleItem sampleItem, List<PossibleSample> sampleList, List<SampleEditItem> currentTests) {

        TypeOfSample typeOfSample = new TypeOfSample();
        typeOfSample.setId(sampleItem.getTypeOfSampleId());
        typeOfSampleDAO.getData(typeOfSample);

        TestDAO testDAO = new TestDAOImpl();
        Test test = new Test();

        TypeOfSampleTestDAO sampleTypeTestDAO = new TypeOfSampleTestDAOImpl();
        List<TypeOfSampleTest> typeOfSampleTestList = sampleTypeTestDAO.getTypeOfSampleTestsForSampleType(typeOfSample.getId());
        List<SampleEditItem> testOrPanelList = new ArrayList<SampleEditItem>();

        List<SampleEditItem> allTests = new ArrayList<>();
        for (SampleEditItem currentTest : currentTests) {
            if (currentTest.isPanel()) {
                allTests.addAll(currentTest.getPanelTests());
            }
            else{
                allTests.add(currentTest);
            }
        }


        for (TypeOfSampleTest typeOfSampleTest : typeOfSampleTestList) {
            boolean alreadyExists = false;
            for (SampleEditItem currentTest : allTests) {
                if (ObjectUtils.equals(currentTest.getTestId(), typeOfSampleTest.getTestId())) {
                    alreadyExists = true;
                }
            }
            if (!alreadyExists) {
                SampleEditItem sampleEditItem = new SampleEditItem();
                sampleEditItem.setTestId(typeOfSampleTest.getTestId());
                test.setId(typeOfSampleTest.getTestId());
                testDAO.getData(test);
                if ("Y".equals(test.getIsActive())) {
                    sampleEditItem.setTestName(test.getLocalizedName());
                    sampleEditItem.setSampleItemId(sampleItem.getId());
                    sampleEditItem.setSortOrder(test.getSortOrder());
                    testOrPanelList.add(sampleEditItem);
                }
            }
        }


        if (!testOrPanelList.isEmpty()) {
			Collections.sort(testOrPanelList, SampleEditItemComparator.NAME_COMPARATOR); // Secondary Sorting
			Collections.sort(testOrPanelList, SampleEditItemComparator.SORT_ORDER_COMPARATOR); // Primary Sorting

			PossibleSample possibleSample = new PossibleSample();
			possibleSample.setAccessionNumber(accessionNumber);
			possibleSample.setSampleType(typeOfSample.getLocalizedName());
            possibleSample.setPossibleTestOrPanel(testOrPanelList);

			sampleList.add(possibleSample);

        }

    }

	protected String getPageTitleKey() {
		return isEditable ? StringUtil.getContextualKeyForKey("sample.edit.title") : StringUtil.getContextualKeyForKey("sample.view.title");
	}

	protected String getPageSubtitleKey() {
		return isEditable ? StringUtil.getContextualKeyForKey("sample.edit.subtitle") : StringUtil
				.getContextualKeyForKey("sample.view.subtitle");
	}

	private static class SampleEditItemComparator implements Comparable<SampleEditItem> {
		String name;

		@Override
		public int compareTo(SampleEditItem sampleEditItem) {
			return this.name.compareTo(sampleEditItem.getTestName());
		}

		public static final Comparator<SampleEditItem> NAME_COMPARATOR = new Comparator<SampleEditItem>() {
			public int compare(SampleEditItem a, SampleEditItem b) {
                if(a.isPanel() && b.isPanel()){
                    return a.getPanelName().toLowerCase().compareTo(b.getPanelName().toLowerCase());
                }
                if(a.isPanel() && !b.isPanel()){
                    return a.getPanelName().toLowerCase().compareTo(b.getTestName().toLowerCase());
                }
                if(b.isPanel() && !a.isPanel()){
                    return b.getPanelName().toLowerCase().compareTo(a.getTestName().toLowerCase());
                }
				return ((a.getTestName().toLowerCase()).compareTo(b.getTestName().toLowerCase()));
			}
		};

		public static final Comparator<SampleEditItem> SORT_ORDER_COMPARATOR = new Comparator<SampleEditItem>() {
			public int compare(SampleEditItem a, SampleEditItem b) {
				return IntegerUtil.getParsedValueOrDefault(a.getSortOrder(), Integer.MAX_VALUE).compareTo(IntegerUtil.getParsedValueOrDefault(b.getSortOrder(), Integer.MAX_VALUE));
			}
		};
	}
}