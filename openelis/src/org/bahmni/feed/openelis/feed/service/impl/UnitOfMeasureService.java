package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.utils.AuditingService;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.unitofmeasure.dao.UnitOfMeasureDAO;
import us.mn.state.health.lims.unitofmeasure.daoimpl.UnitOfMeasureDAOImpl;
import us.mn.state.health.lims.unitofmeasure.valueholder.UnitOfMeasure;

import java.sql.Timestamp;
import java.util.Date;

public class UnitOfMeasureService {
    public static final String CATEGORY_UNIT_OF_MEASURE = "unit_of_measure";
    private final AuditingService auditingService;
    private final UnitOfMeasureDAO unitOfMeasureDAO;

    public UnitOfMeasureService() {
        this.auditingService = new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl());
        this.unitOfMeasureDAO = new UnitOfMeasureDAOImpl();
    }

    public UnitOfMeasureService(AuditingService mockAuditingService, UnitOfMeasureDAO mockUnitOfMeasureDAO) {
        auditingService = mockAuditingService;
        unitOfMeasureDAO = mockUnitOfMeasureDAO;
    }

    public UnitOfMeasure create(String referenceDataTestUnitOfMeasure) {
        String sysUserId = auditingService.getSysUserId();

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure = populateUnitOfMeasure(unitOfMeasure, referenceDataTestUnitOfMeasure, sysUserId);

        UnitOfMeasure unitOfMeasureByName = unitOfMeasureDAO.getUnitOfMeasureByName(unitOfMeasure);

        if (unitOfMeasureByName == null) {
            unitOfMeasureDAO.insertData(unitOfMeasure);
            return unitOfMeasure;
        }
        return unitOfMeasureByName;
    }

    private UnitOfMeasure populateUnitOfMeasure(UnitOfMeasure unitOfMeasure, String referenceDataTestUnitOfMeasure, String sysUserId) {
        unitOfMeasure.setUnitOfMeasureName(referenceDataTestUnitOfMeasure);
        unitOfMeasure.setSysUserId(sysUserId);
        unitOfMeasure.setIsActive(IActionConstants.YES);
        unitOfMeasure.setLastupdated(new Timestamp(new Date().getTime()));
        return unitOfMeasure;
    }
}
