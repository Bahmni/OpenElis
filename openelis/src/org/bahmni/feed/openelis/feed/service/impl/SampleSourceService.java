package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSEncounter;
import us.mn.state.health.lims.samplesource.dao.SampleSourceDAO;
import us.mn.state.health.lims.samplesource.valueholder.SampleSource;

import java.util.List;

public class SampleSourceService {

    private SampleSourceDAO sampleSourceDAO;
    private ExternalReferenceDao externalReferenceDao;

    public SampleSourceService(SampleSourceDAO sampleSourceDAO, ExternalReferenceDao externalReferenceDao) {
        this.sampleSourceDAO = sampleSourceDAO;
        this.externalReferenceDao = externalReferenceDao;
    }

    private int getLatestDisplayOrder() {
        List<SampleSource> allSampleSources = sampleSourceDAO.getAll();
        if(allSampleSources != null && allSampleSources.size() > 0)
            return allSampleSources.get(allSampleSources.size() - 1).getDisplayOrder() + 1;
        return 1;
    }

    public SampleSource getSampleSource(OpenMRSEncounter openMRSEncounter) {
        SampleSource sampleSource;
        ExternalReference externalReference = externalReferenceDao.getData(openMRSEncounter.getLocationUuid(), "SampleSource");
        if(externalReference != null){
            sampleSource = sampleSourceDAO.get(String.valueOf(externalReference.getItemId()));
        } else if (sampleSourceDAO.getByName(openMRSEncounter.getLocationName(), true)  != null) {
            sampleSource = sampleSourceDAO.getByName(openMRSEncounter.getLocationName(), true);
            ExternalReference externalReferenceForSampleSource = new ExternalReference(Long.valueOf(sampleSource.getId()), openMRSEncounter.getLocationUuid(), "SampleSource");
            externalReferenceDao.insertData(externalReferenceForSampleSource);
        }
        else{
            sampleSource = new SampleSource(openMRSEncounter.getLocationName(), openMRSEncounter.getLocationName(), getLatestDisplayOrder());
            sampleSourceDAO.add(sampleSource);
            ExternalReference externalReferenceForSampleSource = new ExternalReference(Long.valueOf(sampleSource.getId()), openMRSEncounter.getLocationUuid(), "SampleSource");
            externalReferenceDao.insertData(externalReferenceForSampleSource);
        }
        return sampleSource;
    }
}
