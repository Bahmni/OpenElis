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
* Copyright (C) CIRG, University of Washington, Seattle WA.  All Rights Reserved.
*
*/
package us.mn.state.health.lims.resultvalidation.action.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.DynaActionForm;
import org.hibernate.mapping.Collection;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.paging.*;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.IdValuePair;
import us.mn.state.health.lims.resultvalidation.bean.AnalysisItem;
import us.mn.state.health.lims.sample.valueholder.SampleComparator;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ResultValidationPaging {
    private PagingUtility<List<AnalysisItem>> paging = new PagingUtility<>(
            IActionConstants.ANALYSIS_RESULTS_SESSION_CACHE, IActionConstants.ANALYSIS_RESULTS_PAGE_MAPPING_SESSION_CACHE);
	private static AnalysisItemPageHelper pagingHelper = new AnalysisItemPageHelper();

	public void setDatabaseResults(HttpServletRequest request, DynaActionForm dynaForm, List<AnalysisItem> analysisItems)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		paging.setDatabaseResults(request.getSession(), analysisItems, pagingHelper);

		List<AnalysisItem> resultPage = paging.getPage(1, request.getSession());

		if (resultPage != null) {
			PropertyUtils.setProperty(dynaForm, "resultList", resultPage);
			PropertyUtils.setProperty(dynaForm, "paging", paging.getPagingBeanWithSearchMapping(1, request.getSession()));
		}
	}

	@SuppressWarnings("unchecked")
	public void page(HttpServletRequest request, DynaActionForm dynaForm, String newPage) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {

		request.getSession().setAttribute(IActionConstants.SAVE_DISABLED, IActionConstants.FALSE);
        PagingBean bean = (PagingBean) dynaForm.get("paging");

		paging.updatePagedResults(request.getSession(), bean, pagingHelper);

		int page = Integer.parseInt(newPage);

		List<AnalysisItem> resultPage = paging.getPage(page, request.getSession());
		if (resultPage != null) {
			PropertyUtils.setProperty(dynaForm, "resultList", resultPage);
			PropertyUtils.setProperty(dynaForm, "paging", paging.getPagingBeanWithSearchMapping(page, request.getSession()));
		}
	}

	@SuppressWarnings("unchecked")
	public void updatePagedResults(HttpServletRequest request, DynaActionForm dynaForm) {
        PagingBean bean = (PagingBean) dynaForm.get("paging");

		paging.updatePagedResults(request.getSession(), bean, pagingHelper);
	}

	public List<AnalysisItem> getResults(HttpServletRequest request) {
		return paging.getAllResults(request.getSession(), pagingHelper);
	}

	private static class AnalysisItemPageHelper implements IPageDivider<List<AnalysisItem>>, IPageUpdater<List<AnalysisItem>>,
			IPageFlattener<List<AnalysisItem>> {

		public void createPages(List<AnalysisItem> analysisList, List<List<AnalysisItem>> pagedResults) {
            int validationPagingSize = Integer.parseInt(ConfigurationProperties.getInstance().getPropertyValue(ConfigurationProperties.Property.RESULTS_VALIDATION_PAGE_SIZE));
			List<AnalysisItem> page = new ArrayList<>();

			String currentAccessionNumber = null;
			int resultCount = 0;

            Collections.sort(analysisList, new Comparator<AnalysisItem>() {
                @Override
                public int compare(AnalysisItem o1, AnalysisItem o2) {
                    return o1.getAccessionNumber().compareTo(o2.getAccessionNumber());
                }
            });

			for (AnalysisItem item : analysisList) {
				if (currentAccessionNumber != null && !currentAccessionNumber.equals(item.getAccessionNumber())) {
					resultCount = 0;
					currentAccessionNumber = null;
					pagedResults.add(page);
					page = new ArrayList<>();
				}
                if (resultCount >= validationPagingSize) {
					currentAccessionNumber = item.getAccessionNumber();
				}
				
				page.add(item);
				resultCount++;
			}

			if (!page.isEmpty() || pagedResults.isEmpty()) {
				pagedResults.add(page);
			}
		}

		public void updateCache(List<AnalysisItem> cacheItems, List<AnalysisItem> clientItems) {
			for (int i = 0; i < clientItems.size(); i++) {
					cacheItems.set(i, clientItems.get(i));
			}

		}

		public List<AnalysisItem> flattenPages(List<List<AnalysisItem>> pages) {

			List<AnalysisItem> allResults = new ArrayList<>();

			for (List<AnalysisItem> page : pages) {
				for (AnalysisItem item : page) {
					allResults.add(item);
				}
			}

			return allResults;

		}

		@Override
		public List<IdValuePair> createSearchToPageMapping(List<List<AnalysisItem>> allPages) {
			List<IdValuePair> mappingList = new ArrayList<>();
			
			int page = 0;
			for( List<AnalysisItem> analysisList : allPages){
				page++;
				String pageString = String.valueOf(page);
				
				String currentAccession = null;
				
				for( AnalysisItem analysisItem : analysisList){
					if( !analysisItem.getAccessionNumber().equals(currentAccession)){
						currentAccession = analysisItem.getAccessionNumber();
						mappingList.add( new IdValuePair(currentAccession, pageString));
					}
				}
			}
			
			return mappingList;
		}
	}
}
