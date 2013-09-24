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
package us.mn.state.health.lims.common.paging;

import us.mn.state.health.lims.common.util.IdValuePair;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class PagingUtility<E> {
	private int totalPages = 0;
    private String resultsSessionCacheKey;
    private String resultsPageMappingSessionCacheKey;

    public PagingUtility(String resultsSessionCacheKey, String resultsPageMappingSessionCacheKey) {
        this.resultsSessionCacheKey = resultsSessionCacheKey;
        this.resultsPageMappingSessionCacheKey = resultsPageMappingSessionCacheKey;
    }

    /**
	 * @param session the Session for the current HttpRequest
	 * @param items The items which will be divided into pages
	 * @param divider The object which knows how to divide the objects into pages
	 */
	public void setDatabaseResults(HttpSession session, E items, IPageDivider<E> divider ) {

		List<E> pagedResults = new ArrayList<>();
		divider.createPages(items, pagedResults);
        session.setAttribute(resultsSessionCacheKey, pagedResults);
		List<IdValuePair> searchPageMapping = divider.createSearchToPageMapping(pagedResults);
        session.setAttribute(resultsPageMappingSessionCacheKey, searchPageMapping);
		totalPages = pagedResults.size();
	}

	@SuppressWarnings("unchecked")
	/*
	 * @param page First page is page 1
	 *
	 * @param session Session for this request
	 */
	public E getPage(int page, HttpSession session) {
		if (page > 0) {
			 List<E> pagedResults = (List<E>) session.getAttribute(resultsSessionCacheKey);

			 if( pagedResults != null && pagedResults.size() >= page ){
				 return pagedResults.get(page - 1);
			 }
		}

		return null;
	}

	/**
     * @param session the Session for the current HttpRequest
     * @param paging The paging bean, it knows the current page
     * @param updater The object which knows how to update the cache
     */
	@SuppressWarnings("unchecked")
	public void updatePagedResults(HttpSession session, PagingBean paging, IPageUpdater<E> updater) {
		List<E> pagedResults = (List<E>) session.getAttribute(resultsSessionCacheKey);

		if( pagedResults != null){
			updateSessionResultCache(pagedResults, paging, updater);
			totalPages = pagedResults.size();
		}
	}

	/**
	 * @param session the Session for the current HttpRequest
	 * @param flattener The object which knows how to take a list of pages and make it into a flat list.
	 * @return The flattened list
	 */
	public E getAllResults(HttpSession session, IPageFlattener<E> flattener) {
		List<E> pagedResults = getAllPages( session);
		return flattener.flattenPages(pagedResults);
	}

	private void updateSessionResultCache(List<E> pagedResults, PagingBean paging, IPageUpdater<E> updater) {

		int currentPage = Integer.parseInt(paging.getCurrentPage()) - 1;

		E sessionTests = pagedResults.get(currentPage);

		updater.updateCache(sessionTests, sessionTests);

	}

	/**
	 * @param currentPage The new current page
	 * @return The bean with the new current page and the total pages
	 */
	public PagingBean getPagingBean(int currentPage) {
		PagingBean paging = new PagingBean();
		paging.setCurrentPage(String.valueOf(currentPage));
		paging.setTotalPages(String.valueOf(totalPages));
		return paging;
	}

	/**
	 * @param currentPage The new current page
	 * @param session which will cause the mapping from search terms to pages to be loaded
	 * @return The bean with the new current page and the total pages
	 */
	public PagingBean getPagingBeanWithSearchMapping(int currentPage, HttpSession session) {
		PagingBean paging = new PagingBean();
		paging.setCurrentPage(String.valueOf(currentPage));
		paging.setTotalPages(String.valueOf(totalPages));
		paging.setSearchTermToPage(getPageMapping(session));
		
		return paging;
	}
	
	/**
	 * 
	 * @param session The session object which holds the pages
	 * @return The pages as a list
	 */
	@SuppressWarnings("unchecked")
	public List<E> getAllPages( HttpSession session){
		return (List<E>) session.getAttribute(resultsSessionCacheKey);
	}
	
	@SuppressWarnings("unchecked")
	public List<IdValuePair> getPageMapping( HttpSession session){
		List<IdValuePair> pairList = (List<IdValuePair>)session.getAttribute(resultsPageMappingSessionCacheKey);
		if( pairList == null){
			pairList = new ArrayList<>();
		}
		
		return pairList;
	}
}
