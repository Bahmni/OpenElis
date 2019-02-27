package us.mn.state.health.lims.result.action.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SortByAccessionNumberAndSequenceTest {

	@Mock
	private ResultItem oneTest;

	@Mock
	private ResultItem anotherTest;

	@Before
	public void setup() {
		initMocks(this);
	}

	private String smallAccessionNumber = "27112018-001";
	private String greaterAccessionNumber = "27112018-002";

	@Test
	public void shouldSortResultItemOnAccessionNumber() {
		when(oneTest.getSequenceAccessionNumber()).thenReturn(greaterAccessionNumber);
		when(anotherTest.getSequenceAccessionNumber()).thenReturn(smallAccessionNumber);
		List<ResultItem> selectedTests = Arrays.asList(oneTest, anotherTest);

		new SortByAccessionNumberAndSequence().sort(selectedTests, true);

		assertEquals(selectedTests.get(0), anotherTest);
		assertEquals(selectedTests.get(1), oneTest);
	}

}
