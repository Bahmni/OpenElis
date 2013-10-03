package org.bahmni.feed.openelis.feed.contract.openmrs.encounter;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OpenMRSEncounterTest {
    @Test
    public void returns_true_when_has_lab_order_tests() {
        OpenMRSOrderType labOrderType = new OpenMRSOrderType();
        labOrderType.setName(OpenMRSOrderType.LAB_ORDER_TYPE);

        OpenMRSOrder labOrder = new OpenMRSOrder();
        labOrder.setOrderType(labOrderType);

        List<OpenMRSOrder> encounterOrders = new ArrayList<>();
        encounterOrders.add(labOrder);

        OpenMRSEncounter openMRSEncounter = new OpenMRSEncounter();
        openMRSEncounter.setOrders(encounterOrders);

        Assert.assertTrue("Should return true. This encounter has a lab order", openMRSEncounter.hasLabOrder());
    }

    @Test
    public void returns_false_when_does_not_have_lab_order_tests() {
        OpenMRSOrderType nonLabOrderType = new OpenMRSOrderType();
        nonLabOrderType.setName("Drug Order");

        OpenMRSOrder nonLabOrder = new OpenMRSOrder();
        nonLabOrder.setOrderType(nonLabOrderType);

        List<OpenMRSOrder> encounterOrders = new ArrayList<>();
        encounterOrders.add(nonLabOrder);

        OpenMRSEncounter openMRSEncounter = new OpenMRSEncounter();
        openMRSEncounter.setOrders(encounterOrders);

        Assert.assertFalse("Should return false. This encounter does not have a lab order", openMRSEncounter.hasLabOrder());

        Assert.assertFalse("Should return false. This encounter does not have a lab order", new OpenMRSEncounter().hasLabOrder());
    }
}
