package us.mn.state.health.lims.result.action.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SortBySampleCollectionDateAndAccessionNumberTest {

    @Mock
    private ResultItem oneTest;

    @Mock
    private ResultItem anotherTest;

    @Before
    public void setup() {
        initMocks(this);
    }

    private long lessTimeStamp = 111L;
    private long moreTimeStamp = 222L;

    private String smallSequenceNumber = "1";
    private String greaterSequenceNumber = "2";
    private String smallTestSortOrder = "90";
    private String greaterTestSortOrder = "291";


    @Test
    public void shouldSortAsRecentCollectionDateTestComeFirst() {
        when(oneTest.getCollectionDate()).thenReturn(new Timestamp(lessTimeStamp));
        when(anotherTest.getCollectionDate()).thenReturn(new Timestamp(moreTimeStamp));
        List<ResultItem> selectedTests = Arrays.asList(oneTest, anotherTest);

        new SortBySampleCollectionDateAndAccessionNumber().sort(selectedTests, false);

        assertEquals(selectedTests.get(0), anotherTest);
        assertEquals(selectedTests.get(1), oneTest);
    }

    @Test
    public void shouldSortBySequenceNumbersIfCollectionDatesSame() {
        when(oneTest.getCollectionDate()).thenReturn(new Timestamp(lessTimeStamp));
        when(oneTest.getSequenceNumber()).thenReturn(greaterSequenceNumber);
        when(anotherTest.getCollectionDate()).thenReturn(new Timestamp(lessTimeStamp));
        when(anotherTest.getSequenceNumber()).thenReturn(smallSequenceNumber);
        List<ResultItem> selectedTests = Arrays.asList(oneTest, anotherTest);

        new SortBySampleCollectionDateAndAccessionNumber().sort(selectedTests, false);

        assertEquals(selectedTests.get(0), anotherTest);
        assertEquals(selectedTests.get(1), oneTest);
    }

    @Test
    public void shouldSortByTestSortOrderIfCollectionDatesAndSequenceNumbersSameRespectively() {
        when(oneTest.getCollectionDate()).thenReturn(new Timestamp(lessTimeStamp));
        when(oneTest.getSequenceNumber()).thenReturn(smallSequenceNumber);
        when(oneTest.getTestSortOrder()).thenReturn(greaterTestSortOrder);
        when(anotherTest.getCollectionDate()).thenReturn(new Timestamp(lessTimeStamp));
        when(anotherTest.getSequenceNumber()).thenReturn(smallSequenceNumber);
        when(anotherTest.getTestSortOrder()).thenReturn(smallTestSortOrder);
        List<ResultItem> selectedTests = Arrays.asList(oneTest, anotherTest);

        new SortBySampleCollectionDateAndAccessionNumber().sort(selectedTests, false);

        assertEquals(selectedTests.get(0), anotherTest);
        assertEquals(selectedTests.get(1), oneTest);
    }

    @Test
    public void shouldSortByNameIfCollectionDatesAndSequenceAreSameAndTestSortOrderIsNull() {
        when(oneTest.getCollectionDate()).thenReturn(new Timestamp(lessTimeStamp));
        when(oneTest.getSequenceNumber()).thenReturn(smallSequenceNumber);
        when(oneTest.getTestSortOrder()).thenReturn(null);
        when(oneTest.getTestName()).thenReturn("CD4");
        when(anotherTest.getCollectionDate()).thenReturn(new Timestamp(lessTimeStamp));
        when(anotherTest.getSequenceNumber()).thenReturn(smallSequenceNumber);
        when(anotherTest.getTestSortOrder()).thenReturn(null);
        when(anotherTest.getTestName()).thenReturn("CBC");
        List<ResultItem> selectedTests = Arrays.asList(oneTest, anotherTest);

        new SortBySampleCollectionDateAndAccessionNumber().sort(selectedTests, false);

        assertEquals(selectedTests.get(0), anotherTest);
        assertEquals(selectedTests.get(1), oneTest);
    }

}
