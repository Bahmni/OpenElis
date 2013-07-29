package us.mn.state.health.lims.feed;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.bahmni.feed.openelis.feed.FeedGeneratorFactory;
import org.bahmni.feed.openelis.utils.OpenElisConnectionProvider;
import org.ict4h.atomfeed.server.service.EventFeedServiceImpl;
import org.ict4h.atomfeed.server.service.helper.EventFeedServiceHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AtomFeedAction extends Action {

    private EventFeedServiceImpl eventFeedService;
    private Logger logger = Logger.getLogger(this.getClass());


    public AtomFeedAction() {
        super();
        this.eventFeedService = new EventFeedServiceImpl(new FeedGeneratorFactory().get(new OpenElisConnectionProvider()));
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String responseData;
        String requestUrl = request.getRequestURL().toString();
        AtomFeedUrlParser feedUrlParser = new AtomFeedUrlParser(requestUrl);
        responseData = feedUrlParser.isForRecentFeed() ? getRecentFeed(feedUrlParser, requestUrl) : getEventFeed(feedUrlParser, requestUrl);
        response.setContentType("application/atom+xml");
        ObjectMapperRepository.objectMapper.writeValue(response.getWriter(), responseData);
        return null;
    }

    public String getRecentFeed(AtomFeedUrlParser parser, String requestUrl) throws IOException {
        String category = parser.getCategory();
        return EventFeedServiceHelper.getRecentFeed(eventFeedService, requestUrl, category, logger);
    }

    public String getEventFeed(AtomFeedUrlParser parser, String requestURL) throws IOException {
        String category = parser.getCategory();
        int feedMarker = parser.getFeedMarker();
        return EventFeedServiceHelper.getEventFeed(eventFeedService, requestURL, category, feedMarker, logger);
    }
}
