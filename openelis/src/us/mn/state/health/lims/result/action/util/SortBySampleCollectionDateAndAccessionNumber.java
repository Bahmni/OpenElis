package us.mn.state.health.lims.result.action.util;

import java.util.List;
import java.util.Comparator;
import java.util.Collections;

public class SortBySampleCollectionDateAndAccessionNumber extends SortBySequenceAndSortOrder {

    private static final int SAME_COLLECTION_DATETIME = 0;

    public void sort(List<? extends ResultItem> selectedTests, boolean forwardSort) {
        final boolean isForwardSort = forwardSort;
        Collections.sort(selectedTests, new Comparator<ResultItem>() {
                    public int compare(ResultItem firstResult, ResultItem secondResult) {
                        int collectionDateSort = isForwardSort == true ? firstResult.getCollectionDate()
                                .compareTo(secondResult.getCollectionDate()) :
                                secondResult.getCollectionDate().compareTo(firstResult.getCollectionDate());
                        if (collectionDateSort != SAME_COLLECTION_DATETIME) {
                            return collectionDateSort;
                        }
                        return sortBySequence(firstResult, secondResult);
                    }
                }
        );
    }

}