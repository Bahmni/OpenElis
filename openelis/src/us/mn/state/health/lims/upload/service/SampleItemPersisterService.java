package us.mn.state.health.lims.upload.service;

import us.mn.state.health.lims.sample.dao.SampleDAO;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SampleItemPersisterService {
    private Map<String, SampleItem> typesOfSamplesAdded;
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
        typesOfSamplesAdded = new HashMap<>();
    }

    public SampleItem save(Sample sample, Test test, String sysUserId) {
        TypeOfSample sampleType = getSampleType(test);
        int sortOrder = 1;
        if (!typesOfSamplesAdded.containsKey(sampleType.getId())) {
            SampleItem sampleItem = new SampleItem();
            sampleItem.setSample(sample);
            sampleItem.setTypeOfSample(sampleType);
            sampleItem.setSysUserId(sysUserId);
            sampleItem.setSortOrder(String.valueOf(sortOrder++));
            sampleItem.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered));
            sampleItemDAO.insertData(sampleItem);
            typesOfSamplesAdded.put(sampleType.getId(), sampleItem);
            return sampleItem;
        }
        return typesOfSamplesAdded.get(sampleType.getId());
    }

    private TypeOfSample getSampleType(Test test) {
        List<TypeOfSampleTest> typeOfSampleTestsForTest = typeOfSampleTestDAO.getTypeOfSampleTestsForTest(test.getId());
        return typeOfSampleDAO.getTypeOfSampleById(typeOfSampleTestsForTest.get(0).getTypeOfSampleId());
    }

}
