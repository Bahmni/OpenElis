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
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

package us.mn.state.health.lims.feed;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bahmni.feed.openelis.feed.transaction.support.AtomFeedHibernateTransactionManager;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsJdbcImpl;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsOffsetMarkersJdbcImpl;
import org.ict4h.atomfeed.server.repository.jdbc.ChunkingEntriesJdbcImpl;
import org.ict4h.atomfeed.server.service.EventFeedServiceImpl;
import org.ict4h.atomfeed.server.service.helper.EventFeedServiceHelper;
import org.ict4h.atomfeed.server.service.helper.ResourceHelper;
import us.mn.state.health.lims.ws.WebServiceAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AtomFeedAction extends WebServiceAction {

    private AtomFeedHibernateTransactionManager transactionManager;
    private EventFeedServiceImpl eventFeedService;
    private Logger logger = Logger.getLogger(this.getClass());


    public AtomFeedAction() {
        super();
        this.transactionManager = new AtomFeedHibernateTransactionManager();
        this.eventFeedService = new EventFeedServiceImpl(new org.ict4h.atomfeed.server.service.feedgenerator.FeedGeneratorFactory().getFeedGenerator(
                new AllEventRecordsJdbcImpl(transactionManager),
                new AllEventRecordsOffsetMarkersJdbcImpl(transactionManager),
                new ChunkingEntriesJdbcImpl(transactionManager),
                new ResourceHelper()));
    }

    @Override
    public ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String responseData;
        String requestUrl = request.getRequestURL().toString();
        AtomFeedUrlParser feedUrlParser = new AtomFeedUrlParser(requestUrl);
        responseData = feedUrlParser.isForRecentFeed() ? getRecentFeed(feedUrlParser, requestUrl) : getEventFeed(feedUrlParser, requestUrl);
        response.setContentType("application/atom+xml");
        response.getWriter().write(responseData);
        return null;
    }

    public String getRecentFeed(AtomFeedUrlParser parser, String requestUrl) throws IOException {
        String category = parser.getCategory();
        return EventFeedServiceHelper.getRecentFeed(eventFeedService, requestUrl, category, logger, transactionManager);
    }

    public String getEventFeed(AtomFeedUrlParser parser, String requestURL) throws IOException {
        String category = parser.getCategory();
        int feedMarker = parser.getFeedMarker();
        return EventFeedServiceHelper.getEventFeed(eventFeedService, requestURL, category, feedMarker, logger, transactionManager);
    }
}
