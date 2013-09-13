package us.mn.state.health.lims.upload.service;

import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleTestDAO;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleTestDAOImpl;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSampleTest;

import java.util.List;

public class SampleItemPersisterService {
    private SampleItemDAO sampleItemDAO;
    private TypeOfSampleTestDAO typeOfSampleTestDAO;
    private TypeOfSampleDAO typeOfSampleDAO;

    public SampleItemPersisterService() {
        this(new SampleItemDAOImpl(), new TypeOfSampleTestDAOImpl(), new TypeOfSampleDAOImpl());
    }

    public SampleItemPersisterService(SampleItemDAO sampleItemDAO, TypeOfSampleTestDAO typeOfSampleTestDAO, TypeOfSampleDAO typeOfSampleDAO) {
        this.sampleItemDAO = sampleItemDAO;
        this.typeOfSampleTestDAO = typeOfSampleTestDAO;
        this.typeOfSampleDAO = typeOfSampleDAO;
    }

    public SampleItem save(Sample sample, Test test, String sysUserId) {
        TypeOfSample sampleType;
        try {
            sampleType = getSampleType(test);
        } catch (Exception e) {
            throw new LIMSRuntimeException("Type of sample does not exist for test: " + test.getTestName(), e);
        }
        int sortOrder = 1;
        List<SampleItem> existingSampleItems = sampleItemDAO.getSampleItemsBySampleId(sample.getId());
        SampleItem existingSampleItem = null;
        if (existingSampleItems != null && !existingSampleItems.isEmpty()) {
            sortOrder = getSortOrder(existingSampleItems);
            existingSampleItem = sampleItemExistsForSampleType(existingSampleItems, sampleType);
        }
        if (existingSampleItems == null || existingSampleItems.isEmpty() || existingSampleItem == null) {
            SampleItem sampleItem = new SampleItem();
            sampleItem.setSample(sample);
            sampleItem.setTypeOfSample(sampleType);
            sampleItem.setSysUserId(sysUserId);
            sampleItem.setSortOrder(String.valueOf(sortOrder));
            sampleItem.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered));
            sampleItemDAO.insertData(sampleItem);
            return sampleItem;
        }
        return existingSampleItem;
    }

    private int getSortOrder(List<SampleItem> existingSampleItems) {
        int sortOrder = 0;
        for (SampleItem existingSampleItem : existingSampleItems) {
            int order = Integer.parseInt(existingSampleItem.getSortOrder());
            if (order > sortOrder)
                sortOrder = order;
        }
        return sortOrder + 1;
    }

    private SampleItem sampleItemExistsForSampleType(List<SampleItem> existingSampleItems, TypeOfSample sampleType) {
        for (SampleItem existingSampleItem : existingSampleItems) {
            if (existingSampleItem.getTypeOfSample().getId() == sampleType.getId())
                return existingSampleItem;
        }
        return null;
    }

    private TypeOfSample getSampleType(Test test) {
        List<TypeOfSampleTest> typeOfSampleTestsForTest = typeOfSampleTestDAO.getTypeOfSampleTestsForTest(test.getId());
        return typeOfSampleDAO.getTypeOfSampleById(typeOfSampleTestsForTest.get(0).getTypeOfSampleId());
    }
}
