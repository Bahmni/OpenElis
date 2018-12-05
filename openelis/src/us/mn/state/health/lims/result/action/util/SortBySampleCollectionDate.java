package us.mn.state.health.lims.result.action.util;

import java.util.*;

public class SortBySampleCollectionDate implements SortStrategy {

    public void sort(List<? extends ResultItem> selectedTests, boolean forwardSort) {
        final boolean isForwardSort = forwardSort;
        Collections.sort(selectedTests, new Comparator<ResultItem>() {
                    public int compare(ResultItem firstResult, ResultItem secondResult) {
                        return isForwardSort == true ? firstResult.getCollectionDate().compareTo(secondResult.getCollectionDate()) :
                                secondResult.getCollectionDate().compareTo(firstResult.getCollectionDate());
                    }
                }
        );
    }

}