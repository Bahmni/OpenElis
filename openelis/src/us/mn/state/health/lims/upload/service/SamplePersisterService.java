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

package us.mn.state.health.lims.upload.service;

import us.mn.state.health.lims.common.exception.LIMSDuplicateRecordException;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplesource.dao.SampleSourceDAO;
import us.mn.state.health.lims.samplesource.daoimpl.SampleSourceDAOImpl;
import us.mn.state.health.lims.samplesource.valueholder.SampleSource;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.upload.sample.CSVSample;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SamplePersisterService {
    private List<SampleSource> sampleSources;
    private SampleDAO sampleDAO;
    private SampleSourceDAO sampleSourceDAO;

    public SamplePersisterService() {
        this(new SampleDAOImpl(), new SampleSourceDAOImpl());
    }

    public SamplePersisterService(SampleDAO sampleDAO, SampleSourceDAO sampleSourceDAO) {
        this.sampleDAO = sampleDAO;
        this.sampleSourceDAO = sampleSourceDAO;
        sampleSources = new ArrayList<>();
    }

    protected Sample save(CSVSample csvSample, String sysUserId) throws Exception {
        if (sampleForAccessionNumberExists(csvSample.accessionNumber)) {
            throw new LIMSDuplicateRecordException("Sample with accessionNumber exists");
        } else {
            Sample sample = new Sample();
            sample.setAccessionNumber(csvSample.accessionNumber);
            SimpleDateFormat datetimeFormatter = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date parsedDate = datetimeFormatter.parse(csvSample.sampleDate);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            sample.setCollectionDate(timestamp);
            sample.setEnteredDate(parsedDate);
            sample.setReceivedTimestamp(timestamp);
            sample.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.OrderStatus.Finished));
            sample.setDomain(SystemConfiguration.getInstance().getHumanDomain());
            sample.setSysUserId(sysUserId);
            sample.setSampleSource(getSampleSource(csvSample.sampleSource));
            sample.setUUID(UUID.randomUUID().toString());
            sampleDAO.insertDataWithAccessionNumber(sample);
            return sample;
        }
    }

    private boolean sampleForAccessionNumberExists(String accessionNumber) {
        return sampleDAO.getSampleByAccessionNumber(accessionNumber) != null;
    }


    private SampleSource getSampleSource(String sampleSource) {
        if(sampleSources.isEmpty()){
            sampleSources = sampleSourceDAO.getAll();
        }
        for (SampleSource source : sampleSources) {
            if(source.getName().equals(sampleSource))
                return source;
        }
        return null;
    }

}
