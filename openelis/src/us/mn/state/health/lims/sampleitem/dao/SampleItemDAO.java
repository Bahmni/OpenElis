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
package us.mn.state.health.lims.sampleitem.dao;

import us.mn.state.health.lims.common.dao.BaseDAO;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;

import java.util.List;
import java.util.Set;

/**
 * @author diane benz
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public interface SampleItemDAO extends BaseDAO {

	public boolean insertData(SampleItem sampleItem)
			throws LIMSRuntimeException;

	public void deleteData(List<SampleItem> sampleItems) throws LIMSRuntimeException;

	public List<SampleItem> getAllSampleItems() throws LIMSRuntimeException;

	public List<SampleItem> getPageOfSampleItems(int startingRecNo)
			throws LIMSRuntimeException;

	public void getData(SampleItem sampleItem) throws LIMSRuntimeException;

	public void updateData(SampleItem sampleItem)
			throws LIMSRuntimeException;
	
	public List<SampleItem> getNextSampleItemRecord(String id) throws LIMSRuntimeException;

	public List<SampleItem> getPreviousSampleItemRecord(String id) throws LIMSRuntimeException;
	
	public void getDataBySample(SampleItem sampleItem) throws LIMSRuntimeException;

	public List<SampleItem> getSampleItemsBySampleId(String id) throws LIMSRuntimeException;

    public List<SampleItem> getSampleItemsBySampleIdAndType(String sampleId, TypeOfSample typeOfSample) throws LIMSRuntimeException;

	public List<SampleItem> getSampleItemsBySampleIdAndStatus(String id, Set<Integer> includedStatusList) throws LIMSRuntimeException;

	public SampleItem getData(String sampleItemId) throws LIMSRuntimeException;

	public boolean isTypeOfSampleAndSampleExists(String sampleId, List<Integer> typeOfSampleId);

}
