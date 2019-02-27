package us.mn.state.health.lims.result.action.util;

import java.util.List;

public interface SortStrategy {
    public void sort(List<? extends ResultItem> list, boolean forwardSort);
}