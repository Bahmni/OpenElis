package us.mn.state.health.lims.common.provider.validation;

import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import us.mn.state.health.lims.sample.dao.SampleDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DateNumAccessionValidatorTest {

    DateNumAccessionValidator dateNumAccessionValidator;
    /*******
     *
     * Do not change the sequence of Tests
     *
     */

    @Mock
    SampleDAO sampleDAO;

    @Before
    public void setUp(){
        initMocks(this);
        dateNumAccessionValidator = new DateNumAccessionValidator();
    }

    @Test
    public void shouldTestNextAccessionNumber() {
        when(sampleDAO.getLargestAccessionNumberWithPrefix(any(String.class))).thenReturn("02022012-004");
        dateNumAccessionValidator.setSampleDAO(sampleDAO);
        String nextNumber = dateNumAccessionValidator.getNextAvailableAccessionNumber(null);
        assertEquals("02022012-005",nextNumber);
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
        assertEquals(IAccessionNumberValidator.ValidationResults.SUCCESS, validAccessionNumber );
    }

    private String getDatePrefix(){
        return  new SimpleDateFormat("ddMMyyyy").format(new Date()) +"-";
    }

  /*  @Test
    public void shouldAssertInvalidAccessionNumber() {
        IAccessionNumberValidator.ValidationResults validAccessionNumber = dateNumAccessionValidator.validFormat("32032012001", false);
        assertEquals(IAccessionNumberValidator.ValidationResults.FORMAT_FAIL,validAccessionNumber);
    }

    @Test
    public void shouldAssertInvalidNumberPartOfAccessionNumber() {
        IAccessionNumberValidator.ValidationResults validAccessionNumber = dateNumAccessionValidator.validFormat("02032012a34", false);
        assertEquals(validAccessionNumber, IAccessionNumberValidator.ValidationResults.FORMAT_FAIL);
    }
*/
    @Test
    public void shouldAssertIncrementAssertionNumber() {
        String nextAssertionNumber = dateNumAccessionValidator.incrementAccessionNumber("02022012-004");
        assertEquals(nextAssertionNumber,"02022012-005");
    }

    @Test
    public void shouldAssertCreateFirstAccessionNumber() {
        String firstAccessionNumber = dateNumAccessionValidator.createFirstAccessionNumber(null);
        assertTrue(firstAccessionNumber.endsWith("001"));
    }





}
