package org.bahmni.feed.openelis.feed.contract.openmrs.encounter;

import junit.framework.Assert;
import org.junit.Test;

public class OpenMRSOrderTest {
    @Test
    public void isLabOrderForPanel_returns_false_for_lab_order_for_panels() {
        Assert.assertFalse("Should return false, as this is not a lab order for a panel", new OpenMRSOrder().isLabOrderForPanel());

        OpenMRSOrderType labOrderType = new OpenMRSOrderType();
        labOrderType.setName(OpenMRSOrderType.LAB_ORDER_TYPE);

        OpenMRSConceptName labTestConcept = new OpenMRSConceptName();
        labTestConcept.setName("CBC Test");

        OpenMRSConcept concept = new OpenMRSConcept();
        concept.setSet(false);
        concept.setName(labTestConcept);

        OpenMRSOrder labTestOpenMRSOrder = new OpenMRSOrder();
        labTestOpenMRSOrder.setOrderType(labOrderType);
        labTestOpenMRSOrder.setConcept(concept);

        Assert.assertFalse("Should return false, as this is not a lab order for a panel", labTestOpenMRSOrder.isLabOrderForPanel());
    }

    @Test
    public void isLabOrderForPanel_returns_true_for_lab_order_for_panels() {
        OpenMRSOrderType labOrderType = new OpenMRSOrderType();
        labOrderType.setName(OpenMRSOrderType.LAB_ORDER_TYPE);

        OpenMRSConceptName labPanelConcept = new OpenMRSConceptName();
        labPanelConcept.setName("Routine Urine Panel");

        OpenMRSConcept concept = new OpenMRSConcept();
        concept.setSet(true);
        concept.setName(labPanelConcept);

        OpenMRSOrder labTestOpenMRSOrder = new OpenMRSOrder();
        labTestOpenMRSOrder.setOrderType(labOrderType);
        labTestOpenMRSOrder.setConcept(concept);

        Assert.assertTrue("Should return true, as this is a lab order for a panel", labTestOpenMRSOrder.isLabOrderForPanel());
    }

    @Test
    public void getLabTestName() {
        OpenMRSOrderType labOrderType = new OpenMRSOrderType();
        labOrderType.setName(OpenMRSOrderType.LAB_ORDER_TYPE);

        OpenMRSConceptName labPanelConcept = new OpenMRSConceptName();
        labPanelConcept.setName("Routine Urine Panel");

        OpenMRSConcept concept = new OpenMRSConcept();
        concept.setSet(true);
        concept.setName(labPanelConcept);

        OpenMRSOrder labTestOpenMRSOrder = new OpenMRSOrder();
        labTestOpenMRSOrder.setOrderType(labOrderType);
        labTestOpenMRSOrder.setConcept(concept);

        Assert.assertEquals("Routine Urine Panel", labTestOpenMRSOrder.getLabTestName());
    }

    @Test
    public void getLabTestName_returns_null_for_non_lab_orders() {
        OpenMRSOrderType labOrderType = new OpenMRSOrderType();
        labOrderType.setName("Drug Order");

        OpenMRSConceptName drugConcept = new OpenMRSConceptName();
        drugConcept.setName("Medicine");

        OpenMRSConcept concept = new OpenMRSConcept();
        concept.setSet(true);
        concept.setName(drugConcept);

        OpenMRSOrder drugOpenMRSOrder = new OpenMRSOrder();
        drugOpenMRSOrder.setOrderType(labOrderType);
        drugOpenMRSOrder.setConcept(concept);

        Assert.assertNull("Non lab orders should return null as lab test name", drugOpenMRSOrder.getLabTestName());
    }
}
