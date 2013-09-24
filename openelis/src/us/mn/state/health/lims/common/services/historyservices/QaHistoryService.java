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

package us.mn.state.health.lims.common.services.historyservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import us.mn.state.health.lims.audittrail.action.workers.AuditTrailItem;
import us.mn.state.health.lims.audittrail.valueholder.History;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.referencetables.dao.ReferenceTablesDAO;
import us.mn.state.health.lims.referencetables.daoimpl.ReferenceTablesDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleqaevent.dao.SampleQaEventDAO;
import us.mn.state.health.lims.sampleqaevent.daoimpl.SampleQaEventDAOImpl;
import us.mn.state.health.lims.sampleqaevent.valueholder.SampleQaEvent;

public class QaHistoryService extends HistoryService {
	private static final String SAMPLE_QAEVENT_TABLE_ID;
	private SampleQaEventDAO sampleQaEventDAO = new SampleQaEventDAOImpl();
	
	static{
		ReferenceTablesDAO tableDAO = new ReferenceTablesDAOImpl();
		SAMPLE_QAEVENT_TABLE_ID = tableDAO.getReferenceTableByName("SAMPLE_QAEVENT").getId();
	}
	
	public QaHistoryService(Sample sample) {
		setUpForSample( sample );
	}

	@SuppressWarnings("unchecked")
	private void setUpForSample(Sample sample) {
		List<SampleQaEvent> qaEventList = sampleQaEventDAO.getSampleQaEventsBySample(sample);
		
		History searchHistory = new History();
		searchHistory.setReferenceTable(SAMPLE_QAEVENT_TABLE_ID);
		historyList = new ArrayList<History>();
		
		for( SampleQaEvent event : qaEventList){
			searchHistory.setReferenceId(event.getId());
			historyList.addAll(auditTrailDAO.getHistoryByRefIdAndRefTableId(searchHistory));
		}
		
		newValueMap = new HashMap<String, String>();
	}

	@Override
	protected void addInsertion(History history, List<AuditTrailItem> items) {
		identifier =  sampleQaEventDAO.getData(history.getReferenceId()).getQaEvent().getLocalizedName();
		items.add(getCoreTrail(history));
	}

	@Override
	protected String getObjectName() {
		return StringUtil.getMessageForKey("qaevent.browse.title");
	}

	@Override
	protected void getObservableChanges(History history,	Map<String, String> changeMap, String changes) {
			changeMap.put(STATUS_ATTRIBUTE, "Gail");

	}

}
