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
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

package us.mn.state.health.lims.common.provider.validation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import us.mn.state.health.lims.sample.dao.SampleDAO;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DateNumAccessionValidatorTest {

    DateNumAccessionValidator dateNumAccessionValidator;

    @Mock
    SampleDAO sampleDAO;

    @Before
    public void setUp() {
        initMocks(this);
        dateNumAccessionValidator = new DateNumAccessionValidator();
        DateNumAccessionValidator.resetAccessionNumber();
    }

    @Test
    public void shouldTestNextAccessionNumber() {
        when(sampleDAO.getLargestAccessionNumberWithPrefix(any(String.class))).thenReturn("02022012-004");
        dateNumAccessionValidator.setSampleDAO(sampleDAO);
        String nextNumber = dateNumAccessionValidator.getNextAvailableAccessionNumber(null);
        assertEquals("02022012-005", nextNumber);
    }

    @Test
    public void shouldTestFirstAccessionNumber() {
        when(sampleDAO.getLargestAccessionNumberWithPrefix(any(String.class))).thenReturn(null);
        dateNumAccessionValidator.setSampleDAO(sampleDAO);
        String nextNumber = dateNumAccessionValidator.getNextAvailableAccessionNumber(null);
        assertTrue(nextNumber.endsWith("001"));
    }


    @Test
    public void shouldTestIfAccessionNumberIsValid() {
        when(sampleDAO.getLargestAccessionNumberWithPrefix(any(String.class))).thenReturn(null);
        dateNumAccessionValidator.setSampleDAO(sampleDAO);
        String accNo = getDatePrefix() + "001";
        IAccessionNumberValidator.ValidationResults validAccessionNumber = dateNumAccessionValidator.validFormat(accNo, false);
        assertEquals(IAccessionNumberValidator.ValidationResults.SUCCESS, validAccessionNumber);
    }

    @Test
    public void shouldAssertIncrementAssertionNumber() {
        String nextAssertionNumber = dateNumAccessionValidator.incrementAccessionNumber("02022012-004");
        assertEquals(nextAssertionNumber, "02022012-005");
    }

    @Test
    public void shouldAssertCreateFirstAccessionNumber() {
        String firstAccessionNumber = dateNumAccessionValidator.createFirstAccessionNumber(null);
        assertTrue(firstAccessionNumber.endsWith("001"));
    }

    private String getDatePrefix() {
        return new SimpleDateFormat("ddMMyyyy").format(new Date()) + "-";
    }
}
