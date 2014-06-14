package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataTestUnitOfMeasure;
import org.bahmni.feed.openelis.utils.AuditingService;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.unitofmeasure.daoimpl.UnitOfMeasureDAOImpl;
import us.mn.state.health.lims.unitofmeasure.valueholder.UnitOfMeasure;

import java.sql.Timestamp;
import java.util.Date;

public class UnitOfMeasureService {
    public static final String CATEGORY_UNIT_OF_MEASURE = "unit_of_measure";
    private final ExternalReferenceDaoImpl externalReferenceDao;
    private final AuditingService auditingService;
    private final UnitOfMeasureDAOImpl unitOfMeasureDAO;

    public UnitOfMeasureService() {
        this.externalReferenceDao = new ExternalReferenceDaoImpl();
        this.auditingService = new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl());
        this.unitOfMeasureDAO = new UnitOfMeasureDAOImpl();
    }

    public void createOrUpdate(ReferenceDataTestUnitOfMeasure referenceDataTestUnitOfMeasure) {
        String sysUserId = auditingService.getSysUserId();
        ExternalReference data = externalReferenceDao.getData(referenceDataTestUnitOfMeasure.getId(), CATEGORY_UNIT_OF_MEASURE);
        if (data == null) {
            UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
            unitOfMeasure = populateUnitOfMeasure(unitOfMeasure, referenceDataTestUnitOfMeasure, sysUserId);
            unitOfMeasureDAO.insertData(unitOfMeasure);
            saveExternalReference(referenceDataTestUnitOfMeasure, unitOfMeasure);
        } else {
            UnitOfMeasure unitOfMeasure = unitOfMeasureDAO.getUnitOfMeasureById(String.valueOf(data.getItemId()));
            populateUnitOfMeasure(unitOfMeasure, referenceDataTestUnitOfMeasure, sysUserId);
            unitOfMeasureDAO.updateData(unitOfMeasure);
        }
    }

    private void saveExternalReference(ReferenceDataTestUnitOfMeasure referenceDataTestUnitOfMeasure, UnitOfMeasure unitOfMeasure) {
        ExternalReference data;
        data = new ExternalReference(Long.parseLong(unitOfMeasure.getId()), referenceDataTestUnitOfMeasure.getId(), CATEGORY_UNIT_OF_MEASURE);
        externalReferenceDao.insertData(data);
    }

    private UnitOfMeasure populateUnitOfMeasure(UnitOfMeasure unitOfMeasure, ReferenceDataTestUnitOfMeasure referenceDataTestUnitOfMeasure, String sysUserId) {
        unitOfMeasure.setUnitOfMeasureName(referenceDataTestUnitOfMeasure.getName());
        unitOfMeasure.setDescription(referenceDataTestUnitOfMeasure.getName());
        unitOfMeasure.setSysUserId(sysUserId);
        unitOfMeasure.setIsActive(referenceDataTestUnitOfMeasure.getIsActive() ? IActionConstants.YES : IActionConstants.NO);
        unitOfMeasure.setLastupdated(new Timestamp(new Date().getTime()));
        return unitOfMeasure;
    }
}
