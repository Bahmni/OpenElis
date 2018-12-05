package us.mn.state.health.lims.result.action.util;

import org.apache.commons.validator.GenericValidator;

public abstract class SortBySequenceAndSortOrder implements SortStrategy {

    private static final int SAME_SEQUENCE_NUMBER = 0;

    protected int sortBySequence(ResultItem firstResult, ResultItem secondResult) {
        int sequenceNumberSort = Integer.parseInt(firstResult.getSequenceNumber()) -
                Integer.parseInt(secondResult.getSequenceNumber());
        if (sequenceNumberSort != SAME_SEQUENCE_NUMBER) {
            return sequenceNumberSort;
        }
        return sortByTestOrderOrName(sequenceNumberSort, firstResult, secondResult);
    }

    protected int sortByTestOrderOrName(int sequenceNumberSort, ResultItem firstResult, ResultItem secondResult) {
        if (!GenericValidator.isBlankOrNull(firstResult.getTestSortOrder()) && !GenericValidator.isBlankOrNull(secondResult.getTestSortOrder())) {
            try {
                return Integer.parseInt(firstResult.getTestSortOrder()) - Integer.parseInt(secondResult.getTestSortOrder());
            } catch (NumberFormatException e) {
                return firstResult.getTestName().compareTo(secondResult.getTestName());
            }
        } else if (!GenericValidator.isBlankOrNull(firstResult.getTestName()) && !GenericValidator.isBlankOrNull(secondResult.getTestName())) {
            return firstResult.getTestName().compareTo(secondResult.getTestName());
        }
        return sequenceNumberSort;
    }
}