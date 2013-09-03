package us.mn.state.health.lims.upload.service;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    protected Sample save(CSVSample csvSample, String sysUserId) throws ParseException {
        Sample sample = new Sample();
        sample.setAccessionNumber(csvSample.accessionNumber);
        SimpleDateFormat datetimeFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Date parsedDate = datetimeFormatter.parse(csvSample.sampleDate);
        Timestamp timestamp = new Timestamp(parsedDate.getTime());
        sample.setCollectionDate(timestamp);
        sample.setEnteredDate(new java.sql.Date(parsedDate.getTime()));
        sample.setReceivedTimestamp(timestamp);
        sample.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.OrderStatus.Finished));
        sample.setDomain(SystemConfiguration.getInstance().getHumanDomain());
        sample.setSysUserId(sysUserId);
        sample.setSampleSource(getSampleSource(csvSample.sampleSource));
        sampleDAO.insertDataWithAccessionNumber(sample);
        return sample;
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
