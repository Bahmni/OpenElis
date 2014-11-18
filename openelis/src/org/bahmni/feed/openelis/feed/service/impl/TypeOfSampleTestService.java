package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.utils.AuditingService;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleTestDAOImpl;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSampleTest;

import java.util.ArrayList;
import java.util.List;

public class TypeOfSampleTestService {

    private final TypeOfSampleTestDAOImpl typeOfSampleTestDAO;
    private final AuditingService auditingService;

    public TypeOfSampleTestService() {
        typeOfSampleTestDAO = new TypeOfSampleTestDAOImpl();
        auditingService = new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl());
    }

    public void createOrUpdate(String typeOfSampleId, String testId){
        String sysUserId = auditingService.getSysUserId();
        TypeOfSampleTest typeOfSampleTest = createTypeOfSampleTest(typeOfSampleId, testId, sysUserId);
        TypeOfSampleTest existingAssoc = typeOfSampleTestDAO.getTypeOfSampleTestForTest(testId);
        if(existingAssoc == null){
            typeOfSampleTestDAO.insertData(typeOfSampleTest);
        }else {
            updateAssociation(sysUserId, typeOfSampleTest, existingAssoc);
        }
    }

    private TypeOfSampleTest createTypeOfSampleTest(String typeOfSampleId, String testId, String sysUserId) {
        TypeOfSampleTest typeOfSampleTest = new TypeOfSampleTest();
        typeOfSampleTest.setTestId(testId);
        typeOfSampleTest.setTypeOfSampleId(typeOfSampleId);
        typeOfSampleTest.setSysUserId(sysUserId);
        return typeOfSampleTest;
    }

    private void updateAssociation(String sysUserId, TypeOfSampleTest typeOfSampleTest, TypeOfSampleTest existingAssoc) {
        if(!existingAssoc.getTypeOfSampleId().equals(typeOfSampleTest.getTypeOfSampleId())){
            typeOfSampleTestDAO.deleteData(new String[]{existingAssoc.getId()}, sysUserId);
            typeOfSampleTestDAO.insertData(typeOfSampleTest);
        }
    }

    public void deleteAllAssociations(String typeOfSampleId, String sysUserId){
        List<TypeOfSampleTest> testsForSample = typeOfSampleTestDAO.getTypeOfSampleTestsForSampleType(typeOfSampleId);
        ArrayList<String> sampleIds = new ArrayList<>();
        for (TypeOfSampleTest typeOfSampleTest : testsForSample) {
            sampleIds.add(typeOfSampleTest.getId());
        }
        typeOfSampleTestDAO.deleteData(sampleIds.toArray(new String []{}), sysUserId);
    }
}
