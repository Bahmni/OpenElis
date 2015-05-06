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
package org.bahmni.feed.openelis.feed.contract.openmrs.encounter;

import junit.framework.Assert;
import org.junit.Test;

public class OpenMRSOrderTest {
    @Test
    public void isLabOrderForPanel_returns_false_for_lab_order_for_panels() {
        Assert.assertFalse("Should return false, as this is not a lab order for a panel", new OpenMRSOrder().isLabOrderForPanel());

        OpenMRSConceptName labTestConcept = new OpenMRSConceptName();
        labTestConcept.setName("CBC Test");

        OpenMRSConcept concept = new OpenMRSConcept();
        concept.setSet(false);
        concept.setName(labTestConcept);

        OpenMRSOrder labTestOpenMRSOrder = new OpenMRSOrder();
        labTestOpenMRSOrder.setOrderType(OpenMRSOrder.LAB_ORDER_TYPE);
        labTestOpenMRSOrder.setConcept(concept);

        Assert.assertFalse("Should return false, as this is not a lab order for a panel", labTestOpenMRSOrder.isLabOrderForPanel());
    }

    @Test
    public void isLabOrderForPanel_returns_true_for_lab_order_for_panels() {

        OpenMRSConceptName labPanelConcept = new OpenMRSConceptName();
        labPanelConcept.setName("Routine Urine Panel");

        OpenMRSConcept concept = new OpenMRSConcept();
        concept.setSet(true);
        concept.setName(labPanelConcept);

        OpenMRSOrder labTestOpenMRSOrder = new OpenMRSOrder();
        labTestOpenMRSOrder.setOrderType(OpenMRSOrder.LAB_ORDER_TYPE);
        labTestOpenMRSOrder.setConcept(concept);

        Assert.assertTrue("Should return true, as this is a lab order for a panel", labTestOpenMRSOrder.isLabOrderForPanel());
    }

    @Test
    public void getLabTestName() {

        OpenMRSConceptName labPanelConcept = new OpenMRSConceptName();
        labPanelConcept.setName("Routine Urine Panel");

        OpenMRSConcept concept = new OpenMRSConcept();
        concept.setSet(true);
        concept.setName(labPanelConcept);

        OpenMRSOrder labTestOpenMRSOrder = new OpenMRSOrder();
        labTestOpenMRSOrder.setOrderType(OpenMRSOrder.LAB_ORDER_TYPE);
        labTestOpenMRSOrder.setConcept(concept);

        Assert.assertEquals("Routine Urine Panel", labTestOpenMRSOrder.getLabTestName());
    }

    @Test
    public void getLabTestName_returns_null_for_non_lab_orders() {

        OpenMRSConceptName drugConcept = new OpenMRSConceptName();
        drugConcept.setName("Medicine");

        OpenMRSConcept concept = new OpenMRSConcept();
        concept.setSet(true);
        concept.setName(drugConcept);

        OpenMRSOrder drugOpenMRSOrder = new OpenMRSOrder();
        drugOpenMRSOrder.setOrderType("Drug Order");
        drugOpenMRSOrder.setConcept(concept);

        Assert.assertNull("Non lab orders should return null as lab test name", drugOpenMRSOrder.getLabTestName());
    }
}
