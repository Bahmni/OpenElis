package us.mn.state.health.lims.result.action.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortByAccessionNumberAndSequence extends SortBySequenceAndSortOrder {

    private static final int SAME_ACCESSION_NUMBER = 0;

    public void sort(List<? extends ResultItem> selectedTests, boolean forwardSort) {
        final boolean isForwardSort = forwardSort;
        Collections.sort(selectedTests, new Comparator<ResultItem>() {
            public int compare(ResultItem firstResult, ResultItem secondResult) {
                int accessionSort = isForwardSort == true ? firstResult.getSequenceAccessionNumber().compareTo(secondResult.getSequenceAccessionNumber()) :
                        secondResult.getSequenceAccessionNumber().compareTo(firstResult.getSequenceAccessionNumber());
                if (accessionSort != SAME_ACCESSION_NUMBER) {
                    return accessionSort;
                }
                return sortByTestOrderOrName(accessionSort, firstResult, secondResult);
            }
        });
    }

}