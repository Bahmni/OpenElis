package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.utils.AuditingService;
import org.junit.Test;
import us.mn.state.health.lims.unitofmeasure.dao.UnitOfMeasureDAO;
import us.mn.state.health.lims.unitofmeasure.valueholder.UnitOfMeasure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UnitOfMeasureServiceTest {
    @Test
    public void save_and_return_new_unitOfMeasure() {
        AuditingService mockAuditingService = mock(AuditingService.class);
        UnitOfMeasureDAO mockUnitOfMeasureDAO = mock(UnitOfMeasureDAO.class);
        UnitOfMeasureService unitOfMeasureService = new UnitOfMeasureService(mockAuditingService, mockUnitOfMeasureDAO);

        when(mockUnitOfMeasureDAO.getUnitOfMeasureByName(any(UnitOfMeasure.class))).thenReturn(null);

        UnitOfMeasure newUOM = unitOfMeasureService.create("newUOM");

        assertNotNull("should return the newly saved unit of measure", newUOM);
        assertEquals("newUOM", newUOM.getUnitOfMeasureName());
    }

    @Test
    public void save_and_return_existing_unitOfMeasure() {
        AuditingService mockAuditingService = mock(AuditingService.class);
        UnitOfMeasureDAO mockUnitOfMeasureDAO = mock(UnitOfMeasureDAO.class);
        UnitOfMeasureService unitOfMeasureService = new UnitOfMeasureService(mockAuditingService, mockUnitOfMeasureDAO);

        UnitOfMeasure expectedUnitOfMeasure = new UnitOfMeasure();
        when(mockUnitOfMeasureDAO.getUnitOfMeasureByName(any(UnitOfMeasure.class))).thenReturn(expectedUnitOfMeasure);

        UnitOfMeasure existingUOM = unitOfMeasureService.create("newUOM");

        assertNotNull("should return the existing saved unit of measure", existingUOM);
        assertEquals(expectedUnitOfMeasure, existingUOM);
    }
}