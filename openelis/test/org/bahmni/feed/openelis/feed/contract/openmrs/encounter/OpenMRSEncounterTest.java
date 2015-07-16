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

import java.util.ArrayList;
import java.util.List;

public class OpenMRSEncounterTest {
    @Test
    public void returns_true_when_has_lab_order_tests() {

        OpenMRSOrder labOrder = new OpenMRSOrder();
        labOrder.setOrderType(OpenMRSOrder.LAB_ORDER_TYPE);

        List<OpenMRSOrder> encounterOrders = new ArrayList<>();
        encounterOrders.add(labOrder);

        OpenMRSEncounter openMRSEncounter = new OpenMRSEncounter();
        openMRSEncounter.setOrders(encounterOrders);

        Assert.assertTrue("Should return true. This encounter has a lab order", openMRSEncounter.hasLabOrder());
    }

    @Test
    public void returns_false_when_does_not_have_lab_order_tests() {
        OpenMRSOrder nonLabOrder = new OpenMRSOrder();
        nonLabOrder.setOrderType("Drug Order");

        List<OpenMRSOrder> encounterOrders = new ArrayList<>();
        encounterOrders.add(nonLabOrder);

        OpenMRSEncounter openMRSEncounter = new OpenMRSEncounter();
        openMRSEncounter.setOrders(encounterOrders);

        Assert.assertFalse("Should return false. This encounter does not have a lab order", openMRSEncounter.hasLabOrder());

        Assert.assertFalse("Should return false. This encounter does not have a lab order", new OpenMRSEncounter().hasLabOrder());
    }
}
