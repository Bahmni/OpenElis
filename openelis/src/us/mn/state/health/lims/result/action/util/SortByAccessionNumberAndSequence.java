package us.mn.state.health.lims.result.action.util;

import java.util.*;

import org.apache.commons.validator.GenericValidator;

public class SortByAccessionNumberAndSequence implements SortStrategy {

    public void sort(List<? extends ResultItem> selectedTests, boolean forwardSort) {
        final boolean isForwardSort = forwardSort;
        Collections.sort(selectedTests, new Comparator<ResultItem>() {
            public int compare(ResultItem firstResult, ResultItem secondResult) {
                int accessionSort = isForwardSort == true ? firstResult.getSequenceAccessionNumber().compareTo(secondResult.getSequenceAccessionNumber()) :
                        secondResult.getSequenceAccessionNumber().compareTo(firstResult.getSequenceAccessionNumber());
                if (accessionSort != 0) {
                    return accessionSort;
                }
                return sortBySequence(accessionSort, firstResult, secondResult);
            }
        });
    }

    private int sortBySequence(int accessionSort, ResultItem firstResult, ResultItem secondResult) {
        if (!GenericValidator.isBlankOrNull(firstResult.getTestSortOrder()) && !GenericValidator.isBlankOrNull(secondResult.getTestSortOrder())) {
            try {
                return Integer.parseInt(firstResult.getTestSortOrder()) - Integer.parseInt(secondResult.getTestSortOrder());
            } catch (NumberFormatException e) {
                return firstResult.getTestName().compareTo(secondResult.getTestName());
            }
        } else if (!GenericValidator.isBlankOrNull(firstResult.getTestName()) && !GenericValidator.isBlankOrNull(secondResult.getTestName())) {
            return firstResult.getTestName().compareTo(secondResult.getTestName());
        }
        return accessionSort;
    }

}